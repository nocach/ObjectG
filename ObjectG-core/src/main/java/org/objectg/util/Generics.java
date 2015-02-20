package org.objectg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import com.google.common.reflect.TypeToken;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class Generics {
	public static Class extractTypeFromField(Field field, int typeVarIndex) {
		if (noGenericInfoInClass(field.getGenericType())) return null;
		Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
		return getClassFromGeneric(actualTypeArguments[typeVarIndex], typeVarIndex);
	}

	public static Class getClassFromGeneric(final Type actualTypeArgument, final int typeVarIndex) {
		Type typeOfVar = actualTypeArgument;
		if (typeOfVar instanceof WildcardType) {
			try {
				return TypeToken.of(((WildcardType) typeOfVar).getLowerBounds()[typeVarIndex]).getRawType();
			} catch (IndexOutOfBoundsException e) {
				//ok, no lower bound, use then upper bound
				return TypeToken.of(((WildcardType) typeOfVar).getUpperBounds()[typeVarIndex]).getRawType();
			}
		}
		return TypeToken.of(typeOfVar).getRawType();
	}

	private static boolean noGenericInfoInClass(Type type) {
		return type == null || type.getClass().equals(Class.class);
	}

	public static Class extractTypeFromGetter(Method getter, int typeVarIndex) {
		if (noGenericInfoInClass(getter.getGenericReturnType())) return null;
		Type[] actualTypeArguments = ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments();
		return getClassFromGeneric(actualTypeArguments[typeVarIndex], typeVarIndex);
	}

	public static Class extractTypeFromSetter(Method writeMethod, int typeVarIndex) {
		if (writeMethod.getGenericParameterTypes().length == 0 || noGenericInfoInClass(
				writeMethod.getGenericParameterTypes()[0])) {
			return null;
		}
		Type[] actualTypeArguments = ((ParameterizedType) writeMethod.getGenericParameterTypes()[0])
				.getActualTypeArguments();
		return getClassFromGeneric(actualTypeArguments[typeVarIndex], typeVarIndex);
	}
}
