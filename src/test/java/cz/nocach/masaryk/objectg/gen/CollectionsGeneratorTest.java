package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.OngoingRules;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 22.9.12
 */
public class CollectionsGeneratorTest {

    @Test
    public void canGenerateCollection(){
        ClassWithCollections collections = ObjectG.unique(ClassWithCollections.class);

        assertNotNull("List<String>", collections.getListString());
        assertNotNull("ArrayList<String>", collections.getArrayListString());
        assertNotNull("Set<String>", collections.getSetString());
        assertNotNull("HashSet<String>", collections.getHashSetString());
        assertEquals("List<String>.size",1, collections.getListString().size());
        assertEquals("ArrayList<String>.size", 1, collections.getArrayListString().size());
        assertEquals("Set<String>.size", 1, collections.getSetString().size());
        assertEquals("HashSet<String>.size", 1, collections.getHashSetString().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfGenericListGenerationNotConfigured(){
        ObjectG.unique(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfGenericSetGenerationNotConfigured(){
        ObjectG.unique(Set.class);
    }

    @Test
    public void canGenerateGenericList(){
        List<ClassWithCollections> result = ObjectG.uniqueList(ClassWithCollections.class);

        assertEquals("must be list with one element", 1, result.size());
        assertNotNull("list must have not null generated object", result.get(0));
        assertNotSame("generated object will have not empty collections also set", 0, result.get(0).getArrayListString().size());
    }

    @Test
    public void canGenerateGenericSet(){
        Set<ClassWithCollections> result = ObjectG.uniqueSet(ClassWithCollections.class);

        assertEquals("must be list with one element", 1, result.size());
        ClassWithCollections firstElement = result.iterator().next();
        assertNotNull("list must have not null generated object", firstElement);
        assertNotSame("generated object will have not empty collections also set", 0, firstElement.getArrayListString().size());
    }

    @Test
    public void canGenerateGenericListWithSize(){
        List<ClassWithCollections> result = ObjectG.uniqueList(ClassWithCollections.class, 2);

        assertEquals("must be collection with two elements", 2, result.size());
        assertNotNull("collection must have not null generated object", result.get(0));
        assertNotNull("collection must have not null generated object", result.get(1));
        assertNotSame("generated object will have not empty collections also set", 0, result.get(0).getArrayListString().size());
        assertNotSame("generated object will have not empty collections also set", 0, result.get(1).getArrayListString().size());
    }

    @Test
    public void canGenerateGenericSetWithSize(){
        Set<ClassWithCollections> result = ObjectG.uniqueSet(ClassWithCollections.class, 2);

        assertEquals("must be collection with two elements", 2, result.size());
        Iterator<ClassWithCollections> iterator = result.iterator();
        assertNotNull("set must have not null generated object, first obj", iterator.next());
        assertNotNull("set must have not null generated object, second obj", iterator.next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenTryingToGenerateCollectionOfNegativeSize(){
        ObjectG.uniqueList(ClassWithCollections.class, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenTryingToGenerateSetOfNegativeSize(){
        ObjectG.uniqueSet(ClassWithCollections.class, -1);
    }

    @Test
    public void canConfigurateClassForGenericListField(){
        ClassWithGenericList prototype = ObjectG.prototype(ClassWithGenericList.class);
        prototype.setGenericList(OngoingRules.listDefinition(String.class));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, prototype);

        assertEquals(1, generated.getGenericList().size());
        assertTrue(generated.getGenericList().get(0) instanceof String);
        assertNotNull(generated.getGenericList().get(0));
    }

    @Test
    public void canConfigurateClassForGenericSetField(){
        ClassWithGenericSet prototype = ObjectG.prototype(ClassWithGenericSet.class);
        prototype.setGenericSet(OngoingRules.setDefinition(String.class));

        ClassWithGenericSet generated = ObjectG.unique(ClassWithGenericSet.class, prototype);

        assertEquals(1, generated.getGenericSet().size());
        Object firstElement = generated.getGenericSet().iterator().next();
        assertTrue(firstElement instanceof String);
        assertNotNull(firstElement);
    }

    @Test
    public void configurateClassForGenericListFieldCanDefineSize(){
        ClassWithGenericList prototype = ObjectG.prototype(ClassWithGenericList.class);
        prototype.setGenericList(OngoingRules.listDefinition(String.class, 0));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, prototype);

        assertEquals(0, generated.getGenericList().size());
    }

    @Test
    public void configurateClassForGenericSetFieldCanDefineSize(){
        ClassWithGenericSet prototype = ObjectG.prototype(ClassWithGenericSet.class);
        prototype.setGenericSet(OngoingRules.setDefinition(String.class, 0));

        ClassWithGenericSet generated = ObjectG.unique(ClassWithGenericSet.class, prototype);

        assertEquals(0, generated.getGenericSet().size());
    }

    @Test
    public void configurateClassForGenericListFieldCanDefineValues(){
        ClassWithGenericList prototype = ObjectG.prototype(ClassWithGenericList.class);
        prototype.setGenericList(OngoingRules.listDefinition(String.class, "one", "two"));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, prototype);

        assertEquals(2, generated.getGenericList().size());
        assertEquals("one", generated.getGenericList().get(0));
        assertEquals("two", generated.getGenericList().get(1));
    }

    @Test
    public void configurateClassForGenericSetFieldCanDefineValues(){
        ClassWithGenericSet prototype = ObjectG.prototype(ClassWithGenericSet.class);
        prototype.setGenericSet(OngoingRules.setDefinition(String.class, "one", "two"));

        ClassWithGenericSet generated = ObjectG.unique(ClassWithGenericSet.class, prototype);

        assertEquals(2, generated.getGenericSet().size());
        assertTrue("one", generated.getGenericSet().contains("one"));
        assertTrue("two", generated.getGenericSet().contains("two"));
    }

    @Test(expected = GenerationException.class)
    public void throwsIfNoInfoAboutListField(){
        ObjectG.unique(ClassWithGenericList.class);
    }

    @Test(expected = GenerationException.class)
    public void throwsIfNoInfoAboutSetField(){
        ObjectG.unique(ClassWithGenericSet.class);
    }

    @Test
    @Ignore
    public void canGenerateListOfInterface(){
        fail("not written");
    }

    @Test
    @Ignore
    public void canGenerateSetOfInterface(){
        fail("not written");
    }

    @Test
    public void canConfigurateCollectionOfConcreteType(){
        ClassWithCollections prototype = ObjectG.prototype(ClassWithCollections.class);
        prototype.setArrayListString(OngoingRules.collectionDefinition(ArrayList.class, String.class));

        ClassWithCollections generated = ObjectG.unique(ClassWithCollections.class, prototype);

        assertEquals(1, generated.getArrayListString().size());
        assertNotNull(generated.getArrayListString().get(0));
    }

    @Test
    @Ignore
    public void canConfigurateCollectionOfConcreteTypeAndCanDefineSize(){
        fail("not implemented");
    }

    @Test
    @Ignore
    public void canConfigurateCollectionOfConcreteTypeAndCanValues(){
        fail("not implemented");
    }


    public static class ClassWithCollections{
        private List<String> listString;
        private Set<String> setString;
        private ArrayList<String> arrayListString;
        private HashSet<String> hashSetString;

        public ArrayList<String> getArrayListString() {
            return arrayListString;
        }

        public void setArrayListString(ArrayList<String> arrayListString) {
            this.arrayListString = arrayListString;
        }

        public List<String> getListString() {
            return listString;
        }

        public void setListString(List<String> listString) {
            this.listString = listString;
        }

        public Set<String> getSetString() {
            return setString;
        }

        public void setSetString(Set<String> setString) {
            this.setString = setString;
        }

        public HashSet<String> getHashSetString() {
            return hashSetString;
        }

        public void setHashSetString(HashSet<String> hashSetString) {
            this.hashSetString = hashSetString;
        }
    }

    public static class ClassWithGenericList{
        private List genericList;

        public List getGenericList() {
            return genericList;
        }

        public void setGenericList(List genericList) {
            this.genericList = genericList;
        }
    }

    public static class ClassWithGenericSet{
        private Set genericSet;

        public Set getGenericSet() {
            return genericSet;
        }

        public void setGenericSet(Set genericSet) {
            this.genericSet = genericSet;
        }
    }

    public static interface InterfaceExample{

    }

    public static class CollectionWithInterfaceList{
        private List<InterfaceExample> interfaceList;

        public List<InterfaceExample> getInterfaceList() {
            return interfaceList;
        }

        public void setInterfaceList(List<InterfaceExample> interfaceList) {
            this.interfaceList = interfaceList;
        }
    }

    public static class WildCardsCollections{
        //TODO: write example List<CharecterSecuence & Comparable>
        //TODO: write example List<? extends Something>
        //TODO: further examples
    }
}
