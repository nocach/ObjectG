package org.objectg.gen;

import org.objectg.conf.GenerationConfiguration;

/**
 * <p>
 *     Class defining logic of post-processing generated values
 * </p>
 * <p>
 * User: __nocach
 * Date: 27.10.12
 * </p>
 */
public interface PostProcessor {
    /**
     *
     * @param configuration configuration of current generation
     * @param generatedObject
     * @param <T>
     * @return processed generated object. Can be another instance of the same class.
     */
    public <T> T process(GenerationConfiguration configuration, T generatedObject);
}
