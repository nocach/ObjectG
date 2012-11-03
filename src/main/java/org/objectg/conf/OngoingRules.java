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

    public static <T> List<T> listDefinition(Class clazzOfObjects, Object... values) {
        OngoingConfiguration.plannedRule = Rules.listDefinition(clazzOfObjects, values);
        return null;
    }

    public static <ListT extends Collection<?>> ListT collectionDefinition(Class<ListT> collectionType, Class clazzOfObjects){
        OngoingConfiguration.plannedRule = Rules.collectionDefinition(collectionType, clazzOfObjects);
        return null;
    }

    public static Set setDefinition(Class clazzOfObjects) {
        return setDefinition(clazzOfObjects, 1);
    }

    public static Set setDefinition(Class<String> clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        OngoingConfiguration.plannedRule = Rules.setDefinition(clazzOfObjects, size);
        return null;
    }

    public static Set setDefinition(Class<String> clazzOfObjects, Object... values) {
        HashSet setWithValues = new HashSet();
        for (Object each: values){
            setWithValues.add(each);
        }
        OngoingConfiguration.plannedRule = Rules.setDefinition(clazzOfObjects, values);
        return null;
    }
}
