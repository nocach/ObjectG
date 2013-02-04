package org.objectg.gen.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.Generator;
import org.objectg.gen.ValueSequence;
import org.objectg.gen.session.GenerationSession;

/**
 * <p>
 *     Thread safe generator of java native types such as String, int, etc.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
class PrimitiveGenerator extends Generator {
	//optimization
	final Set<Class> supportingClasses = new HashSet<Class>(){{
		add(short.class);
		add(Short.class);
		add(boolean.class);
		add(Boolean.class);
		add(Character.class);
		add(char.class);
		add(Float.class);
		add(float.class);
		add(Byte.class);
		add(byte.class);
		add(Long.class);
		add(long.class);
		add(Double.class);
		add(double.class);
		add(Integer.class);
		add(int.class);
		add(BigDecimal.class);
		add(BigInteger.class);
		add(Date.class);
		add(java.sql.Date.class);
		add(String.class);
		add(StringBuilder.class);
		add(StringBuffer.class);
		add(Void.class);
		add(Object.class);
		add(URL.class);
		add(Array.class);
		add(java.sql.Array.class);
		add(java.sql.Blob.class);
		add(java.sql.Clob.class);
		add(java.sql.ResultSet.class);
		add(InputStream.class);
	}};
	//optimization
	private final Map<Class, ValueSequence> classToSequence = new HashMap<Class, ValueSequence>(){{
		final ValueSequence shortSequence = GenerationSession.get().getSequenceFactory().createShortSequence();
		put(short.class, shortSequence);
		put(Short.class, shortSequence);
		final ValueSequence booleanSequence = GenerationSession.get().getSequenceFactory().createBooleanSequence();
		put(boolean.class, booleanSequence);
		put(Boolean.class, booleanSequence);
		final ValueSequence charSequence = GenerationSession.get().getSequenceFactory().createCharacterSequence();
		put(Character.class, charSequence);
		put(char.class, charSequence);
		final ValueSequence floatSequence = GenerationSession.get().getSequenceFactory().createFloatSequence();
		put(Float.class, floatSequence);
		put(float.class, floatSequence);
		final ValueSequence byteSequence = GenerationSession.get().getSequenceFactory().createByteSequence();
		put(Byte.class, byteSequence);
		put(byte.class, byteSequence);
		final ValueSequence longSequence = GenerationSession.get().getSequenceFactory().createLongSequence();
		put(Long.class, longSequence);
		put(long.class, longSequence);
		final ValueSequence doubleSequence = GenerationSession.get().getSequenceFactory().createDoubleSequence();
		put(Double.class, doubleSequence);
		put(double.class, doubleSequence);
		final ValueSequence integerSequence = GenerationSession.get().getSequenceFactory().createIntegerSequence();
		put(Integer.class, integerSequence);
		put(int.class, integerSequence);
		put(BigDecimal.class, GenerationSession.get().getSequenceFactory().createBigDecimalSequence());
		put(BigInteger.class, GenerationSession.get().getSequenceFactory().createBigIntegerSequence());
		put(Date.class, GenerationSession.get().getSequenceFactory().createDateSequence());
		put(java.sql.Date.class, GenerationSession.get().getSequenceFactory().createSqlDateSequence());
		put(String.class, GenerationSession.get().getSequenceFactory().createStringSequence());
		put(StringBuilder.class, GenerationSession.get().getSequenceFactory().createStringBuilderSequence());
		put(StringBuffer.class, GenerationSession.get().getSequenceFactory().createStringBufferSequence());
		put(Void.class, GenerationSession.get().getSequenceFactory().createVoidSequence());
		put(Object.class, GenerationSession.get().getSequenceFactory().createObjectSequence());
		put(URL.class, GenerationSession.get().getSequenceFactory().createUrlSequence());
		put(java.sql.Array.class, GenerationSession.get().getSequenceFactory().createNullSequence());
		put(java.sql.Blob.class, GenerationSession.get().getSequenceFactory().createNullSequence());
		put(java.sql.Clob.class, GenerationSession.get().getSequenceFactory().createNullSequence());
		put(java.sql.ResultSet.class, GenerationSession.get().getSequenceFactory().createNullSequence());
		put(InputStream.class, GenerationSession.get().getSequenceFactory().createInputStreamSequence());
	}};

    @Override
    protected Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
        try {
			if (classToSequence.containsKey(context.getClassThatIsGenerated())){
				final boolean canGeneratePrettyString = String.class.equals(context.getClassThatIsGenerated())
						&& context.getPropertyAccessor() != null;
				if (canGeneratePrettyString){
					final Object next = classToSequence.get(context.getClassThatIsGenerated()).next();
					return context.getPropertyAccessor().getName() + "-unique-"+next;
				}
				return classToSequence.get(context.getClassThatIsGenerated()).next();
			}
            throw new IllegalArgumentException("can't generate value of type " + context);
        } catch (RuntimeException e) {
            throw new GenerationException("can't generate value of type " + context, e);
        }
    }

    @Override
    public boolean supportsType(Class type) {
        return supportingClasses.contains(type);
    }

}
