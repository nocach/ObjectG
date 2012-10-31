package org.objectg.gen.jpa;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.*;
import org.objectg.util.Generics;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class RelationInspector {

    public boolean isEntity(Class clazz){
        return isEntityAnnotationPresent(clazz);
    }

    public Map<Class, List<Relation>> fromEntity(Class clazz){
        Assert.notNull(clazz);

        if (!isEntityAnnotationPresent(clazz)){
            return emptyRelationMap(clazz);
        }

        Set<Class> relatedEntityClasses = collectRelatedEntityClasses(clazz);
        Configuration configuration = buildMappingsForClasses(relatedEntityClasses);

        Iterator collectionMappings = configuration.getCollectionMappings();
        Map<Class, List<Relation>> result = new HashMap<Class, List<Relation>>();
        while (collectionMappings.hasNext()){
            Relation relation = extractRelationFromCollection(configuration, collectionMappings);
            addRelationToResult(result, relation);
        }

        for (Class each : relatedEntityClasses){
            if (!result.containsKey(each)) result.put(each, Collections.<Relation>emptyList());
        }

        return result;
    }

    private void addRelationToResult(Map<Class, List<Relation>> result, Relation relation) {
        if (relation == null) return;
        if (result.get(relation.getOwner()) == null){
            result.put(relation.getOwner(), new LinkedList<Relation>());
        }
        if (result.get(relation.getTarget()) == null){
            result.put(relation.getTarget(), new LinkedList<Relation>());
    }
        result.get(relation.getOwner()).add(relation);
        result.get(relation.getTarget()).add(relation);
    }

    private HashMap<Class, List<Relation>> emptyRelationMap(Class clazz) {
        HashMap<Class, List<Relation>> result = new HashMap<Class, List<Relation>>();
        result.put(clazz, Collections.<Relation>emptyList());
        return result;
    }

    private Relation extractRelationFromCollection(Configuration configuration, Iterator collectionMappings) {
        Collection collection = (Collection) collectionMappings.next();
        collection.getOwner().getMappedClass();
        Class owner = collection.getOwner().getMappedClass();
        String ownerPropertyName = collection.getNodeName();
        PersistentClass classForKey = classForKey(configuration, collection.getKey());
        if (classForKey == null) return null;
        Property propertyForColumn = propertyForColumn(classForKey, collection.getKey().getColumnIterator());
        Class target = classForKey.getMappedClass();
        String targetPropertyName = propertyForColumn.getName();
        Relation relation = new Relation(owner, ownerPropertyName, target, targetPropertyName);
        return relation;
    }

    private Property propertyForColumn(PersistentClass classForKey, Iterator columnIterator) {
        Iterator declaredPropertyIterator = classForKey.getDeclaredPropertyIterator();
        List<Column> searchColumns = Lists.newArrayList(columnIterator);
        while (declaredPropertyIterator.hasNext()){
            Property property = (Property) declaredPropertyIterator.next();
            if (iteratorsAreEqual(property.getColumnIterator(), searchColumns)) return property;
        }
        return null;
    }

    private boolean iteratorsAreEqual(Iterator leftIterator, List<Column> rightIterator) {
        for(Column each : rightIterator){
            if (!leftIterator.hasNext()) return false;
            Column columnFromIterator = (Column) leftIterator.next();
            if (!each.equals(columnFromIterator)) return false;
        }
        return true;
    }

    private PersistentClass classForKey(Configuration configuration, KeyValue key) {
        Iterator<PersistentClass> classMappings = configuration.getClassMappings();
        while (classMappings.hasNext()){
            PersistentClass next = classMappings.next();
            if (key.getTable().equals(next.getTable())) return next;
        }
        return null;
    }

    private Configuration buildMappingsForClasses(Set<Class> entityClasses) {
        Configuration configuration = new Configuration();
        for (Class each : entityClasses){
            configuration.addAnnotatedClass(each);
        }
        configuration.buildMappings();
        return configuration;
    }

    private Set<Class> collectRelatedEntityClasses(Class clazz) {
        Set<Class> relatedEntityClasses = Sets.newHashSet(clazz);
        collectRelatedEntityClassesNested(clazz, relatedEntityClasses);
        return relatedEntityClasses;
    }

    private void collectRelatedEntityClassesNested(Class clazz, Set<Class> relatedEntityClasses) {
        for (Field each : clazz.getDeclaredFields()){
            if (isEntityAnnotationPresent(each.getType()) && !relatedEntityClasses.contains(each.getType())){
                relatedEntityClasses.add(each.getType());
                collectRelatedEntityClassesNested(each.getType(), relatedEntityClasses);
            } else if(isFieldGenericCollection(each)){
                //TODO: currenty will fall if it will be generic collection without TypeVars defined
                Class typeParam = (Class) Generics.extractTypeFromGenerics(each, 0);
                if (isEntityAnnotationPresent(typeParam) && !relatedEntityClasses.contains(typeParam)){
                    relatedEntityClasses.add(typeParam);
                    collectRelatedEntityClassesNested(typeParam, relatedEntityClasses);
                }
            }
        }
    }

    private boolean isFieldGenericCollection(Field each) {
        return java.util.Collection.class.isAssignableFrom(each.getType());
    }

    private boolean isEntityAnnotationPresent(Class clazz) {
        Class current = clazz;
        while (current != Object.class && current != null){
            if (AnnotationUtils.isAnnotationDeclaredLocally(Entity.class, current)
                    || AnnotationUtils.isAnnotationDeclaredLocally(org.hibernate.annotations.Entity.class, current)){
                return true;
            }
            current = current.getSuperclass();
        }
        return false;
    }
}
