package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.conf.exception.ConfigurationException;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.util.Types;
import javassist.*;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

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
    public static final String FIELD_NAME_OF_PROTOTYPE_CREATOR = "$objectgPrototypeCreator";
    public static final int SETTER_PREFIX_LENGTH = 3;
    public static final int GETTER_PREFIX_LENGTH = 3;
    private PrototypeSetterHandler configurationHandler;

    public PrototypeCreator(){
        this.configurationHandler = new PrototypeSetterHandler();
    }

    public <T> T newPrototype(Class<T> clazz){
        if (Types.isJavaType(clazz)){
            throw new ConfigurationException("can't create configurator for java type");
        }
        try {
            Class interceptedClass = interceptClass(clazz);

            T modifiedInstance = (T)createdInterceptedClass(interceptedClass);
            configurationHandler.onInit(modifiedInstance);
            return modifiedInstance;
        } catch (NotFoundException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        } catch (CannotCompileException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            //TODO: review exception throw
            throw new RuntimeException(e);
        }
    }

    private <T> T createdInterceptedClass(Class interceptedClass) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        T modifiedInstance = (T) interceptedClass.newInstance();

        Field handlerField = modifiedInstance.getClass().getDeclaredField(FIELD_NAME_OF_PROTOTYPE_HANDLER);
        ReflectionUtils.setField(handlerField, modifiedInstance, configurationHandler);

        Field setterConfiguratorField = modifiedInstance.getClass().getDeclaredField(FIELD_NAME_OF_PROTOTYPE_CREATOR);
        ReflectionUtils.setField(setterConfiguratorField, modifiedInstance, this);

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
        addPropertyCreatorField(interceptedClass);
        interceptSetters(interceptedClass);
        interceptGetters(interceptedClass);
        return interceptedClass.toClass();
    }

    private void addPropertyCreatorField(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        CtClass setterConfiguratorCtType = ClassPool.getDefault().get(getClass().getName());
        CtField ctField = new CtField(setterConfiguratorCtType, FIELD_NAME_OF_PROTOTYPE_CREATOR, interceptedClass);
        ctField.setModifiers(Modifier.PUBLIC);
        interceptedClass.addField(ctField);
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
        if (!isVoidReturn){
            String propertyName = extractPropertyNameOfGetterSetter(method);
            /**
             * {
             *  return prototypeHandler.newPrototypeFromGetter(this, ReturnType.class, propertyName, prototypeCreator);
             * }
             */
            String interceptedBody = "{"
                        + "return ($r)"
                            + FIELD_NAME_OF_PROTOTYPE_HANDLER + ".newPrototypeFromGetter("
                            + "this"
                            + ", " + method.getReturnType().getName() + ".class"
                            + ", \""+propertyName+"\""
                            + ", " + FIELD_NAME_OF_PROTOTYPE_CREATOR + ""
                         + ");"
                    + "}";
            method.setBody(interceptedBody);
        }
    }

    private boolean isGetterMethod(CtMethod each) {
        return each.getName().startsWith("get")
                && each.getName().length() > GETTER_PREFIX_LENGTH;
    }

    private void interceptSetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        for (CtMethod each : interceptedClass.getDeclaredMethods()){
            if (isSetterMethod(each)){
                setBodyToInvokeConfigurationHandler(each);
            }
        }

    }

    private boolean isSetterMethod(CtMethod each) throws NotFoundException {
        return each.getName().startsWith("set")
                && each.getName().length() > SETTER_PREFIX_LENGTH
                && each.getParameterTypes().length == 1;
    }

    private void setBodyToInvokeConfigurationHandler(CtMethod each) throws NotFoundException, CannotCompileException {
        String propertyName = extractPropertyNameOfGetterSetter(each);
        boolean isVoidReturn = isVoidReturn(each);
        //basically it looks like this:
        // {
        //   prototypeHandler.onSetter(this, $firstArg, propertyName);
        //   return;
        // }
        each.insertBefore(
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
        String propertyNameNotFormatted = each.getName().substring(SETTER_PREFIX_LENGTH, each.getName().length());
        return new StringBuilder()
                //first letter in lowercase
                .append(Character.toLowerCase(propertyNameNotFormatted.charAt(0)))
                //all that is after first letter of property name
                .append(each.getName().substring(SETTER_PREFIX_LENGTH+1, each.getName().length()))
                .toString();
    }

    private <T> CtClass createInterceptedClass(Class<T> clazz) throws NotFoundException, CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass interceptedClass = classPool.get(clazz.getName());
        interceptedClass.setName(getNameForInterceptedClass(clazz));

        //set intercepted class as child of original class, so type check is valid
        CtClass originalClass = classPool.get(clazz.getName());
        interceptedClass.setSuperclass(originalClass);

        CtClass[] interfaces = new CtClass[interceptedClass.getInterfaces().length + 1];
        System.arraycopy(interceptedClass.getInterfaces(), 0, interfaces, 0, interceptedClass.getInterfaces().length);
        int oneNewInterfaceIndex = interceptedClass.getInterfaces().length;
        interfaces[oneNewInterfaceIndex] = classPool.get(InterceptedBySetterConfigurator.class.getName());
        interceptedClass.setInterfaces(interfaces);

        return interceptedClass;
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

    public List<GenerationRule> getRules(Object configObject) {
        return configurationHandler.getRules(configObject);
    }
}
