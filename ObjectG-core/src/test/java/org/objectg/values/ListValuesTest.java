package org.objectg.values;

import org.junit.Test;
import org.objectg.BaseObjectGTest;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: __nocach
 * Date: 21.10.12
 */
public class ListValuesTest extends BaseObjectGTest {

    @Test
    public void canExcludeListValues(){
        String[] values = Values.list("one", "two", "three").without("two").values();

        assertEquals(2, values.length);
        assertTrue("should contain one", asList(values).contains("one"));
        assertTrue("should contan three", asList(values).contains("three"));
    }

    @Test
    public void canExcludeNone(){
        String[] values = Values.list("one", "two", "three").without().values();
        assertEquals(3, values.length);
    }

    @Test
    public void canCreateEmpty(){
        Object[] empty = Values.list().without().values();

        assertEquals(0, empty.length);
    }
}
