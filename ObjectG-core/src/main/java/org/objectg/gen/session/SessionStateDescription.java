package org.objectg.gen.session;

/**
 * <p>
 *     description of value that will be managed by {@link GenerationSession}
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.1.13
 * </p>
 */
public interface SessionStateDescription<T>{
	/**
	 *
	 * @return value that will be set as initial value. This will be the value that will be returned in
	 *  {@link org.objectg.gen.session.SessionState#get()} after {@link GenerationSession}was started.
	 */
	public T getInitValue();
}
