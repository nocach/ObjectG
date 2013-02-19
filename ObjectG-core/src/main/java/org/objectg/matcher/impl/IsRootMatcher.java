package org.objectg.matcher.impl;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.objectg.gen.GenerationContext;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class IsRootMatcher extends TypeSafeMatcher<GenerationContext>{

	private boolean root;

	public IsRootMatcher(boolean isRoot){
		root = isRoot;
	}

	@Override
	public boolean matchesSafely(final GenerationContext o) {
		return root == o.isRoot();
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("isRoot="+root);
	}
}
