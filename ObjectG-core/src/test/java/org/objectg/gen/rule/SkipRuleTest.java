package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.PrototypeRules;

/**
 * User: __nocach
 * Date: 3.2.13
 */
public class SkipRuleTest extends BaseObjectGTest{

	@Test
	public void testCanSkipField(){
		final FieldChangeObserver prototype = ObjectG.prototype(FieldChangeObserver.class);
		prototype.setSkipField(PrototypeRules.<String>skip());

		final FieldChangeObserver changeObserver = ObjectG.unique(prototype, ObjectG.config().access().onlyMethods());

		assertTrue("normal field should be generated as per usual", changeObserver.wasNormalFieldChanged);
		assertFalse("skip field should be untouched", changeObserver.wasSkipFieldChanged);
	}

	public static class FieldChangeObserver{
		private String normalField;
		private String skipField;
		private boolean wasNormalFieldChanged;
		private boolean wasSkipFieldChanged;

		public String getNormalField() {
			return normalField;
		}

		public void setNormalField(final String normalField) {
			wasNormalFieldChanged = true;
			this.normalField = normalField;
		}

		public String getSkipField() {
			return skipField;
		}

		public void setSkipField(final String skipField) {
			this.skipField = skipField;
			wasSkipFieldChanged = true;
		}
	}
}
