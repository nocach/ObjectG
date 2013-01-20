package org.objectg.gen.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.Generator;

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
	static final Set<Class> supportingClasses = new HashSet<Class>(){{
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
	private static final Map<Class, Sequence> classToSequence = new HashMap<Class, Sequence>(){{
		final ShortSequence shortSequence = new ShortSequence();
		put(short.class, shortSequence);
		put(Short.class, shortSequence);
		final BooleanSequence booleanSequence = new BooleanSequence();
		put(boolean.class, booleanSequence);
		put(Boolean.class, booleanSequence);
		final CharSequence charSequence = new CharSequence();
		put(Character.class, charSequence);
		put(char.class, charSequence);
		final FloatSequence floatSequence = new FloatSequence();
		put(Float.class, floatSequence);
		put(float.class, floatSequence);
		final ByteSequence byteSequence = new ByteSequence();
		put(Byte.class, byteSequence);
		put(byte.class, byteSequence);
		final LongSequence longSequence = new LongSequence();
		put(Long.class, longSequence);
		put(long.class, longSequence);
		final DoubleSequence doubleSequence = new DoubleSequence();
		put(Double.class, doubleSequence);
		put(double.class, doubleSequence);
		final IntegerSequence integerSequence = new IntegerSequence();
		put(Integer.class, integerSequence);
		put(int.class, integerSequence);
		put(BigDecimal.class, new BigDecimalSequence());
		put(BigInteger.class, new BigIntegerSequence());
		put(Date.class, new DateSequence());
		put(java.sql.Date.class, new SqlDateSequence());
		put(String.class, new StringSequence());
		put(StringBuilder.class, new StringBuilderSequence());
		put(StringBuffer.class, new StringBufferSequence());
		put(Void.class, new VoidSequence());
		put(Object.class, new ObjectSequence());
		put(URL.class, new URLSequence());
		put(java.sql.Array.class, new NullSequence());
		put(java.sql.Blob.class, new NullSequence());
		put(java.sql.Clob.class, new NullSequence());
		put(java.sql.ResultSet.class, new NullSequence());
		put(InputStream.class, new InputStreamSequence());
	}};
    //TODO: allow resetting of this sequences? E.g. this reset can be performed before any new TestCase is about to
    //be started
    private final AtomicLong longSequence = new AtomicLong();

    @Override
    protected Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
        try {
			if (classToSequence.containsKey(context.getClassThatIsGenerated())){
				return classToSequence.get(context.getClassThatIsGenerated()).getNext();
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

	private static interface Sequence<T>{
		public T getNext();
	}
	private static class LongSequence implements Sequence<Long>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Long getNext() {
			return longSequence.incrementAndGet();
		}
	}
	private static class StringSequence implements Sequence<String>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public String getNext() {
			return Long.toString(longSequence.incrementAndGet());
		}
	}
	private static class IntegerSequence implements Sequence<Integer>{
		private final AtomicInteger intSequence = new AtomicInteger();

		@Override
		public Integer getNext() {
			return intSequence.incrementAndGet();
		}
	}
	private static class ByteSequence implements Sequence<Byte>{
		private final AtomicInteger intSequence = new AtomicInteger();

		@Override
		public Byte getNext() {
			return (byte)intSequence.incrementAndGet();
		}
	}
	private static class FloatSequence implements Sequence<Float>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public Float getNext() {
			return (float)longSequence.incrementAndGet();
		}
	}
	private static class DoubleSequence implements Sequence<Double>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public Double getNext() {
			return (double)longSequence.incrementAndGet();
		}
	}
	private static class ShortSequence implements Sequence<Short>{
		private final AtomicInteger shortSequence = new AtomicInteger();
		@Override
		public Short getNext() {
			return (short)shortSequence.incrementAndGet();
		}
	}
	private static class BooleanSequence implements Sequence<Boolean>{
		private final AtomicBoolean booleanSequence = new AtomicBoolean();

		@Override
		public Boolean getNext() {
			return booleanSequence.getAndSet(!booleanSequence.get());
		}
	}
	private static class CharSequence implements Sequence<Character>{
		private final AtomicInteger charSequence = new AtomicInteger();
		@Override
		public Character getNext() {
			return returnNextCharOrThrow();
		}

		private Character returnNextCharOrThrow() {
			for(int tryCount = 0; tryCount < 100000; tryCount++){
				int charCode = charSequence.incrementAndGet();
				if (Character.isDefined(charCode)) return Character.toChars(charCode)[0];
			}
			//reset sequence
			charSequence.set(0);
			return returnNextCharOrThrow();
		}
	}
	private static class BigDecimalSequence implements Sequence<BigDecimal>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public BigDecimal getNext() {
			return BigDecimal.valueOf(longSequence.incrementAndGet());
		}
	}
	private static class BigIntegerSequence implements Sequence<BigInteger>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public BigInteger getNext() {
			return new BigInteger(Long.toString(longSequence.incrementAndGet()));
		}
	}
	private static class DateSequence implements Sequence<Date>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Date getNext() {
			return new Date(longSequence.incrementAndGet());
		}
	}
	private static class SqlDateSequence implements Sequence<java.sql.Date>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public java.sql.Date getNext() {
			return new java.sql.Date(longSequence.incrementAndGet());
		}
	}
	private static class StringBuilderSequence implements Sequence<StringBuilder>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public StringBuilder getNext() {
			return new StringBuilder(Long.toString(longSequence.incrementAndGet()));
		}
	}
	private static class StringBufferSequence implements Sequence<StringBuffer>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public StringBuffer getNext() {
			return new StringBuffer(Long.toString(longSequence.incrementAndGet()));
		}
	}
	private static class VoidSequence implements Sequence<Void>{
		@Override
		public Void getNext() {
			return null;
		}
	}
	private static class ObjectSequence implements Sequence<Object>{
		@Override
		public Object getNext() {
			return new Object();
		}
	}
	private static class URLSequence implements Sequence<URL>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public URL getNext() {
			try {
				return new URL("http://site"+Long.toString(longSequence.incrementAndGet())+".org");
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	private static class NullSequence implements Sequence<Object>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Object getNext() {
			return null;
		}
	}
	private static class InputStreamSequence implements Sequence<InputStream>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public InputStream getNext() {
			long uniqueLong = longSequence.incrementAndGet();
			return new ByteArrayInputStream(ByteBuffer.allocate(8).putLong(uniqueLong).array());
		}
	}
}
