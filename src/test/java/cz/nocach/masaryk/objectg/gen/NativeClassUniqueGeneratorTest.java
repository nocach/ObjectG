package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class NativeClassUniqueGeneratorTest extends Assert{

    private Generator generator;

    @Before
    public void setup(){
        generator = new NativeClassGenerator();
    }

    @Test
    public void primitivesAreUnique(){
        generator = new NativeClassGenerator();
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
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        int intValue = generator.generate(generationConfiguration, new GenerationContext<Integer>(int.class));
        Integer intValueRef = generator.generate(generationConfiguration, new GenerationContext<Integer>(Integer.class));
        long longValue = generator.generate(generationConfiguration, new GenerationContext<Long>(long.class));
        Long longValueRef = generator.generate(generationConfiguration, new GenerationContext<Long>(Long.class));
        double doubleValue = generator.generate(generationConfiguration, new GenerationContext<Double>(double.class));
        Double doubleValueRef = generator.generate(generationConfiguration, new GenerationContext<Double>(Double.class));
        float floatValue = generator.generate(generationConfiguration, new GenerationContext<Float>(float.class));
        Float floatValueRef = generator.generate(generationConfiguration, new GenerationContext<Float>(Float.class));
        byte byteValue = generator.generate(generationConfiguration, new GenerationContext<Byte>(byte.class));
        Byte byteValueRef = generator.generate(generationConfiguration, new GenerationContext<Byte>(Byte.class));
        char charValue = generator.generate(generationConfiguration, new GenerationContext<Character>(char.class));
        Character charValueRef = generator.generate(generationConfiguration, new GenerationContext<Character>(Character.class));
        String stringValue = generator.generate(generationConfiguration, new GenerationContext<String>(String.class));
    }

    @Test
    public void canGenerateManyUniqueCharacters(){
        NativeClassGenerator nativeClassUniqueGenerator = new NativeClassGenerator();
        int expectToGenerateUniqueChars = 100000;
        Set<Character> chars = new HashSet<Character>(expectToGenerateUniqueChars);
        for (int i = 0; i < expectToGenerateUniqueChars; i++){
            int beforeInsert = chars.size();
            Character generated = (Character) nativeClassUniqueGenerator
                    .generate(new GenerationConfiguration(), new GenerationContext(Character.class));
            chars.add(generated);
            assertNotSame("failed on i="+i, beforeInsert, chars.size());
        }
    }

    @Test
    @Ignore
    public void mustThrowWhenNoNextUniqueByteIsAvailable(){
        //TODO: same tests must exist for long, int, etc
        fail("not implemented");
    }

    private void assertUnique(Class type) {
        assertTrue("expect to support type "+ type.getName(), generator.supportsType(type));
        Object firstValue = ObjectG.unique(type);
        Object secondValue = ObjectG.unique(type);
        assertNotSame("expect to generate unique values for type "+type.getName(), firstValue, secondValue);
    }
}
