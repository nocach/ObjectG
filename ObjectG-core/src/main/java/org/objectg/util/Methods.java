package org.objectg.util;

/**
 * User: __nocach
 * Date: 3.2.13
 */
public class Methods {
	public static final int SETTER_PREFIX_LENGTH = 3;
	public static final int GETTER_PREFIX_LENGTH = 3;

	public static boolean isGetterMethod(String methodName) {
		return methodName.startsWith("get")
				&& methodName.length() > GETTER_PREFIX_LENGTH;
	}

	public static boolean isSetterMethod(String methodName, int paramLength){
		return methodName.startsWith("set")
				&& methodName.length() > SETTER_PREFIX_LENGTH
				&& paramLength == 1;
	}

	public static String getPropertyName(String methodName) {
		String propertyNameNotFormatted = methodName.substring(SETTER_PREFIX_LENGTH, methodName.length());
		return new StringBuilder()
				//first letter in lowercase
				.append(Character.toLowerCase(propertyNameNotFormatted.charAt(0)))
						//all that is after first letter of property name
				.append(methodName.substring(SETTER_PREFIX_LENGTH + 1, methodName.length()))
				.toString();
	}
}
