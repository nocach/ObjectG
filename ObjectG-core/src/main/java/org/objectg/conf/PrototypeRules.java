package org.objectg.conf;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectg.gen.GenerationRule;
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
public class PrototypeRules {

	/**
	 *
	 * @param value set exactly this value
	 * @param <T> value type
	 */
    public static <T> T value(T value){
        return sequence(value);
    }

	/**
	 * @param value set exactly this value
	 */
	public static byte value(byte value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
		return 0;
	}

	/**
	 *
	 * @param value set exactly this value
	 */
	public static boolean value(boolean value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
		return false;
	}

	/**
	 *
	 * @param value set exactly this value
	 */
	public static char value(char value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
		return 0;
	}

	/**
	 * @param value set exactly this value
	 */
	public static short value(short value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
		return 0;
	}

	/**
	 * @param value set exactly this value
	 */
	public static int value(int value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
        return 0;
    }

	/**
	 *
	 * @param value set exactly this value
	 */
	public static long value(long value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
        return 0;
    }

	/**
	 * @param value set exactly this value
	 */
	public static float value(float value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
        return 0;
    }

	/**
	 *
	 * @param value set exactly this value
	 */
	public static double value(double value){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(value));
        return 0;
    }

	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static  <T> T sequence(T... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return null;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
	public static boolean sequence(boolean... values){
		PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
		return false;
	}
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
	public static  byte sequence(byte... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static char sequence(char... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static short sequence(short... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static int sequence(int... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static long sequence(long... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0L;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static float sequence(float... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0f;
    }
	/**
	 *
	 * @param values sequence of values from which field will be populated. Each new generation will use next value
	 *               from the sequence. If the last value is reached then next values will be generated from the start.
	 */
    public static double sequence(double... values){
        PrototypeConfiguration.setPlannedRule(Rules.sequence(values));
        return 0.;
    }

	/**
	 * Rule that will create List with one generated object of type {@code clazzOfObject}. New object will be generated
	 * on each rule invocation.
	 * @param clazzOfObject class of object that will be in the list
	 */
    public static <T> List<T> listDefinition(Class<T> clazzOfObject) {
        return listDefinition(clazzOfObject, 1);
    }
	/**
	 * Rule that will create List with {@code objectsInCollection} generated objects of type {@code clazzOfObjects}. Objects will
	 * be generated on each rule invocation.
	 * @param clazzOfObjects class of object that will be in the list
 	 * @param objectsInCollection number of objects that will be generated into collection
	 */
    public static <T> List<T> listDefinition(Class<T> clazzOfObjects, int objectsInCollection) {
        Assert.isTrue(objectsInCollection >= 0, "objectsInCollection must be >= 0");
        PrototypeConfiguration.setPlannedRule(Rules.listDefinition(clazzOfObjects, objectsInCollection));
        return null;
    }
	/**
	 * Rule that will create List populated with {@code values}.
	 * @param values values that will be added to collection
	 */
    public static <T> List<T> listDefinition(T... values) {
        PrototypeConfiguration.setPlannedRule(Rules.listDefinition(values));
        return null;
    }

	/**
	 * Rule that will create collection of type {@code collectionType} with one generated object of
	 * type {@code clazzOfObject}. New object will be generated on each rule invocation.
	 * @param collectionType type of the collection that will be created for each rule invocation
	 * @param clazzOfObject class of object that will be in the list
	 */
    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType, Class<ItemT> clazzOfObject){
        PrototypeConfiguration.setPlannedRule(Rules.collectionDefinition(collectionType, clazzOfObject));
        return null;
    }
	/**
	 * Rule that will create collection of type {@code collectionType} populated with {@code values}.
	 * @param collectionType type of the collection that will be created for each rule invocation
	 * @param values values that will be added to collection
	 */
    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType, ItemT... values){
        PrototypeConfiguration.setPlannedRule(Rules.collectionDefinition(collectionType, values));
        return null;
    }
	/**
	 * Rule that will create collection of type {@code collectionType} populated with {@code objectsInCollection}
	 * generated objects of type {@code clazzOfObject}. New objects will be generated on each rule invocation.
	 * @param collectionType type of the collection that will be created for each rule invocation
	 * @param clazzOfObjects class of objects that will be in the list
	 * @param objectsInCollection number of objects that will be generated into the list
	 */
    public static <ItemT, CollT extends Collection<ItemT>> CollT collectionDefinition(Class<CollT> collectionType,
			Class<ItemT> clazzOfObjects, int objectsInCollection){
		Assert.isTrue(objectsInCollection >= 0, "objectsInCollection must be >= 0");
        PrototypeConfiguration
				.setPlannedRule(Rules.collectionDefinition(collectionType, clazzOfObjects, objectsInCollection));
        return null;
    }

	/**
	 * Rule that will create Set with one generated object of type {@code clazzOfObject}. New object will be generated
	 * on each rule invocation.
	 * @param clazzOfObject class of object that will be in the set
	 */
    public static <T> Set<T> setDefinition(Class<T> clazzOfObject) {
        return setDefinition(clazzOfObject, 1);
    }
	/**
	 * Rule that will create Set with {@code objectsInCollection} generated objects of type {@code clazzOfObjects}. Objects will
	 * be generated on each rule invocation.
	 * @param clazzOfObjects class of object that will be in the set
	 * @param objectsInCollection number of objects that will be generated into collection
	 */
    public static <T> Set<T> setDefinition(Class<T> clazzOfObjects, int objectsInCollection) {
        Assert.isTrue(objectsInCollection >= 0, "objectsInCollection must be >= 0");
        PrototypeConfiguration.setPlannedRule(Rules.setDefinition(clazzOfObjects, objectsInCollection));
        return null;
    }
	/**
	 * Rule that will create Set populated with {@code values}.
	 * @param values values that will be added to collection
	 */
    public static <T> Set<T> setDefinition(T... values) {
        HashSet setWithValues = new HashSet();
        for (T each: values){
            setWithValues.add(each);
        }
        PrototypeConfiguration.setPlannedRule(Rules.setDefinition(values));
        return null;
    }

	/**
	 * <p>
	 *     Rule specifying that generated object itself must be set on field (e.g. Person has Address, Address
	 *     referencing Employer (subtype of Person) and you want that Employer be generated object itself)
	 * </p>
	 * <p>
	 * You need to explicitly type this method call. E.g. Person.setChild(PrototypeRules.<Person>generatedObject()).
	 * This is due to the fact, that Java can't infer type without any object passing as arg, that can help in type
	 * determining. Must be typed by the expecting type of the setter, from which this OngoingRule was called
	 * </p>
	 */
	public static <T> T generatedObject() {
		PrototypeConfiguration.setPlannedRule(Rules.generatedObject());
		return null;
	}
	/**
	 * <p>
	 * Rule allowing to override generation configuration for field.
	 * </p>
	 * Generated of this field will use passed configuration merge with normal configuration.
	 * You can use this method to explicitly change how field will be generated - e.g. you can define another depth
	 * (see {@link ConfigurationBuilder#depth(int)})
	 * @param configurationBuilder not null configurationBuilder from which to take configuration
	 */
	public static <T> T overrideConfiguration(final ConfigurationBuilder configurationBuilder) {
		return (T)overrideConfiguration(configurationBuilder.done());
	}
	/**
	 * <p>
	 * Rule allowing to override generation configuration for field.
	 * </p>
	 * Generated of this field will use passed configuration merge with normal configuration.
	 * You can use this method to explicitly change how field will be generated - e.g. you can define another depth
	 * (see {@link ConfigurationBuilder#depth(int)})
	 * @param configuration not null configuration that will be used for generating this field
	 */
	public static <T> T overrideConfiguration(final GenerationConfiguration configuration){
		PrototypeConfiguration.setPlannedRule(Rules.configurationOverride(configuration));
		return null;
	}

	/**
	 * use passed range to generating values. See {@link Range} for more info.
	 * @param range not null
	 */
	public static <T> T range(Range<T> range) {
		PrototypeConfiguration.setPlannedRule(Rules.range(range));
		return null;
	}
	/**
	 * use passed range to generating values. See {@link Range} for more info.
	 * @param range not null
	 */
	public static double rangeDouble(Range<Double> range){
		PrototypeConfiguration.setPlannedRule(Rules.range(range));
		return 0.;
	}
	/**
	 * use passed range to generating values. See {@link Range} for more info.
	 * @param range not null
	 */
	public static int rangeInt(Range<Integer> range){
		PrototypeConfiguration.setPlannedRule(Rules.range(range));
		return 0;
	}
	/**
	 * use passed range to generating values. See {@link Range} for more info.
	 * @param range not null
	 */
	public static long rangeLong(Range<Long> range){
		PrototypeConfiguration.setPlannedRule(Rules.range(range));
		return 0;
	}
	/**
	 * use passed range to generating values. See {@link Range} for more info.
	 * @param range not null
	 */
	public static float rangeFloat(Range<Float> range){
		PrototypeConfiguration.setPlannedRule(Rules.range(range));
		return 0;
	}

	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static <T> T rule(GenerationRule<T> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return null;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static byte ruleByte(GenerationRule<Byte> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static boolean ruleBoolean(GenerationRule<Boolean> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return false;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static char ruleChar(GenerationRule<Character> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static short ruleShort(GenerationRule<Short> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static int ruleInt(GenerationRule<Integer> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static long ruleLong(GenerationRule<Long> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static float ruleFloat(GenerationRule<Float> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}
	/**
	 * Use passed rule for generating field's value. {@code rule} must return value of matching type.
	 * @param rule not null
	 */
	public static double ruleDouble(GenerationRule<Double> rule){
		Assert.notNull(rule, "rule must be not null");
		PrototypeConfiguration.setPlannedRule(rule);
		return 0;
	}

	/**
	 * skip generation of this attribute
	 */
	public static <T> T skip() {
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return null;
	}

	/**
	 * skip generation of this attribute
	 */
	public static boolean skipBoolean(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return false;
	}

	/**
	 * skip generation of this attribute
	 */
	public static byte skipByte(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}

	/**
	 * skip generation of this attribute
	 */
	public static char skipChar(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
	/**
	 * skip generation of this attribute
	 */
	public static short skipShort(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
	/**
	 * skip generation of this attribute
	 */
	public static int skipInt(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
	/**
	 * skip generation of this attribute
	 */
	public static long skipLong(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
	/**
	 * skip generation of this attribute
	 */
	public static float skipFloat(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
	/**
	 * skip generation of this attribute
	 */
	public static double skipDouble(){
		PrototypeConfiguration.setPlannedRule(Rules.skip());
		return 0;
	}
}
