package org.objectg.gen.impl;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.Generator;
import org.objectg.gen.GeneratorRegistry;

/**
 * User: __nocach
 * Date: 27.10.12
 */
class ArrayGenerator extends Generator {
    @Override
    protected Object generateValue(GenerationConfiguration configuration, GenerationContext context){
        try {
            return createArray(configuration, context);
        } catch (ClassNotFoundException e) {
            throw new GenerationException(e, configuration, context);
        }
    }

    private Class getArrayObjectClass(Class arrayType) {
        if (arrayType.equals(int[].class)) return int.class;
        if (arrayType.equals(double[].class)) return double.class;
        if (arrayType.equals(long[].class)) return long.class;
        if (arrayType.equals(float[].class)) return float.class;
        if (arrayType.equals(byte[].class)) return byte.class;
        if (arrayType.equals(char[].class)) return char.class;
        if (arrayType.equals(boolean[].class)) return boolean.class;
        if (arrayType.equals(short[].class)) return short.class;
        try {
            return Class.forName(arrayType.getName().substring(2, arrayType.getName().length() - 1));
        } catch (ClassNotFoundException e) {
            throw new GenerationException(e);
        }
    }

    private <T, U> T createArray(GenerationConfiguration configuration, GenerationContext context) throws ClassNotFoundException {
        Class arrayType = context.getClassThatIsGenerated();
        if (arrayType.equals(int[].class))  {
            int[] result = new int[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Integer>generate(configuration, context.push(int.class));
            }
            return (T)result;
        }
        if (arrayType.equals(double[].class)) {
            double[] result = new double[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Double>generate(configuration, context.push(double.class));
            }
            return (T)result;
        }
        if (arrayType.equals(long[].class)) {
            long[] result = new long[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Long>generate(configuration, context.push(long.class));
            }
            return (T)result;
        }
        if (arrayType.equals(float[].class)) {
            float[] result = new float[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Float>generate(configuration, context.push(float.class));
            }
            return (T)result;
        }
        if (arrayType.equals(byte[].class)) {
            byte[] result = new byte[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Byte>generate(configuration, context.push(byte.class));
            }
            return (T)result;
        }
        if (arrayType.equals(char[].class)) {
            char[] result = new char[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Character>generate(configuration, context.push(char.class));
            }
            return (T)result;
        }
        if (arrayType.equals(boolean[].class)) {
            boolean[] result = new boolean[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Boolean>generate(configuration, context.push(boolean.class));
            }
            return (T)result;
        }
        if (arrayType.equals(short[].class)) {
            short[] result = new short[configuration.getObjectsInCollections()];
            for (int i = 0; i < configuration.getObjectsInCollections(); i++){
                result[i] = GeneratorRegistry.getInstance().<Short>generate(configuration, context.push(short.class));
            }
            return (T)result;
        }
        //magic magic magic
        Class<U> arrayObjectClass = (Class<U>)Class.forName(arrayType.getName().substring(2, arrayType.getName().length() - 1));
        U[] array = (U[])Array.newInstance(arrayObjectClass, configuration.getObjectsInCollections());
        for (int i = 0; i < configuration.getObjectsInCollections(); i++){
            array[i] = GeneratorRegistry.getInstance().<U>generate(configuration, context.push(getArrayObjectClass(arrayType)));
        }
        return (T)array;
    }

    @Override
    public boolean supportsType(Class type) {
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
}
