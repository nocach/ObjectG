package cz.nocach.masaryk.objectg;

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
    /**
     * contains classes and objects that WERE generated before current context
     */
    private Hierarchy hierarchy;
    private boolean isPushed;

    GenerationContext(Class<T> classThatIsGenerated) {
        this(classThatIsGenerated, null);
    }
    GenerationContext(Class<T> classThatIsGenerated, Object parentObject) {
        this.classThatIsGenerated = classThatIsGenerated;
        this.parentObject = parentObject;
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

    public boolean isCycle(){
        if (hierarchy == null) return false;
        return hierarchy.isCycle(classThatIsGenerated);
    }

    /**
     * @param clazz
     * @return new GenerationContext after pushing clazz
     */
    public GenerationContext push(Class clazz){
        return push(clazz, null);
    }

    /**
     * @param clazz
     * @return new GenerationContext after pushing clazz
     */
    public GenerationContext push(Class clazz, Object parentObject){
        GenerationContext newContext = new GenerationContext(clazz, parentObject);
        newContext.hierarchy = hierarchy;
        newContext.hierarchy.push(classThatIsGenerated, parentObject);
        newContext.isPushed = true;
        return newContext;
    }

    public void pop(){
        if (isPushed) {
            hierarchy.pop();
            isPushed = false;
        }
    }

    @Override
    public String toString() {
        return "GenerationContext{" +
                "classGenerated=" + classThatIsGenerated.getName() +
                ", parentObject=" + parentObject +
                ", field=" + field +
                ", hierarchy.size=" + (hierarchy == null ? null : hierarchy.size()) +
                '}';
    }

    /**
     *
     * @param clazz
     * @return the last object of type {@code clazz} that was generated in generation-hierarchy
     *          or null if no such object
     */
    public Object getLastGeneratedObject(Class clazz) {
        return hierarchy.pick(clazz);
    }

    public String dumpHierarchy() {
        return hierarchy.dump();
    }

    public static GenerationContext createRoot(Class classThatIsGenerated){
        GenerationContext result = new GenerationContext(classThatIsGenerated);
        result.hierarchy = new Hierarchy();
        return result;
    }


    public boolean isPushed() {
        return isPushed;
    }
}