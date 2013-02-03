package org.objectg.conf;

import org.hamcrest.Matchers;
import org.objectg.conf.prototype.PrototypeCreator;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.access.AccessStrategy;
import org.objectg.gen.cycle.CycleStrategy;
import org.objectg.gen.rule.Rules;
import org.objectg.matcher.ContextMatchers;
import org.objectg.matcher.ValueTypeHintMatcher;
import org.objectg.matcher.impl.GenerationContextFeatures;
import org.objectg.matcher.impl.JavaNativeTypeMatcher;
import org.springframework.util.Assert;

import static org.objectg.matcher.impl.GenerationContextFeatures.forIsRoot;

/**
 * <p>
 *     call {@link #done()} to create configuration itself
 * </p>
 * <p>
 * User: __nocach
 * Date: 29.9.12
 * </p>
 */
public class ConfigurationBuilder {
    private GenerationConfiguration resultConfiguration;
    private PrototypeCreator prototypeCreator;
	boolean notAllowSetInObjectChange = false;

    public ConfigurationBuilder(PrototypeCreator prototypeCreator){
        Assert.notNull(prototypeCreator, "prototypeCreator");
        this.prototypeCreator = prototypeCreator;
        resultConfiguration = new GenerationConfiguration();
    }

    /**
     * will set only primitive attributes
     * collections, arrays and maps are still generated, but will be empty.
     */
    public ConfigurationBuilder onlyPrimitives() {
        GenerationRule nullForNotRootObjects = Rules.onlyNull();
		//for not native types set null
		//except the generated object
        nullForNotRootObjects.when(
                Matchers.<GenerationContext>allOf(
                        Matchers.not(GenerationContextFeatures.forClass(JavaNativeTypeMatcher.INSTANCE))
                        , forIsRoot(Matchers.is(false))
                )
        );
        resultConfiguration.addRule(nullForNotRootObjects);

		resultConfiguration.setObjectsInCollections(0);
		notAllowSetInObjectChange = true;

        return this;
    }

    public GenerationConfiguration done() {
        return resultConfiguration;
    }

    /**
     *
     * @param contextMatcher see {@link ContextMatchers}
     * @param <U>
     * @return
     */
    public <U> WhenBuilder<U> when(ValueTypeHintMatcher<GenerationContext, ? extends U> contextMatcher){
        return new WhenBuilder(contextMatcher, this);
    }

    public <T> WhenBuilder<T> forClass(Class<? extends T>... classes){
        Assert.isTrue(classes.length > 0, "should be at least one class");
        Assert.noNullElements(classes, "should not contain null elements");
        return new WhenBuilder(ContextMatchers.typeOf(classes), this);
    }

    void addRule(GenerationRule rule) {
        Assert.notNull(rule, "rule");
        resultConfiguration.addRule(rule);
    }

    /**
     *
     * @param propertyExpression expression defining property for which this WhenBuilder will be applied.
     *                           collections and arrays are supported.
     *                           Example : {@code when("person2Address[0].owner.firstName)"}
     * @param <T>
     * @return
     */
    public <T> WhenBuilder<T> when(String propertyExpression) {
        return new WhenBuilder<T>(propertyExpression, this);
    }

    public ConfigurationBuilder addPostProcessor(PostProcessor postProcessor) {
        resultConfiguration.addPostProcessor(postProcessor);
        return this;
    }

    PrototypeCreator getPrototypeCreator() {
        return prototypeCreator;
    }

	public <T> WhenBuilder<T> when(final Class<T> clazz) {
		return new WhenBuilder<T>(ContextMatchers.typeOf(clazz), this);
	}

	public ConfigurationBuilder setObjectsInCollection(final int objectsInCollection) {
		Assert.isTrue(objectsInCollection >= 0 , "objectsInCollection should be >= 0");
		Assert.isTrue(!notAllowSetInObjectChange, "can't change ObjectsInCollection after onlyPrimitives() was called");

		resultConfiguration.setObjectsInCollections(objectsInCollection);
		return this;
	}

	public CycleBuilder onCycle() {
		return new CycleBuilder(this);
	}

	public ConfigurationBuilder setCycleStrategy(final CycleStrategy strategy) {
		resultConfiguration.setCycleStrategy(strategy);
		return this;
	}

	/**
	 *
	 * @param depth how deep to go into the object graph when generating root object
	 *              depth(0) means that only generating object will be created, any other not primitive field will
	 *              be omitted.
	 * @return
	 */
	public ConfigurationBuilder depth(final int depth) {
		Assert.isTrue(depth >= 0, "depth must be >= 0");
		resultConfiguration.setDepth(depth);
		return this;
	}

	/**
	 * set no limit generation depth
	 */
	public ConfigurationBuilder noDepth(){
		resultConfiguration.setDepth(GenerationConfiguration.UNLIMITED_DEPTH);
		return this;
	}

	public AccessBuilder access() {
		return new AccessBuilder(this);
	}

	/**
	 *
	 * @param accessStrategy not null
	 */
	public ConfigurationBuilder setAccessStrategy(final AccessStrategy accessStrategy) {
		Assert.notNull(accessStrategy, "accessStrategy should not be null");
		resultConfiguration.setAccessStrategy(accessStrategy);
		return this;
	}
}
