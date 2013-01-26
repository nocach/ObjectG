package org.objectg.gen.rule;

import java.util.Collection;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class CollectionGenerationRule<T> extends GenerationRule<T>{
    private final Class<? extends Collection> collectionClass;
	private Object[] values;
	private Class classOfObjects;
	private int objectsInCollection;

    public CollectionGenerationRule(Class<? extends Collection> collectionClass, Class classOfObjects,
			final int objectsInCollection){
        this.collectionClass = collectionClass;
        this.classOfObjects = classOfObjects;
		this.objectsInCollection = objectsInCollection;
	}

    public CollectionGenerationRule(Class<? extends Collection> collectionClass, Class classOfObjects){
		this(collectionClass, classOfObjects, 1);
	}

	public CollectionGenerationRule(final Class<? extends Collection> collectionClass, final Object[] values) {
		this.collectionClass = collectionClass;
		this.values = values;
		this.objectsInCollection = 0;
	}

	@Override
    public T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(objectsInCollection);
		configurationOfCollection.removeRule(this);
        Collection collection = GenerationSession.get().generate(configurationOfCollection, context);
		if (values != null){
			for (Object each : values) collection.add(each);
		}
        return (T)collection;
    }
}
