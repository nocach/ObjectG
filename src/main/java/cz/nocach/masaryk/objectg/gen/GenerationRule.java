package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import org.hamcrest.Matcher;

/**
 * <p>
 *     GenerationRule defines what specific rules and when are used for generating of property value
 * </p>
 * <p>
 * User: __nocach
 * Date: 1.9.12
 * </p>
 */
public abstract class GenerationRule {
    private String forProperty;

    public void when(Matcher<?> matcher) {
        throw new RuntimeException("not implemented yet");
    }

    protected abstract <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context);

    /**
     * @param forProperty for which property this rule must be applied
     */
    public void setForProperty(String forProperty) {
        this.forProperty = forProperty;
    }

    public String getForProperty() {
        return forProperty;
    }

    /**
     *
     * @param context
     * @return true if this rule must be applied in the passed context
     */
    public boolean matches(GenerationContext context){
        //TODO: use matchers in future
        if (context.getField() == null) return false;
        return  context.getField().getName().equals(forProperty);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" +
                "forProperty='" + forProperty + '\'' +
                '}';
    }

}
