package org.objectg.gen.impl;

import java.lang.reflect.Array;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.session.GenerationSession;
import org.objectg.util.Types;

/**
 * User: __nocach
 * Date: 27.10.12
 */
class ArrayGenerator extends MultipleItemsGenerator {
	@Override
	protected Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
		try {
			return createArray(configuration, context);
		} catch (ClassNotFoundException e) {
			throw new GenerationException(configuration, context, e);
		}
	}

	private Class getArrayObjectClass(Class arrayType) {
		try {
			return Types.getArrayElementClass(arrayType);
		} catch (ClassNotFoundException e) {
			throw new GenerationException(e);
		}
	}

	private <T, U> T createArray(GenerationConfiguration configuration, GenerationContext context)
			throws ClassNotFoundException {
		Class arrayType = context.getClassThatIsGenerated();
		if (arrayType.equals(int[].class)) {
			int[] result = new int[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, int.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Integer>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(double[].class)) {
			double[] result = new double[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, double.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Double>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(long[].class)) {
			long[] result = new long[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, long.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Long>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(float[].class)) {
			float[] result = new float[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, float.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Float>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(byte[].class)) {
			byte[] result = new byte[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, byte.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Byte>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(char[].class)) {
			char[] result = new char[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, char.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Character>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(boolean[].class)) {
			boolean[] result = new boolean[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, boolean.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Boolean>generate(configuration, objectContext);
			}
			return (T) result;
		}
		if (arrayType.equals(short[].class)) {
			short[] result = new short[configuration.getObjectsInCollections()];
			for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
				final GenerationContext objectContext = createArrayObjectContext(context, short.class);
				if (!shouldAddObjectForContext(configuration, objectContext)) continue;
				result[i] = GenerationSession.get().<Short>generate(configuration, objectContext);
			}
			return (T) result;
		}
		//magic magic magic
		Class<U> arrayObjectClass = (Class<U>) Class
				.forName(arrayType.getName().substring(2, arrayType.getName().length() - 1));
		U[] array = (U[]) Array.newInstance(arrayObjectClass, configuration.getObjectsInCollections());
		for (int i = 0; i < configuration.getObjectsInCollections(); i++) {
			final GenerationContext objectContext = createArrayObjectContext(context, getArrayObjectClass(arrayType));
			if (!shouldAddObjectForContext(configuration, objectContext)) continue;
			array[i] = GenerationSession.get().<U>generate(configuration, objectContext);
		}
		return (T) array;
	}

	private GenerationContext createArrayObjectContext(final GenerationContext context,
			final Class elementClass) {
		final GenerationContext objectContext = context.push(elementClass);
		objectContext.increaseDepthIgnoreByOne();
		return objectContext;
	}

	@Override
	public boolean supportsType(Class type) {
		return Types.isArray(type);
	}
}
