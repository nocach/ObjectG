package org.objectg.matcher.impl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.objectg.util.Types;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class JavaNativeTypeMatcher extends TypeSafeMatcher<Class> {
    public static Matcher<Class> INSTANCE  = new JavaNativeTypeMatcher();

    public JavaNativeTypeMatcher() {
        super(Class.class);
    }

    @Override
    protected boolean matchesSafely(Class item) {
        return Types.isJavaType(item);
    }


    @Override
    public void describeTo(Description description) {
        description.appendValue("java native type");
    }
}
