package org.objectg.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javassist.CtClass;

/**
 * User: __nocach
 * Date: 5.10.12
 */
public class Types {
	public static boolean isJavaType(Class clazz) {
		return clazz.getPackage() == null
				|| clazz.getPackage().getName().startsWith("java.")
				|| clazz.getPackage().getName().startsWith("javax.")
				|| Enum.class.isAssignableFrom(clazz);
	}

	/**
	 * @param clazz not null
	 * @return all interfaces implemented by the passed class,
	 *         plus clazz itself if it is Interface as well.
	 */
	public static Class<?>[] getAllInterfaces(Class clazz) {
		if (!clazz.isInterface()) return clazz.getInterfaces();
		Class[] interfaces = new Class[clazz.getInterfaces().length + 1];
		System.arraycopy(clazz.getInterfaces(), 0, interfaces, 0, clazz.getInterfaces().length);
		int oneNewInterfaceIndex = clazz.getInterfaces().length;
		interfaces[oneNewInterfaceIndex] = clazz;
		return interfaces;
	}


	public static boolean isPrimitive(Class clazz) {
		return boolean.class.equals(clazz)
				|| char.class.equals(clazz)
				|| byte.class.equals(clazz)
				|| short.class.equals(clazz)
				|| int.class.equals(clazz)
				|| long.class.equals(clazz)
				|| float.class.equals(clazz)
				|| double.class.equals(clazz)
				;
	}

	public static boolean isPrimitive(CtClass ctClass) {
		return CtClass.booleanType.equals(ctClass)
				|| CtClass.charType.equals(ctClass)
				|| CtClass.byteType.equals(ctClass)
				|| CtClass.shortType.equals(ctClass)
				|| CtClass.intType.equals(ctClass)
				|| CtClass.longType.equals(ctClass)
				|| CtClass.floatType.equals(ctClass)
				|| CtClass.doubleType.equals(ctClass)
				;
	}

	/**
	 * @param type
	 * @return if passed type is an array
	 */
	public static boolean isArray(Class type) {
		//TODO: could be optimized with hash map
		return type.equals(Integer[].class)
				|| type.equals(int[].class)
				|| type.equals(Double[].class)
				|| type.equals(double[].class)
				|| type.equals(Long[].class)
				|| type.equals(long[].class)
				|| type.equals(Float[].class)
				|| type.equals(float[].class)
				|| type.equals(Byte[].class)
				|| type.equals(byte[].class)
				|| type.equals(Character[].class)
				|| type.equals(char[].class)
				|| type.equals(String[].class)
				|| type.equals(BigDecimal[].class)
				|| type.equals(BigInteger[].class)
				|| type.equals(Boolean[].class)
				|| type.equals(boolean[].class)
				|| type.equals(Date[].class)
				|| type.equals(java.sql.Date[].class)
				|| type.equals(short[].class)
				|| type.equals(Short[].class)
				|| type.equals(StringBuilder[].class)
				|| type.equals(StringBuffer[].class)
				|| type.getName().startsWith("[L");
	}

	/**
	 * @param arrayType
	 * @return type of the array's elements
	 * @throws ClassNotFoundException
	 */
	public static Class getArrayElementClass(Class arrayType) throws ClassNotFoundException {
		if (arrayType.equals(int[].class)) return int.class;
		if (arrayType.equals(double[].class)) return double.class;
		if (arrayType.equals(long[].class)) return long.class;
		if (arrayType.equals(float[].class)) return float.class;
		if (arrayType.equals(byte[].class)) return byte.class;
		if (arrayType.equals(char[].class)) return char.class;
		if (arrayType.equals(boolean[].class)) return boolean.class;
		if (arrayType.equals(short[].class)) return short.class;
		return Class.forName(arrayType.getName().substring(2, arrayType.getName().length() - 1));
	}
}
