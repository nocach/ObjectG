package org.objectg.gen;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class GenerationContextTest {

    @Test
    public void canDefineRoot(){
        GenerationContext root = GenerationContext.createRoot(String.class);

        assertTrue(root.isRoot());
    }

    @Test
    public void afterPushThisIsNotRoot(){
        GenerationContext root = GenerationContext.createRoot(String.class);
        GenerationContext pushed = root.push(String.class);

        assertFalse(pushed.isRoot());
    }

	@Test
	public void willNotFallWhenParentObjectToStringThrewException(){
		final GenerationContext root = GenerationContext.createRoot(Object.class);
		root.setParentObject(new ParentObjectThrows());

		root.toString();
	}

	private static class ParentObjectThrows{
		@Override
		public String toString() {
			throw new RuntimeException();
		}
	}
}
