package org.objectg.conf;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.defaults.AbstractObjectGConfiguration;
import org.objectg.fixtures.domain.Person;

import static junit.framework.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 10.11.12
 */
public class LocalVsDefaultsConfigTest extends FakeConfigurationProviderBaseTest {

	@Override
	public void setup() {
		super.setup();
		ObjectG.configLocal(ObjectG
				.config()
				.setObjectsInCollection(2)
				.done());
	}

	@Test
	public void localConfigurationOverridesDefaults(){
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration() {
			@Override
			protected GenerationConfiguration getConfiguration() {
				return ObjectG.config().setObjectsInCollection(3).done();
			}
		};

		final Person generated = ObjectG.unique(Person.class);

		assertEquals(2, generated.getEmployee2Addresses().size());
	}
}
