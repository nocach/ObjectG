package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * User: __nocach
 * Date: 30.8.12
 */
public class NotNativeClassGenerator implements Generator {
    @Override
    public Object generate(Class type) {
        try {
            Constructor constructor = getConstructorWithMostArgs(type);
            final Object resultObj = constructor.newInstance(createUniqueConstructorArgs(constructor));
            ReflectionUtils.doWithFields(type, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    Object generatedValueForField = findAndGenerate(field.getType());
                    field.setAccessible(true);
                    field.set(resultObj, generatedValueForField);
                }
            });
            return resultObj;
        } catch (InstantiationException e) {
            throw new RuntimeException("can't create new instance of "+type.getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't create new instance of "+type.getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("can't create new instance of "+type.getName(), e);
        }
    }

    private Object[] createUniqueConstructorArgs(Constructor constructor) {
        Object []constructorParams = new Object[constructor.getParameterTypes().length];
        for (int i = 0; i < constructor.getParameterTypes().length; i++){
            Class paramType = constructor.getParameterTypes()[i];
            constructorParams[i] = findAndGenerate(paramType);
        }
        return constructorParams;
    }

    private Object findAndGenerate(Class paramType) {
        return GeneratorRegistry.getInstance().find(getGenerationContext(paramType), paramType).generate(paramType);
    }

    /**
     * subclasses return GenerationContext suitable for their needs
     * @param generatingClass class for which generator should be found.
     * @return
     */
    protected GenerationContext getGenerationContext(Class generatingClass) {
        return null;
    }

    private Constructor getConstructorWithMostArgs(Class type) {
        int maxArgs = 0;
        Constructor maxArgsConstructor = null;
        for (Constructor each : type.getConstructors()) {
            if (each.getParameterTypes().length >= maxArgs){
                maxArgs = each.getParameterTypes().length;
                maxArgsConstructor = each;
            }
        }
        if (maxArgsConstructor == null){
            throw new IllegalArgumentException("no constructor found for type " + type.getName());
        }
        return maxArgsConstructor;
    }

    @Override
    public boolean supportsType(Class type) {
        //TODO: return !Types.isJavaType(type)
        return false;
    }
}
