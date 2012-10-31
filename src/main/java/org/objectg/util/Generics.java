package org.objectg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class Generics {
    public static Type extractTypeFromGenerics(Field field , int typeVarIndex){
        if (noGenericInfoInClass(field.getGenericType())) return null;
        Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        // TypeVariableImpl, WildcardTypeImple
        Type firstType = actualTypeArguments[typeVarIndex];
        return firstType;
    }

    private static boolean noGenericInfoInClass(Type type) {
        return type == null || type.getClass().equals(Class.class);
    }

    public static Type extractTypeFromGetter(Method getter, int typeVarIndex){
        if (noGenericInfoInClass(getter.getGenericReturnType())) return null;
        Type[] actualTypeArguments = ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        // TypeVariableImpl, WildcardTypeImple
        Type firstType = actualTypeArguments[typeVarIndex];
        return firstType;
    }

    public static Type extractTypeFromSetter(Method writeMethod, int typeVarIndex) {
        if (writeMethod.getGenericParameterTypes().length == 0 || noGenericInfoInClass(writeMethod.getGenericParameterTypes()[0])){
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) writeMethod.getGenericParameterTypes()[0]).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        // TypeVariableImpl, WildcardTypeImple
        Type firstType = actualTypeArguments[typeVarIndex];
        return firstType;
    }
}
