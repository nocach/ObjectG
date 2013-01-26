package org.objectg.gen;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.objectg.BaseObjectGTest;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.session.SessionState;
import org.objectg.gen.session.SessionStateDescription;

/**
 * User: __nocach
 * Date: 26.1.13
 */

public class SessionStatusTest extends BaseObjectGTest{

	public static class ConcurrentManagedState extends BaseObjectGTest{
		private static Object owner1 = new Object();
		private static SessionState<Integer> managedState;

		@BeforeClass
		public static void createManagedState(){
			managedState = GenerationSession.createManagedState(owner1, Integer.class, new SessionStateDescription<Integer>() {
				@Override
				public Integer getInitValue() {
					return 0;
				}
			});
		}

		@Test
		public void testOne(){
			//managedState should preserve state per thread
			for (int i = 0; i < 1000; i++){
				assertTrue(managedState.get() < 1000);
				managedState.set(i);
			}
		}

		@Test
		public void testTwo(){
			//managedState should preserve state per thread
			for (int i = 1001; i < 2000; i++){
				assertTrue("managedState="+managedState.get(), managedState.get() > 1000 || managedState.get() == 0);
				managedState.set(i);
			}
		}
	}

	@Test
	public void testManagedStateIsThreadSafe(){
		final Result result = JUnitCore.runClasses(ParallelComputer.methods(), ConcurrentManagedState.class);

		assertTrue("failures="+ Arrays.toString(result.getFailures().toArray()), result.wasSuccessful());
	}
}
