package cz.nocach.masaryk.objectg.gen.conf;

import javassist.*;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class SetterConfigurator {

    public static final String FIELD_NAME_OF_CONFIGURATION_HANDLER = "$objectgConfigurationHandler";
    public static final int SETTER_PREFIX_LENGTH = 3;
    private static final AtomicLong interceptorNameSequence = new AtomicLong();
    private ConfigurationHandler configurationHandler;

    public SetterConfigurator(ConfigurationHandler configurationHandler){
        Assert.notNull(configurationHandler, "configurationHandler");
        this.configurationHandler = configurationHandler;
    }

    public <T> T newConfigurator(Class<T> clazz){
        try {
            Class interceptedClass = interceptClass(clazz);

            T modifiedInstance = (T) interceptedClass.newInstance();
            Field field = modifiedInstance.getClass().getDeclaredField(FIELD_NAME_OF_CONFIGURATION_HANDLER);
            ReflectionUtils.setField(field, modifiedInstance, configurationHandler);
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

    private <T> Class interceptClass(Class<T> originalClass) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        if (ClassPool.getDefault().getOrNull(getNameForInterceptedClass(originalClass)) != null){
            //if we have already intercepted this class, then no need to create new one
            return Class.forName(getNameForInterceptedClass(originalClass));
        }
        CtClass interceptedClass = createInterceptedClass(originalClass);
        addHandlerField(interceptedClass);
        interceptSetters(interceptedClass);
        return interceptedClass.toClass();
    }

    private void interceptSetters(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        ClassPool classPool= ClassPool.getDefault();
        CtClass vVoidClass = classPool.get(Void.class.getName());
        CtClass vvoidClass = classPool.get(void.class.getName());
        for (CtMethod each : interceptedClass.getDeclaredMethods()){
            if (each.getName().startsWith("set") && each.getName().length() > SETTER_PREFIX_LENGTH){
                setBodyToInvokeConfigurationHandler(vVoidClass, vvoidClass, each);
            }
        }

    }

    private void setBodyToInvokeConfigurationHandler(CtClass vVoidClass, CtClass vvoidClass, CtMethod each) throws NotFoundException, CannotCompileException {
        String propertyName = extractPropertyNameOfSetter(each);
        boolean isVoidReturn = each.getReturnType().equals(vVoidClass) || each.getReturnType().equals(vvoidClass);
        String returnType = isVoidReturn ? "" : "null";
        //basically it looks like this:
        // {
        //   configurationHandler.onSetter(this, propertyName);
        //   return;
        // }
        each.setBody(
                "{" +
                    FIELD_NAME_OF_CONFIGURATION_HANDLER +".onSetter(this, \""+propertyName+"\");" +
                    "return " + returnType + " ;" +
                "}");
    }

    private String extractPropertyNameOfSetter(CtMethod each) {
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

        return interceptedClass;
    }

    private <T> String getNameForInterceptedClass(Class<T> clazz) {
        return clazz.getName() + "Intercepted";
    }

    private void addHandlerField(CtClass interceptedClass) throws NotFoundException, CannotCompileException {
        CtClass configurationHandlerCtType = ClassPool.getDefault().get(ConfigurationHandler.class.getName());
        CtField ctField = new CtField(configurationHandlerCtType, FIELD_NAME_OF_CONFIGURATION_HANDLER, interceptedClass);
        ctField.setModifiers(Modifier.PUBLIC);
        interceptedClass.addField(ctField);
    }
}
