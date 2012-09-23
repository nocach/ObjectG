package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 22.9.12
 */
public class CollectionGeneratorTest {

    @Test
    public void canGenerateList(){
        ClassWithCollections collections = ObjectG.unique(ClassWithCollections.class);

        assertNotNull("List<String>", collections.getListString());
        assertNotNull("ArrayList<String>", collections.getArrayListString());
        assertEquals("List<String>.size",1, collections.getListString().size());
        assertEquals("ArrayList<String>.size", 1, collections.getArrayListString().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfGenericListGenerationNotConfigured(){
        ObjectG.unique(List.class);
    }

    @Test
    public void canGenerateGenericList(){
        List<ClassWithCollections> result = ObjectG.generateList(ClassWithCollections.class);

        assertEquals("must be collection with one elelemnt", 1, result.size());
        assertNotNull("collection must have not null generated object", result.get(0));
        assertNotSame("generated object will have not empty collections also set", 0, result.get(0).getArrayListString().size());
    }

    @Test
    public void canGenerateGenericListWithSize(){
        List<ClassWithCollections> result = ObjectG.generateList(ClassWithCollections.class, 2);

        assertEquals("must be collection with one elelemnt", 2, result.size());
        assertNotNull("collection must have not null generated object", result.get(0));
        assertNotNull("collection must have not null generated object", result.get(1));
        assertNotSame("generated object will have not empty collections also set", 0, result.get(0).getArrayListString().size());
        assertNotSame("generated object will have not empty collections also set", 0, result.get(1).getArrayListString().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenTryingToGenerateCollectionOfNegativeSize(){
        ObjectG.generateList(ClassWithCollections.class, -1);
    }

    @Test
    public void canConfigurateClassForGenericListField(){
        ClassWithGenericList building = ObjectG.config(ClassWithGenericList.class);
        building.setGenericList(Rules.listDefinition(String.class));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, building);

        assertEquals(1, generated.getGenericList().size());
        assertTrue(generated.getGenericList().get(0) instanceof String);
        assertNotNull(generated.getGenericList().get(0) instanceof String);
    }

    @Test
    public void canConfigurateClassForGenericListFieldCanDefineSize(){
        ClassWithGenericList building = ObjectG.config(ClassWithGenericList.class);
        building.setGenericList(Rules.listDefinition(String.class, 0));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, building);

        assertEquals(0, generated.getGenericList().size());
    }

    @Test
    public void canConfigurateClassForGenericListFieldCanDefineValues(){
        ClassWithGenericList building = ObjectG.config(ClassWithGenericList.class);
        building.setGenericList(Rules.listDefinition(String.class, "one", "two"));

        ClassWithGenericList generated = ObjectG.unique(ClassWithGenericList.class, building);

        assertEquals(2, generated.getGenericList().size());
        assertEquals("one", generated.getGenericList().get(0));
        assertEquals("two", generated.getGenericList().get(1));
    }

    @Test(expected = GenerationException.class)
    public void throwsIfNoInfoAboutField(){
        ObjectG.unique(ClassWithGenericList.class);
    }

    @Test
    @Ignore
    public void canGenerateListOfInterface(){
        fail("not written");
    }

    public static class ClassWithCollections{
        private List<String> listString;
        private ArrayList<String> arrayListString;

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
