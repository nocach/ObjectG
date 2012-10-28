package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.Generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 *     Thread safe generator of java native types such as String, int, etc.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
class PrimitiveGenerator extends Generator {
    //TODO: allow resetting of this sequences? E.g. this reset can be performed before any new TestCase is about to
    //be started
    private final AtomicLong longSequence = new AtomicLong();
    private final AtomicInteger shortSequence = new AtomicInteger();
    private final AtomicInteger charSequence = new AtomicInteger();
    private final AtomicBoolean booleanSequence = new AtomicBoolean();

    @Override
    protected Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
        try {
            //TODO: can be optimized by using hashmap Map<Class, Sequence>
            Class generatedClass = context.getClassThatIsGenerated();
            if (String.class.equals(generatedClass)) return nextString();
            if (isLong(generatedClass)) return longSequence.incrementAndGet();
            if (isInteger(generatedClass)) return (int)longSequence.incrementAndGet();
            if (isDouble(generatedClass)) return (double)longSequence.incrementAndGet();
            if (isFloat(generatedClass)) return (float)longSequence.incrementAndGet();
            if (isByte(generatedClass)) return (byte)longSequence.incrementAndGet();
            if (isChar(generatedClass)) return returnNextCharOrThrow();
            if (BigDecimal.class.equals(generatedClass)) return returnNextBigDecimal();
            if (BigInteger.class.equals(generatedClass)) return new BigInteger(nextString());
            if (isBoolean(generatedClass)) return booleanSequence.getAndSet(!booleanSequence.get());
            if (Date.class.equals(generatedClass)) return new Date(longSequence.incrementAndGet());
            if (isSqlDate(generatedClass)) return new java.sql.Date(longSequence.incrementAndGet());
            if (isShort(generatedClass)) return (short)shortSequence.incrementAndGet();
            if (StringBuffer.class.equals(generatedClass)) return new StringBuffer(nextString());
            if (StringBuilder.class.equals(generatedClass)) return new StringBuilder(nextString());
            if (Void.class.equals(generatedClass)) return null;
            if (URL.class.equals(generatedClass)) return new URL("http://site"+nextString()+".org");
            if (Object.class.equals(generatedClass)) return new Object();
            throw new IllegalArgumentException("can't generate value of type " + context);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("can't generate value of type " + context, e);
        }
    }

    private String nextString() {
        return Long.toString(longSequence.incrementAndGet());
    }

    private boolean isSqlDate(Class clazz) {
        return java.sql.Date.class.equals(clazz);
    }

    private BigDecimal returnNextBigDecimal() {
        return BigDecimal.valueOf(longSequence.incrementAndGet());
    }

    private Object returnNextCharOrThrow() {
        for(int tryCount = 0; tryCount < 100000; tryCount++){
            int charCode = charSequence.incrementAndGet();
            if (Character.isDefined(charCode)) return Character.toChars(charCode)[0];
        }
        throw new IllegalStateException("could not generate new unique char, char sequence is at "
                + charSequence.get());
    }

    @Override
    public boolean supportsType(Class type) {
        //TODO: can be optimized by using hashmap Map<Class, Boolean>
        if (isInteger(type)
                || isDouble(type)
                || isLong(type)
                || isFloat(type)
                || isByte(type)
                || isChar(type)
                || String.class.isAssignableFrom(type)
                || isBoolean(type)
                || BigDecimal.class.equals(type)
                || BigInteger.class.equals(type)
                || Date.class.equals(type)
                || isSqlDate(type)
                || isShort(type)
                || StringBuilder.class.equals(type)
                || StringBuffer.class.equals(type)
                || Void.class.equals(type)
                || Object.class.equals(type)
                || URL.class.equals(type)){
            return true;
        }
        return false;
    }

    private boolean isShort(Class type) {
        return short.class.equals(type) || Short.class.equals(type);
    }

    private boolean isBoolean(Class type) {
        return boolean.class.equals(type) || Boolean.class.equals(type);
    }

    private boolean isChar(Class type) {
        return Character.class.equals(type) || char.class.equals(type);
    }

    private boolean isFloat(Class type) {
        return Float.class.equals(type) || float.class.equals(type);
    }

    private boolean isByte(Class type) {
        return Byte.class.equals(type) || byte.class.equals(type);
    }

    private boolean isLong(Class type) {
        return Long.class.equals(type)|| long.class.equals(type);
    }

    private boolean isDouble(Class type) {
        return type.equals(Double.class) || type.equals(double.class);
    }

    private boolean isInteger(Class type) {
        return type.equals(Integer.class) || type.equals(int.class);
    }
}
