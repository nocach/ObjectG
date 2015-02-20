package org.objectg.conf;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.GoodToHave;
import org.objectg.conf.prototype.PrototypeCreationException;
import org.objectg.conf.prototype.PrototypeCreator;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class PrototypeCreatorTest extends BaseObjectGTest {

	private PrototypeCreator prototypeCreator;

	@Before
	public void createPrototypeCreator() {
		prototypeCreator = new PrototypeCreator();
	}

	@Test
	public void canCreateTwoPrototypes() {
		prototypeCreator.newPrototype(ClassWithProperties.class);
		prototypeCreator.newPrototype(ClassWithProperties.class);
	}

	@Test
	public void canCreatePrototypeForClassWithFinalsAndStatics() {
		prototypeCreator.newPrototype(ClassWithFinalAndStaticMethods.class);
	}

	@Test
	@Ignore
	@GoodToHave
	public void canCreatePrototypeForAbstractClass() {
		fail("think about it");
	}

	@Test
	public void canCreatePrototypeForClassWithoutNoArgConstructor() {
		prototypeCreator.newPrototype(ClassWithoutNoArgConstructor.class);
	}

	@Test(expected = PrototypeCreationException.class)
	public void throwIfValidationInMoreArgsConstructor() {
		prototypeCreator.newPrototype(ClassWithoutNoArgConstructorWithLogic.class);
	}

	@Test
	public void canCreatePrototypeOfClassReferringItself() {
		prototypeCreator.newPrototype(ClassReferingItSelf.class);
	}

	public static class ClassWithoutNoArgConstructorWithLogic {
		public ClassWithoutNoArgConstructorWithLogic(Object arg) {
			Assert.notNull(arg);
		}
	}

	public static class ClassWithoutNoArgConstructor {
		private final Object someFinalObjectField;
		private final int someFinalPrimitiveField;

		public ClassWithoutNoArgConstructor(Object arg, int primitive) {
			someFinalObjectField = arg;
			someFinalPrimitiveField = primitive;
		}
	}

	public static class ClassWithFinalAndStaticMethods {
		public final String getString() {
			return null;
		}

		public static void setString(String arg) {

		}
	}

	public static class ClassWithProperties {
		private String property1;
		private Object property2;

		public String getProperty1() {
			return property1;
		}

		public void setProperty1(String property1) {
			this.property1 = property1;
		}

		public Object getProperty2() {
			return property2;
		}

		//special case returning not null object
		public Object setProperty2(Object property2) {
			this.property2 = property2;
			return property2;
		}

		//special case of short method name
		public void setA() {
		}

		//special case of method that looks like setter, but it is not
		public void set() {
		}
	}

	public static class ClassReferingItSelf {
		private ClassReferingItSelf self;

		public ClassReferingItSelf getSelf() {
			return self;
		}

		public void setSelf(final ClassReferingItSelf self) {
			this.self = self;
		}
	}
}
