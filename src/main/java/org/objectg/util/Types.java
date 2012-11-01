package org.objectg.util;

/**
 * User: __nocach
 * Date: 5.10.12
 */
public class Types {
    public static boolean isJavaType(Class clazz){
        return clazz.getPackage() == null
                || clazz.getPackage().getName().startsWith("java.")
                || clazz.getPackage().getName().startsWith("javax.");
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

}
