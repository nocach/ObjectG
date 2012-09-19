package cz.nocach.masaryk.objectg.gen.conf;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class SetterConfiguratorTest implements ConfigurationHandler{

    private String onSetterPropertyName;
    private Object onSetterObjectUnderConfiguration;
    private Object onInitObjectUnderConfiguration;

    @Test
    public void handlerNotifiedOnSetterCall(){
        SetterConfigurator setterConfigurator = new SetterConfigurator(this);

        ClassWithProperties classWithProperty = setterConfigurator.newConfigurator(ClassWithProperties.class);

        classWithProperty.setProperty1("any value");
        assertEquals("property1", onSetterPropertyName);
        assertEquals(classWithProperty, onSetterObjectUnderConfiguration);

        classWithProperty.setProperty2("any value");
        assertEquals("property2", onSetterPropertyName);

        classWithProperty.setA();
        assertEquals("a", onSetterPropertyName);

        classWithProperty.set();
        //property was not altered because set() is not setter
        assertEquals("a", onSetterPropertyName);
    }

    @Test
    public void canCreateTwoConfigurators(){
        SetterConfigurator setterConfigurator = new SetterConfigurator(this);
        setterConfigurator.newConfigurator(ClassWithProperties.class);
        setterConfigurator.newConfigurator(ClassWithProperties.class);
    }

    @Test
    public void handlerNotifiedAboutConfigurationStart(){
        SetterConfigurator setterConfigurator = new SetterConfigurator(this);

        ClassWithProperties classWithProperty = setterConfigurator.newConfigurator(ClassWithProperties.class);
        assertEquals(onInitObjectUnderConfiguration, classWithProperty);
    }

    @Test
    @Ignore
    public void handlerNotifiedOnSetterCallForInterface(){
        fail("test showing that SetterConfigurator can be used to configure interface classes (IPerson.class)");
    }

    @Override
    public void onInit(Object objectUnderConfiguration) {
        onInitObjectUnderConfiguration = objectUnderConfiguration;
    }

    @Override
    public void onSetter(Object objectUnderConfiguration, String propertyName) {
        onSetterObjectUnderConfiguration = objectUnderConfiguration;
        onSetterPropertyName = propertyName;
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
