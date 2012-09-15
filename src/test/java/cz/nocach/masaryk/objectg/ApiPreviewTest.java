package cz.nocach.masaryk.objectg;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static cz.nocach.masaryk.objectg.ApiPreviewTest.Api.*;
import static cz.nocach.masaryk.objectg.ApiPreviewTest.Generators.fromList;
import static cz.nocach.masaryk.objectg.ApiPreviewTest.Generators.range;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class ApiPreviewTest {

    @Test
    @Ignore //fails because preview shows only COMPILABLE code, not working
    public void configureFields(){
        Person buildPerson = build(Person.class);
        buildPerson.setName(fromList("one", "two"));
        buildPerson.setBirthDate(range(1980, 1982));

        Person person = gen(Person.class);
        //if field is not configured, then it is created anyway
        assertNotNull(person.getAddress());
        assertTrue(person.getName().equals("one")
            || person.getName().equals("two"));
    }

    @Test
    @Ignore //fails because preview shows only COMPILABLE code, not working
    public void configureTypes(){
        Person buildPerson = build(Person.class,
                //for any field in any object during Person generation field of type Address
                //will be set with null
                globalNull(Address.class),
                //for any field of type Date that is in Person class itself (not in other objects, to which
                // it has references) null will be set
                localNull(Date.class),
                //for fields of type WorkPlace.getClass() will inject new WorkPlace()
                instance(new WorkPlace()),
                 //for fields of type WorkPlace.class will inject new WorkPlace()
                instance(WorkPlace.class, new WorkPlace()));
    }

    private GeneratorParam instance(Class<WorkPlace> workPlaceClass, WorkPlace workPlace) {
        return null;
    }

    private GeneratorParam instance(WorkPlace workPlace) {
        return null;
    }

    private GeneratorParam localNull(Class<Date> dateClass) {
        return null;
    }

    private GeneratorParam globalNull(Class<Address> addressClass) {
        return null;
    }

    public static class Api{
        public static <T> T gen(Class<T> clazz){
            return null;
        }
        public static <T> T build(Class<T> clazz, GeneratorParam... params){
            return null;
        }
    }

    private static class GeneratorParam{
    }

    public static class Generators{
        public static <T> T fromList(T... values){
            return null;
        }

        public static <T extends Date> T range(T from, T to){
            return null;
        }
        public static <T extends Date> T range(int from, int to){
            return null;
        }
    }

    public static class Person{
        private String name;
        private Address address;
        private Date birthDate;
        List<WorkPlace> workPlaces;

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public List<WorkPlace> getWorkPlaces() {
            return workPlaces;
        }

        public void setWorkPlaces(List<WorkPlace> workPlaces) {
            this.workPlaces = workPlaces;
        }
    }

    public static class Address{
        private String country;
        private String town;
        private String street;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    public static class WorkPlace{
        private Person person;
        private Address address;

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }
}
