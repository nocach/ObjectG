package org.objectg.gen.impl;

import java.util.ArrayList;
import java.util.List;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.session.GenerationSession;

/**
 * User: __nocach
 * Date: 22.9.12
 */
class ListGenerator extends CollectionGenerator<List>{


    @Override
    public boolean supportsType(Class type) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    protected void addNewObject(List collection,
                                GenerationConfiguration configuration, GenerationContext contextOfCollection) {
        GenerationContext contextForCollectionsObject = getContextForGeneratingObjectsOfCollection(contextOfCollection);
        if (!shouldAddObjectForContext(configuration, contextForCollectionsObject)) {
			//we are not generating any value, but have pushed the context, so need to pop manually
			contextForCollectionsObject.pop();
			return;
		}
        Object generatedValue = GenerationSession.get().generate(configuration, contextForCollectionsObject);
        collection.add(generatedValue);
    }

    private <T> GenerationContext getContextForGeneratingObjectsOfCollection(GenerationContext<T> context) {
        return getContextForGenericType(context, 0);
    }

    @Override
    protected List createInstanceForAbstractType() {
        return new ArrayList();
    }
}
