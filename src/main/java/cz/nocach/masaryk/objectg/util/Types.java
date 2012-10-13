package cz.nocach.masaryk.objectg.util;

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

}
