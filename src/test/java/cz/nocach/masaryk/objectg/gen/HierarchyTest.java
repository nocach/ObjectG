package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.Hierarchy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public class HierarchyTest {

    private Hierarchy hierarchy;

    @Before
    public void setup(){
        hierarchy = new Hierarchy();
    }

    @Test
    public void simpleCycle(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        assertTrue(hierarchy.isCycle(ClassA.class));
    }

    @Test
    public void noCycleTest(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        assertFalse(hierarchy.isCycle(ClassC.class));
    }

    @Test
    public void innerCycleTest(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        hierarchy.push(ClassC.class);
        assertTrue(hierarchy.isCycle(ClassB.class));
    }

    @Test
    public void canPopAfterCycle(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        assertTrue(hierarchy.isCycle(ClassA.class));
        hierarchy.pop();
        hierarchy.pop();
        assertFalse(hierarchy.isCycle(ClassA.class));
    }

    public static class ClassA{}
    public static class ClassB{}
    public static class ClassC{}

}
