package org.objectg.fixtures.big;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: __nocach
 * Date: 16.11.12
 */
public class BigHierarchyD {
	private Long longOne;
	private String string;
	private int intOne;
	private BigDecimal bigDecimal;
	private Date date;
	private Date date2;
	private Date date3;
	private SimpleClass simpleClass;
	private List<SimpleClass> simpleClassList;
	private Set<SimpleClass> simpleClassSet;
	private Map<String, SimpleClass> stringSimpleClassMap;
	private List<BigHierarchyA> bigHierarchyAList;
	private List<BigHierarchyA> bigHierarchyAList2;
	private List<BigHierarchyA> bigHierarchyAList3;
	private List<BigHierarchyB> bigHierarchyBList;
	private List<BigHierarchyC> bigHierarchyCList;
	private List<BigHierarchyD> bigHierarchyDList;

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

	public List<SimpleClass> getSimpleClassList() {
		return simpleClassList;
	}

	public void setSimpleClassList(final List<SimpleClass> simpleClassList) {
		this.simpleClassList = simpleClassList;
	}

	public Set<SimpleClass> getSimpleClassSet() {
		return simpleClassSet;
	}

	public void setSimpleClassSet(final Set<SimpleClass> simpleClassSet) {
		this.simpleClassSet = simpleClassSet;
	}

	public Map<String, SimpleClass> getStringSimpleClassMap() {
		return stringSimpleClassMap;
	}

	public void setStringSimpleClassMap(final Map<String, SimpleClass> stringSimpleClassMap) {
		this.stringSimpleClassMap = stringSimpleClassMap;
	}

	public List<BigHierarchyA> getBigHierarchyAList() {
		return bigHierarchyAList;
	}

	public void setBigHierarchyAList(final List<BigHierarchyA> bigHierarchyAList) {
		this.bigHierarchyAList = bigHierarchyAList;
	}

	public List<BigHierarchyA> getBigHierarchyAList2() {
		return bigHierarchyAList2;
	}

	public void setBigHierarchyAList2(final List<BigHierarchyA> bigHierarchyAList2) {
		this.bigHierarchyAList2 = bigHierarchyAList2;
	}

	public List<BigHierarchyA> getBigHierarchyAList3() {
		return bigHierarchyAList3;
	}

	public void setBigHierarchyAList3(final List<BigHierarchyA> bigHierarchyAList3) {
		this.bigHierarchyAList3 = bigHierarchyAList3;
	}

	public List<BigHierarchyB> getBigHierarchyBList() {
		return bigHierarchyBList;
	}

	public void setBigHierarchyBList(final List<BigHierarchyB> bigHierarchyBList) {
		this.bigHierarchyBList = bigHierarchyBList;
	}

	public List<BigHierarchyC> getBigHierarchyCList() {
		return bigHierarchyCList;
	}

	public void setBigHierarchyCList(final List<BigHierarchyC> bigHierarchyCList) {
		this.bigHierarchyCList = bigHierarchyCList;
	}

	public List<BigHierarchyD> getBigHierarchyDList() {
		return bigHierarchyDList;
	}

	public void setBigHierarchyDList(final List<BigHierarchyD> bigHierarchyDList) {
		this.bigHierarchyDList = bigHierarchyDList;
	}
}
