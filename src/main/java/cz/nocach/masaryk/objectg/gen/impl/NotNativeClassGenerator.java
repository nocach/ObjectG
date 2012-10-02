package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 *     Generator supporting generating of not native java types (custom classes). Knows how to construct object
 *     and how to set each field on it.
 * </p>
 * <p>
 * User: __nocach
 * Date: 30.8.12
 * </p>
 */
class NotNativeClassGenerator extends Generator {

    @Override
    public Object generateValue(final GenerationConfiguration configuration, final GenerationContext context) {
        try {
            Constructor constructor = getConstructorWithMostArgs(context.getClassThatIsGenerated());
            final Object resultObj = constructor.newInstance(createUniqueConstructorArgs(configuration, context, constructor));
            ReflectionUtils.doWithFields(context.getClassThatIsGenerated(), new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    Object generatedValueForField = findAndGenerate(configuration, context, resultObj, field);
                    field.setAccessible(true);
                    field.set(resultObj, generatedValueForField);
                }

            });
            return resultObj;
        } catch (InstantiationException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        }
    }

    private Object[] createUniqueConstructorArgs(GenerationConfiguration configuration, GenerationContext context, Constructor constructor) {
        Object []constructorParams = new Object[constructor.getParameterTypes().length];
        for (int i = 0; i < constructor.getParameterTypes().length; i++){
            Class paramType = constructor.getParameterTypes()[i];
            constructorParams[i] = findAndGenerate(configuration, context, paramType);
        }
        return constructorParams;
    }

    private Object findAndGenerate(GenerationConfiguration configuration, GenerationContext context, Class paramType) {
        GenerationContext pushedContext = context.push(paramType);
        return generateForHierarchy(configuration, context, pushedContext);
    }

    private Object generateForHierarchy(GenerationConfiguration configuration, GenerationContext context, GenerationContext pushedContext) {
        if (context.isCycle()) {
            Object valueForCycle = configuration.getCycleStrategy().generateForCycle(configuration, context);
            context.pop();
            return valueForCycle;
        }
        Object result = GeneratorRegistry.getInstance().generate(configuration, pushedContext);
        context.pop();
        return result;
    }

    private Object findAndGenerate(GenerationConfiguration configuration, GenerationContext context, Object parentObject, Field field) {
        GenerationContext generationContext = context.push(field.getType());
        generationContext.setField(field);
        generationContext.setParentObject(parentObject);
        return generateForHierarchy(configuration, context, generationContext);
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
        return true;
    }
}
