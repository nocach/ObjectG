package org.objectg.util;

import java.util.List;

import org.junit.Test;
import org.objectg.BaseObjectGTest;

import static junit.framework.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 17.11.12
 */
public class GenericsTest extends BaseObjectGTest {

	@Test
	public void simpleGeneric() throws NoSuchFieldException {
		final Class classAList = Generics.extractTypeFromField(GenericFields.class.getDeclaredField("classAList"), 0);
		assertEquals(ClassA.class, classAList);
	}

	@Test
	public void boundedTypeGeneric() throws NoSuchFieldException {
		final Class classBoundedType = Generics.extractTypeFromField(
				GenericFields.class.getDeclaredField("boundedTypeParam"), 0);

		assertEquals(ClassA.class, classBoundedType);
	}

	@Test
	public void superTypeGeneric() throws NoSuchFieldException {
		final Class classSuper = Generics.extractTypeFromField(
				GenericFields.class.getDeclaredField("classASuperList"), 0
		);

		assertEquals(ClassA.class, classSuper);
	}

	@Test
	public void unboundedGeneric() throws NoSuchFieldException {
		final Class unboundClass = Generics.extractTypeFromField(
				GenericFields.class.getDeclaredField("unboundedList"), 0
		);

		assertEquals(Object.class, unboundClass);
	}

	@Test
	public void recursiveTypeBoundGeneric() throws NoSuchFieldException{
		final Class recursiveClass = Generics.extractTypeFromField(
				GenericFields.class.getDeclaredField("recursiveTypeBound"), 0
		);

		assertEquals(Comparable.class, recursiveClass);
	}

	public static class GenericFields<T extends Comparable<T>>{
		private List<ClassA> classAList;
		private List<? extends ClassA> boundedTypeParam;
		//object must be super to ClassA
		private List<? super ClassA> classASuperList;
		private List<?> unboundedList;
		private List<T> recursiveTypeBound;
	}

	public static class ClassA{
	}

	public static class ClassB extends ClassA{

	}
}
