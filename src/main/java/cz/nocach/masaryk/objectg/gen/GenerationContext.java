package cz.nocach.masaryk.objectg.gen;

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
public class GenerationContext<T> {

    /**
     * optionall parentObject for which generation is performed
     */
    private Object parentObject;

    private Class classThatIsGenerated;
    private Field field;

    public GenerationContext(Class<T> classThatIsGenerated) {
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

    public Object getParentObject() {
        return parentObject;
    }

    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }

    @Override
    public String toString() {
        return "GenerationContext{" +
                "parentObject=" + parentObject +
                ", classThatIsGenerated=" + classThatIsGenerated +
                ", field=" + field +
                '}';
    }
}
