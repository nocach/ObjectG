package cz.nocach.masaryk.objectg.conf;

/**
 * <p>
 *     Listener that will be notified by {@link SetterConfigurator} when configuration must by processed for certain
 *     properties.
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public interface ConfigurationHandler {
    /**
     * called when new object is about to be configured
     * @param objectUnderConfiguration not null object on which configuration will be set
     */
    public void onInit(Object objectUnderConfiguration);
    /**
     * called when setter was called for configuration.
     * @param propertyName property of the setter
     */
    public void onSetter(Object objectUnderConfiguration, String propertyName);
}