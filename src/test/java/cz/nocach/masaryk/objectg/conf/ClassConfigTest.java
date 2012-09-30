package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Ignore;
import org.junit.Test;
import sun.java2d.pipe.RegionSpanIterator;

import java.util.List;

import static cz.nocach.masaryk.objectg.conf.OngoingRules.fromList;
import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class ClassConfigTest {
    @Test
    public void basic(){
        Person building = ObjectG.config(Person.class);
        building.setName(fromList("configuredValue"));

        Person person = ObjectG.unique(Person.class, ObjectG.contextFromObjects(building));
        assertEquals("configuredValue", person.getName());
    }

    @Test
    public void nestedClassConfigurationThroughParentObject(){
        Person building = ObjectG.config(Person.class);
        building.setName(fromList("nameConfiguredValue"));
        building.setHomeAddress(ObjectG.config(Address.class));
        building.getHomeAddress().setStreet(fromList("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, ObjectG.contextFromObjects(building));
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("streetConfiguredValue", person.getHomeAddress().getStreet());
        //we configured only HomeAddress setter
        assertNotSame("streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void nestedClassConfigurationImplicit(){
        Person buildingPerson = ObjectG.config(Person.class);
        buildingPerson.setName(fromList("nameConfiguredValue"));
        Address buildingAddress = ObjectG.config(Address.class);
        buildingAddress.setStreet(fromList("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, ObjectG.contextFromObjects(buildingPerson, buildingAddress));
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("streetConfiguredValue", person.getHomeAddress().getStreet());
        assertEquals("streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void nestedClassCanBeConfiguredMoreThanOnce(){
        Person confPerson = ObjectG.config(Person.class);

        confPerson.setHomeAddress(ObjectG.config(Address.class));
        confPerson.getHomeAddress().setStreet("homeConf");

        confPerson.setWorkAddress(ObjectG.config(Address.class));
        confPerson.getWorkAddress().setStreet("workConf");

        Person generated = ObjectG.unique(Person.class, confPerson);

        assertEquals("homeAddress must be configured", "homeConf", generated.getHomeAddress().getStreet());
        assertEquals("workAddress must be configured", "workConf", generated.getWorkAddress().getStreet());
    }

    @Test
    public void canCombineExplicitAndImplicitObjectConfiguraiton(){
        Person confPerson = ObjectG.config(Person.class);

        confPerson.setHomeAddress(ObjectG.config(Address.class));
        confPerson.getHomeAddress().setStreet("homeConf");

        Address implicitAddressConf = ObjectG.config(Address.class);
        implicitAddressConf.setStreet("workConf");

        Person generated = ObjectG.unique(Person.class, confPerson, implicitAddressConf);

        assertEquals("homeConf", generated.getHomeAddress().getStreet());
        assertEquals("workConf", generated.getWorkAddress().getStreet());
    }

    @Test
    public void samePropertyIsConfiguredOnlyOnRightClass(){
        ClassWithSameProperty1 buildingClass = ObjectG.config(ClassWithSameProperty1.class);
        buildingClass.setProperty(fromList("configuredValue"));

        ClassWithSameProperty1 instance = ObjectG.unique(ClassWithSameProperty1.class, buildingClass);

        assertEquals("configuredValue", instance.getProperty());
        assertNotSame("same property but on the other class should not be touched by configuration",
                "configuredValue", instance.getClassWithSameProperty2().getProperty());
    }

    @Test
    @Ignore
    public void globalConfigurationIsAppliedToInnerConfiguredClassesImplicitConfiguration(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA configA = ObjectG.config(ClassWithCollectionA.class);
        configA.setForConfiguration(fromList("confValueA"));
        configA.setClassB(ObjectG.config(ClassWithCollectionB.class));
        configA.getClassB().setForConfiguration(fromList("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, configA);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
    }

    @Test
    @Ignore
    public void globalConfigurationIsAppliedToInnerConfiguredClassesExplicitConfiguration(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA configA = ObjectG.config(ClassWithCollectionA.class);
        configA.setForConfiguration(fromList("confValueA"));

        ClassWithCollectionB configB = ObjectG.config(ClassWithCollectionB.class);
        configB.setForConfiguration(fromList("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, configA, configB);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
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

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
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
