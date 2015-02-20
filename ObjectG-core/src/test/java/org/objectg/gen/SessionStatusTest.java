package org.objectg.gen;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.session.SessionState;
import org.objectg.gen.session.SessionStateDescription;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 26.1.13
 */

public class SessionStatusTest extends BaseObjectGTest {

	@Test
	public void testManagedStateIsThreadSafe() {

		Object owner = new Object();
		SessionState<Integer> managedState = GenerationSession.createManagedState(owner, Integer.class,
				new SessionStateDescription<Integer>() {
					@Override
					public Integer getInitValue() {
						return 0;
					}
				});
		//create main thread
		final SetStateTask mainSetStateTask = new SetStateTask(2, managedState);
		final RunnableThread mainThread = new RunnableThread(mainSetStateTask);
		//create other thread
		final SetStateTask otherSetStateTask = new SetStateTask(3, managedState);
		final RunnableThread otherThread = new RunnableThread(otherSetStateTask);

		//manipulate state from two different threads
		mainThread.start();
		otherThread.start();

		waitBothThreadsAreExecuted(mainThread, otherThread);

		//get states previously saved on different threads
		final GetStateTask getMainStateTask = new GetStateTask(managedState);
		final GetStateTask getOtherStateTask = new GetStateTask(managedState);
		mainThread.setRunnable(getMainStateTask);
		otherThread.setRunnable(getOtherStateTask);

		waitBothThreadsAreExecuted(mainThread, otherThread);
		//finish threads
		mainThread.doExist = true;
		otherThread.doExist = true;

		assertEquals("main thread state should be preserved, 2 was previously saved", (int) getMainStateTask.result, 2);
		assertEquals("other thread state should be preserved, 3 was previously saved", (int) getOtherStateTask.result,
				3);
	}

	private void waitBothThreadsAreExecuted(final RunnableThread mainThread,
			final RunnableThread otherThread) {
		while (!mainThread.wasRun || !otherThread.wasRun) ;
	}

	private class SetStateTask implements Runnable {
		private final int newState;
		private final SessionState<Integer> sessionState;

		public SetStateTask(int newState, SessionState<Integer> sessionState) {
			this.newState = newState;
			this.sessionState = sessionState;
		}

		@Override
		public void run() {
			sessionState.set(newState);
		}
	}

	private class GetStateTask implements Runnable {
		private volatile Integer result = null;
		private SessionState<Integer> sessionState;

		public GetStateTask(SessionState<Integer> sessionState) {
			this.sessionState = sessionState;
		}

		@Override
		public void run() {
			result = sessionState.get();
		}
	}

	/**
	 * thread that will execute runnable one. Runnable can be submitted more then once.
	 */
	private static class RunnableThread extends Thread {
		private Runnable runnable;
		volatile boolean wasRun = false;
		volatile boolean doExist = false;

		private RunnableThread(final Runnable runnable) {
			Assert.notNull(runnable, "runnable should not be null");
			this.runnable = runnable;
		}

		public Runnable getRunnable() {
			return runnable;
		}

		public void setRunnable(final Runnable runnable) {
			Assert.notNull(runnable, "runnable should not be null");
			wasRun = false;
			this.runnable = runnable;
		}

		@Override
		public void run() {
			while (!doExist) {
				if (runnable == null) continue;
				wasRun = false;
				try {
					runnable.run();
					runnable = null;
				} finally {
					wasRun = true;
				}
			}
		}
	}
}
