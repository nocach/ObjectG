package org.objectg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import org.objectg.conf.ConfigurationBuilder;
import org.objectg.conf.ConfigurationManager;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.OngoingConfiguration;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.conf.prototype.InterceptedByPrototypeCreator;
import org.objectg.conf.prototype.PrototypeCreator;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.RuleScope;
import org.objectg.gen.rule.Rules;
import org.objectg.gen.session.GenerationSession;
import org.springframework.util.Assert;

/**
 * <p>
 *     User-facing api of the framework.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
public class ObjectG {
    private static final PrototypeCreator PROTOTYPE_CREATOR = new PrototypeCreator();
	private static final ConfigurationManager CONFIGURATION_MANAGER = new ConfigurationManager(ObjectG.class);

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param prototype prototype created using {@link #prototype(Class)}
	 * @param <T> type of the generated object
	 * @return object generated using passed prototype
	 */
    public static <T> T unique(T prototype){
        return unique(PROTOTYPE_CREATOR.getRealObjectClass(prototype), new GenerationConfiguration(), prototype);
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(Class<T> clazz){
        return unique(clazz, new GenerationConfiguration());
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param configuration not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(Class<T> clazz, GenerationConfiguration configuration){
        configuration.setUnique(true);
        return generate(clazz, configuration);
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param prototype prototype created using {@link #prototype(Class)}
	 * @param configuration not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(T prototype, GenerationConfiguration configuration){
		return unique(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configuration, prototype);
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param configurationBuilder not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(Class<T> clazz, ConfigurationBuilder configurationBuilder){
        return unique(clazz, configurationBuilder.done());
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param prototype prototype created using {@link #prototype(Class)}
	 * @param configurationBuilder not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(T prototype, ConfigurationBuilder configurationBuilder){
        return unique(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configurationBuilder.done(), prototype);
    }

	/**
	 *generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param configurationBuilder not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(Class<T> clazz, ConfigurationBuilder configurationBuilder, Object... prototypes){
        return unique(clazz, configurationBuilder.done(), prototypes);
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param prototype prototype created using {@link #prototype(Class)}
	 * @param configurationBuilder not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(T prototype, ConfigurationBuilder configurationBuilder, Object... prototypes){
		final Object[] allPrototypesArray = merge(prototype, prototypes);
		return unique(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configurationBuilder.done(),
				allPrototypesArray);
    }

	private static <T> Object[] merge(final T prototype, final Object[] prototypes) {
		final ArrayList<Object> allPrototypes = Lists.newArrayList(prototypes);
		allPrototypes.add(prototype);
		return allPrototypes.toArray();
	}

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return generated object
	 */
	public static <T> T unique(Class<T> clazz, Object... prototypes){
        assertPrototypesAreValid(prototypes);
        return unique(clazz, new GenerationConfiguration(), prototypes);
    }

    private static void assertPrototypesAreValid(Object[] prototypes) {
        for (Object each : prototypes){
            if (each instanceof ConfigurationBuilder){
                throw new ConfigurationException("tried to use ConfigurationBuilder as prototype object. " +
                        "Did you forget to call .done()? Check ObjectG.config() for more info");
            }
            if ( !(each instanceof InterceptedByPrototypeCreator) ){
                throw new ConfigurationException("tried to pass not prototype instance. " +
                        "Make sure you passed instances return by ObjectG.prototype()");
            }
        }
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param clazz class of the generating object
	 * @param configuration not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(Class<T> clazz, GenerationConfiguration configuration, Object... prototypes){
        configuration.setUnique(true);
        configuration.addAllRules(localRulesFromPrototypes(prototypes));
        return generate(clazz, configuration);
    }

	/**
	 * generate object that will have unique attributes comparing to any other object created
	 * with unique() methods
	 *
	 * @param prototype prototype created using {@link #prototype(Class)}
	 * @param configuration not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T unique(T prototype, GenerationConfiguration configuration, Object... prototypes){
		return unique(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configuration, merge(prototype, prototypes));
    }

	/**
	 * generate object of the passed class
	 *
	 * @param clazz class of the generating object
	 * @param configuration not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return generated object
	 */
    public static <T> T generate(Class<T> clazz, GenerationConfiguration configuration) {
		GenerationSession.get().assertReady();
        addDefaultRules(configuration);
		GenerationConfiguration finalConfiguration = CONFIGURATION_MANAGER
				.getFinalConfiguration(PROTOTYPE_CREATOR, configuration);
        T result = (T) GenerationSession.get().generate(finalConfiguration, GenerationContext.createRoot(clazz));
        configuration.postProcess(result);
        return result;
    }

	private static void addDefaultRules(GenerationConfiguration configuration) {
        configuration.addAllRules(Rules.defaultRules());
    }

	/**
	 * <p>
	 * 	Create prototype for the passed clazz
	 * </p>
	 * <p>
	 *     Prototype is the main mechanism on how to configure generation of objects of some type. To configure
	 *     specific rule for generation of attribute call setter and pass as argument special value
	 *     returned from {@link org.objectg.conf.OngoingRules}.
	 * </p>
	 * <p>
	 *     Calling setter with some value is the same as calling {@link org.objectg.conf.OngoingRules#value(Object)}.
	 *     So calling personPrototype.setName("someName") and generating instance of Person using that prototype
	 *     will result in object, that will have name set to "someName".
	 * </p>
	 * <p>
	 *     You can call setter with another prototype and attribute for that setter will be generated using passed
	 *     prototype. E.g. you could call {@code personPrototype.setAddress(addressPrototype)}and generating field address
	 *     using personPrototype will be done by using addressPrototype
	 * </p>
	 * <p>
	 *     Calling getters on prototype will automatically return prototype for the return type. So
	 *     calling personPrototype.getAddress() will return prototype for type Address.class. This means that
	 *     you don't need to manually set prototypes if you want use them only to configure specific field.
	 * </p>
	 * <p>
	 *     For methods of {@link org.objectg.conf.OngoingRules} that can't derive return type
	 *     (like {@link org.objectg.conf.OngoingRules#skip()}) you should add type parameter to the call, e.g.
	 *     {@code personPrototype.setAddress(OngoingRules.<Address>skip())}
	 * </p>
	 *
	 * @param clazz not null class for which to create prototype. Must be not final and not abstract class.
	 *                 Can be interface.
	 * @param <T> type of the prototype to create
	 * @return created prototype
	 */
    public static <T> T prototype(Class<T> clazz){
        T result = PROTOTYPE_CREATOR.newPrototype(clazz);
        OngoingConfiguration.setPlannedPrototype(result);
        return result;
    }

    private static List<GenerationRule> localRulesFromPrototypes(Object... prototypes){
		final List<GenerationRule> result = PROTOTYPE_CREATOR.getRulesFromPrototypes(prototypes);
		for (GenerationRule each : result) {
			each.setScope(RuleScope.LOCAL);
		}
		return result;
    }

    /**
	 * create list of unique objects. List will contain 1 unique object.
     *
     * @param clazz type of the objects for list
     * @param <T> type of the generating object
     * @return not null generated list
     */
    public static <T> List<T> uniqueList(Class<T> clazz) {
        return uniqueList(clazz, 1);
    }
    /**
     * create list of unique objects. List will contain 1 unique object.
	 *
     * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
     * @param <T> type of the generating object
     * @return not null generated list
     */
    public static <T> List<T> uniqueList(T prototype) {
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), 1, prototype);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param clazz type of the objects for list
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, int size, Object... prototypes){
        return uniqueList(clazz, new GenerationConfiguration(), size, prototypes);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, int size, Object... prototypes){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), new GenerationConfiguration(), size, merge(prototype, prototypes));
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param clazz type of the objects for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, Object... prototypes){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueList(clazz, generationConfiguration, 1, prototypes);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param clazz type of the objects for list
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, GenerationConfiguration generationConfiguration){
        return uniqueList(clazz, generationConfiguration, 1);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, GenerationConfiguration generationConfiguration){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, 1, prototype);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param clazz type of the objects for list
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, GenerationConfiguration generationConfiguration, int size){
        return uniqueList(clazz, generationConfiguration, size, new Object[]{});
    }

	/**
	 * create list of unique objects.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, GenerationConfiguration generationConfiguration, int size){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, size, prototype);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param clazz type of the objects for list
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, ConfigurationBuilder generationBuilder){
        return uniqueList(clazz, generationBuilder.done(), 1);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, ConfigurationBuilder generationBuilder){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), 1, prototype);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param clazz type of the objects for list
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, ConfigurationBuilder generationBuilder, int size){
        return uniqueList(clazz, generationBuilder.done(), size);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, ConfigurationBuilder generationBuilder, int size){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), size, prototype);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param clazz type of the objects for list
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, GenerationConfiguration generationConfiguration, Object... prototypes){
        return uniqueList(clazz, generationConfiguration, 1, prototypes);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationConfiguration not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, GenerationConfiguration generationConfiguration, Object... prototypes){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, 1, merge(prototype, prototypes));
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param clazz type of the objects for list
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, ConfigurationBuilder generationBuilder, Object... prototypes){
        return uniqueList(clazz, generationBuilder.done(), 1, prototypes);
    }

	/**
	 * create list of unique objects.  List will contain 1 unique object.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, ConfigurationBuilder generationBuilder, Object... prototypes){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), 1, merge(prototype, prototypes));
    }

	/**
	 * create list of unique objects.
	 *
	 * @param clazz  type of the objects for list
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, ConfigurationBuilder generationBuilder, int size, Object... prototypes){
        return uniqueList(clazz, generationBuilder.done(), size, prototypes);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param generationBuilder not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, ConfigurationBuilder generationBuilder, int size, Object... prototypes){
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), size, merge(prototype, prototypes));
    }

    /**
     * create list of unique objects.
	 *
     * @param clazz  type of the objects for list
     * @param size  how much unique objects to generate for list
     * @param <T> type of the generating object
	 * @return not null generated list
     */
    public static <T> List<T> uniqueList(Class<T> clazz, int size) {
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueList(clazz, generationConfiguration, size);
    }
    /**
	 * create list of unique objects.
     *
     * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
     * @param size  how much unique objects to generate for list
	 * @param <T> type of the generating object
	 * @return not null generated list
     */
    public static <T> List<T> uniqueList(T prototype, int size) {
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, size, prototype);
    }

	/**
	 * create list of unique objects.
	 *
	 * @param clazz type of the objects for list
	 * @param configuration not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(Class<T> clazz, GenerationConfiguration configuration, int size, Object... prototypes){
        Assert.isTrue(size >= 0, "size must be >= 0");
        List<T> result = new ArrayList<T>(size);
        configuration.setUnique(true);
        configuration.addAllRules(localRulesFromPrototypes(prototypes));
        for (int i = 0; i < size; i++){
            result.add(i, generate(clazz, configuration));
        }
        return result;
    }

	/**
	 * create list of unique objects.
	 *
	 * @param prototype not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 * @param configuration not null configuration that will be used during generation
	 * @param size how much unique objects to generate for list
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated list
	 */
    public static <T> List<T> uniqueList(T prototype, GenerationConfiguration configuration, int size, Object... prototypes){
		return uniqueList(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configuration, size, merge(prototype, prototypes));
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz) {
        return uniqueSet(clazz, 1);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype) {
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), 1, prototype);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, Object... prototypes){
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueSet(clazz, generationConfiguration, 1, prototypes);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, GenerationConfiguration generationConfiguration){
        return uniqueSet(clazz, generationConfiguration, 1);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, GenerationConfiguration generationConfiguration){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, 1, prototype);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, GenerationConfiguration generationConfiguration, int size){
        return uniqueSet(clazz, generationConfiguration, size, new Object[]{});
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, GenerationConfiguration generationConfiguration, int size){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, size, prototype);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, ConfigurationBuilder generationBuilder){
        return uniqueSet(clazz, generationBuilder.done(), 1);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, ConfigurationBuilder generationBuilder){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), 1, prototype);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, ConfigurationBuilder generationBuilder, int size){
        return uniqueSet(clazz, generationBuilder.done(), size);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, ConfigurationBuilder generationBuilder, int size){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), size, prototype);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, GenerationConfiguration generationConfiguration, Object... prototypes){
        return uniqueSet(clazz, generationConfiguration, 1, prototypes);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, GenerationConfiguration generationConfiguration, Object... prototypes){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, 1, merge(prototype, prototypes));
    }

	/**
	 *
	 *  see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, ConfigurationBuilder generationBuilder, Object... prototypes){
        return uniqueSet(clazz, generationBuilder.done(), 1, prototypes);
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * create set with single object.
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, ConfigurationBuilder generationBuilder, Object... prototypes){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), 1, merge(prototype, prototypes));
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, ConfigurationBuilder generationBuilder, int size, Object... prototypes){
        return uniqueSet(clazz, generationBuilder.done(), size, prototypes);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, ConfigurationBuilder generationBuilder, int size, Object... prototypes){
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationBuilder.done(), size, merge(prototype, prototypes));
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, int size) {
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueSet(clazz, generationConfiguration, size);
    }
	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
    public static <T> Set<T> uniqueSet(T prototype, int size) {
        GenerationConfiguration generationConfiguration = new GenerationConfiguration();
        return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), generationConfiguration, size, prototype);
    }

	/**
	 * create set of unique objects.
	 *
	 * @param clazz type of the objects for set
	 * @param configuration not null configuration that will be used during generation
	 * @param size how much unique objects to generate for set
	 * @param prototypes prototypes created using {@link #prototype(Class)}. These prototypes will be used as a default
	 *                   way how to generate classes of the types. E.g. if you are generating Person.class
	 *                   you can pass addressPrototype and any object of type Address.class will be generated
	 *                   using addressPrototype.
	 * @param <T> type of the generating object
	 * @return not null generated set
	 */
    public static <T> Set<T> uniqueSet(Class<T> clazz, GenerationConfiguration configuration, int size, Object... prototypes){
        Assert.isTrue(size >= 0, "size must be >= 0");
        Set<T> result = new HashSet<T>();
        configuration.setUnique(true);
        configuration.addAllRules(localRulesFromPrototypes(prototypes));
        for (int i = 0; i < size; i++){
            result.add(generate(clazz, configuration));
        }
        return result;
    }

	/**
	 * see {@link #uniqueSet(Class, org.objectg.conf.GenerationConfiguration, int, Object...)}
	 * @param prototype  not null prototype, see {@link #prototype(Class)}. Will be used to create object for list.
	 */
	public static <T> Set<T> uniqueSet(T prototype, GenerationConfiguration configuration, int size, Object... prototypes){
		return uniqueSet(PROTOTYPE_CREATOR.getRealObjectClass(prototype), configuration, size, merge(prototype, prototypes));
	}

	/**
	 * Start setting configuration for generation. See {@link ConfigurationBuilder} for more info
	 *
	 * @return {@link ConfigurationBuilder}
	 */
    public static ConfigurationBuilder config() {
        return new ConfigurationBuilder(PROTOTYPE_CREATOR);
    }

	/**
	 * Set configuration to be used as a default one in any generation that will be executed in the calling class.
	 *
	 * @param configuration not null configuration that will be used during all generations that will be called
	 *                      from the class which invoked this method. E.g. if TestCaseA invoked in setup phase
	 *                      <pre>
	 *                      public void setup(){
	 *                      	ObjectG.configLocal(configuration);
	 *                      }
	 *                      </pre>
	 *                      then all generations in this class will have their local configuration taken
	 *                      from {@code configuration} object
	 *                      so e.g.
	 *                      <pre>
	 *                      public void someTest(){
	 *                      	...
	 *                      	ObjectG.unique(Person.class);
	 *                      }
	 *                      </pre>
	 *                      will use provided local configuration.
	 *
	 *                      Local configuration is preserved during deriving classes, so if TestCaseB extends TestCaseA,
	 *                      then generating objects in TestCaseB will use local configuration of TestCaseA. If another
	 *                      local configuration is defined in TestCaseB, then those changes will be merged in
	 *                      local configuration of TestCaseA. Merging of configuration is done in the order of
	 *                      test case class hierarchy. This means that more specific class local configuration is more
	 *                      respected (in our case local configuration of TestCaseB is preffered over local configuration
	 *                      of TestCaseA).
	 */
	public static void configLocal(final GenerationConfiguration configuration) {
		CONFIGURATION_MANAGER.register(configuration);
	}

	/**
	 * see {@link #configLocal(org.objectg.conf.GenerationConfiguration)}
	 */
	public static void configLocal(final ConfigurationBuilder configurationBuilder){
		configLocal(configurationBuilder.done());
	}

	/**
	 * must be called before any new generation is executed. Calling this method means starting of
	 * {@link GenerationSession}. Normally you need to call this method before each test method and their setup phase.
	 * See {@link org.objectg.integration.ObjectGTestRule} for simple integration into your tests.
	 */
	public static void setup() {
		GenerationSession.get().begin();
	}

	/**
	 * must be called when all generations have been executed. Calling this methods means stopping
	 * {@link GenerationSession}. Normally you need to call this method after each test method and their
	 * teardown phase.
	 * * See {@link org.objectg.integration.ObjectGTestRule} for simple integration into your tests.
	 */
	public static void teardown() {
		PROTOTYPE_CREATOR.clear();
		GenerationSession.get().end();
	}
}
