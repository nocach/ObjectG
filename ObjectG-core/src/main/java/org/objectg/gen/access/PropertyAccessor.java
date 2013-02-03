package org.objectg.gen.access;

/**
 * <p>
 *     Abstraction layer for accessing property
 * </p>
 * <p>
 *     Why: Properties can be accessed through fields or methods, thus higher level of abstraction is needed.
 * </p>
 * <p>
 * User: __nocach
 * Date: 3.2.13
 * </p>
 */
public interface PropertyAccessor {
	/**
	 *
	 * @param obj
	 * @param value
	 * @throws IllegalAccessException
	 */
	public void set(Object obj, Object value) throws IllegalAccessException;
	/**
	 *
	 * @return name of the property
	 */
	public String getName();

	/**
	 *
	 * @return type of the property
	 */
	public Class<?> getType();

	/**
	 *
	 * @param varIndex index of the type var
	 * @return Generic Type of the passed type var index
	 */
	public Class<?> getGenericType(int varIndex);
}