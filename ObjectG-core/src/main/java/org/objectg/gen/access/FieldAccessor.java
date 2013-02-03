package org.objectg.gen.access;

import java.lang.reflect.Field;

import org.objectg.util.Generics;
import org.springframework.util.Assert;

/**
 * <p>
 * User: __nocach
 * Date: 3.2.13
 * </p>
 */
public class FieldAccessor implements PropertyAccessor {
	private Field field;

	public FieldAccessor(Field field){
		Assert.notNull(field, "field can't be null");
		this.field = field;
	}
	@Override
	public void set(final Object obj, final Object value) throws IllegalAccessException {
		field.set(obj, value);
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	@Override
	public Class<?> getGenericType(int varIndex) {
		return Generics.extractTypeFromField(field, varIndex);
	}

	public Field getField() {
		return field;
	}

	@Override
	public String toString() {
		return "FieldAccessor{" +
				"field=" + field +
				'}';
	}
}
