package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import cz.nocach.masaryk.objectg.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private static final Logger logger = LoggerFactory.getLogger(NotNativeClassGenerator.class);
    private FakeInterfaceFactory fakeInterfaceFactory = new FakeInterfaceFactory();

    @Override
    public Object generateValue(final GenerationConfiguration configuration, final GenerationContext context) {
        try {
            final Object resultObj = createResultInstance(configuration, context);
            setValuesOnFields(configuration, context, resultObj);
            return resultObj;
        } catch (InstantiationException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        }
    }

    private void setValuesOnFields(final GenerationConfiguration configuration, final GenerationContext context, final Object resultObj) {
        try {
            final List<Field> allFieldsToSetValues = getFieldForValueSetting(context);
            final Map<String, PropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(context);
            for (Field each : allFieldsToSetValues){
                GenerationContext pushedContext = context.push(each.getType(), resultObj);
                pushedContext.setField(each);
                pushedContext.setFieldPropertyDescriptor(propertyDescriptorMap.get(each.getName()));
                Object generatedValueForField = generateForHierarchy(configuration, pushedContext);
                each.set(resultObj, generatedValueForField);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *
     * @param context
     * @return key is propertyName, value is PropertyDescriptor for propertyName
     */
    private Map<String, PropertyDescriptor> getPropertyDescriptorMap(GenerationContext context) {
        Map<String, PropertyDescriptor> result = new HashMap<String, PropertyDescriptor>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(context.getClassThatIsGenerated(), Object.class);
            for (PropertyDescriptor each : beanInfo.getPropertyDescriptors()){
                result.put(each.getName(), each);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private List<Field> getFieldForValueSetting(GenerationContext context) {
        final List<Field> allFieldsToSetValues = new LinkedList<Field>();
        ReflectionUtils.FieldCallback generateValueForField = new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                field.setAccessible(true);
                allFieldsToSetValues.add(field);
            }

        };
        ReflectionUtils.FieldFilter filteringStaticAndFinals = new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {
                return !Modifier.isStatic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers());
            }
        };
        ReflectionUtils.doWithFields(context.getClassThatIsGenerated(), generateValueForField, filteringStaticAndFinals);
        return allFieldsToSetValues;
    }

    private Object createResultInstance(GenerationConfiguration configuration, GenerationContext context) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (context.getClassThatIsGenerated().isInterface()){
            Object instance = fakeInterfaceFactory.create(context.getClassThatIsGenerated());
            context.setClassThatIsGenerated(instance.getClass());
            return instance;
        }
        Constructor constructor = getConstructorWithMostArgs(context.getClassThatIsGenerated());
        return constructor.newInstance(createUniqueConstructorArgs(configuration, context, constructor));
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
        return generateForHierarchy(configuration, pushedContext);
    }

    private Object generateForHierarchy(GenerationConfiguration configuration, GenerationContext pushedContext) {
        Object result = GeneratorRegistry.getInstance().generate(configuration, pushedContext);
        return result;
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
        return !Types.isJavaType(type);
    }
}
