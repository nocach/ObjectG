package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;

/**
 * User: __nocach
 * Date: 27.10.12
 */
public interface PostProcessor {
    /**
     *
     * @param configuration
     * @param generatedObject
     * @param <T>
     * @return processed generated object. Can be another instance of the same class.
     */
    public <T> T process(GenerationConfiguration configuration, T generatedObject);
}
