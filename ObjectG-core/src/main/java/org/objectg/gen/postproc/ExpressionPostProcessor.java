package org.objectg.gen.postproc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import javax.el.ValueExpression;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.ExtendedPropertyDescriptor;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.access.FieldAccessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * User: __nocach
 * Date: 27.10.12
 */
public class ExpressionPostProcessor implements PostProcessor {

    private static final javax.el.ExpressionFactory factory = new de.odysseus.el.ExpressionFactoryImpl();

    private Handler handler;
    private String targetPropertyExpression;

    public ExpressionPostProcessor(String targetPropertyExpression, Handler handler){
        Assert.notNull(targetPropertyExpression, "targetPropertyExpression");
        Assert.notNull(handler, "handler");
        this.targetPropertyExpression = targetPropertyExpression;
        this.handler = handler;
    }
    public <T> T process(GenerationConfiguration configuration, T generatedObject){
		try{
			Assert.isTrue(!targetPropertyExpression.trim().isEmpty(), "targetPropertyExpression should not be empty");
			//TODO: add logging here on how we make things
			SimpleContext context = new SimpleContext(new SimpleResolver());

			setRootObject(generatedObject, context);
			ValueExpression targetValueExpression = createTargetExpression(context);
			Object targetObject = targetValueExpression.getValue(context);

			Object targetParentObject = getTargetParentObject(context);
			Assert.notNull(targetParentObject, "target's parent object can't be null");

			final String targetPropertyName = getTargetPropertyName();
			final Object handledPropertyValue;
			//expression resulted into some attribute of object (e.g. person.name)
			if (targetPropertyName != null){
				handledPropertyValue = createValueForTargetProperty(configuration, generatedObject, targetObject,
						targetParentObject, targetPropertyName);
			}
			else {
				//expression resulted in object itself (e.g. persons[0] - we generate instance of Person)
				if (targetObject != null){
					final GenerationContext targetObjectGenContext = GenerationContext.createRoot(targetObject.getClass());
					handledPropertyValue = handler.handle(configuration, generatedObject, targetObjectGenContext);
				}
				else {
					throw new GenerationException("could not infer type for expression: "+targetPropertyExpression);
				}
			}

			targetValueExpression.setValue(context, handledPropertyValue);

			return generatedObject;
		}
		catch (Exception e){
			throw new PostProcessingException("could not post process " +
					"generatedObject="+generatedObject+
					", configuration="+configuration, e);
		}
    }

	private <T> Object createValueForTargetProperty(final GenerationConfiguration configuration,
			final T generatedObject,
			final Object targetObject, final Object targetParentObject, final String targetPropertyName) {
		PropertyDescriptor targetPropertyDesc = getTargetPropertyDescription(targetParentObject, targetPropertyName);

		Field targetField = ReflectionUtils.findField(targetParentObject.getClass(), targetPropertyName);

		Class targetType = getTargetPropertyType(targetObject, targetPropertyDesc, targetField);

		if (targetType == null){
			throw new IllegalArgumentException("could not infer target expression type " +
					"for propertyExpression="+targetPropertyExpression);
		}

		GenerationContext generationContext = createGenerationContext(targetParentObject, targetPropertyDesc, targetField, targetType);

		return handler.handle(configuration, generatedObject, generationContext);
	}

	private ValueExpression createTargetExpression(final SimpleContext context) {
		return factory.createValueExpression(context
                , "#{root" + "." + targetPropertyExpression + "}", Object.class);
	}

	private <T> void setRootObject(final T generatedObject, final SimpleContext context) {
		factory.createValueExpression(context, "#{root}", Object.class).setValue(context, generatedObject);
	}

	private String targetParentExpression() {
        return getTargetPropertyExpression();
    }

    private GenerationContext createGenerationContext(Object targetParentObject, PropertyDescriptor targetPropertyDesc, Field targetField, Class targetType) {
        GenerationContext generationContext = GenerationContext.createRoot(targetType);
		//TODO: probably should decide here if to use FieldAccessor or MethodAccessor
		generationContext.setPropertyAccessor(new FieldAccessor(targetField));
        generationContext.setFieldPropertyDescriptor(new ExtendedPropertyDescriptor(targetField, targetPropertyDesc));
        generationContext.setParentObject(targetParentObject);
        return generationContext;
    }

    private Object getTargetParentObject(SimpleContext context) {
        ValueExpression targetParentValueExpression = factory.createValueExpression(context
                , "#{root" + targetParentExpression() + "}", Object.class);
        return targetParentValueExpression.getValue(context);
    }

    private Class getTargetPropertyType(Object targetObject, PropertyDescriptor targetPropertyDesc, Field targetField) {
        Class targetType = null;
        if (targetField != null){
            targetType = targetField.getType();
        } else if (targetPropertyDesc != null){
            targetType = targetPropertyDesc.getPropertyType();
        } else if (targetObject != null){
            targetType = targetObject.getClass();
        }
        //TODO: for root=person and propertyExpression=employee2Address[0] we will return null
        //but in future we can try to examine Generic info
        //but more refactoring is required to do this
        return targetType;
    }

    private PropertyDescriptor getTargetPropertyDescription(Object targetParentObject, String targetPropertyName) {
        BeanInfo parentBeanInfo = null;
        try {
            parentBeanInfo = Introspector.getBeanInfo(targetParentObject.getClass(), Object.class);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor targetPropertyDesc = null;
        for (PropertyDescriptor each : parentBeanInfo.getPropertyDescriptors()){
            if (each.getName().equals(targetPropertyName)){
                targetPropertyDesc = each;
                break;
            }
        }
        return targetPropertyDesc;
    }

    private String getTargetPropertyExpression() {
        int lastPropertySeparation = targetPropertyExpression.lastIndexOf(".");
        String targetParentExpression = "";
        if (lastPropertySeparation != -1){
            targetParentExpression = "."+targetPropertyExpression.substring(0, lastPropertySeparation);
        }
        return targetParentExpression;
    }

    private String getTargetPropertyName() {
        int lastPropertySeparation = targetPropertyExpression.lastIndexOf(".");
        final String targetPropertyName;
        if (lastPropertySeparation == -1){
            if (targetPropertyExpression.contains("[")){
                targetPropertyName = null;
            }
            else {
                targetPropertyName = targetPropertyExpression;
            }
        }
        else {
            targetPropertyName = targetPropertyExpression.substring(lastPropertySeparation + 1);
        }
        return targetPropertyName;
    }

    public interface Handler{
        /**
         *
         * @param configuration
         * @param generatedObject
         * @param contextForExpression
         * @return value that will be set for the propertyExpression
         */
        public Object handle(GenerationConfiguration configuration, Object generatedObject, GenerationContext contextForExpression);
    }
}
