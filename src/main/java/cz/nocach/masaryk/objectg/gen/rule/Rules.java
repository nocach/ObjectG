package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import org.springframework.util.Assert;

import java.util.*;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Rules {
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

    public static GenerationRule listDefinition(Class clazzOfObjects, Object... values) {
        return new SingleValueGenerationRule(Arrays.asList(values));
    }

    public static GenerationRule collectionDefinition(Class collectionType, Class clazzOfObjects){
        return new CollectionGenerationRule(collectionType, clazzOfObjects);
    }

    public static GenerationRule setDefinition(Class clazzOfObjects) {
        return setDefinition(clazzOfObjects, 1);
    }

    public static GenerationRule setDefinition(Class<String> clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        return new GenericSetGenerationRule(clazzOfObjects, size);
    }

    public static GenerationRule setDefinition(Class<String> clazzOfObjects, Object... values) {
        HashSet setWithValues = new HashSet();
        for (Object each: values){
            setWithValues.add(each);
        }
        return new SingleValueGenerationRule(setWithValues);
    }

    public static GenerationRule onlyNull(){
        return new NullValueGenerationRule();
    }
}
