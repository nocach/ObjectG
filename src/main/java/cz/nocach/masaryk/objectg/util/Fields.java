package cz.nocach.masaryk.objectg.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class Fields {
    public static Type extractTypeFromGenerics(Field field , int typeVarIndex){
        Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        // TypeVariableImpl, WildcardTypeImple
        Type firstType = actualTypeArguments[typeVarIndex];
        return firstType;
    }
}
