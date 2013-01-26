package org.objectg.integration;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.objectg.ObjectG;

/**
 * User: __nocach
 * Date: 22.1.13
 */
public class ObjectGTestRule implements TestRule {

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new ObjectGLifeCycleStatement(base);
	}

	private static class ObjectGLifeCycleStatement extends Statement{

		private Statement next;

		public ObjectGLifeCycleStatement(Statement next){
			this.next = next;
		}

		@Override
		public void evaluate() throws Throwable {
			ObjectG.setup();
			try{
				next.evaluate();
			}
			finally {
				ObjectG.teardown();
			}
		}
	}
}
