package org.objectg.gen.jpa;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.objectg.util.Generics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * <p>
 * Inspector for hibernate annotations (jpa as well) that will build list of {@link Relation} for the entity.
 * </p>
 * <p>
 * User: __nocach
 * Date: 13.10.12
 * </p>
 */
public class RelationInspector {
	private Logger logger = LoggerFactory.getLogger(RelationInspector.class);

	public boolean isEntity(Class clazz) {
		return isEntityAnnotationPresent(clazz);
	}

	public Map<Class, List<Relation>> fromEntity(Class clazz) {
		Assert.notNull(clazz);

		if (!isEntityAnnotationPresent(clazz)) {
			return emptyRelationMap(clazz);
		}

		Set<Class> relatedEntityClasses = collectRelatedEntityClasses(clazz);
		logRelatedClasses(clazz, relatedEntityClasses);
		Configuration configuration = buildMappingsForClasses(relatedEntityClasses);

		Iterator collectionMappings = configuration.getCollectionMappings();
		Map<Class, List<Relation>> result = new HashMap<Class, List<Relation>>();
		while (collectionMappings.hasNext()) {
			Relation relation = extractRelationFromCollection(configuration, collectionMappings);
			logCreatedRelation(relation);
			addRelationToResult(result, relation);
		}

		for (Class each : relatedEntityClasses) {
			if (!result.containsKey(each)) result.put(each, Collections.<Relation>emptyList());
		}

		return result;
	}

	private void logCreatedRelation(final Relation relation) {
		if (relation == null) return;
		logger.info("creating relation: owner=" + relation.getOwner().getName() + "." + relation.getOwnerProperty()
				+ ", target=" + relation.getTarget().getName() + "." + relation.getTargetProperty());
	}

	private void logRelatedClasses(final Class clazz, final Set<Class> relatedEntityClasses) {
		logger.debug(
				"found for " + clazz + " related entity classes: " + Arrays.toString(relatedEntityClasses.toArray()));
	}

	private void addRelationToResult(Map<Class, List<Relation>> result, Relation relation) {
		if (relation == null) return;
		if (result.get(relation.getOwner()) == null) {
			result.put(relation.getOwner(), new LinkedList<Relation>());
		}
		if (result.get(relation.getTarget()) == null) {
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
		Class owner = collection.getOwner().getMappedClass();
		String ownerPropertyName = collection.getNodeName();
		PersistentClass classForKey = classForKey(configuration, collection.getKey());
		if (classForKey == null) return null;
		Property propertyForColumn = propertyForColumn(classForKey, collection.getKey().getColumnIterator());
		if (propertyForColumn == null) return null;
		Class target = classForKey.getMappedClass();
		String targetPropertyName = propertyForColumn.getName();
		Relation relation = new Relation(owner, ownerPropertyName, target, targetPropertyName);

		return relation;
	}

	private Property propertyForColumn(PersistentClass classForKey, Iterator columnIterator) {
		Iterator declaredPropertyIterator = classForKey.getDeclaredPropertyIterator();
		List<Column> searchColumns = Lists.newArrayList(columnIterator);
		while (declaredPropertyIterator.hasNext()) {
			Property property = (Property) declaredPropertyIterator.next();
			if (iteratorsAreEqual(property.getColumnIterator(), searchColumns)) return property;
		}
		return null;
	}

	private boolean iteratorsAreEqual(Iterator leftIterator, List<Column> rightIterator) {
		for (Column each : rightIterator) {
			if (!leftIterator.hasNext()) return false;
			Column columnFromIterator = (Column) leftIterator.next();
			if (!each.equals(columnFromIterator)) return false;
		}
		return true;
	}

	private PersistentClass classForKey(Configuration configuration, KeyValue key) {
		Iterator<PersistentClass> classMappings = configuration.getClassMappings();
		while (classMappings.hasNext()) {
			PersistentClass next = classMappings.next();
			if (key.getTable().equals(next.getTable())) return next;
		}
		return null;
	}

	private Configuration buildMappingsForClasses(Set<Class> entityClasses) {
		Configuration configuration = new Configuration();
		for (Class each : entityClasses) {
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
		addFromHierarchyIfNotPresent(clazz, relatedEntityClasses);
		for (Field each : clazz.getDeclaredFields()) {
			final Class<?> fieldType = each.getType();
			if (isEntityAnnotationPresent(fieldType)) {
				addFromHierarchyIfNotPresent(fieldType, relatedEntityClasses);
			} else if (isFieldGenericCollection(each)) {
				//TODO: currenty will fall if it will be generic collection without TypeVars defined
				Class typeParam = Generics.extractTypeFromField(each, 0);
				if (isEntityAnnotationPresent(typeParam)) {
					addFromHierarchyIfNotPresent(typeParam, relatedEntityClasses);
				}
			}
		}
	}

	private void addFromHierarchyIfNotPresent(final Class<?> entityClass, final Set<Class> relatedEntityClasses) {
		Class currentClass = entityClass;
		while (!currentClass.equals(Object.class)) {
			if (!relatedEntityClasses.contains(currentClass)) {
				relatedEntityClasses.add(currentClass);
				collectRelatedEntityClassesNested(currentClass, relatedEntityClasses);
			}
			currentClass = currentClass.getSuperclass();
		}
	}

	private boolean isFieldGenericCollection(Field each) {
		return java.util.Collection.class.isAssignableFrom(each.getType());
	}

	private boolean isEntityAnnotationPresent(Class clazz) {
		Class current = clazz;
		while (current != Object.class && current != null) {
			if (AnnotationUtils.isAnnotationDeclaredLocally(Entity.class, current)
					|| AnnotationUtils.isAnnotationDeclaredLocally(org.hibernate.annotations.Entity.class, current)) {
				return true;
			}
			current = current.getSuperclass();
		}
		return false;
	}
}
