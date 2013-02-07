package org.objectg.conf;

import java.util.Collection;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.defaults.AbstractObjectGConfiguration;
import org.objectg.fixtures.domain.Departure;
import org.objectg.fixtures.domain.Guide;
import org.objectg.fixtures.domain.GuideAssignment;
import org.objectg.fixtures.domain.Person;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.rule.Rules;
import org.objectg.matcher.impl.PropertyNameMatcher;

import static java.util.Arrays.asList;

/**
 * User: __nocach
 * Date: 8.11.12
 */
public class DefaultsConfigurationTest extends FakeConfigurationProviderBaseTest {

	@Test
	public void emptyConfigurationWorks(){
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration(){
		};

		final Person generated = ObjectG.unique(Person.class);
		assertNotNull(generated);
	}

	@Test
	public void canDefineDefaultPrototypes(){
		//setup
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration(){
			@Override
			protected Collection<Object> getPrototypes() {
				final Departure departure = ObjectG.prototype(Departure.class);
				departure.setId(999L);

				final Guide guide = ObjectG.prototype(Guide.class);
				guide.setTourSpecialites("prototypedTourSpecialites");

				return asList(departure, guide);
			}
		};

		//execute
		final Departure generated = ObjectG.unique(Departure.class);

		assertEquals(999L, (long)generated.getId());
		assertEquals("prototypedTourSpecialites", generated.getStaff().getGuide().getTourSpecialites());
	}

	@Test
	public void canDefineDefaultGenerationConfiguration(){
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration() {
			@Override
			protected GenerationConfiguration getConfiguration() {
				return ObjectG.config().setObjectsInCollection(2).done();
			}
		};

		final Person generated = ObjectG.unique(Person.class);

		assertEquals(2, generated.getEmployee2Addresses().size());
	}

	@Test
	public void canDefineDefaultRules(){
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration() {
			@Override
			protected Collection<GenerationRule> getRules() {
				final GenerationRule firstNameRule = Rules.value("ruledFirstName");
				firstNameRule.when(new PropertyNameMatcher<String>(Person.class, "firstName"));
				return asList(firstNameRule);
			}
		};

		final Person generated = ObjectG.unique(Person.class);

		assertEquals("ruledFirstName", generated.getFirstName());
	}

	@Test
	public void canDefineDefaultPostProcessors(){
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration() {
			@Override
			protected Collection<? extends PostProcessor> getPostProcessors() {
				return asList(new PostProcessor() {
					@Override
					public <T> T process(final GenerationConfiguration configuration, final T generatedObject) {
						((Person)generatedObject).setFirstName("postProcessedFirstName");
						return generatedObject;
					}
				});
			}
		};

		final Person generated = ObjectG.unique(Person.class);

		assertEquals("postProcessedFirstName", generated.getFirstName());
	}


	@Test
	public void canRedefineDefaultPrototypes(){
		//setup
		fakeConfigurationProvider.defaultConfiguration = new AbstractObjectGConfiguration(){
			@Override
			protected Collection<? extends Object> getPrototypes() {
				final Departure departure = ObjectG.prototype(Departure.class);
				departure.setId(999L);
				return asList(departure);
			}
		};

		final Departure departurePrototype = ObjectG.prototype(Departure.class);
		departurePrototype.setId(111L);

		final GuideAssignment unique = ObjectG.unique(GuideAssignment.class, departurePrototype);

		assertEquals(111L, (long)unique.getDeparture().getId());
	}
}
