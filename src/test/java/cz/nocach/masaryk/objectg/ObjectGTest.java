package cz.nocach.masaryk.objectg;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.fixtures.domain.Person;
import org.junit.Test;

/**
 * User: __nocach
 * Date: 21.10.12
 */
public class ObjectGTest {

    @Test
    public void canGenerateFromPrototype(){
        Person prototype = ObjectG.prototype(Person.class);
        Person unique = ObjectG.unique(prototype);
    }

    @Test
    public void canGenerateList(){
        Person prototype = ObjectG.prototype(Person.class);

        ObjectG.uniqueList(Person.class);
        ObjectG.uniqueList(Person.class, 1);
        ObjectG.uniqueList(Person.class, 1, prototype);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration());
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), prototype);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), 1);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), 1, prototype);
        ObjectG.uniqueList(Person.class, ObjectG.config());
        ObjectG.uniqueList(Person.class, ObjectG.config(), 1);
        ObjectG.uniqueList(Person.class, ObjectG.config(), prototype);
        ObjectG.uniqueList(Person.class, ObjectG.config(), 1, prototype);
    }

    @Test
    public void canGenerateSet(){
        Person prototype = ObjectG.prototype(Person.class);

        ObjectG.uniqueSet(Person.class);
        ObjectG.uniqueSet(Person.class, 1);
        ObjectG.uniqueSet(Person.class, prototype);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration());
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), prototype);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), 1);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), 1, prototype);
        ObjectG.uniqueSet(Person.class, ObjectG.config());
        ObjectG.uniqueSet(Person.class, ObjectG.config(), 1);
        ObjectG.uniqueSet(Person.class, ObjectG.config(), prototype);
        ObjectG.uniqueSet(Person.class, ObjectG.config(), 1, prototype);
    }

}
