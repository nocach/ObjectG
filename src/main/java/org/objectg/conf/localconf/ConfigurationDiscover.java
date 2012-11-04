package org.objectg.conf.localconf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectg.conf.ConfigurationBuilder;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.exception.ConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class ConfigurationDiscover {

	/**
	 *
	 * @param objectWithConfiguration
	 * @return found configuration or null if no local configuration found
	 */
	public GenerationConfiguration get(final Object objectWithConfiguration){
		Assert.notNull(objectWithConfiguration, "objectWithConfiguration should be not null");
		final ResultHolder resultHolder = new ResultHolder();
		ReflectionUtils.doWithMethods(objectWithConfiguration.getClass(),
				updateResultHoderFromAnnotatedMethod(objectWithConfiguration, resultHolder), filterOnlyAnnotatedWithConfigurationAnnotation());
		return resultHolder.result;
	}

	private ReflectionUtils.MethodCallback updateResultHoderFromAnnotatedMethod(final Object objectWithConfiguration,
			final ResultHolder resultHolder) {
		return new ReflectionUtils.MethodCallback() {
			@Override
			public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
				try {
					method.setAccessible(true);
					GenerationConfiguration confFromMethod = extractConfigurationSafely(method, objectWithConfiguration);
					updateResultHolder(confFromMethod, resultHolder);
				} catch (InvocationTargetException e) {
					throwMisuseException();
				}
			}
		};
	}

	private void updateResultHolder(final GenerationConfiguration confFromMethod,
			final ResultHolder resultHolder) {
		if (confFromMethod != null){
			if (resultHolder.result == null){
				resultHolder.result = confFromMethod;
			}
			else{
				resultHolder.result.merge(confFromMethod);
			}
		}
	}

	private GenerationConfiguration extractConfigurationSafely(final Method method,
			final Object objectWithConfiguration) throws IllegalAccessException, InvocationTargetException {
		Object configuration = method.invoke(objectWithConfiguration);
		if (!(configuration instanceof ConfigurationBuilder)
				&& !(configuration instanceof GenerationConfiguration)){
			throwMisuseException();
		}
		GenerationConfiguration confFromMethod = null;
		if (configuration instanceof ConfigurationBuilder){
			confFromMethod = ((ConfigurationBuilder) configuration).done();
		}
		if (configuration instanceof GenerationConfiguration){
			confFromMethod= (GenerationConfiguration) configuration;
		}
		return confFromMethod;
	}

	private ReflectionUtils.MethodFilter filterOnlyAnnotatedWithConfigurationAnnotation() {
		return new ReflectionUtils.MethodFilter() {
					@Override
					public boolean matches(final Method method) {
						return AnnotationUtils.getAnnotation(method, ConfigurationProvider.class) != null;
					}
				};
	}

	private void throwMisuseException() {
		throw new ConfigurationException("method annotated with ConfigurationProvider "
			+"should have no parameters and " +
			"should return instance of " + GenerationConfiguration.class.getSimpleName()
			+" or instance of " + ConfigurationBuilder.class.getSimpleName());
	}

	private static class ResultHolder{
		GenerationConfiguration result = null;
	}
}
