package org.objectg.util;

import javassist.CtClass;

/**
 * User: __nocach
 * Date: 5.10.12
 */
public class Types {
    public static boolean isJavaType(Class clazz){
        return clazz.getPackage() == null
                || clazz.getPackage().getName().startsWith("java.")
                || clazz.getPackage().getName().startsWith("javax.")
				|| Enum.class.isAssignableFrom(clazz);
    }

    /**
     *
     * @param clazz not null
     * @return all interfaces implemented by the passed class,
     *      plus clazz itself if it is Interface as well.
     */
    public static Class<?>[] getAllInterfaces(Class clazz){
        if (!clazz.isInterface()) return clazz.getInterfaces();
        Class[] interfaces = new Class[clazz.getInterfaces().length + 1];
        System.arraycopy(clazz.getInterfaces(), 0, interfaces, 0, clazz.getInterfaces().length);
        int oneNewInterfaceIndex = clazz.getInterfaces().length;
        interfaces[oneNewInterfaceIndex] = clazz;
        return interfaces;
    }


	public static boolean isPrimitive(Class clazz){
		return     boolean.class.equals(clazz)
				|| char.class.equals(clazz)
				|| byte.class.equals(clazz)
				|| short.class.equals(clazz)
				|| int.class.equals(clazz)
				|| long.class.equals(clazz)
				|| float.class.equals(clazz)
				|| double.class.equals(clazz)
				;
	}

	public static boolean  isPrimitive(CtClass ctClass){
		return     CtClass.booleanType.equals(ctClass)
				|| CtClass.charType.equals(ctClass)
				|| CtClass.byteType.equals(ctClass)
				|| CtClass.shortType.equals(ctClass)
				|| CtClass.intType.equals(ctClass)
				|| CtClass.longType.equals(ctClass)
				|| CtClass.floatType.equals(ctClass)
				|| CtClass.doubleType.equals(ctClass)
				;
	}
}
