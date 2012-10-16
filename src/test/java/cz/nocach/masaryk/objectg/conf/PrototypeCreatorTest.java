package cz.nocach.masaryk.objectg.conf;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class PrototypeCreatorTest {

    @Test
    public void canCreateTwoPrototypes(){
        PrototypeCreator prototypeCreator = new PrototypeCreator();
        prototypeCreator.newPrototype(ClassWithProperties.class);
        prototypeCreator.newPrototype(ClassWithProperties.class);
    }

    @Test
    @Ignore
    public void whatWillHappenWhenClassIsInterface(){
        fail("think about it");
    }

    @Test
    @Ignore
    public void whatWillHappenWhenClassIsAbstract(){
        fail("think about it");
    }

    public static class ClassWithProperties{
        private String property1;
        private Object property2;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public Object getProperty2() {
            return property2;
        }

        //special case returning not null object
        public Object setProperty2(Object property2) {
            this.property2 = property2;
            return property2;
        }
        //special case of short method name
        public void setA(){
        }

        //special case of method that looks like setter, but it is not
        public void set(){
        }
    }
}
