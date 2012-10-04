package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;
import cz.nocach.masaryk.objectg.gen.Generator;

import java.math.BigDecimal;
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
class NativeClassGenerator extends Generator {
    //TODO: allow resetting of this sequences? E.g. this reset can be performed before any new TestCase is about to
    //be started
    private final AtomicLong longSequence = new AtomicLong();
    private final AtomicInteger charSequence = new AtomicInteger();
    private final AtomicBoolean booleanSequence = new AtomicBoolean();

    @Override
    protected Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
        if (String.class.equals(context.getClassThatIsGenerated())) return Long.toString(longSequence.incrementAndGet());
        if (isLong(context.getClassThatIsGenerated())) return longSequence.incrementAndGet();
        if (isInteger(context.getClassThatIsGenerated())) return (int)longSequence.incrementAndGet();
        if (isDouble(context.getClassThatIsGenerated())) return (double)longSequence.incrementAndGet();
        if (isFloat(context.getClassThatIsGenerated())) return (float)longSequence.incrementAndGet();
        if (isByte(context.getClassThatIsGenerated())) return (byte)longSequence.incrementAndGet();
        if (isChar(context.getClassThatIsGenerated())) return returnNextCharOrThrow();
        if (BigDecimal.class.isAssignableFrom(context.getClassThatIsGenerated())) return returnNextBigDecimal();
        if (isBoolean(context.getClassThatIsGenerated())) return booleanSequence.getAndSet(!booleanSequence.get());
        throw new IllegalArgumentException("can't generate value of type " + context);
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
        if (isInteger(type)
                || isDouble(type)
                || isLong(type)
                || isFloat(type)
                || isByte(type)
                || isChar(type)
                || String.class.isAssignableFrom(type)
                || isBoolean(type)
                || BigDecimal.class.isAssignableFrom(type)){
            return true;
        }
        return false;
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
