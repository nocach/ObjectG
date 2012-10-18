package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * User: __nocach
 * Date: 17.10.12
 */
public class InterfacesGenerationTest {

    @Test
    @Ignore
    public void canInstantiateNoMethodsInterface(){
        NoMethodsInterface noMethodsInterface = ObjectG.unique(NoMethodsInterface.class);
        assertNotNull(noMethodsInterface);
    }

    @Test
    @Ignore
    public void canSpecifyImplementationForClass(){
        fail("not implemented yet");
    }

    public static interface NoMethodsInterface{

    }
}
