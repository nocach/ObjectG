package org.objectg.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.access.AccessStrategy;
import org.objectg.gen.cycle.CycleStrategy;
import org.objectg.gen.cycle.GoDeeperCycleStrategy;
import org.objectg.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 *     Defines how generation must be performed. Preferred way to define configuration is through builder
 *     returned by {@link org.objectg.ObjectG#config()}.
 * </p>
 * <p>
 *     Control of generation can be performed by using rules {@link GenerationRule} or by setting certain properties
 *     on configuration instance itself (e.g. unique)
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
public class GenerationConfiguration implements Cloneable{
    private static final Logger logger = LoggerFactory.getLogger(GenerationConfiguration.class);
	public static final int UNLIMITED_DEPTH = -1;

	////////////////////////
	//if you add property don't forget to modify the init and merge method
	//on initializing null attributes to null
	////////////////////////
    private CycleStrategy cycleStrategy;
    private Boolean isUnique;
    private Queue<GenerationRule> rules = new PriorityQueue();
    private List<PostProcessor> postProcessors = new LinkedList<PostProcessor>();
	private boolean wasInit = false;
    /**
     * how many objects to generate into collections
     */
    private Integer objectsInCollections;
	private LEVEL level;
	/**
	 * -1 means no depth
	 */
	private Integer depth;
	private AccessStrategy accessStrategy;

	public GenerationConfiguration(){
		this(LEVEL.USER);
	}

	/**
	 *
	 * @param level not null level of this configuration. Normally you should not use this constructor,
	 *              use the default one.
	 */
	public GenerationConfiguration(LEVEL level){
		Assert.notNull(level, "level");
		this.level = level;
	}

	/**
	 * Add rule that will be used during generation
	 * @param rule to add
	 */
    public void addRule(GenerationRule rule) {
		Assert.notNull(rule, "rule should not be null");
        rules.add(rule);
    }

    public boolean isUnique() {
        return isUnique;
    }

	/**
	 * Currently this does nothing
	 * @param unique if generated values should be unique.
	 */
    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    /**
     *
     * @param context not null context for which to return rule
     * @return most closely matched rule for the passed context.
     *          null if no rule was found
     */
    public GenerationRule getRule(GenerationContext context) {
        Assert.notNull(context, "context");

        for(GenerationRule eachRule : rules){
            if (eachRule.matches(context)) {
                logger.debug("rule=" +eachRule + " \nmatched context="+context);
                return eachRule;
            }
        }

        return null;
    }

	/**
	 *
	 * @return cloned configuration. list of rules and postprocessors are recreated but reference
	 * same instances. So deleting rule in returned configuration will not alter original configuraiton
	 * from which it was copied, but changing rule itself will still be reflected.
	 */
    public GenerationConfiguration clone() {
        try{
            GenerationConfiguration result = (GenerationConfiguration) super.clone();
            //create defencive copies for collections
            result.rules = new PriorityQueue<GenerationRule>(this.rules);
			result.postProcessors = new ArrayList<PostProcessor>(this.postProcessors);
            return result;
        }
        catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }



    /**
     * create copy of current configuration, but additionally add new rules passed as param
     *
     * @param rulesToAdd not null rules to add
     * @return copied configuraiton with added rules
     */
    public GenerationConfiguration newWithMoreRules(List<GenerationRule> rulesToAdd) {
        GenerationConfiguration overriden = clone();
        overriden.addAllRules(rulesToAdd);
        return overriden;
    }

    public int getObjectsInCollections() {
        return objectsInCollections;
    }

    public void setObjectsInCollections(int objectsInCollections) {
        Assert.isTrue(objectsInCollections >= 0, "objectsInCollections must be >= 0");
        this.objectsInCollections = objectsInCollections;
    }

    @Override
    public String toString() {
        return "GenerationConfiguration{" +
                "isUnique=" + isUnique +
				", objectsInCollection=" + objectsInCollections +
				", postProcessors.size=" + postProcessors.size() +
				", depth=" + depth +
				", cycleStrategy=" + cycleStrategy +
                ", rules.size=" + rules.size() +
                '}';
    }

    public void addAllRules(Collection<? extends GenerationRule> newRules) {
        this.rules.addAll(newRules);
    }

    public CycleStrategy getCycleStrategy() {
        return cycleStrategy;
    }

    public void setCycleStrategy(CycleStrategy cycleStrategy) {
        Assert.notNull(cycleStrategy, "cycleStrategy");
        this.cycleStrategy = cycleStrategy;
    }

    public void removeRule(GenerationRule rule) {
        rules.remove(rule);
    }

    public void addPostProcessor(PostProcessor postProcessor) {
        Assert.notNull(postProcessor, "postProcessor");
        postProcessors.add(postProcessor);
    }

	/**
	 * called by framework to apply postProcessors for the generated value
	 * @param result result of generation
	 * @return value after post processing
	 */
    public <T> T postProcess(T result) {
        Iterator<PostProcessor> iterator = postProcessors.iterator();
        while (iterator.hasNext()){
            PostProcessor each = iterator.next();
            iterator.remove();
            result = each.process(this, result);
        }
        return result;
    }

	/**
	 * merge all information from passed configuration into this instance.
	 * Will override DEFINED values only if thatConfiguration's level is higher then this configuration's level
	 * @param thatConfiguration
	 */
	public GenerationConfiguration merge(final GenerationConfiguration thatConfiguration) {
		//if some this.property is set then we can override it with that.property from
		//configuration we merge, only if that.level configuration is bigger then this.level
		//e.g. this.level=LOCAL and we merge that.level=USER.
		//During this merge we WANT to override any property that is defined in both configurations
		//but for reverse this is not true
		boolean canOverride = thatConfiguration.level.compareTo(this.level) > 0;
		merge(thatConfiguration, canOverride);

		return this;
	}

	/**
	 * merge all information from passed configuration into this instance
	 * @param thatConfiguration
	 * @param canOverride if thatConfiguration can override DEFINED values
	 */
	public void merge(final GenerationConfiguration thatConfiguration, final boolean canOverride) {
		//only those properties that are defined in passed configuration
		//will be set
		//This is need, because if some this.property isn't null and that.property is null
		//then in a result this.property will be set to null but DEFAULT will be used instead
		if (thatConfiguration.cycleStrategy != null){
			if (this.cycleStrategy == null || canOverride)
				cycleStrategy = thatConfiguration.cycleStrategy;
		}
		if (thatConfiguration.accessStrategy != null){
			if (this.accessStrategy == null || canOverride)
				accessStrategy = thatConfiguration.accessStrategy;
		}
		if (thatConfiguration.isUnique != null){
			if (this.isUnique == null || canOverride)
				isUnique = thatConfiguration.isUnique;
		}
		if (thatConfiguration.objectsInCollections != null){
			if (this.objectsInCollections == null || canOverride)
				objectsInCollections = thatConfiguration.objectsInCollections;
		}
		if (thatConfiguration.depth != null){
			if (this.depth == null || canOverride){
				this.depth = thatConfiguration.depth;
			}
		}
		rules.addAll(thatConfiguration.rules);
		postProcessors.addAll(thatConfiguration.postProcessors);
	}

	/**
	 * call when GenerationConfiguration is about to be used.
	 * This method will set defaults on all null attributes
	 */
	public void init() {
		if (wasInit) return;
		if (cycleStrategy == null){
			cycleStrategy = new GoDeeperCycleStrategy(1);
		}
		if (isUnique == null){
			isUnique = false;
		}
		if (objectsInCollections == null){
			objectsInCollections = 1;
		}
		if (depth == null){
			depth = -1;
		}
		if (accessStrategy == null){
			accessStrategy = AccessStrategy.ONLY_FIELDS;
		}
		wasInit = true;
	}

	public void addAllPostProcessors(final Collection<? extends PostProcessor> postProcessors) {
		this.postProcessors.addAll(postProcessors);
	}

	public void setLevel(final LEVEL level) {
		this.level = level;
	}

	/**
	 * how deep to go into object graph.
	 * @param depth >= 0. -1 means no depth.
	 */
	public void setDepth(final int depth) {
		this.depth = depth;
	}

	public Integer getDepth() {
		return depth;
	}

	public <T> boolean shouldGenerate(final GenerationContext<T> context) {
		if (getDepth() == UNLIMITED_DEPTH) return true;
		return context.getGenerationDepth() <= getDepth()
				|| Types.isJavaType(context.getClassThatIsGenerated());
	}

	public void setAccessStrategy(final AccessStrategy accessStrategy) {
		this.accessStrategy = accessStrategy;
	}

	public AccessStrategy getAccessStrategy() {
		return accessStrategy;
	}

	/**
	 * level on which this configuration should be applied
	 */
	public static enum LEVEL{
		/**
		 * configuration should be applied for every generation
		 */
		GLOBAL,
		/**
		 * configuration should be applied for single test case
		 */
		LOCAL,
		/**
		 * configuration should be applied for single generation
		 */
		USER
	}
}
