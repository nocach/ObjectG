package org.objectg.gen.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectg.util.Generics;
import org.objectg.util.Methods;

/**
 * User: __nocach
 * Date: 3.2.13
 */
public class MethodAccessor implements PropertyAccessor{
	private final Method setter;
	private final Method getter;

	public MethodAccessor(Method setter, Method getter){
		this.setter = setter;
		this.getter = getter;
	}
	@Override
	public void set(final Object obj, final Object value) throws IllegalAccessException {
		try {
			if (setter.isAccessible()){
				setter.invoke(obj, value);
			}
			else {
				setRetainingAccessableLevel(obj, value);
			}
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void setRetainingAccessableLevel(final Object obj, final Object value)
			throws IllegalAccessException, InvocationTargetException {
		try{
			setter.setAccessible(true);
			setter.invoke(obj, value);
		}
		finally {
			setter.setAccessible(false);
		}
	}

	@Override
	public String getName() {
		return getter != null
				? Methods.getPropertyName(getter.getName())
				: Methods.getPropertyName(setter.getName());
	}

	@Override
	public Class<?> getType() {
		return getter.getReturnType();
	}

	@Override
	public Class<?> getGenericType(final int varIndex) {
		return Generics.extractTypeFromGetter(getter, varIndex);
	}

	public boolean isFull(){
		return setter != null && getter != null;
	}

	@Override
	public String toString() {
		return "MethodAccessor{" +
				"setter=" + setter +
				", getter=" + getter +
				'}';
	}
}
