package org.objectg.gen.access;

/**
 * <p>
 *     Why: some property can miss some access capibilites (e.g. property has no read method). This Accessor
 *     will delegate action firstly to "main" accessor and if it is not capable of perfoming action, then "backed"
 *     accessor is used.
 * </p>
 * <p>
 * User: __nocach
 * Date: 3.2.13
 * </p>
 */
public class BackedUpAccessor implements PropertyAccessor {
	private final PropertyAccessor main;
	private final PropertyAccessor backed;

	public BackedUpAccessor(PropertyAccessor main, PropertyAccessor backed){
		this.main = main;
		this.backed = backed;
	}

	@Override
	public void set(final Object obj, final Object value) throws IllegalAccessException {
		try{
			main.set(obj, value);
		}
		catch (Exception e){
			backed.set(obj, value);
		}
	}

	@Override
	public String getName() {
		try{
			return main.getName();
		}
		catch (Exception e){
			return backed.getName();
		}
	}

	@Override
	public Class<?> getType() {
		try{
			return main.getType();
		}
		catch (Exception e){
			return backed.getType();
		}
	}

	@Override
	public Class<?> getGenericType(final int varIndex) {
		try{
			return main.getGenericType(varIndex);
		}
		catch (Exception e){
			return backed.getGenericType(varIndex);
		}
	}
}
