package org.objectg.gen.rule.range;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class DateRange extends Range<Date> {
	private DateTime from;
	private DateTime to;
	private Period step;

	DateRange(Date from, Date to, Period step){
		Assert.notNull(step, "step");
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.step = step;
		this.from = new DateTime(from);
		this.to = new DateTime(to);
	}
	DateRange(Date from, Date to){
		this(from, to, Period.days(1));
	}

	public DateRange step(Period step){
		Assert.notNull(step, "step");
		this.step = step;
		return this;
	}

	@Override
	public Date getStart() {
		return from.toDate();
	}

	@Override
	public Date getNext(final Date currentValue) {
		final DateTime result = new DateTime(currentValue).plus(step);
		return result.toDate();
	}

	@Override
	public boolean hasNext(final Date currentValue) {
		final DateTime nextValue = new DateTime(currentValue).plus(step);
		return nextValue.isBefore(to) || nextValue.isEqual(to);
	}

	@Override
	public boolean hasPrevious(final Date currentValue) {
		final DateTime previousValue = new DateTime(currentValue).minus(step);
		return previousValue.isAfter(from) || previousValue.isEqual(from);
	}

	@Override
	public Date getPrevious(final Date currentValue) {
		final DateTime previousValue = new DateTime(currentValue).minus(step);
		return previousValue.toDate();
	}

	@Override
	public Date getEnd() {
		return to.toDate();
	}

	public static DateRange createReverse(Date from, Date to){
		Assert.isTrue(from.after(to) || from.equals(to), "from >= to");
		final DateRange result = new DateRange(to, from);
		result.setReversed(true);
		return result;
	}

	/**
	 * create DateRange with default step = {@link Period#days(1)} - one day
	 * @param from not null
	 * @param to not null
	 * @return
	 */
	public static DateRange create(Date from, Date to){
		Assert.isTrue(from.before(to) || from.equals(to), "from <= to");
		return new DateRange(from, to);
	}
}
