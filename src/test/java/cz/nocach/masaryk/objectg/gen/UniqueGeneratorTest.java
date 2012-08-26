package cz.nocach.masaryk.objectg.gen;

import junit.framework.Assert;
import org.junit.Test;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class UniqueGeneratorTest extends Assert{

    @Test
    public void primitives(){
        PrimitiveUniqueGenerator primitiveUniqueGenerator = new PrimitiveUniqueGenerator();
        assertUnique(primitiveUniqueGenerator, Integer.class);
        assertUnique(primitiveUniqueGenerator, int.class);
        //TODO: fill other types
    }

    private void assertUnique(Generator uniqueGenerator, Class type) {
        assertTrue("expect to support type "+ type.getName(), uniqueGenerator.supportsType(type));
        assertNotSame("expect to generate unique values for type "+type.getName(),
                uniqueGenerator.generate(type), uniqueGenerator.generate(type));
    }
}
