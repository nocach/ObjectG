package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * User: __nocach
 * Date: 30.8.12
 */
class NotNativeClassGenerator implements Generator {
    private GenerationConfiguration generationConfiguration;

    public NotNativeClassGenerator(GenerationConfiguration generationConfiguration){
        this.generationConfiguration = generationConfiguration;
    }
    @Override
    public Object generate(Class type) {
        try {
            Constructor constructor = getConstructorWithMostArgs(type);
            final Object resultObj = constructor.newInstance(createUniqueConstructorArgs(constructor));
            ReflectionUtils.doWithFields(type, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    Object generatedValueForField = findAndGenerate(field);
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
        return GeneratorRegistry.getInstance().find(generationConfiguration, new GenerationContext(paramType)).generate(paramType);
    }

    private Object findAndGenerate(Field field) {
        GenerationContext generationContext = new GenerationContext(field.getType());
        generationContext.setField(field);
        return GeneratorRegistry.getInstance().find(generationConfiguration, generationContext).generate(field.getType());
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
