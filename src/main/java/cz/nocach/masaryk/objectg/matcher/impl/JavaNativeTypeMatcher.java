package cz.nocach.masaryk.objectg.matcher.impl;

import cz.nocach.masaryk.objectg.util.Types;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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
