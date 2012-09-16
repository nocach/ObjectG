package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.apache.commons.beanutils.BeanUtils;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 1.9.12
 */
public abstract class GenerationRule {
    private String forProperty;

    public void when(Matcher<?> matcher) {
        throw new RuntimeException("not implemented yet");
    }

    protected abstract Generator getGenerator();

    /**
     * @param forProperty for which property this rule must be applied
     */
    public void setForProperty(String forProperty) {
        this.forProperty = forProperty;
    }

    public String getForProperty() {
        return forProperty;
    }

    public boolean matches(GenerationContext context){
        //TODO: use matchers in future
        if (context.getField() == null) return false;
        return  context.getField().getName().equals(forProperty);
    }
}
