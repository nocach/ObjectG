package org.objectg;

import org.junit.Before;
import org.junit.Test;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 17.11.12
 */
public class CanEasilyDefineOwnGeneratorTest extends BaseObjectGTest {

	@Before
	public void setup() {
		ObjectG.configLocal(ObjectG.config()
				.when(SpecificClass.class)
				.useRule(new SpecificClassGenerationRule()));
	}

	@Test
	public void show() {
		final SpecificClass unique = ObjectG.unique(SpecificClass.class);

		assertNotNull(unique);
	}


	public static class SpecificClass {
		private int index;

		private SpecificClass(int index) {
			this.index = index;
		}

		public static SpecificClass create(int index) {
			return new SpecificClass(index);
		}
	}

	private static class SpecificClassGenerationRule extends GenerationRule<SpecificClass> {

		@Override
		public SpecificClass getValueInner(final GenerationConfiguration currentConfiguration,
				final GenerationContext context) {
			return SpecificClass.create(ObjectG.unique(int.class));
		}
	}
}
