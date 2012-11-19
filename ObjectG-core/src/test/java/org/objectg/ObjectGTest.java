package org.objectg;

import org.junit.Test;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;

/**
 * User: __nocach
 * Date: 21.10.12
 */
public class ObjectGTest {

    @Test
    public void canGenerateFromPrototype(){
        Person prototype = ObjectG.prototype(Person.class);
		final Tour anotherPrototype = ObjectG.prototype(Tour.class);
		ObjectG.unique(prototype);
        ObjectG.unique(prototype, new GenerationConfiguration());
        ObjectG.unique(prototype, ObjectG.config());
        ObjectG.unique(prototype, ObjectG.config(), anotherPrototype);
        ObjectG.unique(prototype, new GenerationConfiguration(), anotherPrototype);
    }

    @Test
    public void canGenerateList(){
        Person prototype = ObjectG.prototype(Person.class);
		final Tour anotherPrototype = ObjectG.prototype(Tour.class);

        ObjectG.uniqueList(Person.class);
        ObjectG.uniqueList(prototype);
        ObjectG.uniqueList(Person.class, 1);
        ObjectG.uniqueList(prototype, 1);
        ObjectG.uniqueList(Person.class, 1, prototype);
        ObjectG.uniqueList(prototype, 1, prototype);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration());
        ObjectG.uniqueList(prototype, new GenerationConfiguration());
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), prototype);
        ObjectG.uniqueList(prototype, new GenerationConfiguration(), anotherPrototype);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), 1);
        ObjectG.uniqueList(prototype, new GenerationConfiguration(), 1);
        ObjectG.uniqueList(Person.class, new GenerationConfiguration(), 1, prototype);
        ObjectG.uniqueList(prototype, new GenerationConfiguration(), 1, anotherPrototype);
        ObjectG.uniqueList(Person.class, ObjectG.config());
        ObjectG.uniqueList(prototype, ObjectG.config());
        ObjectG.uniqueList(Person.class, ObjectG.config(), 1);
        ObjectG.uniqueList(prototype, ObjectG.config(), 1);
        ObjectG.uniqueList(Person.class, ObjectG.config(), prototype);
        ObjectG.uniqueList(prototype, ObjectG.config(), anotherPrototype);
        ObjectG.uniqueList(Person.class, ObjectG.config(), 1, prototype);
        ObjectG.uniqueList(prototype, ObjectG.config(), 1, anotherPrototype);
    }

    @Test
    public void canGenerateSet(){
        Person prototype = ObjectG.prototype(Person.class);
        Tour anotherPrototype = ObjectG.prototype(Tour.class);

        ObjectG.uniqueSet(Person.class);
        ObjectG.uniqueSet(prototype);
        ObjectG.uniqueSet(Person.class, 1);
        ObjectG.uniqueSet(prototype, 1);
        ObjectG.uniqueSet(Person.class, prototype);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration());
        ObjectG.uniqueSet(prototype, new GenerationConfiguration());
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), prototype);
        ObjectG.uniqueSet(prototype, new GenerationConfiguration(), anotherPrototype);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), 1);
        ObjectG.uniqueSet(prototype, new GenerationConfiguration(), 1);
        ObjectG.uniqueSet(Person.class, new GenerationConfiguration(), 1, prototype);
        ObjectG.uniqueSet(prototype, new GenerationConfiguration(), 1, anotherPrototype);
        ObjectG.uniqueSet(Person.class, ObjectG.config());
        ObjectG.uniqueSet(prototype, ObjectG.config());
        ObjectG.uniqueSet(Person.class, ObjectG.config(), 1);
        ObjectG.uniqueSet(prototype, ObjectG.config(), 1);
        ObjectG.uniqueSet(Person.class, ObjectG.config(), prototype);
        ObjectG.uniqueSet(prototype, ObjectG.config(), anotherPrototype);
        ObjectG.uniqueSet(Person.class, ObjectG.config(), 1, prototype);
        ObjectG.uniqueSet(prototype, ObjectG.config(), 1, anotherPrototype);
    }

}
