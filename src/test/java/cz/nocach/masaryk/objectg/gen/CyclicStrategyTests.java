package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * User: __nocach
 * Date: 2.10.12
 */
public class CyclicStrategyTests {

    @Test
    public void backReferenceStrategyBasic(){
        ClassA classA = ObjectG.unique(ClassA.class, ObjectG
                .config()
                .backReferenceCycle()
                .done());

        assertNotNull(classA.getClassB());
        assertEquals("classB must have classA set as first generated ClassA",
                classA, classA.getClassB().getClassA());
    }

    @Test
    public void backReferenceStrategyCollection(){
        ClassARefCollectionB instance = ObjectG.unique(ClassARefCollectionB.class,
                ObjectG.config().backReferenceCycle().done());

        assertNotNull("list must be set", instance.getClassBList());
        assertEquals("generated list be not empty", 1, instance.getClassBList().size());
        ClassB firstClassB = instance.getClassBList().get(0);
        assertEquals("back reference must be set in collection generated objects",
                instance, firstClassB.getClassARefCollectionB());
    }

    @Test
    public void worksWithConfiguredObjects(){
        ClassA prototypeClassA = ObjectG.prototype(ClassA.class);
        prototypeClassA.setClassB(ObjectG.prototype(ClassB.class));

        ClassA generatedA = ObjectG.unique(ClassA.class, ObjectG
                .config()
                .backReferenceCycle()
                .done(), prototypeClassA);

        assertEquals(generatedA, generatedA.getClassB().getClassA());
    }

    @Test
    public void nullCyclicStrategy(){
        ClassA classA = ObjectG.unique(ClassA.class, ObjectG.config().nullCycle().done());
        assertNotNull("classB should be generated", classA.getClassB());
        assertNull("classA referenced in ClassB should be null, because of cycle", classA.getClassB().getClassA());
    }

    @Test
    public void nullCyclicStrategyLeavesCollectionAsEmpty(){
        ClassB classB = ObjectG.unique(ClassB.class, ObjectG.config().nullCycle().done());
        assertEquals(0, classB.getClassARefCollectionB().getClassBList().size());
    }

    public static class ClassA{
        private ClassB classB;

        public ClassB getClassB() {
            return classB;
        }
        public void setClassB(ClassB classB) {
            this.classB = classB;
        }
    }

    public static class ClassARefCollectionB {
        private List<ClassB> classBList;

        public List<ClassB> getClassBList() {
            return classBList;
        }

        public void setClassBList(List<ClassB> classBList) {
            this.classBList = classBList;
        }
    }

    public static class ClassB{
        private ClassA classA;
        private ClassARefCollectionB classARefCollectionB;

        public ClassA getClassA() {
            return classA;
        }
        public void setClassA(ClassA classA) {
            this.classA = classA;
        }

        public ClassARefCollectionB getClassARefCollectionB() {
            return classARefCollectionB;
        }

        public void setClassARefCollectionB(ClassARefCollectionB classARefCollectionB) {
            this.classARefCollectionB = classARefCollectionB;
        }
    }
}
