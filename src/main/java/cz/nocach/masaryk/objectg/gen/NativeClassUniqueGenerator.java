package cz.nocach.masaryk.objectg.gen;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 *     Thread safe
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
public class NativeClassUniqueGenerator implements UniqueGenerator {
    private final AtomicLong longSequence = new AtomicLong();
    private final AtomicInteger charSequence = new AtomicInteger();
    @Override
    public Object generate(Class type) {
        if (String.class.equals(type)) return Long.toString(longSequence.incrementAndGet());
        if (isLong(type)) return longSequence.incrementAndGet();
        if (isInteger(type)) return (int)longSequence.incrementAndGet();
        if (isDouble(type)) return (double)longSequence.incrementAndGet();
        if (isFloat(type)) return (float)longSequence.incrementAndGet();
        if (isByte(type)) return (byte)longSequence.incrementAndGet();
        if (isChar(type)) return returnNextCharOrThrow();
        throw new IllegalArgumentException("can't generate value of type " + type);
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
                || String.class.equals(type)){
            return true;
        }
        return false;
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
