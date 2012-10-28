package cz.nocach.masaryk.objectg.gen.postproc;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.ExtendedPropertyDescriptor;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.PostProcessor;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.el.ValueExpression;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

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
        //TODO: add logging here on how we make things
        SimpleContext context = new SimpleContext(new SimpleResolver());

        factory.createValueExpression(context, "#{root}", Object.class).setValue(context, generatedObject);
        ValueExpression targetValueExpression = factory.createValueExpression(context
                , "#{root" + "." + targetPropertyExpression + "}", Object.class);
        Object targetObject = targetValueExpression.getValue(context);

        Object targetParentObject = getTargetParentObject(context);
        Assert.notNull(targetParentObject, "target's parent object can't be null");

        final String targetPropertyName = getTargetPropertyName();

        PropertyDescriptor targetPropertyDesc = getTargetPropertyDescription(targetParentObject, targetPropertyName);

        Field targetField = ReflectionUtils.findField(targetParentObject.getClass(), targetPropertyName);

        Class targetType = getTargetPropertyType(targetObject, targetPropertyDesc, targetField);

        if (targetType == null){
            throw new IllegalArgumentException("could not infer target expression type " +
                    "for propertyExpression="+targetPropertyExpression);
        }

        GenerationContext generationContext = createGenerationContext(targetParentObject, targetPropertyDesc, targetField, targetType);

        Object handledPropertyValue = handler.handle(configuration, generatedObject, generationContext);
        targetValueExpression.setValue(context, handledPropertyValue);

        return generatedObject;
    }

    private String targetParentExpression() {
        return getTargetPropertyExpression();
    }

    private GenerationContext createGenerationContext(Object targetParentObject, PropertyDescriptor targetPropertyDesc, Field targetField, Class targetType) {
        GenerationContext generationContext = GenerationContext.createRoot(targetType);
        generationContext.setField(targetField);
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
