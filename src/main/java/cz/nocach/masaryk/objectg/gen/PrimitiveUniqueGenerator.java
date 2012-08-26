package cz.nocach.masaryk.objectg.gen;

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
public class PrimitiveUniqueGenerator implements UniqueGenerator {
    private static final AtomicLong longSequence = new AtomicLong();
    @Override
    public Object generate(Class type) {
        if (isInteger(type)){
            return longSequence.incrementAndGet();
        }
        return null;
    }

    @Override
    public boolean supportsType(Class type) {
        if (isInteger(type)){
            return true;
        }
        return false;
    }

    private boolean isInteger(Class type) {
        return type.equals(Integer.class) || type.equals(int.class);
    }
}
