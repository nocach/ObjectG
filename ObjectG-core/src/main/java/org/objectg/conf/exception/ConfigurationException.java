package org.objectg.conf.exception;

/**
 * <p>
 *     Indicates wrong usage of frameworks' configuration
 * </p>
 * <p>
 * User: __nocach
 * Date: 5.10.12
 * </p>
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException() {
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
