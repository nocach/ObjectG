package org.objectg.gen.impl;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.Generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: __nocach
 * Date: 9.10.12
 */
class EnumerationGenerator extends Generator {
    private final Map<Class, Integer> enumConstantIndexes = new ConcurrentHashMap<Class, Integer>();
    @Override
    protected <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> type) {
        Class<Enum> enumClass = (Class<Enum>)type.getClassThatIsGenerated();
        if (enumClass.getEnumConstants().length == 0) return null;
        if (enumConstantIndexes.get(enumClass) == null) enumConstantIndexes.put(enumClass, 0);
        T resultConstant = (T) enumClass.getEnumConstants()[enumConstantIndexes.get(enumClass)];
        cycleIncrement(enumClass);
        return resultConstant;
    }

    private void cycleIncrement(Class<Enum> enumClass) {
        //TODO: NOT THREAD SAFE
        Integer currentIndex = enumConstantIndexes.get(enumClass);
        Integer newIndex = currentIndex + 1;
        if (newIndex > enumClass.getEnumConstants().length - 1){
            newIndex = 0;
        }
        enumConstantIndexes.put(enumClass, newIndex);
    }

    @Override
    public boolean supportsType(Class type) {
        return Enum.class.isAssignableFrom(type);
    }
}
