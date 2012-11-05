package org.objectg.conf.prototype;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.gen.GenerationRule;
import org.objectg.util.Types;
import org.springframework.util.ReflectionUtils;

/**
 * <p>
 *     Component allowing to perform certain configurations by using setters of objects.
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public class PrototypeCreator {

    public static final String FIELD_NAME_OF_PROTOTYPE_HANDLER = "$objectgPrototypeCreatorHandler";
    public static final int SETTER_PREFIX_LENGTH = 3;
    public static final int GETTER_PREFIX_LENGTH = 3;
    private PrototypeSetterHandler configurationHandler;
    private Map<Object, Class> proxyToRealClass = new ConcurrentHashMap<Object, Class>();

    public PrototypeCreator(){
        this.configurationHandler = new PrototypeSetterHandler(this);
    }

    public <T> T newPrototype(Class<T> clazz){
        if (Types.isJavaType(clazz)){
            throw new ConfigurationException("can't create configurator for java type");
        }
        if (clazz.isInterface()){
            return createPrototypeForInterface(clazz);
        }
        try {
            return createPrototypeForConcreteClass(clazz);
        } catch (NotFoundException e) {
            throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        } catch (CannotCompileException e) {
			throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        } catch (InstantiationException e) {
			throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        } catch (IllegalAccessException e) {
			throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        } catch (NoSuchFieldException e) {
			throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        } catch (ClassNotFoundException e) {
			throw new PrototypeCreationException("could not creation prototype for class="+clazz.getName(), e);
        }
    }

    private <T> T createPrototypeForConcreteClass(Class<T> clazz) throws NotFoundException, CannotCompileException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class interceptedClass = interceptClass(clazz);
        T modifiedInstance = (T)createdInterceptedClass(interceptedClass);
        configurationHandler.onInit(modifiedInstance);
        return modifiedInstance;
    }

    private <T> T createPrototypeForInterface(Class<T> clazz) {
        Set<Class<?>> interfacesSet = Sets.newHashSet(Types.getAllInterfaces(clazz));
        //need to mark result proxy as intercepted
        interfacesSet.add(InterceptedByPrototypeCreator.class);
        Class<?>[] interfaces = interfacesSet.toArray(new Class<?>[]{});
        T resultPrototypeInterface = (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                interfaces, new CallSetterHandler(configurationHandler));
        configurationHandler.onInit(resultPrototypeInterface);
        proxyToRealClass.put(resultPrototypeInterface, clazz);
        return resultPrototypeInterface;
    }

    private <T> T createdInterceptedClass(Class interceptedClass) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T modifiedInstance = (T) interceptedClass.newInstance();

        Field handlerField = modifiedInstance.getClass().getDeclaredField(FIELD_NAME_OF_PROTOTYPE_HANDLER);
        ReflectionUtils.setField(handlerField, modifiedInstance, configurationHandler);

        return modifiedInstance;
    }

    private <T> Class interceptClass(Class<T> originalClass)
            throws NotFoundException, CannotCompileException, ClassNotFoundException {
        if (ClassPool.getDefault().getOrNull(getNameForInterceptedClass(originalClass)) != null){
            //if we have already intercepted this class, then no need to create new one
            return Class.forName(getNameForInterceptedClass(originalClass));
        }
        CtClass interceptedClass = createInterceptedClass(originalClass);
        addHandlerField(interceptedClass);
        interceptSetters(interceptedClass);
        interceptGetters(interceptedClass);
        return interceptedClass.toClass();
    }

    public Class<?> getRealObjectClass(Object prototype) {
        if (prototype.getClass().getSuperclass().equals(java.lang.reflect.Proxy.class)){
            return proxyToRealClass.get(prototype);
        }
        return prototype.getClass().getSuperclass();
    }

    private void interceptGetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        for (CtMethod each : interceptedClass.getDeclaredMethods()){
            if (isGetterMethod(each) && !isVoidReturn(each)){
                interceptGetter(each);
            }
        }
    }

    private void interceptGetter(CtMethod method) throws NotFoundException, CannotCompileException {
        boolean isVoidReturn = isVoidReturn(method);
		if (isNotInterceptable(method)) return;
        if (!isVoidReturn){
            String propertyName = extractPropertyNameOfGetterSetter(method);
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
                            + ", \""+propertyName+"\""
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
        return isGetterMethod(methodName);
    }

    private static boolean isGetterMethod(String methodName) {
        return methodName.startsWith("get")
                && methodName.length() > GETTER_PREFIX_LENGTH;
    }

    private void interceptSetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        for (CtMethod each : interceptedClass.getDeclaredMethods()){
            if (isSetterMethod(each)){
                setBodyToInvokeConfigurationHandler(each);
            }
        }

    }

    private boolean isSetterMethod(CtMethod each) throws NotFoundException {
        return isSetterMethod(each.getName(), each.getParameterTypes().length);
    }

    private static boolean isSetterMethod(String methodName, int paramLength){
        return methodName.startsWith("set")
                && methodName.length() > SETTER_PREFIX_LENGTH
                && paramLength == 1;
    }

    private void setBodyToInvokeConfigurationHandler(CtMethod method) throws NotFoundException, CannotCompileException {
		if (isNotInterceptable(method)) return;
        String propertyName = extractPropertyNameOfGetterSetter(method);
        //basically it looks like this:
        // {
        //   prototypeHandler.onSetter(this, $firstArg, propertyName);
        //   return;
        // }
        method.insertBefore(
				"{" +
						FIELD_NAME_OF_PROTOTYPE_HANDLER + ".onSetter(this, ($w)$1, \"" + propertyName + "\");" +
						"}");
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

    private static String getPropertyName(String methodName) {
        String propertyNameNotFormatted = methodName.substring(SETTER_PREFIX_LENGTH, methodName.length());
        return new StringBuilder()
                //first letter in lowercase
                .append(Character.toLowerCase(propertyNameNotFormatted.charAt(0)))
                //all that is after first letter of property name
                .append(methodName.substring(SETTER_PREFIX_LENGTH + 1, methodName.length()))
                .toString();
    }

    private <T> CtClass createInterceptedClass(Class<T> clazz) throws NotFoundException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass interceptedClass = classPool.get(clazz.getName());
        interceptedClass.setName(getNameForInterceptedClass(clazz));

        //set intercepted class as child of original class, so type check is valid
        CtClass originalClass = classPool.get(clazz.getName());
        interceptedClass.setSuperclass(originalClass);
		removeNotInheritedMethods(interceptedClass);

        CtClass[] interfaces = new CtClass[interceptedClass.getInterfaces().length + 1];
        System.arraycopy(interceptedClass.getInterfaces(), 0, interfaces, 0, interceptedClass.getInterfaces().length);
        int oneNewInterfaceIndex = interceptedClass.getInterfaces().length;
        interfaces[oneNewInterfaceIndex] = classPool.get(InterceptedByPrototypeCreator.class.getName());
        interceptedClass.setInterfaces(interfaces);

        return interceptedClass;
    }

	private void removeNotInheritedMethods(final CtClass originalClass){
		for (CtMethod each : originalClass.getMethods()){
			if (isNotInterceptable(each) && !Modifier.isNative(each.getModifiers())){
				try{
					originalClass.removeMethod(each);
				}
				catch (NotFoundException e){
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
                PrototypeSetterHandler.class.getName());
        CtField ctField = new CtField(configurationHandlerCtType, FIELD_NAME_OF_PROTOTYPE_HANDLER, interceptedClass);
        ctField.setModifiers(Modifier.PUBLIC);
        interceptedClass.addField(ctField);
    }

    public List<GenerationRule> getRules(Object prototype) {
        return configurationHandler.getRules(prototype);
    }

    private static class CallSetterHandler implements InvocationHandler{

        private PrototypeSetterHandler configurationHandler;

        public CallSetterHandler(PrototypeSetterHandler configurationHandler) {
            this.configurationHandler = configurationHandler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null && isSetterMethod(method.getName(), args.length)){
                configurationHandler.onSetter(proxy, args[0], getPropertyName(method.getName()));
            }
            if (isGetterMethod(method.getName())){
                return configurationHandler.newPrototypeFromGetter(proxy, method.getReturnType()
                        , getPropertyName(method.getName()));
            }
            //HashCode and Equals need to work, so we could put this proxies into hashSet
            if (ReflectionUtils.isHashCodeMethod(method)){
                return System.identityHashCode(this);
            }
            if (ReflectionUtils.isEqualsMethod(method)){
                return proxy == args[0];
            }
            return null;
        }
    }
}
