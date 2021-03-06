package org.objectg.gen.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.mapping.PersistentClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.GoodToHave;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class JpaRelationTest extends BaseObjectGTest {

	private RelationInspector relationInspector;

	@Before
	public void setup() {
		relationInspector = new RelationInspector();
	}

	@Test
	public void solderMappingIsValid() {
		assertClassMappignsAreValid(Solder.class, Troop.class);
	}

	@Test
	public void organizationUnitMappingIsValid() {
		assertClassMappignsAreValid(OrganizationUnit.class, Organization.class);
	}

	@Test
	public void organizationUnitBaseEntityMappingIsValid() {
		assertClassMappignsAreValid(OrganizationUnitBaseEntity.class, OrganizationBaseEntity.class);
	}

	@Test
	public void organizationUnitWithoutCollectionTypeMappingIsValid() {
		assertClassMappignsAreValid(OrganizationWithoutCollectionType.class,
				OrganizationUnitWithoutCollectionType.class);
	}

	@Test
	public void projectEntitiesMappingIsValid() {
		assertClassMappignsAreValid(Project.class, SubProject.class, Team.class);
	}

	private void assertClassMappignsAreValid(Class... classes) {
		Configuration configuration = new Configuration();
		for (Class each : classes) {
			configuration.addAnnotatedClass(each);
		}
		configuration.buildMappings();
		Mapping mapping = configuration.buildMapping();
		for (PersistentClass each : Lists.newArrayList(configuration.getClassMappings())) {
			each.validate(mapping);
		}
	}

	@Test
	public void relationsForNotEntity() {
		Map<Class, List<Relation>> relations = relationInspector.fromEntity(NotEntity.class);
		assertTrue(relations.get(NotEntity.class).isEmpty());
	}

	@Test
	public void oneToManyWithoutMappedBy() {
		Map<Class, List<Relation>> relationsFromSolder = relationInspector.fromEntity(Solder.class);
		assertTrue(relationsFromSolder.get(Solder.class).isEmpty());

		Map<Class, List<Relation>> relationsFromTroop = relationInspector.fromEntity(Troop.class);
		assertTrue(relationsFromTroop.get(Troop.class).isEmpty());
		assertTrue(relationsFromTroop.get(Solder.class).isEmpty());
	}

	@Test
	public void oneToManyWithMappedByFromOrganization() {
		Map<Class, List<Relation>> relationsFromOrganization = relationInspector.fromEntity(Organization.class);

		assertEquals(2, relationsFromOrganization.size());
		Relation firstRelation = relationsFromOrganization.get(Organization.class).get(0);
		assertEquals("relation.owner", Organization.class, firstRelation.getOwner());
		assertEquals("relation.ownerProperty", "units", firstRelation.getOwnerProperty());
		assertEquals("relation.target", OrganizationUnit.class, firstRelation.getTarget());
		assertEquals("relation.targetProperty", "organization", firstRelation.getTargetProperty());
	}

	@Test
	public void oneToManyWithMappedByFromOrganizationUnit() {
		Map<Class, List<Relation>> relationsFromOrganization = relationInspector.fromEntity(OrganizationUnit.class);

		assertEquals(2, relationsFromOrganization.size());
		Relation firstRelation = relationsFromOrganization.get(OrganizationUnit.class).get(0);
		assertEquals("relation.owner", Organization.class, firstRelation.getOwner());
		assertEquals("relation.ownerProperty", "units", firstRelation.getOwnerProperty());
		assertEquals("relation.target", OrganizationUnit.class, firstRelation.getTarget());
		assertEquals("relation.targetProperty", "organization", firstRelation.getTargetProperty());
	}

	@Test
	public void shouldWorkWithInheritedEntityAnnotation() {
		Map<Class, List<Relation>> relationsFromOrganization = relationInspector
				.fromEntity(OrganizationUnitBaseEntity.class);

		assertEquals("BaseEntity, OrganizationBaseEntity, OrganizationUnitBaseEntity", 3,
				relationsFromOrganization.size());
	}

	@Test
	public void shouldDiscoverRelationsReferencedFromCollection() {
		Map<Class, List<Relation>> relations = relationInspector.fromEntity(Project.class);
		assertEquals(3, relations.size());
	}

	@Test
	@Ignore
	@GoodToHave
	public void shouldWorkWithGenericCollectionUsingTargetClassValue() {
		Map<Class, List<Relation>> relationsFromOrganization = relationInspector
				.fromEntity(OrganizationUnitWithoutCollectionType.class);

		Relation firstRelation = relationsFromOrganization.get(OrganizationUnitWithoutCollectionType.class).get(0);
		assertEquals("relation.owner", OrganizationWithoutCollectionType.class, firstRelation.getOwner());
		assertEquals("relation.ownerProperty", "units", firstRelation.getOwnerProperty());
		assertEquals("relation.target", OrganizationUnitWithoutCollectionType.class, firstRelation.getTarget());
		assertEquals("relation.targetProperty", "organization", firstRelation.getTargetProperty());
	}

	@Test
	public void testNoColumnInChildResultsInNoRelation() {
		final Map<Class, List<Relation>> relations = relationInspector.fromEntity(OwnerWithoutBackReference.class);

		assertEquals("when no back reference relation is not occurred - OwnerWithoutBackReference", 0,
				relations.get(OwnerWithoutBackReference.class).size());
		assertEquals("when no back reference relation is not occurred - SimpleEntity", 0,
				relations.get(SimpleEntity.class).size());
	}

	@Test
	public void testWillWorkIfParentEntityHasOneToMany() {
		relationInspector.fromEntity(ExtendingBaseReferringOther.class);
	}

	public static class NotEntity {

	}

	@Entity
	public static class Solder {
		@Id
		@GeneratedValue
		private Long id;
	}

	@Entity
	public static class Troop {
		@Id
		@GeneratedValue
		private Long id;
		@OneToMany
		private List<Solder> solders;

	}

	@Entity
	public static class OrganizationUnit {
		@Id
		@GeneratedValue
		private Long id;
		@ManyToOne
		private Organization organization;
	}

	@Entity
	public static class Organization {
		@Id
		@GeneratedValue
		private Long id;
		@OneToMany(mappedBy = "organization")
		private List<OrganizationUnit> units;
	}

	@Entity
	public static class BaseEntity {
		@Id
		@GeneratedValue
		private Long id;

	}

	public static class OrganizationBaseEntity extends BaseEntity {
		@OneToMany
		private Set<OrganizationUnitBaseEntity> units;
	}

	public static class OrganizationUnitBaseEntity extends BaseEntity {
		@ManyToOne
		OrganizationBaseEntity organization;
	}

	@Entity
	public static class OrganizationWithoutCollectionType {
		@Id
		@GeneratedValue
		private Long id;
		@OneToMany(targetEntity = OrganizationUnitWithoutCollectionType.class)
		private Collection units;
	}

	@Entity
	public static class OrganizationUnitWithoutCollectionType {
		@Id
		@GeneratedValue
		private Long id;
		@ManyToOne
		private OrganizationWithoutCollectionType organization;
	}

	@Entity
	public static class Project {
		@Id
		private Long id;
		@OneToMany
		private List<SubProject> subProjects;
	}

	@Entity
	public static class SubProject {
		@Id
		private Long id;
		@OneToMany(mappedBy = "subProject")
		private List<Team> teams;
	}

	@Entity
	public static class Team {
		@Id
		private Long id;
		@ManyToOne
		private SubProject subProject;
	}

	@Entity
	public static class OwnerWithoutBackReference {
		@Id
		private Long id;
		@OneToMany
		@JoinColumn(name = "COLUMN_NOT_IN_CHILD_ENTITY")
		private List<SimpleEntity> entities;
	}

	@Entity
	public static class SimpleEntity {
		@Id
		private Long id;
	}


	@Entity
	@Inheritance(strategy = InheritanceType.JOINED)
	public abstract static class BaseReferringOther {
		@Id
		private Long id;
		@OneToMany
		private List<SimpleEntity> otherEntity;
	}

	@Entity
	public static class ExtendingBaseReferringOther extends BaseReferringOther {
	}

}
