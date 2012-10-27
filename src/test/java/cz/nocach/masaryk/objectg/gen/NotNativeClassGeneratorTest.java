package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.fixtures.domain.IPerson;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * User: __nocach
 * Date: 29.8.12
 */
public class NotNativeClassGeneratorTest extends Assert{


    @Test
    public void noArgConstructor(){
        Object generated = ObjectG.unique(NoArgConstructor.class);

        assertNotNull(generated);
    }

    @Test
    public void oneArgConstructor(){
        OneArgConstructor generated = ObjectG.unique(OneArgConstructor.class);

        assertNotNull(generated);
        assertNotNull(generated.arg);
    }

    @Test
    public void betweenMultipleConstructorsWithMostArgsIsUsed(){
        MultipleConstructors generated = ObjectG.unique(MultipleConstructors.class);

        assertNotNull(generated);
        assertNotNull(generated.field1);
        assertNotNull(generated.field2);
        assertNotNull(generated.field3);
    }

    @Test
    public void fieldsInClassAreSetWithUniqueValues(){
        ClassWithMultipleField generated =  ObjectG.unique(ClassWithMultipleField.class);

        assertClassWithMultipleFieldsIsFilled(generated);
    }

    private void assertClassWithMultipleFieldsIsFilled(ClassWithMultipleField generated) {
        assertNotNull(generated);
        assertNotNull(generated.field1);
        assertNotNull(generated.field2);
        assertNotSame(generated.field1, generated.field2);
        assertNotSame(generated.field3, generated.field4);
    }

    @Test
    public void fieldsOfObjectAreGenerated(){
        ClassReferencingOtherClass generated =  ObjectG.unique(ClassReferencingOtherClass.class);

        assertNotNull(generated);
        assertClassWithMultipleFieldsIsFilled(generated.classWithMultipleField1);
        assertClassWithMultipleFieldsIsFilled(generated.classWithMultipleField2);
        assertNotSame(generated.classWithMultipleField1.field1, generated.classWithMultipleField2.field1);
    }

    @Test
    public void canConstructObjectWithCyclicDependency(){
        ObjectG.unique(CyclicClassA.class);
    }

    @Test
    public void cyclicConstructorTest(){
        ObjectG.unique(CyclicConstructor.class);
    }

    @Test
    public void willWorkWithInherritedFields(){
        ExtendingBaseClass generated = ObjectG.unique(ExtendingBaseClass.class);

        assertNotNull("propertyForOverride", generated.getPropertyForOverride());
        assertNotNull("baseClassProperty", generated.getBaseClassProperty());
        assertNotNull("extendingBaseClassProperty", generated.getExtendingBaseClassProperty());
    }

    @Test
    public void canConstructInterfaces(){
        IPerson generated1 = ObjectG.unique(IPerson.class);
        IPerson generated2 = ObjectG.unique(IPerson.class);
        assertNotSame("firstName", generated1.getFirstName(), generated2.getFirstName());
        assertEquals("person.Employee2Address.size", 1, generated1.getEmployee2Addresses().size());
    }

    @Test
    public void canInferCollectionObjectsTypeFromSetter(){
        CollectionSetterType unique = ObjectG.unique(CollectionSetterType.class);

        assertTrue(unique.getList().get(0) instanceof String);
        assertNotNull(unique.getList().get(0));

        assertTrue(unique.getMap().size() == 1);
        assertNotNull(unique.getMap().keySet().iterator().next());
    }

    @Test
    public void canInferCollectionObjectsTypeFromGetter(){
        CollectionGetterType unique = ObjectG.unique(CollectionGetterType.class);

        assertTrue(unique.getList().get(0) instanceof String);
        assertNotNull(unique.getList().get(0));

        assertTrue(unique.getMap().size() == 1);
        assertNotNull(unique.getMap().keySet().iterator().next());
    }

    @Test
    public void canInferCollectionObjectsTypeFromField(){
        CollectionFieldType unique = ObjectG.unique(CollectionFieldType.class);

        assertTrue(unique.getList().get(0) instanceof String);
        assertNotNull(unique.getList().get(0));

        assertTrue(unique.getMap().size() == 1);
        assertNotNull(unique.getMap().keySet().iterator().next());
    }

