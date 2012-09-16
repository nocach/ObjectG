package cz.nocach.masaryk.objectg.gen.context;

import java.lang.reflect.Field;

/**
 * <p>
 *     Defines data under which Generation is performed. Used by {@link cz.nocach.masaryk.objectg.gen.GeneratorRegistry}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GenerationContext {

    private Class classThatIsGenerated;
    private Field field;

    public GenerationContext(Class classThatIsGenerated) {
        this.classThatIsGenerated = classThatIsGenerated;
    }

    public Class getClassThatIsGenerated() {
        return classThatIsGenerated;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }
}
