package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.GeneratorForSingleClass;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * User: __nocach
 * Date: 1.9.12
 */
public class FromListGenerationRule extends GenerationRule {
    private Class classOfTheFirstValue;
    private List values;

    public <T> FromListGenerationRule(T... values){
        this(Arrays.asList(values));
    }
    public <T> FromListGenerationRule(List<T> values){
        Assert.notNull(values, "at least one value must be provided, values are null");
        Assert.isTrue(!values.isEmpty(), "at least one value must be provided, values are empty");
        assignClassOfNotNullValue(values);
        this.values = values;
    }

    private <T> void assignClassOfNotNullValue(List<T> values) {
        for (T each : values){
            if (each != null){
                classOfTheFirstValue = each.getClass();
                return;
            }
        }
        //TODO: add hint to the exception that NullGenerator must be used to set null valeus only
        throw new IllegalArgumentException("at least one not null value must exist");
    }

    @Override
    protected Generator getGenerator(GenerationConfiguration currentConfiguration) {
        return new FromListGenerator(classOfTheFirstValue, values);
    }

    private static class FromListGenerator extends GeneratorForSingleClass {
        private List values;
        private int currentIndex = 0;

        private FromListGenerator(Class targetClass, List values) {
            super(targetClass);
            Assert.isTrue(!values.isEmpty());
            this.values = values;
        }

        @Override
        public <T> T generate(Class<T> type) {
            T result = (T)values.get(currentIndex);
            currentIndex++;
            if (currentIndex > values.size() - 1){
                currentIndex = 0;
            }
            return result;
        }

    }
}
