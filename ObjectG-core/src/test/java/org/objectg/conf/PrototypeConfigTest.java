package org.objectg.conf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.conf.exception.PrototypeMisuseException;
import org.objectg.fixtures.DerivedInterface;
import org.objectg.fixtures.domain.Departure;
import org.objectg.fixtures.domain.GuideAssignment;
import org.objectg.fixtures.domain.IPerson;
import org.objectg.fixtures.domain.ITour;

import static org.objectg.conf.PrototypeRules.value;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class PrototypeConfigTest extends BaseObjectGTest {

    @Test
    public void valueSetOnPrototypeIsTheSameForEveryGeneratedObject(){
        Person personPrototype = ObjectG.prototype(Person.class);
        personPrototype.setName("name");

        Person person1 = ObjectG.unique(Person.class, personPrototype);
        Person person2 = ObjectG.unique(Person.class, personPrototype);
        assertNotSame("should generate two different instances", person1, person2);
        assertNotSame("properties not set on prototype should be unique",
                person1.getHomeAddress(), person2.getHomeAddress());
        assertEquals("name", person1.getName());
        assertEquals("name", person2.getName());
    }

    @Test
    public void basic(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setName(PrototypeRules.sequence("configuredValue"));

        Person person = ObjectG.unique(Person.class, prototype);
        assertEquals("configuredValue", person.getName());
    }

    @Test
    public void canUsePrototypeAsAnotherPrototypeAttribute(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setName(value("nameConfiguredValue"));
        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet(value("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, prototype);
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("streetConfiguredValue", person.getHomeAddress().getStreet());
        //we configured only HomeAddress setter
        assertNotSame("streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void canUseMultiplePrototypesDuringGeneration(){
        Person personPrototype = ObjectG.prototype(Person.class);
        personPrototype.setName(PrototypeRules.sequence("nameConfiguredValue"));
        Address addressPrototype = ObjectG.prototype(Address.class);
        addressPrototype.setStreet(PrototypeRules.sequence("streetConfiguredValue"));

        Person person = ObjectG.unique(Person.class, personPrototype, addressPrototype);
        assertEquals("nameConfiguredValue", person.getName());
        assertEquals("addressPrototype should be used for generating ANY Address"
                , "streetConfiguredValue", person.getHomeAddress().getStreet());
        assertEquals("addressPrototype should be used for generating ANY Address"
                , "streetConfiguredValue", person.getWorkAddress().getStreet());
    }

    @Test
    public void canUseSeparatePrototypesForSameType(){
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
    public void canUsePrototypesOnAttributesAndAsDefaultsForClassGeneration(){
        Person prototype = ObjectG.prototype(Person.class);

        prototype.setHomeAddress(ObjectG.prototype(Address.class));
        prototype.getHomeAddress().setStreet("homeConf");

        Address implicitAddressPrototype = ObjectG.prototype(Address.class);
        implicitAddressPrototype.setStreet("workConf");

        Person generated = ObjectG.unique(Person.class, prototype, implicitAddressPrototype);

        assertEquals("value should be used from prototype set as attribute"
                , "homeConf", generated.getHomeAddress().getStreet());
        assertEquals("value should be used from implicitAddressPrototype as generally applied rule"
                , "workConf", generated.getWorkAddress().getStreet());
    }

    @Test
    public void samePropertyIsConfiguredOnlyOnRightClass(){
        ClassWithSameProperty1 prototype = ObjectG.prototype(ClassWithSameProperty1.class);
        prototype.setProperty(PrototypeRules.sequence("configuredValue"));

        ClassWithSameProperty1 instance = ObjectG.unique(ClassWithSameProperty1.class, prototype);

        assertEquals("configuredValue", instance.getProperty());
        assertNotSame("same property but on the other class should not be touched by configuration",
                "configuredValue", instance.getClassWithSameProperty2().getProperty());
    }

    @Test
    public void globalConfigurationIsUsedInPrototypesSetAsAttributes(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA prototype = ObjectG.prototype(ClassWithCollectionA.class);
        prototype.setForConfiguration(PrototypeRules.sequence("confValueA"));
        prototype.setClassB(ObjectG.prototype(ClassWithCollectionB.class));
        prototype.getClassB().setForConfiguration(PrototypeRules.sequence("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, prototype);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
    }

    @Test
    public void globalConfigurationIsUsedInAllPrototypes(){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        generationConfiguration.setUnique(true);
        generationConfiguration.setObjectsInCollections(2);

        ClassWithCollectionA prototypeA = ObjectG.prototype(ClassWithCollectionA.class);
        prototypeA.setForConfiguration(PrototypeRules.sequence("confValueA"));

        ClassWithCollectionB prototypeB = ObjectG.prototype(ClassWithCollectionB.class);
        prototypeB.setForConfiguration(PrototypeRules.sequence("confValueB"));

        ClassWithCollectionA generated = ObjectG.unique(ClassWithCollectionA.class,
                generationConfiguration, prototypeA, prototypeB);

        assertEquals("confValueA", generated.getForConfiguration());
        assertEquals(2, generated.getStringList().size());
        assertEquals("confValueB", generated.getClassB().getForConfiguration());
        assertEquals(2, generated.getClassB().getStringList().size());
    }

    @Test
    public void getterOnPrototypeWillReturnPrototypeAsADefault(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.getHomeAddress().setStreet("configured");

        Person generated = ObjectG.unique(Person.class, prototype);

        assertEquals("person.homeAddress.street should be configured"
                , "configured", generated.getHomeAddress().getStreet());
        assertNotSame("person.workAddress.steet should be generated the usual way"
                , "configured", generated.getWorkAddress().getStreet());
    }

    @Test
    public void getterOnPrototypeWillReturnSamePrototypeOnMultipleCalls(){
        Person prototype = ObjectG.prototype(Person.class);
        Address addressPrototypeFirstCall = prototype.getHomeAddress();
        Address addressPrototypeSecondCall = prototype.getHomeAddress();

        assertEquals(addressPrototypeFirstCall, addressPrototypeSecondCall);
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
    public void canConfigureAttributeThatWasFirstlyUsedAsGetter(){
        Person prototype = ObjectG.prototype(Person.class);
        //prototype for HomeAddress field was returned
        prototype.getHomeAddress();
        //set explicit value to the field
        Address explicitAddress = new Address();
        prototype.setHomeAddress(explicitAddress);

        Person person = ObjectG.unique(Person.class, prototype);

        assertEquals(explicitAddress, person.getHomeAddress());
    }

    @Test
    public void canConfigureInterfaceBasic(){
        IPerson prototype = ObjectG.prototype(IPerson.class);
        prototype.setFirstName("firstNamePrototyped");

        IPerson unique = ObjectG.unique(prototype);
        assertEquals("firstNamePrototyped", unique.getFirstName());
    }

    @Test
    public void canConfigureInterfaceGetterReturnsPrototype(){
        ITour prototype = ObjectG.prototype(ITour.class);
        prototype.getSeason().setName("prototyped");

        ITour generated = ObjectG.unique(prototype);
        assertEquals("prototyped", generated.getSeason().getName());
    }

    @Test
    public void canConfigureInterfaceInheritedInterfacesWillWork(){
        DerivedInterface prototype = ObjectG.prototype(DerivedInterface.class);
        prototype.getParentPerson().setFirstName("prototypedPerson");
        prototype.getTour().setName("prototypedTour");

        DerivedInterface generated = ObjectG.unique(prototype);

        assertEquals("prototypedPerson", generated.getParentPerson().getFirstName());
        assertEquals("prototypedTour", generated.getTour().getName());
    }

	@Test
	public void prototypeWorksWithInheritedMethods(){
		final PersonExtended prototype = ObjectG.prototype(PersonExtended.class);
		prototype.getHomeAddress().setStreet("streetPrototyped");
		prototype.setName("namePrototyped");

		final PersonExtended generated = ObjectG.unique(prototype);
		assertEquals("namePrototyped", generated.getName());
		assertEquals("streetPrototyped", generated.getHomeAddress().getStreet());
	}

	@Test
	public void canOverrideConfiguration(){
		final GuideAssignment prototype = ObjectG.prototype(GuideAssignment.class);
		prototype.setDeparture(PrototypeRules.<Departure>overrideConfiguration(ObjectG.config().depth(1)));

		final GuideAssignment unique = ObjectG.unique(prototype);
		assertNotNull(unique.getDeparture().getTour());
		//depth on Tour is 1
		assertNull(unique.getDeparture().getTour().getSeason());
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

	public static class PersonExtended extends Person{
		private Address homeAddress;

		public Address getHomeAddress() {
			return homeAddress;
		}

		public void setHomeAddress(final Address homeAddress) {
			this.homeAddress = homeAddress;
		}
	}

}
