package org.objectg.gen;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Person;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class NativeClassUniqueGeneratorTest extends BaseObjectGTest {

    @Test
    public void primitivesAreUnique(){
        assertUnique(Integer.class);
        assertUnique(Integer[].class);
        assertUnique(int.class);
        assertUnique(int[].class);
        assertUnique(Double.class);
        assertUnique(Double[].class);
        assertUnique(double.class);
        assertUnique(double[].class);
        assertUnique(Long.class);
        assertUnique(Long[].class);
        assertUnique(long.class);
        assertUnique(long[].class);
        assertUnique(Float.class);
        assertUnique(Float[].class);
        assertUnique(float.class);
        assertUnique(float[].class);
        assertUnique(Byte.class);
        assertUnique(Byte[].class);
        assertUnique(byte.class);
        assertUnique(byte[].class);
        assertUnique(Character.class);
        assertUnique(Character[].class);
        assertUnique(char.class);
        assertUnique(char[].class);
        assertUnique(String.class);
        assertUnique(String[].class);
        assertUnique(BigDecimal.class);
        assertUnique(BigDecimal[].class);
        assertUnique(BigInteger.class);
        assertUnique(BigInteger[].class);
        assertUnique(Boolean.class);
        assertUnique(Boolean[].class);
        assertUnique(boolean.class);
        assertUnique(boolean[].class);
        assertUnique(Date.class);
        assertUnique(Date[].class);
        assertUnique(java.sql.Date.class);
        assertUnique(java.sql.Date[].class);
        assertUnique(short.class);
        assertUnique(short[].class);
        assertUnique(Short.class);
        assertUnique(Short[].class);
        assertUnique(StringBuilder.class);
        assertUnique(StringBuilder[].class);
        assertUnique(StringBuffer.class);
        assertUnique(StringBuffer[].class);
        assertUnique(Object.class);
        assertUnique(Object[].class);
        assertUnique(URL.class);
        assertUnique(URL[].class);
		assertUnique(InputStream.class);
    }

	@Test
	public void forProviderSpecificTypesNullIsGenerated(){
		assertNull(ObjectG.unique(java.sql.Blob.class));
		assertNull(ObjectG.unique(java.sql.Array.class));
		assertNull(ObjectG.unique(java.sql.Clob.class));
		assertNull(ObjectG.unique(java.sql.ResultSet.class));
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
        short shortValue = ObjectG.unique(short.class);
        Short shortRefValue = ObjectG.unique(Short.class);
        URL[] urlArray = ObjectG.unique(URL[].class);
    }

    @Test
    public void forVoidOnlyNullIsGenerated(){
        assertNull("first value", ObjectG.unique(Void.class));
        assertNull("second value", ObjectG.unique(Void.class));
    }

    private void assertUnique(Class type) {
        Object firstValue = ObjectG.unique(type);
        Object secondValue = ObjectG.unique(type);
        assertNotSame("expect to generate unique values for type "+type.getName(), firstValue, secondValue);
    }

	@Test
	public void stringWillHavePropertyNameInValue(){
		final Person unique1 = ObjectG.unique(Person.class);
		final Person unique2 = ObjectG.unique(Person.class);

		assertTrue("string property should have property name in generated value",
				unique1.getFirstName().contains("firstName"));
		assertTrue("string property should have property name in generated value",
				unique2.getFirstName().contains("firstName"));
		assertNotSame("string property should be unique in generation session",
				unique1.getFirstName(), unique2.getFirstName());
	}
}
