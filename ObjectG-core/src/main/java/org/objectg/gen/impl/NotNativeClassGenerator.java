package org.objectg.gen.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.ExtendedPropertyDescriptor;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.Generator;
import org.objectg.gen.GeneratorRegistry;
import org.objectg.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

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
            throw new GenerationException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (IllegalAccessException e) {
            throw new GenerationException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        } catch (InvocationTargetException e) {
            throw new GenerationException("can't create new instance of "+context.getClassThatIsGenerated().getName(), e);
        }
    }

    private void setValuesOnFields(final GenerationConfiguration configuration, final GenerationContext context, final Object resultObj) {
            final List<Field> allFieldsToSetValues = getFieldForValueSetting(context);
            final Map<String, ExtendedPropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(context);
            for (Field each : allFieldsToSetValues){
                ExtendedPropertyDescriptor fieldPropertyDesc = getPropertyDescriptorForField(each, propertyDescriptorMap);
                GenerationContext pushedContext = context.push(getMostSpecificFieldType(each, fieldPropertyDesc), resultObj);
                pushedContext.setField(each);
                pushedContext.setFieldPropertyDescriptor(fieldPropertyDesc);
                Object generatedValueForField = generateForHierarchy(configuration, pushedContext);
				tryToSetValueOnField(resultObj, each, generatedValueForField);
			}
    }

	private void tryToSetValueOnField(final Object resultObj, final Field field, final Object generatedValueForField) {
		try {
			field.set(resultObj, generatedValueForField);
		} catch (IllegalAccessException e) {
			throw new GenerationException("could not set value on field="+field, e);
		}
	}

	private Class<?> getMostSpecificFieldType(Field each, ExtendedPropertyDescriptor fieldPropertyDesc) {
        if (fieldPropertyDesc != null) return fieldPropertyDesc.getMostSpecificPropertyType();
        return each.getType();
    }

    private ExtendedPropertyDescriptor getPropertyDescriptorForField(Field field, Map<String, ExtendedPropertyDescriptor> propertyDescriptorMap) {
        return propertyDescriptorMap.get(field.getName());
    }

    /**
     *
     * @param context
     * @return key is propertyName, value is PropertyDescriptor for propertyName
     */
    private Map<String, ExtendedPropertyDescriptor> getPropertyDescriptorMap(GenerationContext context) {
        Map<String, ExtendedPropertyDescriptor> result = new HashMap<String, ExtendedPropertyDescriptor>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(context.getClassThatIsGenerated(), Object.class);
            for (PropertyDescriptor each : beanInfo.getPropertyDescriptors()){
                Field filedProperty = ReflectionUtils.findField(context.getClassThatIsGenerated(), each.getName());
                result.put(each.getName(), new ExtendedPropertyDescriptor(filedProperty, each));
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
		return createClassInstance(configuration, context);
    }

	private Object createClassInstance(final GenerationConfiguration configuration, final GenerationContext context)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		try{
			Constructor constructor = getConstructorWithMostArgs(context.getClassThatIsGenerated());
			constructor.setAccessible(true);
			return constructor.newInstance(createUniqueConstructorArgs(configuration, context, constructor));
		}
		//ok, most args constructor was not ok
		catch (InvocationTargetException e){
			logger.debug("most arg constructor did not work, trying other constructors");
			return createInstanceFromFirstSuccessfulConstructor(configuration, context);

		}
	}

	private Object createInstanceFromFirstSuccessfulConstructor(final GenerationConfiguration configuration,
			final GenerationContext context) throws InstantiationException, IllegalAccessException {
		//then we will try ALL constructors if any will work
		final Constructor[] allConstructors = context.getClassThatIsGenerated().getDeclaredConstructors();
		for (Constructor each : allConstructors){
			try{
				each.setAccessible(true);
				return each.newInstance(createUniqueConstructorArgs(configuration, context, each));
			}
			catch (InvocationTargetException ignore){
			}
		}
		throw new GenerationException("no workable constructor found", context);
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
        for (Constructor each : type.getDeclaredConstructors()) {
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
