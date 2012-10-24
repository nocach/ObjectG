package cz.nocach.masaryk.objectg.fixtures.hibernate;

import cz.nocach.masaryk.objectg.fixtures.domain.Person;
import cz.nocach.masaryk.objectg.fixtures.domain.Person2Address;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.*;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class HibernateMetadataTest{
    @Test
    public void canExtractMetaInfoFromClass(){
        Configuration partialConfiguration = new Configuration()
                .addAnnotatedClass(Person.class)
                .addAnnotatedClass(Person2Address.class);
        partialConfiguration.buildMappings();
        Iterator collectionMappingsIterator = partialConfiguration.getCollectionMappings();
        Collection collection1 = (Collection) collectionMappingsIterator.next();
        PersistentClass ownerClass = collection1.getOwner();
        String ownerClassFullFieldName = collection1.getRole();
        KeyValue keyValue = collection1.getKey();
        Iterator targetColumns = keyValue.getColumnIterator();
        Column targetColumn = (Column) targetColumns.next();
        Iterator<PersistentClass> persistentClassIterator = partialConfiguration.getClassMappings();
        while (persistentClassIterator.hasNext()){
            PersistentClass next = persistentClassIterator.next();
            if (next.getTable().equals(keyValue.getTable())){
                Iterator declaredPropertyIterator = next.getDeclaredPropertyIterator();
                while(declaredPropertyIterator.hasNext()){
                    Property nextProperty = (Property)declaredPropertyIterator.next();
                    Iterator propertyColumnIterator = nextProperty.getColumnIterator();
                    try{
                        Column foundColumn = (Column) propertyColumnIterator.next();
                        if (targetColumn.equals(foundColumn)){
                            String propertyToSetOnTarget = nextProperty.getName();
                        }
                        nextProperty.getColumnIterator();
                    }
                    catch (NoSuchElementException ignore){

                    }
                    catch (UnsupportedOperationException ignore){

                    }
                }
            }
        }
        PersistentClass persistentClass = partialConfiguration.getClassMappings().next();
        //persistentClass.declaredProperties['employee2Address'].value.column?
        persistentClass.getClass();
        Collection collection2 = (Collection) collectionMappingsIterator.next();
    }
}
