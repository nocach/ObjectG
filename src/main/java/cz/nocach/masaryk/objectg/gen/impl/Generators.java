package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.gen.Generator;

/**
 * User: __nocach
 * Date: 2.10.12
 */
public class Generators {
    public static Generator map(){
        return new MapGenerator();
    }

    public static Generator list(){
        return new ListGenerator();
    }

    public static Generator nativeClass(){
        return new NativeClassGenerator();
    }

    public static Generator notNativeClass(){
        return new NotNativeClassGenerator();
    }

    public static Generator set(){
        return new SetGenerator();
    }

    public static Generator enumeration(){
        return new EnumerationGenerator();
    }
}
