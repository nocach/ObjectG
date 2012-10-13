package cz.nocach.masaryk.objectg.matcher;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class FieldNameMatcher extends TypeSafeMatcher<GenerationContext> {
    private Class<?> parentClass;
    private String fieldName;

    public FieldNameMatcher(Class<?> parentClass, String fieldName){
        super(GenerationContext.class);
        this.parentClass = parentClass;
        this.fieldName = fieldName;
    }
    @Override
    protected boolean matchesSafely(GenerationContext item) {
        if (fieldName == null) return false;
        if (item.getField() == null) return false;
        boolean fieldMatched = fieldName.equals(item.getField().getName());
        boolean parentClassInfoDefined = parentClass != null;
        boolean parentClassInContext = item.getParentObject() != null;
        boolean parentClassMatched = !parentClassInfoDefined ||
                parentClassInContext && parentClass.isAssignableFrom(item.getParentObject().getClass());
        return fieldMatched && parentClassMatched;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue("fieldName="+fieldName);
    }
}
