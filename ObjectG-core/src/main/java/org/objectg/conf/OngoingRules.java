package org.objectg.conf;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectg.gen.rule.Rules;
import org.objectg.gen.rule.range.Range;
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
	public static byte value(byte value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
		return 0;
	}
	public static char value(char value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
		return 0;
	}
	public static short value(short value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
		return 0;
	}
	public static int value(int value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
        return 0;
    }
	public static long value(long value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
        return 0;
    }
	public static float value(float value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
        return 0;
    }
	public static double value(double value){
		OngoingConfiguration.plannedRule = Rules.fromList(value);
        return 0;
    }

    public static  <T> T fromList(T... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return null;
    }
    public static  byte fromList(byte... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0;
    }
    public static char fromList(char... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0;
    }
    public static short fromList(short... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0;
    }
    public static int fromList(int... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0;
    }
    public static long fromList(long... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0L;
    }
    public static float fromList(float... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0f;
    }
    public static double fromList(double... values){
        OngoingConfiguration.plannedRule = Rules.fromList(values);
        return 0.;
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

	public static <T> T range(Range<T> range) {
		OngoingConfiguration.plannedRule = Rules.range(range);
		return null;
	}

	public static double range(Range<Double> range){
		OngoingConfiguration.plannedRule = Rules.range(range);
		return 0.;
	}

	public static int range(Range<Integer> range){
		OngoingConfiguration.plannedRule = Rules.range(range);
		return 0;
	}

	public static long range(Range<Long> range){
		OngoingConfiguration.plannedRule = Rules.range(range);
		return 0;
	}

	public static float range(Range<Float> range){
		OngoingConfiguration.plannedRule = Rules.range(range);
		return 0;
	}
}
