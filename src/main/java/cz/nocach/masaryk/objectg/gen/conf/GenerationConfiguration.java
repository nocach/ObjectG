package cz.nocach.masaryk.objectg.gen.conf;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     Defines how generation must be performed. GenerationConfiguration is either global (for no class)
 *     or class specific. GenerationConfiguration for the same class or both for no classes (both global) are considered
 *     to be the same.
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
public class GenerationConfiguration implements Cloneable{
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
                if (eachRule.matches(context)) return eachRule;
            }
        }

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

    public GenerationConfiguration clone() throws CloneNotSupportedException {
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
}
