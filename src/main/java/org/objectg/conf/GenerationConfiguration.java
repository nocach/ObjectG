package org.objectg.conf;

import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.cycle.CycleStrategy;
import org.objectg.gen.cycle.NullValueCycleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>
 *     Defines how generation must be performed.
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
    private CycleStrategy cycleStrategy = new NullValueCycleStrategy();
    private boolean isUnique;
    private Queue<GenerationRule> rules = new PriorityQueue();
    private List<PostProcessor> postProcessors = new LinkedList<PostProcessor>();

    /**
     * how many objects to generate into collections
     */
    private int objectsInCollections = 1;

    public GenerationConfiguration(){
    }

    public void addRule(GenerationRule rule) {
        rules.add(rule);
    }

    public boolean isUnique() {
        return isUnique;
    }

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
                logger.debug("rule=" +eachRule + " matched for context="+context);
                return eachRule;
            }
        }

        return null;
    }

    public GenerationConfiguration clone() {
        try{
            GenerationConfiguration result = (GenerationConfiguration) super.clone();
            //create defencive copies for collections
            result.rules = new PriorityQueue<GenerationRule>(this.rules);
            return result;
        }
        catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }



    /**
     * create copy of current configuration, but additional add new rules passed as param
     *
     * @param rulesToAdd
     * @return
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
                ", rules.size=" + rules.size() +
                '}';
    }

    public void addAllRules(List<GenerationRule> newRules) {
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

    public <T> T postProcess(T result) {
        Iterator<PostProcessor> iterator = postProcessors.iterator();
        while (iterator.hasNext()){
            PostProcessor each = iterator.next();
            iterator.remove();
            result = each.process(this, result);
        }
        return result;
    }
}