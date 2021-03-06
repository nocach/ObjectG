package org.objectg.conf.prototype;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.common.collect.Sets;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.gen.GenerationRule;
import org.objectg.util.Methods;
import org.objectg.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import static org.objectg.util.Methods.getPropertyName;

/**
 * <p>
 * Component allowing to perform certain configurations by using setters of objects.
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public class PrototypeCreator {

	public static final String FIELD_NAME_OF_PROTOTYPE_HANDLER = "$objectgPrototypeCreatorHandler";
	private static Logger logger = LoggerFactory.getLogger(PrototypeCreator.class);

	private PrototypeMethodsHandler prototypeMethodsHandler;
	private Map<Object, Class> proxyToRealClass = Collections.synchronizedMap(new WeakHashMap<Object, Class>());

	public PrototypeCreator() {
		this.prototypeMethodsHandler = new PrototypeMethodsHandler(this);
	}

	public <T> T newPrototype(Class<T> clazz) {
		if (Types.isJavaType(clazz)) {
			throw new ConfigurationException("can't create configurator for java type");
		}
		if (clazz.isInterface()) {
			return createPrototypeForInterface(clazz);
		}
		try {
			return createPrototypeForConcreteClass(clazz);
		} catch (NotFoundException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		} catch (CannotCompileException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		} catch (InstantiationException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		} catch (NoSuchFieldException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		} catch (ClassNotFoundException e) {
			throw new PrototypeCreationException("could not create prototype for class=" + clazz.getName(), e);
		}
	}

	private <T> T createPrototypeForConcreteClass(Class<T> clazz)
			throws NotFoundException, CannotCompileException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchFieldException {
		Class interceptedClass = interceptClass(clazz);
		T modifiedInstance = (T) createInterceptedClassInstance(interceptedClass);
		prototypeMethodsHandler.onInit(modifiedInstance);
		return modifiedInstance;
	}

	private <T> T createPrototypeForInterface(Class<T> clazz) {
		Set<Class<?>> interfacesSet = Sets.newHashSet(Types.getAllInterfaces(clazz));
		//need to mark result proxy as intercepted
		interfacesSet.add(InterceptedByPrototypeCreator.class);
		Class<?>[] interfaces = interfacesSet.toArray(new Class<?>[]{});
		T resultPrototypeInterface = (T) Proxy.newProxyInstance(getClass().getClassLoader(),
				interfaces, new CallSetterHandler(prototypeMethodsHandler));
		prototypeMethodsHandler.onInit(resultPrototypeInterface);
		proxyToRealClass.put(resultPrototypeInterface, clazz);
		return resultPrototypeInterface;
	}

	private <T> T createInterceptedClassInstance(Class interceptedClass)
			throws InstantiationException, IllegalAccessException, NoSuchFieldException {
		T modifiedInstance = null;
		try {
			modifiedInstance = (T) interceptedClass.newInstance();
		} catch (Exception e) {
			throw new PrototypeCreationException("could not instantiate prototype. " +
					"Do you have validation logic in your constructor?", e);
		}

		Field handlerField = modifiedInstance.getClass().getDeclaredField(FIELD_NAME_OF_PROTOTYPE_HANDLER);
		ReflectionUtils.setField(handlerField, modifiedInstance, prototypeMethodsHandler);

		return modifiedInstance;
	}

	private <T> Class interceptClass(Class<T> originalClass)
			throws NotFoundException, CannotCompileException, ClassNotFoundException {
		if (ClassPool.getDefault().getOrNull(getNameForInterceptedClass(originalClass)) != null) {
			//if we have already intercepted this class, then no need to create new one
			return Class.forName(getNameForInterceptedClass(originalClass));
		}
		CtClass interceptedClass = createInterceptedCtClass(originalClass);
		addHandlerField(interceptedClass);
		interceptSetters(interceptedClass);
		interceptGetters(interceptedClass);
		return interceptedClass.toClass();
	}

	public <T> Class<T> getRealObjectClass(T prototype) {
		if (prototype.getClass().getSuperclass().equals(java.lang.reflect.Proxy.class)) {
			return proxyToRealClass.get(prototype);
		}
		return (Class<T>) prototype.getClass().getSuperclass();
	}

	private void interceptGetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
		for (CtMethod each : getAllDelcaredMethods(interceptedClass)) {
			if (isGetterMethod(each) && !isVoidReturn(each)) {
				interceptGetter(interceptedClass, each);
			}
		}
	}

	private Iterable<CtMethod> getAllDelcaredMethods(final CtClass interceptedClass) throws NotFoundException {
		Set<CtMethod> result = new HashSet<CtMethod>();
		CtClass objectClass = ClassPool.getDefault().get(Object.class.getName());
		CtClass current = interceptedClass;
		while (!current.equals(objectClass)) {
			result.addAll(Arrays.asList(current.getDeclaredMethods()));
			current = current.getSuperclass();
		}
		return result;
	}

	private void interceptGetter(CtClass interceptedClass, CtMethod method)
			throws NotFoundException, CannotCompileException {
		boolean isVoidReturn = isVoidReturn(method);
		if (isNotInterceptable(method)) return;
		if (!isVoidReturn) {
			String propertyName = extractPropertyNameOfGetterSetter(method);
			method = getMethodThatSeesObjectGFields(interceptedClass, method);
			/**
			 * {
			 *  return prototypeHandler.newPrototypeFromGetter(this, ReturnType.class, propertyName);
			 * }
			 */
			String interceptedBody = "{"
					+ "return ($r)"
					+ FIELD_NAME_OF_PROTOTYPE_HANDLER + ".newPrototypeFromGetter("
					+ "this"
					+ ", " + method.getReturnType().getName() + ".class"
					+ ", \"" + propertyName + "\""
					+ ");"
					+ "}";
			method.setBody(interceptedBody);
		}
	}

	private boolean isNotInterceptable(final CtMethod method) {
		return Modifier.isFinal(method.getModifiers())
				|| Modifier.isStatic(method.getModifiers());
	}

	private boolean isGetterMethod(CtMethod each) {
		String methodName = each.getName();
		return Methods.isGetterMethod(methodName);
	}

	private void interceptSetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
		for (CtMethod each : getAllDelcaredMethods(interceptedClass)) {
			if (isSetterMethod(each)
					//when javassist creates derived class then it replaces class name in methods declared on original
					//class with parameters to original class itself. Those methods must be ignored, because lead
					//to duplication in methods that are inherrited, see test "canCreatePrototypeOfClassReferringItself"
					//for more info on that
					&& !isSelfReferencingSetter(interceptedClass, each)) {
				setBodyToInvokeConfigurationHandler(interceptedClass, each);
			}
		}

	}

	private boolean isSelfReferencingSetter(final CtClass interceptedClass, final CtMethod method)
			throws NotFoundException {
		return method.getParameterTypes()[0].equals(interceptedClass.getSuperclass());
	}

	private boolean isSetterMethod(CtMethod each) throws NotFoundException {
		return Methods.isSetterMethod(each.getName(), each.getParameterTypes().length);
	}

	private void setBodyToInvokeConfigurationHandler(final CtClass interceptedClass, CtMethod method)
			throws NotFoundException, CannotCompileException {
		if (isNotInterceptable(method)) return;
		String propertyName = extractPropertyNameOfGetterSetter(method);
		method = getMethodThatSeesObjectGFields(interceptedClass, method);
		String returnStatement = isVoidReturn(method) ? "return;" : "return null;";
		//basically it looks like this:
		// {
		//   prototypeHandler.onSetter(this, $firstArg, propertyName);
		//   return;
		// }
		method.setBody(
				"{" +
						FIELD_NAME_OF_PROTOTYPE_HANDLER + ".onSetter(this, ($w)$1, \"" + propertyName + "\");" +
						returnStatement +
						"}");
	}

	private CtMethod getMethodThatSeesObjectGFields(final CtClass interceptedClass, CtMethod method)
			throws CannotCompileException, NotFoundException {
		if (!method.getDeclaringClass().equals(interceptedClass)) {
			//from javassist tutorial
			//For example, if class Point declares method move() and a subclass ColorPoint of
			// Point does not override move(), the two move() methods declared in Point and
			// inherited in ColorPoint are represented by the identical CtMethod object.
			// If the method definition represented by this CtMethod object is modified,
			// the modification is reflected on both the methods. If you want to modify only
			// the move() method in ColorPoint, you first have to add to ColorPoint a copy of
			// the CtMethod object representing move() in Point. A copy of the the CtMethod
			// object can be obtained by CtNewMethod.copy().

			//need to do this, because we add our handler field to the intercepted class, and this
			//field is NOT visible from methods that are inherited. So we copy method to the intercepted class
			//so it can see and use our added handler field.
			method = CtNewMethod.copy(method, interceptedClass, null);
			interceptedClass.addMethod(method);
		}
		return method;
	}

	private boolean isVoidReturn(CtMethod each) throws NotFoundException {
		CtClass vVoidClass = ClassPool.getDefault().get(Void.class.getName());
		CtClass vvoidClass = ClassPool.getDefault().get(void.class.getName());
		return each.getReturnType().equals(vVoidClass) || each.getReturnType().equals(vvoidClass);
	}

	private String extractPropertyNameOfGetterSetter(CtMethod each) {
		String methodName = each.getName();
		return getPropertyName(methodName);
	}

	private <T> CtClass createInterceptedCtClass(Class<T> clazz)
			throws NotFoundException, CannotCompileException, ClassNotFoundException {
		ClassPool classPool = ClassPool.getDefault();
		CtClass interceptedClass = classPool.get(clazz.getName());
		interceptedClass.setName(getNameForInterceptedClass(clazz));

		//set intercepted class as child of original class, so type check is valid
		CtClass originalClass = classPool.get(clazz.getName());
		interceptedClass.setSuperclass(originalClass);
		removeNotInheritedMethods(interceptedClass);

		if (!doesHaveNoArgConstructor(interceptedClass)) {
			addNoArgConstructor(interceptedClass);
		}

		CtClass[] interfaces = new CtClass[interceptedClass.getInterfaces().length + 1];
		System.arraycopy(interceptedClass.getInterfaces(), 0, interfaces, 0, interceptedClass.getInterfaces().length);
		int oneNewInterfaceIndex = interceptedClass.getInterfaces().length;
		interfaces[oneNewInterfaceIndex] = classPool.get(InterceptedByPrototypeCreator.class.getName());
		interceptedClass.setInterfaces(interfaces);

		return interceptedClass;
	}

	private void addNoArgConstructor(final CtClass interceptedClass)
			throws CannotCompileException, NotFoundException, ClassNotFoundException {
		CtConstructor superConstructor = interceptedClass.getConstructors()[0]; //first constructor will be used
		CtConstructor noArgConstructor = new CtConstructor(null, interceptedClass);
		noArgConstructor.setBody(createCallingSuper(superConstructor));
		interceptedClass.addConstructor(noArgConstructor);
	}

	private String createCallingSuper(final CtConstructor superConstructor) throws NotFoundException {
		StringBuilder src = new StringBuilder("{super(");
		for (CtClass each : superConstructor.getParameterTypes()) {
			if (CtClass.booleanType.equals(each)) {
				src.append("false,");
			} else if (Types.isPrimitive(each)) {
				src.append("0,");
			} else {
				src.append("null,");
			}
		}
		final String withoutLastComma = src.substring(0, src.length() - 1);
		return withoutLastComma + ");}";
	}

	private String createEmptyInitializer(final CtClass interceptedClass) throws NotFoundException {
		StringBuilder src = new StringBuilder("{");
		for (CtField each : interceptedClass.getDeclaredFields()) {
			if (Modifier.isFinal(each.getModifiers())) {
				final CtClass fieldType = each.getType();
				if (CtClass.booleanType.equals(fieldType)) {
					src.append(each.getName()).append("=false;");
				} else if (Types.isPrimitive(fieldType)) {
					src.append(each.getName()).append("=0;");
				} else {
					src.append(each.getName()).append("=null;");
				}
			}
		}
		return src.append("}").toString();
	}

	private boolean doesHaveNoArgConstructor(final CtClass interceptedClass) throws NotFoundException {
		boolean hasNoArgConstructor = false;
		for (CtConstructor ctConstructor : interceptedClass.getConstructors()) {
			if (ctConstructor.getParameterTypes().length == 0) hasNoArgConstructor = true;
		}
		return hasNoArgConstructor;
	}

	private void removeNotInheritedMethods(final CtClass originalClass) {
		for (CtMethod each : originalClass.getMethods()) {
			if (isNotInterceptable(each) && !Modifier.isNative(each.getModifiers())) {
				try {
					originalClass.removeMethod(each);
				} catch (NotFoundException e) {
					//it's ok, method was some java default method like wait()
				}
			}
		}

	}

	private <T> String getNameForInterceptedClass(Class<T> clazz) {
		return clazz.getName() + "Intercepted";
	}

	private void addHandlerField(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
		CtClass configurationHandlerCtType = ClassPool.getDefault().get(
				PrototypeMethodsHandler.class.getName());
		CtField ctField = new CtField(configurationHandlerCtType, FIELD_NAME_OF_PROTOTYPE_HANDLER, interceptedClass);
		ctField.setModifiers(Modifier.PUBLIC);
		interceptedClass.addField(ctField);
	}

	public List<GenerationRule> getRules(Object prototype) {
		return prototypeMethodsHandler.getRules(prototype);
	}

	public List<GenerationRule> getRulesFromPrototypes(Object... prototypes) {
		List<GenerationRule> result = new ArrayList<GenerationRule>();
		for (Object each : prototypes) {
			List<GenerationRule> rules = getRules(each);
			if (rules == null) {
				logger.debug("no rules contained in prototypeMethodsHandler for object " + each
						+ " was this object created using ObjectG.prototype(Class)?");
			} else {
				result.addAll(rules);
			}
		}
		return result;
	}

	public void clear() {
		this.prototypeMethodsHandler.clear();
	}

	private static class CallSetterHandler implements InvocationHandler {

		private PrototypeMethodsHandler configurationHandler;

		public CallSetterHandler(PrototypeMethodsHandler configurationHandler) {
			this.configurationHandler = configurationHandler;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (args != null && Methods.isSetterMethod(method.getName(), args.length)) {
				configurationHandler.onSetter(proxy, args[0], getPropertyName(method.getName()));
			}
			if (Methods.isGetterMethod(method.getName())) {
				return configurationHandler.newPrototypeFromGetter(proxy, method.getReturnType()
						, getPropertyName(method.getName()));
			}
			//HashCode and Equals need to work, so we could put this proxies into hashSet
			if (ReflectionUtils.isHashCodeMethod(method)) {
				return System.identityHashCode(this);
			}
			if (ReflectionUtils.isEqualsMethod(method)) {
				return proxy == args[0];
			}
			return null;
		}
	}

}
