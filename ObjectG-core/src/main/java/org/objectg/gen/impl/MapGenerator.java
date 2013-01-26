package org.objectg.gen.impl;

import java.util.HashMap;
import java.util.Map;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.session.GenerationSession;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class MapGenerator extends CollectionGenerator<Map>{

    @Override
    public boolean supportsType(Class type) {
        return Map.class.isAssignableFrom(type);
    }

    @Override
    protected void addNewObject(Map collection,
                                GenerationConfiguration configuration, GenerationContext contextOfCollection) {
        GenerationContext contextForGeneratingKey = getContextForGeneratingKey(contextOfCollection);
        if (!shouldAddObjectForContext(configuration, contextForGeneratingKey)) {
			//we are not generating any value, but have pushed the context, so need to pop manually
			contextForGeneratingKey.pop();
			return;
		}
        Object mapKey = GenerationSession.get()
                .generate(configuration, contextForGeneratingKey);
        Object mapValue = GenerationSession.get()
                .generate(configuration, getContextForGeneratingValue(contextOfCollection));
        collection.put(mapKey, mapValue);
    }

    private <T> GenerationContext getContextForGeneratingKey(GenerationContext<T> context) {
        return getContextForGenericType(context, 0);
    }

    private <T> GenerationContext getContextForGeneratingValue(GenerationContext<T> context) {
       return getContextForGenericType(context, 1);
    }

    @Override
    protected Map createInstanceForAbstractType() {
        return new HashMap();
    }
}
