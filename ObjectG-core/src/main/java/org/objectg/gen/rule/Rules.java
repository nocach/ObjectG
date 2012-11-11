package org.objectg.gen.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.objectg.gen.GenerationRule;
import org.objectg.gen.RuleScope;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Rules {

    private static final JpaRule jpaRule = new JpaRule(RuleScope.INTERNAL_DEFAULT);

    public static GenerationRule value(Object value){
        return fromList(value);
    }

    public static GenerationRule fromList(Object... values){
        return new FromListGenerationRule(values);
    }

    public static GenerationRule specificRules(List<GenerationRule> specificRules) {
        return new RulesOverrideGenerationRule(specificRules);
    }

    public static GenerationRule listDefinition(Class clazzOfObjects) {
        return listDefinition(clazzOfObjects, 1);
    }
    public static GenerationRule listDefinition(Class clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        return new GenericListGenerationRule(clazzOfObjects, size);
    }

    public static GenerationRule listDefinition(Object... values) {
        return new SingleValueGenerationRule(Arrays.asList(values));
    }

    public static GenerationRule collectionDefinition(Class collectionType, Class clazzOfObjects){
        return new CollectionGenerationRule(collectionType, clazzOfObjects);
    }

    public static GenerationRule setDefinition(Class clazzOfObjects) {
        return setDefinition(clazzOfObjects, 1);
    }

    public static GenerationRule setDefinition(Class clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        return new GenericSetGenerationRule(clazzOfObjects, size);
    }

    public static <T> GenerationRule setDefinition(T... values) {
        HashSet setWithValues = new HashSet();
        for (Object each: values){
            setWithValues.add(each);
        }
        return new SingleValueGenerationRule(setWithValues);
    }

    public static GenerationRule onlyNull(){
        return new NullValueGenerationRule();
    }

    public static List<GenerationRule> defaultRules(){
        List<GenerationRule> result = new ArrayList<GenerationRule>();
        result.add(jpaRule);
        return result;
    }

    public static GenerationRule emptyCollection(){
        return new EmptyCollectionGenerationRule();
    }

    public static GenerationRule emptyMap(){
        return new EmptyMapCollectionGenerationRule();
    }

	/**
	 *
	 * @param contextTransformer not null
	 * @return permanent contextOverride rule
	 */
	public static GenerationRule contextOverride(GenerationContextTransformer contextTransformer){
		boolean permanent = true;
		return contextOverride(contextTransformer, permanent);
	}

	/**
	 *
	 * @param contextTransformer not null
	 * @param permanent if this rule should be permanent (will be present during whole generation)
	 * @return
	 */
	public static GenerationRule contextOverride(GenerationContextTransformer contextTransformer
		, boolean permanent){
		return new OverrideContextGenerationRule(contextTransformer, permanent);
	}

	public static GenerationRule generatedObject() {
		return new SetGeneratedObjectGenerationRule();
	}
}
