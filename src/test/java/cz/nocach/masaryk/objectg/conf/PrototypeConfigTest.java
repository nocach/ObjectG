package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.exception.ConfigurationException;
import cz.nocach.masaryk.objectg.conf.exception.PrototypeMisuseException;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static cz.nocach.masaryk.objectg.conf.OngoingRules.fromList;
import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class PrototypeConfigTest {
    @Test
    public void basic(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setName(fromList("configuredValue"));

        Person person = ObjectG.unique(Person.class, prototype);
        assertEquals("configuredValue", person.getName());
    }

    @Test
    public void nestedClassConfigurationThroughParentObject(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setName(fromList("nameConfiguredValue"));
        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet(fromList("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, prototype);
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("streetConfiguredValue", person.getHomeAddress().getStreet());
        //we configured only HomeAddress setter
        assertNotSame("streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void nestedClassConfigurationImplicit(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setName(fromList("nameConfiguredValue"));
        Address buildingAddress = ObjectG.prototype(Address.class);
        buildingAddress.setStreet(fromList("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, prototype, buildingAddress);
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("streetConfiguredValue", person.getHomeAddress().getStreet());
        assertEquals("streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void nestedClassCanBeConfiguredMoreThanOnce(){
        Person prototype = ObjectG.prototype(Person.class);

        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet("homeConf");

        prototype.setWorkAddress(ObjectG.prototype(Address.class));
        prototype.getWorkAddress().setStreet("workConf");

        Person generated = ObjectG.unique(Person.class, prototype);

        assertEquals("homeAddress must be configured", "homeConf", generated.getHomeAddress().getStreet());
        assertEquals("workAddress must be configured", "workConf", generated.getWorkAddress().getStreet());
    }

    @Test
    public void canCombineExplicitAndImplicitObjectConfiguraiton(){
        Person prototype = ObjectG.prototype(Person.class);

        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet("homeConf");

        //does not work, because setHomeAddress creates OverrideRule
        //and getHomeAddress() creates another OverrideRule and only
        //first is applied.
        //must throw when getter and setter are used together

        Address implicitAddressConf = ObjectG.prototype(Address.class);
        implicitAddressConf.setStreet("workConf");

        Person generated = ObjectG.unique(Person.class, prototype, implicitAddressConf);

        assertEquals("homeConf", generated.getHomeAddress().getStreet());
        assertEquals("workConf", generated.getWorkAddress().getStreet());
    }

    @Test
    public void canConfigureMoreThanOnePropertyOnGetter(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.getHomeAddress().setStreet("configuredStreetName");
        prototype.getHomeAddress().setHouseNumber(222);

        Person generated = ObjectG.unique(Person.class, prototype);

        assertEquals("configuredStreetName", generated.getHomeAddress().getStreet());
        assertEquals(222, generated.getHomeAddress().getHouseNumber());
    }

    @Test
    public void samePropertyIsConfiguredOnlyOnRightClass(){
        ClassWithSameProperty1 prototype = ObjectG.prototype(ClassWithSameProperty1.class);
        prototype.setProperty(fromList("configuredValue"));

        ClassWithSameProperty1 instance = ObjectG.unique(ClassWithSameProperty1.class, prototype);

        assertEquals("configuredValue", instance.getProperty());
        assertNotSame("same property but on the other class should not be touched by configuration",
                "configuredValue", instance.getClassWithSameProperty2().getProperty());
    }

    @Test
    public void globalConfigurationIsAppliedToInnerConfiguredClassesImplicitConfiguration(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA prototype = ObjectG.prototype(ClassWithCollectionA.class);
        prototype.setForConfiguration(fromList("confValueA"));
        prototype.setClassB(ObjectG.prototype(ClassWithCollectionB.class));
        prototype.getClassB().setForConfiguration(fromList("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, prototype);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
    }

    @Test
    public void globalConfigurationIsAppliedToInnerConfiguredClassesExplicitConfiguration(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA prototypeA = ObjectG.prototype(ClassWithCollectionA.class);
        prototypeA.setForConfiguration(fromList("confValueA"));

        ClassWithCollectionB prototypeB = ObjectG.prototype(ClassWithCollectionB.class);
        prototypeB.setForConfiguration(fromList("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, prototypeA, prototypeB);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
    }

    @Test
    public void configObjectGetterWillReturnAnotherConfigObject(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.getHomeAddress().setStreet("configured");

        Person generated = ObjectG.unique(Person.class, prototype);

        assertEquals("person.homeAddress.street should be configured"
                , "configured", generated.getHomeAddress().getStreet());
        assertNotSame("person.workAddress.steet should be generated the usual way"
                , "configured", generated.getWorkAddress().getStreet());
    }

    @Test
    public void canSetPrototypeAfterGetterIsCalled(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.getHomeAddress().setStreet("configuredWithGetter");

        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet("configuredWithSetter");

        Person generated = ObjectG.unique(Person.class, prototype);

        assertEquals("configuredWithSetter", generated.getHomeAddress().getStreet());
    }

    @Test(expected = PrototypeMisuseException.class)
    public void configObjectGetterWillNotWorkWithPrimitive(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.getName();
    }

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingConfigPrimitiveJavaLang(){
        ObjectG.prototype(int.class);
    }

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingConfigPrimitiveJavaMath(){
        ObjectG.prototype(BigDecimal.class);
    }

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingConfigPrimitiveJavaUtil(){
        ObjectG.prototype(ArrayList.class);
    }

    @Test
    @Ignore
    public void configObjectGetterUsedWithSetter(){
        fail("throw?");
    }

    @Test
    @Ignore
    public void configObjectWithPrimitiveClass(){
        fail("throw?");
    }

    public static class ClassWithCollectionA{
        private ClassWithCollectionB classB;
        private List<String> stringList;
        private String forConfiguration;

        public ClassWithCollectionB getClassB() {
            return classB;
        }

        public void setClassB(ClassWithCollectionB classB) {
            this.classB = classB;
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public String getForConfiguration() {
            return forConfiguration;
        }

        public void setForConfiguration(String forConfiguration) {
            this.forConfiguration = forConfiguration;
        }
    }

    public static class ClassWithCollectionB{
        private List<String> stringList;
        private String forConfiguration;

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public String getForConfiguration() {
            return forConfiguration;
        }

        public void setForConfiguration(String forConfiguration) {
            this.forConfiguration = forConfiguration;
        }
    }

    public static class Person{
        private String name;
        private Address homeAddress;
        private Address workAddress;

        public Address getHomeAddress() {
            return homeAddress;
        }

        public void setHomeAddress(Address homeAddress) {
            this.homeAddress = homeAddress;
        }

        public Address getWorkAddress() {
            return workAddress;
        }

        public void setWorkAddress(Address workAddress) {
            this.workAddress = workAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Address{
        private String street;
        private int houseNumber;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public int getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(int houseNumber) {
            this.houseNumber = houseNumber;
        }
    }

    public static class ClassWithSameProperty1{
        private String property;
        private ClassWithSameProperty2 classWithSameProperty2;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public ClassWithSameProperty2 getClassWithSameProperty2() {
            return classWithSameProperty2;
        }

        public void setClassWithSameProperty2(ClassWithSameProperty2 classWithSameProperty2) {
            this.classWithSameProperty2 = classWithSameProperty2;
        }
    }

    public static class ClassWithSameProperty2{
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

}
