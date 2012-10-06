package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: __nocach
 * Date: 29.8.12
 */
public class NotNativeClassGeneratorTest extends Assert{


    @Test
    public void noArgConstructor(){
        Object generated = ObjectG.unique(NoArgConstructor.class);

        assertNotNull(generated);
    }

    @Test
    public void oneArgConstructor(){
        OneArgConstructor generated = ObjectG.unique(OneArgConstructor.class);

        assertNotNull(generated);
        assertNotNull(generated.arg);
    }

    @Test
    public void betweenMultipleConstructorsWithMostArgsIsUsed(){
        MultipleConstructors generated = ObjectG.unique(MultipleConstructors.class);

        assertNotNull(generated);
        assertNotNull(generated.field1);
        assertNotNull(generated.field2);
        assertNotNull(generated.field3);
    }

    @Test
    public void fieldsInClassAreSetWithUniqueValues(){
        ClassWithMultipleField generated =  ObjectG.unique(ClassWithMultipleField.class);

        assertClassWithMultipleFieldsIsFilled(generated);
    }

    private void assertClassWithMultipleFieldsIsFilled(ClassWithMultipleField generated) {
        assertNotNull(generated);
        assertNotNull(generated.field1);
        assertNotNull(generated.field2);
        assertNotSame(generated.field1, generated.field2);
        assertNotSame(generated.field3, generated.field4);
    }

    @Test
    public void fieldsOfObjectAreGenerated(){
        ClassReferencingOtherClass generated =  ObjectG.unique(ClassReferencingOtherClass.class);

        assertNotNull(generated);
        assertClassWithMultipleFieldsIsFilled(generated.classWithMultipleField1);
        assertClassWithMultipleFieldsIsFilled(generated.classWithMultipleField2);
        assertNotSame(generated.classWithMultipleField1.field1, generated.classWithMultipleField2.field1);
    }

    @Test
    public void canConstructObjectWithCyclicDependency(){
        ObjectG.unique(CyclicClassA.class);
    }

    @Test
    public void cyclicConstructorTest(){
        ObjectG.unique(CyclicConstructor.class);
    }

    @Test
    @Ignore
    public void canConstructInterfaces(){
        fail("field IPerson");
    }

    @Test
    @Ignore
    public void canConstructAbstractClasses(){
        fail("field AbstractPerson");
    }

    public static class CyclicConstructor{
        public CyclicConstructor(CyclicConstructor arg){
        }
    }

    public static class CyclicClassA{
        private CyclicClassB classB;

        public CyclicClassB getClassB() {
            return classB;
        }

        public void setClassB(CyclicClassB classB) {
            this.classB = classB;
        }
    }

    public static class CyclicClassB{
        private CyclicClassA classA;

        public CyclicClassA getClassA() {
            return classA;
        }

        public void setClassA(CyclicClassA classA) {
            this.classA = classA;
        }
    }

    public static class NoArgConstructor{}

    public static class OneArgConstructor{
        private String arg;

        public OneArgConstructor(String arg){
            this.arg = arg;
        }
    }

    public static class MultipleConstructors{
        private String field1;
        private String field2;
        private String field3;

        public MultipleConstructors() {
        }

        public MultipleConstructors(String field1) {
            this.field1 = field1;
        }

        public MultipleConstructors(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public MultipleConstructors(String field1, String field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    public static class ClassWithMultipleField{
        private String field1;
        private String field2;
        private int field3;
        private int field4;
    }

    public static class ClassReferencingOtherClass{
        private ClassWithMultipleField classWithMultipleField1;
        private ClassWithMultipleField classWithMultipleField2;
    }
}
