package org.objectg.gen.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.ExtendedPropertyDescriptor;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.access.BackedUpAccessor;
import org.objectg.gen.access.FieldAccessor;
import org.objectg.gen.access.PropertyAccessor;
import org.objectg.gen.access.MethodAccessor;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.Generator;
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
            final List<PropertyAccessor> allPropertiesToSetValues = getPropertiesForValueSetting(configuration, context);
            final Map<String, ExtendedPropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(context);
            for (PropertyAccessor each : allPropertiesToSetValues){
                ExtendedPropertyDescriptor fieldPropertyDesc = getPropertyDescriptorForField(each, propertyDescriptorMap);
                GenerationContext pushedContext = context.push(getMostSpecificFieldType(each, fieldPropertyDesc), resultObj);
				pushedContext.setPropertyAccessor(each);
                pushedContext.setFieldPropertyDescriptor(fieldPropertyDesc);
				tryToSetValueOnField(resultObj, each, configuration, pushedContext);
			}
    }

	private void tryToSetValueOnField(final Object resultObj, final PropertyAccessor propertyAccessor,
			final GenerationConfiguration configuration, final GenerationContext pushedContext) {
		try {
			Object generatedValueForField = generateForHierarchy(configuration, pushedContext);
			propertyAccessor.set(resultObj, generatedValueForField);
		} catch (IllegalAccessException e) {
			throw new GenerationException("could not set value on field="+propertyAccessor, e);
		}
		catch (SkipValueGenerationException e){
			//skip this value
		}
	}

	private Class<?> getMostSpecificFieldType(PropertyAccessor propertyAccessor,
			ExtendedPropertyDescriptor fieldPropertyDesc) {
        if (fieldPropertyDesc != null) return fieldPropertyDesc.getMostSpecificPropertyType();
        return propertyAccessor.getType();
    }

    private ExtendedPropertyDescriptor getPropertyDescriptorForField(PropertyAccessor propertyAccessor,
			Map<String, ExtendedPropertyDescriptor> propertyDescriptorMap) {
        return propertyDescriptorMap.get(propertyAccessor.getName());
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

    private List<PropertyAccessor> getPropertiesForValueSetting(final GenerationConfiguration configuration,
			GenerationContext context) {
		switch (configuration.getAccessStrategy()){
			case ONLY_FIELDS:
				return getFieldPropertyAccessors(context);
			case ONLY_METHODS:
				final boolean onlyFull = true;
				return getMethodPropertyAccessors(context, onlyFull);
			case PREFER_METHODS:
				return getPreferMethodsAccessors(context);
			default:
					throw new GenerationException("unknown AccessStrategy "+configuration.getAccessStrategy());
		}
    }

	private List<PropertyAccessor> getPreferMethodsAccessors(final GenerationContext context) {
		List<PropertyAccessor> result = new ArrayList<PropertyAccessor>();
		final List<PropertyAccessor> fieldPropertyAccessors = getFieldPropertyAccessors(context);
		boolean onlyFull = false;
		final List<PropertyAccessor> allMethodPropertyAccessors = getMethodPropertyAccessors(context, onlyFull);
		Map<String, PropertyAccessor> fieldAccessorByProperty = groupByPropertyName(fieldPropertyAccessors);
		Map<String, PropertyAccessor> methodAccessorsByProperty = groupByPropertyName(allMethodPropertyAccessors);
		//now we have method and field accessor group by property

		Set<String> usedPropertyNames = new HashSet<String>();
		for (String each : methodAccessorsByProperty.keySet()){
			final MethodAccessor methodAccessor = (MethodAccessor) methodAccessorsByProperty.get(each);
			//we will prefer method accessor: if read and write method for property exists - we use full method access
			if (methodAccessor.isFull()){
				usedPropertyNames.add(each);
				result.add(methodAccessor);
			}else{
				//if method accessor is not full (read or write method is missing - back up this accessor
				//with existing field accessor (only if such exists)
				if (fieldAccessorByProperty.containsKey(each)){
					usedPropertyNames.add(each);
					result.add(new BackedUpAccessor(methodAccessor, fieldAccessorByProperty.get(each)));
				}
			}
		}
		//finally we should add field accessors for those properties which did not receive their love in full method
		//accessor or semi-method accessor
		for (String each : fieldAccessorByProperty.keySet()) {
			if (usedPropertyNames.contains(each)) continue;
			result.add(fieldAccessorByProperty.get(each));
		}
		return result;
	}

	private Map<String, PropertyAccessor> groupByPropertyName(final List<PropertyAccessor> fieldPropertyAccessors) {
		Map<String, PropertyAccessor> result = new HashMap<String, PropertyAccessor>();
		for (PropertyAccessor each : fieldPropertyAccessors) {
			result.put(each.getName(), each);
		}
		return result;
	}

	private List<PropertyAccessor> getMethodPropertyAccessors(final GenerationContext context, boolean onlyFull){
		try{
			List<PropertyAccessor> result = new ArrayList<PropertyAccessor>();
			BeanInfo beanInfo = Introspector.getBeanInfo(context.getClassThatIsGenerated(), Object.class);
			for (PropertyDescriptor each : beanInfo.getPropertyDescriptors()){
				//skip properties that miss some access method
				if (onlyFull && (each.getReadMethod() == null || each.getWriteMethod() == null)) {
					logger.debug("will not generate value for property " + each.getName()
						+" because it does not have setter or getter");
					continue;
				}
				result.add(new MethodAccessor(each.getWriteMethod(), each.getReadMethod()));
			}
			return result;
		} catch (IntrospectionException e) {
		throw new RuntimeException(e);
		}
	}

	private List<PropertyAccessor> getFieldPropertyAccessors(final GenerationContext context) {
		final List<PropertyAccessor> allFieldsToSetValues = new LinkedList<PropertyAccessor>();
		ReflectionUtils.FieldCallback collectFieldCallback = new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				allFieldsToSetValues.add(new FieldAccessor(field));
			}

		};
		ReflectionUtils.FieldFilter filteringStaticAndFinals = new ReflectionUtils.FieldFilter() {
			@Override
			public boolean matches(Field field) {
				return !Modifier.isStatic(field.getModifiers())
						&& !Modifier.isFinal(field.getModifiers());
			}
		};
		ReflectionUtils.doWithFields(context.getClassThatIsGenerated(), collectFieldCallback, filteringStaticAndFinals);
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
		return GenerationSession.get().generate(configuration, pushedContext);
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