    @Test
    public void canInferCollectionObjectTypeFromInterfaceGetter(){
        CollectionSetterTypeImpl unique = ObjectG.unique(CollectionSetterTypeImpl.class);

        assertTrue(unique.getList().get(0) instanceof String);
        assertNotNull(unique.getList().get(0));

        assertTrue(unique.getMap().size() == 1);
        assertNotNull(unique.getMap().keySet().iterator().next());
    }

    @Test
    public void canInferCollectionObjectTypeFromInterfaceSetter(){
        CollectionGetterTypeImpl unique = ObjectG.unique(CollectionGetterTypeImpl.class);

        assertTrue(unique.getList().get(0) instanceof String);
        assertNotNull(unique.getList().get(0));

        assertTrue(unique.getMap().size() == 1);
        assertNotNull(unique.getMap().keySet().iterator().next());
    }

    @Test
    @Ignore
    public void canConstructAbstractClasses(){
        fail("field AbstractPerson");
    }

    public static class CollectionSetterType{
        private List list;
        private Map map;

        public List getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

    public static interface ICollectionSetterType{
        public List getList();
        public void setList(List<String> list);
        public Map getMap();
        public void setMap(Map<String, String> map);
    }

    public static class CollectionSetterTypeImpl implements ICollectionSetterType{
        private List list;
        private Map map;

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }

    public static interface ICollectionGetterType{
        public List<String> getList();
        public void setList(List list);
        public Map<String, String> getMap();
        public void setMap(Map map);
    }

    public static class CollectionGetterTypeImpl implements ICollectionGetterType{
        private List list;
        private Map map;

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }

    public static class CollectionGetterType{
        private List list;
        private Map map;

        public List<String> getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }

    public static class CollectionFieldType{
        private List<String> list;
        private Map<String, String> map;

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }

    public static class CyclicConstructor{
        public CyclicConstructor(CyclicConstructor arg){
        }
    }

    public static class CyclicClassA{
        private CyclicClassB classB;

        public CyclicClassB getClassB() {
            return classB;
        }

        public void setClassB(CyclicClassB classB) {
            this.classB = classB;
        }
    }

    public static class CyclicClassB{
        private CyclicClassA classA;

        public CyclicClassA getClassA() {
            return classA;
        }

        public void setClassA(CyclicClassA classA) {
            this.classA = classA;
        }
    }

    public static class NoArgConstructor{}

    public static class OneArgConstructor{
        private String arg;

        public OneArgConstructor(String arg){
            this.arg = arg;
        }
    }

    public static class MultipleConstructors{
        private String field1;
        private String field2;
        private String field3;

        public MultipleConstructors() {
        }

        public MultipleConstructors(String field1) {
            this.field1 = field1;
        }

        public MultipleConstructors(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public MultipleConstructors(String field1, String field2, String field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }
    }

    public static class ClassWithMultipleField{
        private String field1;
        private String field2;
        private int field3;
        private int field4;
    }

    public static class ClassReferencingOtherClass{
        private ClassWithMultipleField classWithMultipleField1;
        private ClassWithMultipleField classWithMultipleField2;
    }

    public static class BaseClass{
        private String baseClassProperty;
        private String propertyForOverride;

        public String getBaseClassProperty() {
            return baseClassProperty;
        }

        public void setBaseClassProperty(String baseClassProperty) {
            this.baseClassProperty = baseClassProperty;
        }

        public String getPropertyForOverride() {
            return propertyForOverride;
        }

        public void setPropertyForOverride(String propertyForOverride) {
            this.propertyForOverride = propertyForOverride;
        }
    }

    public static class ExtendingBaseClass extends BaseClass{
        private String extendingBaseClassProperty;
        private String propertyForOverride;

        public String getPropertyForOverride() {
            return propertyForOverride;
        }

        public void setPropertyForOverride(String propertyForOverride) {
            this.propertyForOverride = propertyForOverride;
        }

        public String getExtendingBaseClassProperty() {
            return extendingBaseClassProperty;
        }

        public void setExtendingBaseClassProperty(String extendingBaseClassProperty) {
            this.extendingBaseClassProperty = extendingBaseClassProperty;
        }
    }
}
