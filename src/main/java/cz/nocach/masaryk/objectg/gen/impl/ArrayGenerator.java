package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.Generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * User: __nocach
 * Date: 27.10.12
 */
class ArrayGenerator extends Generator {
    private Generator primitiveGenerator;

    public ArrayGenerator(Generator primitiveGenerator){
        this.primitiveGenerator = primitiveGenerator;
    }
    @Override
    protected <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> context) {
        //TODO: can be optimized with HashMap
        Class type = context.getClassThatIsGenerated();
        if (type.equals(Integer[].class)) return (T) new Integer[]{generatePrimitive(configuration, context, Integer.class)};
        if (type.equals(int[].class)) return (T) new int[]{generatePrimitive(configuration, context, int.class)};
        if (type.equals(Double[].class)) return (T) new Double[]{generatePrimitive(configuration, context, Double.class)};
        if (type.equals(double[].class)) return (T) new double[]{generatePrimitive(configuration, context, double.class)};
        if (type.equals(Long[].class)) return (T) new Long[]{generatePrimitive(configuration, context, Long.class)};
        if (type.equals(long[].class)) return (T) new long[]{generatePrimitive(configuration, context, long.class)};
        if (type.equals(Float[].class)) return (T) new Float[]{generatePrimitive(configuration, context, Float.class)};
        if (type.equals(float[].class)) return (T) new float[]{generatePrimitive(configuration, context, float.class)};
        if (type.equals(Byte[].class)) return (T) new Byte[]{generatePrimitive(configuration, context, Byte.class)};
        if (type.equals(byte[].class)) return (T) new byte[]{generatePrimitive(configuration, context, byte.class)};
        if (type.equals(Character[].class)) return (T) new Character[]{generatePrimitive(configuration, context, Character.class)};
        if (type.equals(char[].class)) return (T) new char[]{generatePrimitive(configuration, context, char.class)};
        if (type.equals(String[].class)) return (T) new String[]{generatePrimitive(configuration, context, String.class)};
        if (type.equals(BigDecimal[].class)) return (T) new BigDecimal[]{generatePrimitive(configuration, context, BigDecimal.class)};
        if (type.equals(BigInteger[].class)) return (T) new BigInteger[]{generatePrimitive(configuration, context, BigInteger.class)};
        if (type.equals(Boolean[].class)) return (T) new Boolean[]{generatePrimitive(configuration, context, Boolean.class)};
        if (type.equals(boolean[].class)) return (T) new boolean[]{generatePrimitive(configuration, context, boolean.class)};
        if (type.equals(Date[].class)) return (T) new Date[]{generatePrimitive(configuration, context, Date.class)};
        if (type.equals(java.sql.Date[].class)) return (T) new java.sql.Date[]{generatePrimitive(configuration, context, java.sql.Date.class)};
        if (type.equals(short[].class)) return (T) new short[]{generatePrimitive(configuration, context, short.class)};
        if (type.equals(Short[].class)) return (T) new Short[]{generatePrimitive(configuration, context, Short.class)};
        if (type.equals(StringBuilder[].class)) return (T) new StringBuilder[]{generatePrimitive(configuration, context, StringBuilder.class)};
        if (type.equals(StringBuffer[].class)) return (T) new StringBuffer[]{generatePrimitive(configuration, context, StringBuffer.class)};
        if (type.equals(Object[].class)) return (T) new Object[]{generatePrimitive(configuration, context, Object.class)};
        throw new IllegalArgumentException("can't generate value of type " + context);
    }

    private <T> T generatePrimitive(GenerationConfiguration configuration, GenerationContext<?> context, Class<T> clazz) {
        return primitiveGenerator.generate(configuration, context.push(clazz));
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
            || type.equals(Object[].class);
    }
}
