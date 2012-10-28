package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.util.Generics;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>
 *     Provides more information about property
 * </p>
 * <p>
 * User: __nocach
 * Date: 27.10.12
 * </p>
 */
public class ExtendedPropertyDescriptor {
    private Field propertyField;
    private final PropertyDescriptor propertyDescriptor;

    public ExtendedPropertyDescriptor(Field propertyField, PropertyDescriptor propertyDescriptor){
        this.propertyField = propertyField;
        this.propertyDescriptor = propertyDescriptor;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public Method getReadMethod() {
        if (propertyDescriptor != null) return propertyDescriptor.getReadMethod();
        return null;
    }

    public Method getWriteMethod() {
        if (propertyDescriptor != null) return propertyDescriptor.getWriteMethod();
        return null;
    }

    public Type getGenericTypeFromMethodsSignature(int typeVarIndex) {
        Type genericType = extractGenericTypeFromGetter(typeVarIndex);
        if (genericType == null){
            genericType = extractGenericTypeFromSetter(typeVarIndex);
        }
        return genericType;
    }

    private Type extractGenericTypeFromSetter(int typeVarIndex) {
        if (getWriteMethod() == null) return null;
        Type genericType = Generics.extractTypeFromSetter(getWriteMethod(), typeVarIndex);
        if (genericType == null){
            //what if interfaces of parent object have generic info hints?
            for (Class each : propertyField.getDeclaringClass().getInterfaces()){
                Method methodWithGenericInfo = ReflectionUtils.findMethod(each, getWriteMethod().getName(), getWriteMethod().getParameterTypes());
                if (methodWithGenericInfo != null){
                    genericType = Generics.extractTypeFromSetter(methodWithGenericInfo, typeVarIndex);
                }
            }
        }
        return genericType;
    }


    private Type extractGenericTypeFromGetter(int typeVarIndex) {
        if (getReadMethod() == null) return null;
        Type genericType;
        genericType = Generics.extractTypeFromGetter(getReadMethod(), typeVarIndex);
        if (genericType == null){
            //what if interfaces of parent object have generic info hints?
            for (Class each : propertyField.getDeclaringClass().getInterfaces()){
                Method methodWithGenericInfo = ReflectionUtils.findMethod(each, getReadMethod().getName());
                if (methodWithGenericInfo != null){
                    genericType = Generics.extractTypeFromGetter(methodWithGenericInfo, typeVarIndex);
                }
            }
        }
        return genericType;
    }

    public Class<?> getMostSpecificPropertyType() {
        Class result = propertyField.getType();
        final Set<Class> possibleTypes = new HashSet<Class>();
        ReflectionUtils.doWithMethods(propertyField.getDeclaringClass(),
                addPossibleSubtypesFromSettersAndGetters(possibleTypes),
                leaveOnlyPropertySetterOrGetter());

        return getLowestFromHierarchy(result, possibleTypes);
    }

    private Class<?> getLowestFromHierarchy(Class result, Set<Class> possibleTypes) {
        Map<Class, Integer> hierarchyPlaceMap = getHierarchyPlaceMap(result, possibleTypes);
        Integer maxIndex = Collections.max(hierarchyPlaceMap.values());
        for (Map.Entry<Class, Integer> each : hierarchyPlaceMap.entrySet()){
            if (each.getValue().equals(maxIndex)) return each.getKey();
        }
        return result;
    }

    private Map<Class, Integer> getHierarchyPlaceMap(Class baseClass, Set<Class> possibleSubTypes) {
        Map<Class, Integer> result = new HashMap<Class, Integer>();
        result.put(baseClass, 0);
        for (Class each : possibleSubTypes){
            result.put(each, getHierarchyPlace(baseClass, each));
        }
        return result;
    }

    private Integer getHierarchyPlace(Class baseClass, Class each) {
        int result = 0;
        Class current = each;
        while (current != baseClass){
            result++;
            current = current.getSuperclass();
        }
        return result;
    }

    private ReflectionUtils.MethodCallback addPossibleSubtypesFromSettersAndGetters(final Set<Class> possibleTypes) {
        return new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                final Class<?> candidateType;
                if (method.getName().startsWith("set")){
                    candidateType = method.getParameterTypes()[0];
                }
                else {
                    candidateType= method.getReturnType();
                }
                if (propertyField.getType().isAssignableFrom(candidateType)){
                    possibleTypes.add(candidateType);
                }
            }
        };
    }

    private ReflectionUtils.MethodFilter leaveOnlyPropertySetterOrGetter() {
        return new ReflectionUtils.MethodFilter() {
                    @Override
                    public boolean matches(Method method) {
                        String propertyCapitalized = StringUtils.capitalize(propertyField.getName());
                        return method.getName().equals("get"+propertyCapitalized)
                                || (method.getName().equals("set"+propertyCapitalized)
                                    && method.getParameterTypes().length == 1);
                    }
                };
    }
}
