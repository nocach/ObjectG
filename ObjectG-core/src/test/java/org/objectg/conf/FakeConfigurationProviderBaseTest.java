package org.objectg.conf;

import org.junit.After;
import org.junit.Before;
import org.objectg.BaseObjectGTest;
import org.objectg.conf.defaults.AbstractObjectGConfiguration;
import org.objectg.conf.defaults.DefaultConfigurationProvider;
import org.objectg.conf.defaults.DefaultConfigurationProviderHolder;

/**
 * User: __nocach
 * Date: 10.11.12
 */
public abstract class FakeConfigurationProviderBaseTest extends BaseObjectGTest {
	private DefaultConfigurationProvider previous;
	protected FakeConfigurationProvider fakeConfigurationProvider;
	@Before
	public void setup(){
		previous = DefaultConfigurationProviderHolder.get();
		fakeConfigurationProvider = new FakeConfigurationProvider();
		DefaultConfigurationProviderHolder.set(fakeConfigurationProvider);
	}

	@After
	public void teardown(){
		DefaultConfigurationProviderHolder.set(previous);
	}

	protected static class FakeConfigurationProvider implements DefaultConfigurationProvider{

		protected AbstractObjectGConfiguration defaultConfiguration;

		@Override
		public AbstractObjectGConfiguration getDefaultConfiguration() {
			return defaultConfiguration;
		}
	}
}
