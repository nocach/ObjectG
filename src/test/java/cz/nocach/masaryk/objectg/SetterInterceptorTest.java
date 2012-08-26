package cz.nocach.masaryk.objectg;

import javassist.*;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class SetterInterceptorTest extends Assert{

    private static Person modifiedInstance;

    @Test
    public void canSubstituteRealClassMethod() throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        Person intercepted = intercept(Person.class);

        intercepted.setName("someValue");

        assertNull(intercepted.name);
    }

    private Person intercept(Class<Person> personClass) throws NotFoundException, CannotCompileException,
            IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        if (modifiedInstance != null) return modifiedInstance;
        ClassPool classPool= ClassPool.getDefault();
        CtClass modifiedPerson = classPool.get(personClass.getName());
        modifiedPerson.setName(personClass.getName() + "Intercepted");
        CtClass originalPerson = classPool.get(personClass.getName());
        modifiedPerson.setSuperclass(originalPerson);
        CtMethod setName = modifiedPerson.getDeclaredMethod("setName");
        setName.setBody(
                "{" +
                    "cz.nocach.masaryk.objectg.SetterInterceptorTest$ClassWatcher.currentFieldName=\"name\";" +
                    "return;" +
                "}");
        modifiedInstance = (Person) modifiedPerson.toClass().newInstance();
        return modifiedInstance;
    }

    @Test
    public void canAssociateRandomizeCallWithSetterInterceptor() throws IOException, ClassNotFoundException, NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
        Person intercepted = intercept(Person.class);

        intercepted.setName(randomFromList("one", "two", "three"));

        assertTrue(ClassWatcher.fieldsRandomizers.containsKey("name"));
        assertEquals("fromList", ClassWatcher.fieldsRandomizers.get("name"));
    }

    private <T> T randomFromList(T... args) {
        ClassWatcher.fieldsRandomizers.put(ClassWatcher.currentFieldName, "fromList");
        return null;
    }

    public static class ClassWatcher{
        public static String currentFieldName;
        private static Map<String, String> fieldsRandomizers = new HashMap<String, String>();
    }

    public static class Person{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
