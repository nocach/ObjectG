package org.objectg.gen;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.ObjectG;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertThat;

/**
 * User: __nocach
 * Date: 17.10.12
 */
public class InterfacesGenerationTest {

    @Test
    public void canInstantiateNoMethodsInterface(){
        NoMethodsInterface noMethodsInterface = ObjectG.unique(NoMethodsInterface.class);
        assertNotNull(noMethodsInterface);
    }

    @Test
    public void willUseMoreConcreteTypeForInterface(){
        DifferentTypesFromSettersAndGetters unique = ObjectG.unique(DifferentTypesFromSettersAndGetters.class);

        assertThat(unique.getString(), Matchers.instanceOf(String.class));
        assertThat(unique.getInteger(), Matchers.instanceOf(Integer.class));
        assertThat(unique.getBaseClass(), Matchers.instanceOf(DerivedDerivedClass.class));
    }

    @Test
    @Ignore
    public void canSpecifyImplementationForClass(){
        fail("not implemented yet");
    }

    public static interface NoMethodsInterface{
    }

    public static class BaseClass{}
    public static class DerivedClass extends BaseClass{}
    public static class DerivedDerivedClass extends DerivedClass{}

    public static interface DifferentTypesFromSettersAndGetters{
        public Object getString();
        public void setString(String string);

        public Integer getInteger();
        public void setInteger(Object integer);

        public BaseClass getBaseClass();
        public void setBaseClass(DerivedClass derivedClass);
        public void setBaseClass(DerivedDerivedClass derivedDerivedClass);
    }
}