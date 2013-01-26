package org.objectg.conf;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectg.gen.rule.Rules;
import org.springframework.util.Assert;

/**
 * <p>
 *     Factory for rules used in configuration through {@link org.objectg.conf.prototype.PrototypeCreator}
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public class OngoingRules {

    public static <T> T value(T value){
        return fromList(value);
    }

    public static  <T> T fromList(T... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return null;
    }

    public static <T> List<T> listDefinition(Class clazzOfObjects) {
        return listDefinition(clazzOfObjects, 1);
    }
    public static <T> List<T> listDefinition(Class clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        OngoingConfiguration.plannedRule = Rules.listDefinition(clazzOfObjects, size);
        return null;
    }

    public static <T> List<T> listDefinition(Object... values) {
        OngoingConfiguration.plannedRule = Rules.listDefinition(values);
        return null;
    }

    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType, Class<ItemT> clazzOfObjects){
        OngoingConfiguration.plannedRule = Rules.collectionDefinition(collectionType, clazzOfObjects);
        return null;
    }

    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType, ItemT... values){
        OngoingConfiguration.plannedRule = Rules.collectionDefinition(collectionType, values);
        return null;
    }

    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType,
			Class<ItemT> clazzOfObjects, int objectsInCollection){
        OngoingConfiguration.plannedRule = Rules.collectionDefinition(collectionType, clazzOfObjects, objectsInCollection);
        return null;
    }

    public static <T> Set<T> setDefinition(Class<T> clazzOfObjects) {
        return setDefinition(clazzOfObjects, 1);
    }

    public static <T> Set<T> setDefinition(Class<T> clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        OngoingConfiguration.plannedRule = Rules.setDefinition(clazzOfObjects, size);
        return null;
    }

    public static <T> Set<T> setDefinition(T... values) {
        HashSet setWithValues = new HashSet();
        for (T each: values){
            setWithValues.add(each);
        }
        OngoingConfiguration.plannedRule = Rules.setDefinition(values);
        return null;
    }

	/**
	 * You need to explicitly type this method call. E.g. Person.setChild(OngoingRules.<Person>generatedObject()).
	 * This is due to the fact, that Java can't infer type without any object passing as arg, that can help in type
	 * determining. Must be typed by the expecting type of the setter, from which this OngoingRule was called
	 * @param <T>
	 * @return
	 */
	public static <T> T generatedObject() {
		OngoingConfiguration.plannedRule = Rules.generatedObject();
		return null;
	}

	public static <T> T overrideConfiguration(final ConfigurationBuilder configurationBuilder) {
		return (T)overrideConfiguration(configurationBuilder.done());
	}

	public static <T> T overrideConfiguration(final GenerationConfiguration configuration){
		OngoingConfiguration.plannedRule = Rules.configurationOverride(configuration);
		return null;
	}

	public static int range(final int from, final int to) {
		OngoingConfiguration.plannedRule = Rules.range(from, to);
		return 0;
	}
}
