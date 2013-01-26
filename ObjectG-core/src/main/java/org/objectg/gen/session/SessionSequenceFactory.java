package org.objectg.gen.session;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.objectg.gen.Generator;
import org.objectg.gen.ValueSequence;

/**
 * <p>
 *     Factory providing methods to create GenerationSession managed sequences of values.
 * </p>
 * <p>
 *     <b>Why</b>: Sequences are used through multiple Generators to maintain state. This state should
 *     be relevant to the current GenerationSession and should not affect the results of different subsequent
 *     generations. This requires explicit managing on GenerationSession basis. ValueSequences created
 *     through this class are automatically managed by framework and that is why no extra effort from
 *     user side is required.
 * </p>
 * <p>
 *     Created {@link ValueSequence} is relevant to the GenerationSession for which it was created. So, for different
 *     GenerationSessions new {@link ValueSequence} must be created. Normally you don't need to know this information
 *     because GenerationSession will automatically create {@link Generator}s when new GenerationSession is created.
 *     That is why you can use {@code GenerationSession.get().getSequenceFactory()} for creating sequence without warring
 *     about sequence being relative to right GenerationSession.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.1.13
 * </p>
 */
public class SessionSequenceFactory {
	private Set<WeakReference<ValueSequence>> managedSequences = new HashSet<WeakReference<ValueSequence>>();

	SessionSequenceFactory() {
	}


	void resetManagedSequences() {
		final Iterator<WeakReference<ValueSequence>> iterator = managedSequences.iterator();
		while(iterator.hasNext()){
			final WeakReference<ValueSequence> sequenceRef = iterator.next();
			final ValueSequence valueSequence = sequenceRef.get();
			if (valueSequence != null) {
				valueSequence.reset();
			}
			else {
				iterator.remove();
			}
		}
	}

	private <T> ValueSequence<T> startManage(final ValueSequence<T> sequence) {
		managedSequences.add(new WeakReference<ValueSequence>(sequence));
		return sequence;
	}

	public ValueSequence<String> createStringSequence(){
		return startManage(new StringSequence());
	}

	public ValueSequence<Integer> createIntegerSequence(){
		return startManage(new IntegerSequence());
	}

	public  ValueSequence<Byte> createByteSequence(){
		return startManage(new ByteSequence());
	}

	public  ValueSequence<Float> createFloatSequence(){
		return startManage(new FloatSequence());
	}

	public  ValueSequence<Double> createDoubleSequence(){
		return startManage(new DoubleSequence());
	}

	public  ValueSequence<Short> createShortSequence(){
		return startManage(new ShortSequence());
	}

	public  ValueSequence<Boolean> createBooleanSequence(){
		return startManage(new BooleanSequence());
	}

	public  ValueSequence<Character> createCharacterSequence(){
		return startManage(new CharSequence());
	}

	public  ValueSequence<BigDecimal> createBigDecimalSequence(){
		return startManage(new BigDecimalSequence());
	}

	public  ValueSequence<BigInteger> createBigIntegerSequence(){
		return startManage(new BigIntegerSequence());
	}

	public  ValueSequence<Date> createDateSequence(){
		return startManage(new DateSequence());
	}

	public  ValueSequence<java.sql.Date> createSqlDateSequence(){
		return startManage(new SqlDateSequence());
	}

	public  ValueSequence<StringBuilder> createStringBuilderSequence(){
		return startManage(new StringBuilderSequence());
	}

	public  ValueSequence<StringBuffer> createStringBufferSequence(){
		return startManage(new StringBufferSequence());
	}

	public  ValueSequence<Void> createVoidSequence(){
		return startManage(new VoidSequence());
	}

	public  ValueSequence<Object> createObjectSequence(){
		return startManage(new ObjectSequence());
	}

	public  ValueSequence<URL> createUrlSequence(){
		return startManage(new URLSequence());
	}

	public  ValueSequence<Object> createNullSequence(){
		return startManage(new NullSequence());
	}

	public  ValueSequence<InputStream> createInputStreamSequence(){
		return startManage(new InputStreamSequence());
	}

