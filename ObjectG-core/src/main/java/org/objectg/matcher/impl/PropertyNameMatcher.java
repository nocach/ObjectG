package org.objectg.matcher.impl;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class PropertyNameMatcher<U> extends TypeSafeMatcher<GenerationContext>
                                implements ValueTypeHintMatcher<GenerationContext, U>{
    private Class<?> parentClass;
    private String fieldName;

    public PropertyNameMatcher(Class<?> parentClass, String fieldName){
        super(GenerationContext.class);
        this.parentClass = parentClass;
        this.fieldName = fieldName;
    }
    @Override
    protected boolean matchesSafely(GenerationContext item) {
        if (fieldName == null) return false;
        if (item.getPropertyAccessor() == null) return false;
        boolean fieldMatched = fieldName.equals(item.getPropertyAccessor().getName());
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
