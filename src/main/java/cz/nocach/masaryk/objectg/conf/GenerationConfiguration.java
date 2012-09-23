package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     Defines how generation must be performed.
 * </p>
 * <p>GenerationConfiguration is either global (for no class)
 *     or class specific. Configuration can be extended by child nodes (see {@link #putChild(GenerationConfiguration)}.
 *     Only one child of GenerationConfiguration for the same class or for no class (global) can exists at the same time
 *     (so if you call {@link #putChild(GenerationConfiguration)} and there is already child for the same class, then
 *     old configuration will be override). For side effect free adding of child configuration
 *     use {@link #newWithOverride(GenerationConfiguration)}.
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
    private boolean isUnique;
    private final Class forClass;
    private Set<GenerationConfiguration> children = new HashSet<GenerationConfiguration>();
    private List<GenerationRule> rules = new LinkedList<GenerationRule>();

    public GenerationConfiguration(){
        this(null);
    }

    /**
     *
     * @param forClass if not null, then GenerationConfiguration will be applied ONLY for the passed class generation
     */
    public GenerationConfiguration(Class forClass) {
        this.forClass = forClass;
    }

    public void addRule(GenerationRule rule) {
        rules.add(rule);
    }

    /**
     *
     * @param generationConfiguration
     */
    public void putChild(GenerationConfiguration generationConfiguration){
        //must retain consistency in globally applied properties
        generationConfiguration.setUnique(this.isUnique());
        children.add(generationConfiguration);
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
        for (GenerationConfiguration each : children){
            each.setUnique(unique);
        }
    }

    /**
     *
     * @param context not null context for which to return rule
     * @return most closely matched rule for the passed context.
     *          null if no rule was found
     */
    public GenerationRule getRule(GenerationContext context) {
        Assert.notNull(context, "context");

        if (isConfigurationForThePassedContext(context)){
            for(GenerationRule eachRule : rules){
                if (eachRule.matches(context)) {
                    logger.debug("rule=" +eachRule + " matched for context="+context);
                    return eachRule;
                }
                else {
                    logger.debug("rule=" +eachRule + " did not match for context="+context);
                }
            }
        }

        //try to find GenerationRule in children
        for (GenerationConfiguration eachChild : children){
            GenerationRule result = eachChild.getRule(context);
            if (result != null) return result;
        }
        return null;
    }

    private boolean isConfigurationForThePassedContext(GenerationContext context) {
        return forClass == null ||
                (context.getField() != null && forClass.isAssignableFrom(context.getField().getDeclaringClass()));
    }

    protected GenerationConfiguration clone() throws CloneNotSupportedException {
        GenerationConfiguration result = (GenerationConfiguration) super.clone();
        //create defencive copies for collections
        result.children = new HashSet<GenerationConfiguration>(this.children);
        result.rules = new LinkedList<GenerationRule>(this.rules);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenerationConfiguration that = (GenerationConfiguration) o;

        if (forClass != null ? !forClass.equals(that.forClass) : that.forClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return forClass != null ? forClass.hashCode() : 0;
    }

    /**
     * create copy of current configuration, but additional add or replace configuration of {@code configuration}
     * @param configuration
     * @return
     */
    public GenerationConfiguration newWithOverride(GenerationConfiguration configuration) {
        try {
            GenerationConfiguration overriden = clone();
            overriden.putChild(configuration);
            return overriden;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "GenerationConfiguration{" +
                "isUnique=" + isUnique +
                ", forClass=" + forClass +
                ", children.size=" + children.size() +
                ", rules.size=" + rules.size() +
                '}';
    }
}
