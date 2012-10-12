package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.exception.ConfigurationException;
import org.junit.Test;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class ConfigBuilderTest {

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingToGenerateWithoutCompleteConfiguration(){
        //user forgot to call .done()
        ObjectG.unique(String.class, ObjectG.config().backReferenceCycle());
    }
    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingToPassNotPrototypeObject(){
        //user passes object that was not created by ObjectG.prototype()
        ObjectG.unique(String.class, new PrototypeConfigTest.Person());
    }
}
