package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *     GenerationRule specifying that values will be generated from pre-defined list of objects.
 * </p>
 * <p>
 * User: __nocach
 * Date: 1.9.12
 * </p>
 */
class FromListGenerationRule<T> extends GenerationRule<T> {
    private List<T> values;
    private FromListGenerator fromListGenerator;

    public FromListGenerationRule(T... values){
        this(Arrays.asList(values));

    }
    public FromListGenerationRule(List<T> values){
        Assert.notNull(values, "at least one value must be provided, values are null");
        Assert.isTrue(!values.isEmpty(), "at least one value must be provided, values are empty");
        this.values = values;
        fromListGenerator = new FromListGenerator(values);
    }

    @Override
    protected T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return (T)fromListGenerator.generateValue();
    }

    private static class FromListGenerator {
        private List values;
        private int currentIndex = 0;

        private FromListGenerator(List values) {
            Assert.isTrue(!values.isEmpty());
            this.values = values;
        }

        public <T> T generateValue() {
            T result = (T)values.get(currentIndex);
            currentIndex++;
            if (currentIndex > values.size() - 1){
                currentIndex = 0;
            }
            return result;
        }

    }
}
