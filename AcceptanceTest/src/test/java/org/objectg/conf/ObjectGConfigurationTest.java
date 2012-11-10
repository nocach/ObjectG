package org.objectg.conf;

import java.util.List;

import org.junit.Test;
import org.objectg.ObjectG;

import static junit.framework.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 10.11.12
 */
public class ObjectGConfigurationTest {

	@Test
	public void globalConfigurationIsApplied(){
		final NameCatalog generated = ObjectG.unique(NameCatalog.class);

		assertEquals("ObjectGConfiguration sets collection size to 2",
				2, generated.getNames().size());
	}

	public static class NameCatalog {
		private List<String> names;

		public List<String> getNames() {
			return names;
		}

		public void setNames(final List<String> names) {
			this.names = names;
		}
	}
}
