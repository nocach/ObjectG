package org.objectg.fixtures.big;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * User: __nocach
 * Date: 16.11.12
 */
public class BigHierarchyC {
	private Long longOne;
	private String string;
	private int intOne;
	private BigDecimal bigDecimal;
	private Date date;
	private Date date2;
	private Date date3;
	private SimpleClass simpleClass;
	private BigEnum bigEnum;
	private Map<BigEnum, BigHierarchyA> map;

	public Long getLongOne() {
		return longOne;
	}

	public void setLongOne(final Long longOne) {
		this.longOne = longOne;
	}

	public String getString() {
		return string;
	}

	public void setString(final String string) {
		this.string = string;
	}

	public int getIntOne() {
		return intOne;
	}

	public void setIntOne(final int intOne) {
		this.intOne = intOne;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(final BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(final Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(final Date date3) {
		this.date3 = date3;
	}

	public SimpleClass getSimpleClass() {
		return simpleClass;
	}

	public void setSimpleClass(final SimpleClass simpleClass) {
		this.simpleClass = simpleClass;
	}

	public BigEnum getBigEnum() {
		return bigEnum;
	}

	public void setBigEnum(final BigEnum bigEnum) {
		this.bigEnum = bigEnum;
	}

	public Map<BigEnum, BigHierarchyA> getMap() {
		return map;
	}

	public void setMap(final Map<BigEnum, BigHierarchyA> map) {
		this.map = map;
	}
}
