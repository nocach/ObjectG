package cz.nocach.masaryk.objectg.gen;

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
    public void afterStartNotCycle(){
        hierarchy.push(ClassA.class);
        assertFalse(hierarchy.isCycle());
    }

    @Test
    public void simpleCycle(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        hierarchy.push(ClassA.class);
        assertTrue(hierarchy.isCycle());
    }

    @Test
    public void noCycleTest(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        hierarchy.push(ClassC.class);
        assertFalse(hierarchy.isCycle());
    }

    @Test
    public void innerCycleTest(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        hierarchy.push(ClassC.class);
        hierarchy.push(ClassB.class);
        assertTrue(hierarchy.isCycle());
    }

    @Test
    public void canPopAfterCycle(){
        hierarchy.push(ClassA.class);
        hierarchy.push(ClassB.class);
        hierarchy.push(ClassA.class);
        assertTrue(hierarchy.isCycle());
        hierarchy.pop();
        assertFalse(hierarchy.isCycle());
    }

    public static class ClassA{}
    public static class ClassB{}
    public static class ClassC{}

}
