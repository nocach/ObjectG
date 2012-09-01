package cz.nocach.masaryk.objectg.gen;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class NativeClassUniqueGeneratorTest extends Assert{

    private UniqueGenerator generator;

    @Before
    public void setup(){
        generator = new NativeClassUniqueGenerator();
    }

    @Test
    public void primitivesAreUnique(){
        generator = new NativeClassUniqueGenerator();
        assertUnique(Integer.class);
        assertUnique(int.class);
        assertUnique(Double.class);
        assertUnique(double.class);
        assertUnique(Long.class);
        assertUnique(long.class);
        assertUnique(Float.class);
        assertUnique(float.class);
        assertUnique(Byte.class);
        assertUnique(byte.class);
        assertUnique(Character.class);
        assertUnique(char.class);
        assertUnique(String.class);
        //TODO: fill other types
    }

    @Test
    public void generatedPrimitivesCanBeAssigned(){
        int intValue = generator.generate(int.class);
        Integer intValueRef = generator.generate(Integer.class);
        long longValue = generator.generate(long.class);
        Long longValueRef = generator.generate(Long.class);
        double doubleValue = generator.generate(double.class);
        Double doubleValueRef = generator.generate(Double.class);
        float floatValue = generator.generate(float.class);
        Float floatValueRef = generator.generate(Float.class);
        byte byteValue = generator.generate(byte.class);
        Byte byteValueRef = generator.generate(Byte.class);
        char charValue = generator.generate(char.class);
        Character charValueRef = generator.generate(Character.class);
        String stringValue = generator.generate(String.class);
    }

    @Test
    public void canGenerateManyUniqueCharacters(){
        NativeClassUniqueGenerator nativeClassUniqueGenerator = new NativeClassUniqueGenerator();
        int expectToGenerateUniqueChars = 100000;
        Set<Character> chars = new HashSet<Character>(expectToGenerateUniqueChars);
        for (int i = 0; i < expectToGenerateUniqueChars; i++){
            int beforeInsert = chars.size();
            Character generated = (Character) nativeClassUniqueGenerator.generate(Character.class);
            chars.add(generated);
            assertNotSame("failed on i="+i, beforeInsert, chars.size());
        }
    }

    @Test
    public void mustThrowWhenNoNextUniqueByteIsAvailable(){
        //TODO: same tests must exist for long, int, etc
        fail("not implemented");
    }

    private void assertUnique(Class type) {
        assertTrue("expect to support type "+ type.getName(), generator.supportsType(type));
        assertNotSame("expect to generate unique values for type "+type.getName(),
                generator.generate(type), generator.generate(type));
    }
}
