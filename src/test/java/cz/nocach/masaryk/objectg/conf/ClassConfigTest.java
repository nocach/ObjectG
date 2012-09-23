package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Test;

import static cz.nocach.masaryk.objectg.gen.rule.Rules.fromList;
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
    public void samePropertyIsConfiguredOnlyOnRightClass(){
        ClassWithSameProperty1 buildingClass = ObjectG.config(ClassWithSameProperty1.class);
        buildingClass.setProperty(fromList("configuredValue"));

        ClassWithSameProperty1 instance = ObjectG.unique(ClassWithSameProperty1.class, buildingClass);

        assertEquals("configuredValue", instance.getProperty());
        assertNotSame("same property but on the other class should not be touched by configuration",
                "configuredValue", instance.getClassWithSameProperty2().getProperty());
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