	public ValueSequence<Long> createLongSequence(){
		return startManage(new LongSequence());
	}

	private static class LongSequence implements ValueSequence<Long>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Long next() {
			return longSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class StringSequence implements ValueSequence<String>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public String next() {
			return Long.toString(longSequence.incrementAndGet());
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class IntegerSequence implements ValueSequence<Integer>{
		private final AtomicInteger intSequence = new AtomicInteger();

		@Override
		public Integer next() {
			return intSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			intSequence.set(0);
		}
	}
	private static class ByteSequence implements ValueSequence<Byte>{
		private final AtomicInteger intSequence = new AtomicInteger();

		@Override
		public Byte next() {
			return (byte)intSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			intSequence.set(0);
		}
	}
	private static class FloatSequence implements ValueSequence<Float>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public Float next() {
			return (float)longSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class DoubleSequence implements ValueSequence<Double>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public Double next() {
			return (double)longSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class ShortSequence implements ValueSequence<Short>{
		private final AtomicInteger shortSequence = new AtomicInteger();
		@Override
		public Short next() {
			return (short)shortSequence.incrementAndGet();
		}

		@Override
		public void reset() {
			shortSequence.set(0);
		}
	}
	private static class BooleanSequence implements ValueSequence<Boolean>{
		private final AtomicBoolean booleanSequence = new AtomicBoolean();

		@Override
		public Boolean next() {
			return booleanSequence.getAndSet(!booleanSequence.get());
		}

		@Override
		public void reset() {
			booleanSequence.set(false);
		}
	}
	private static class CharSequence implements ValueSequence<Character>{
		private final AtomicInteger charSequence = new AtomicInteger();
		@Override
		public Character next() {
			return returnNextCharOrThrow();
		}

		@Override
		public void reset() {
			charSequence.set(0);
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
	private static class BigDecimalSequence implements ValueSequence<BigDecimal>{
		private final AtomicLong longSequence = new AtomicLong();

		@Override
		public BigDecimal next() {
			return BigDecimal.valueOf(longSequence.incrementAndGet());
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class BigIntegerSequence implements ValueSequence<BigInteger>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public BigInteger next() {
			return new BigInteger(Long.toString(longSequence.incrementAndGet()));
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class DateSequence implements ValueSequence<Date>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Date next() {
			return new Date(longSequence.incrementAndGet());
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class SqlDateSequence implements ValueSequence<java.sql.Date>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public java.sql.Date next() {
			return new java.sql.Date(longSequence.incrementAndGet());
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class StringBuilderSequence implements ValueSequence<StringBuilder>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public StringBuilder next() {
			return new StringBuilder(Long.toString(longSequence.incrementAndGet()));
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class StringBufferSequence implements ValueSequence<StringBuffer>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public StringBuffer next() {
			return new StringBuffer(Long.toString(longSequence.incrementAndGet()));
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class VoidSequence implements ValueSequence<Void>{
		@Override
		public Void next() {
			return null;
		}

		@Override
		public void reset() {
			//nothing
		}
	}
	private static class ObjectSequence implements ValueSequence<Object>{
		@Override
		public Object next() {
			return new Object();
		}

		@Override
		public void reset() {
			//nothing
		}
	}
	private static class URLSequence implements ValueSequence<URL>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public URL next() {
			try {
				return new URL("http://site"+Long.toString(longSequence.incrementAndGet())+".org");
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
	private static class NullSequence implements ValueSequence<Object>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public Object next() {
			return null;
		}

		@Override
		public void reset() {
			//nothing
		}
	}
	private static class InputStreamSequence implements ValueSequence<InputStream>{
		private final AtomicLong longSequence = new AtomicLong();
		@Override
		public InputStream next() {
			long uniqueLong = longSequence.incrementAndGet();
			return new ByteArrayInputStream(ByteBuffer.allocate(8).putLong(uniqueLong).array());
		}

		@Override
		public void reset() {
			longSequence.set(0);
		}
	}
}
