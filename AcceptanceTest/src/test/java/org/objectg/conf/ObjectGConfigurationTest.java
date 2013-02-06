package org.objectg.conf;

import java.util.List;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;

/**
 * <p>
 *     Test showing how ObjectGConfiguration definition works (see {@link ObjectGConfiguration}
 * </p>
 * <p>
 * User: __nocach
 * Date: 10.11.12
 * </p>
 */
public class ObjectGConfigurationTest extends BaseObjectGTest {

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
