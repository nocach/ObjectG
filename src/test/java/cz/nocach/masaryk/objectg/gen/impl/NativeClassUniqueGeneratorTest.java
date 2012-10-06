package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class NativeClassUniqueGeneratorTest extends Assert{

    @Test
    public void primitivesAreUnique(){
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
        assertUnique(BigDecimal.class);
        assertUnique(Boolean.class);
        assertUnique(boolean.class);
        //TODO: fill other types
    }

    @Test
    public void generatedPrimitivesCanBeAssigned(){
        int intValue = ObjectG.unique(int.class);
        Integer intValueRef = ObjectG.unique(Integer.class);
        long longValue = ObjectG.unique(long.class);
        Long longValueRef = ObjectG.unique(Long.class);
        double doubleValue = ObjectG.unique(double.class);
        Double doubleValueRef = ObjectG.unique(Double.class);
        float floatValue = ObjectG.unique(float.class);
        Float floatValueRef = ObjectG.unique(Float.class);
        byte byteValue = ObjectG.unique(byte.class);
        Byte byteValueRef = ObjectG.unique(Byte.class);
        char charValue = ObjectG.unique(char.class);
        Character charValueRef = ObjectG.unique(Character.class);
        String stringValue = ObjectG.unique(String.class);
        boolean booleanValue = ObjectG.unique(boolean.class);
        Boolean booleanRefValue = ObjectG.unique(Boolean.class);
    }

    @Test
    @Ignore
    public void mustThrowWhenNoNextUniqueByteIsAvailable(){
        //TODO: same tests must exist for long, int, etc
        //must allow in generation configuration if to use strict mode (throwing exception)
        //or to revert sequence to the start and continue in generating
        fail("not implemented");

    }

    private void assertUnique(Class type) {
        Object firstValue = ObjectG.unique(type);
        Object secondValue = ObjectG.unique(type);
        assertNotSame("expect to generate unique values for type "+type.getName(), firstValue, secondValue);
    }
}
