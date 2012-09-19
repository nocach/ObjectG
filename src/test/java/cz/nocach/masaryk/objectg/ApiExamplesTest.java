package cz.nocach.masaryk.objectg;

import org.junit.Test;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class ApiExamplesTest {
    @Test
    public void uniqueClassWithSimpleProperties(){
        ClassWithSimpleProperties first = ObjectG.unique(ClassWithSimpleProperties.class);
        ClassWithSimpleProperties second = ObjectG.unique(ClassWithSimpleProperties.class);

        assertNotSame(first.getInteger1(), second.getInteger1());
        assertNotSame(first.getInteger2(), second.getInteger2());
        assertNotSame(first.getString1(), second.getString1());
    }

    @Test
    public void uniqueClassWithNotNativeTypeProperty(){
        ClassWithNotNativeTypeProperty first = ObjectG.unique(ClassWithNotNativeTypeProperty.class);
        ClassWithNotNativeTypeProperty second = ObjectG.unique(ClassWithNotNativeTypeProperty.class);

        assertNotSame(first.getOtherClass().getInteger1(), second.getOtherClass().getInteger1());
        assertNotSame(first.getOtherClass().getInteger2(), second.getOtherClass().getInteger2());
        assertNotSame(first.getOtherClass().getString1(), second.getOtherClass().getString1());
        assertNotSame(first.getSimpleProperty(), second.getSimpleProperty());
    }

    public static class ClassWithSimpleProperties{
        private int integer1;
        private Integer integer2;
        private String string1;

        public int getInteger1() {
            return integer1;
        }

        public void setInteger1(int integer1) {
            this.integer1 = integer1;
        }

        public Integer getInteger2() {
            return integer2;
        }

        public void setInteger2(Integer integer2) {
            this.integer2 = integer2;
        }

        public String getString1() {
            return string1;
        }

        public void setString1(String string1) {
            this.string1 = string1;
        }
    }

    public static class ClassWithNotNativeTypeProperty {
        private String simpleProperty;
        private ClassWithSimpleProperties otherClass;

        public String getSimpleProperty() {
            return simpleProperty;
        }

        public void setSimpleProperty(String simpleProperty) {
            this.simpleProperty = simpleProperty;
        }

        public ClassWithSimpleProperties getOtherClass() {
            return otherClass;
        }

        public void setOtherClass(ClassWithSimpleProperties otherClass) {
            this.otherClass = otherClass;
        }
    }
}
