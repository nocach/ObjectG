package org.objectg.conf;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.GoodToHave;
import org.objectg.ObjectG;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.fixtures.ClassWithIPerson;
import org.objectg.fixtures.domain.IPerson;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;
import org.objectg.fixtures.domain.TourSeason;
import org.objectg.fixtures.domain.TourStop;
import org.objectg.fixtures.domain.TourType;
import org.objectg.gen.rule.Rules;
import org.objectg.matcher.ContextMatchers;

import static org.junit.Assert.assertThat;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class ConfigBuilderTest extends BaseObjectGTest {

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingToPassNotPrototypeObject(){
        //user passes object that was not created by ObjectG.prototype()
        ObjectG.unique(String.class, new PrototypeConfigTest.Person());
    }

    @Test
    public void canConfigureRuleWithMatcher(){
        Person generated = ObjectG.unique(Person.class,
                ObjectG.config().when(ContextMatchers.typeOf(String.class)).useRule(Rules.value("someValue")));

        assertEquals("someValue", generated.getFirstName());
        assertEquals("someValue", generated.getMiddleName());
        assertEquals("someValue", generated.getLastName());

    }

    @Test
    public void canConfigureNoObjects(){
        Tour tour = ObjectG.unique(Tour.class, ObjectG.config().onlyPrimitives());

        assertNull("not primitive should be null", tour.getSeason());
        assertEquals("collections should be empty", 0, tour.getStops().size());
    }

	@Test
	public void noObjectsGeneratesEmptyArrays(){
		Tour tour = ObjectG.unique(Tour.class, ObjectG.config().onlyPrimitives());

		assertEquals(0, tour.getBarcode().length);
	}

	@Test
	public void noObjectsWillSetEnums(){
		Tour tour = ObjectG.unique(Tour.class, ObjectG.config().onlyPrimitives());

		assertNotNull("enum is considered primitive", tour.getTourType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void cantChangeObjectsInCollectionsAfterNoObjectsCall(){
		ObjectG.config().onlyPrimitives().setObjectsInCollection(1);
	}

    @Test
    public void canConfigureNullObjectsClassWithMap(){
        ClassWithMap classWithMap = ObjectG.unique(ClassWithMap.class, ObjectG.config().onlyPrimitives());

        assertEquals("map should be empty", 0, classWithMap.getMap().size());
    }

    @Test
    public void canSimplyConfigValueForClass(){
        TourSeason expectedSeason = new TourSeason();
        Tour generated = ObjectG.unique(Tour.class,
                ObjectG.config().forClass(TourSeason.class).setValue(expectedSeason));

        assertEquals(expectedSeason, generated.getSeason());
    }

    @Test
    public void canSimplyConfigNullForClass(){
        Tour generated = ObjectG.unique(Tour.class,
                ObjectG.config().forClass(TourSeason.class).setNull());

        assertNull(generated.getSeason());
    }

    @Test
    public void canUsePropertyExpressionToSetValue(){
        Person unique = ObjectG.unique(Person.class
				, ObjectG.config()
				.onCycle()
					.backReference()
				.when("employee2Addresses[0].owner.firstName")
				.setValue("setByExpression"));

        assertEquals("setByExpression", unique.getEmployee2Addresses().get(0).getOwner().getFirstName());
    }

    @Test
    public void canUsePropertyExpressionToUsePrototype(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setFirstName("fromPrototype");
        Person unique = ObjectG.unique(Person.class
                , ObjectG.config()
                .when("employee2Addresses[0].owner")
                .usePrototype(prototype));

        assertEquals("fromPrototype", unique.getEmployee2Addresses().get(0).getOwner().getFirstName());
    }

    @Test
    public void canUsePropertyExpressionToSpecifyRule(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setFirstName("fromPrototype");
        Person unique = ObjectG.unique(Person.class
                , ObjectG.config()
                .when("employee2Addresses")
                .useRule(Rules.emptyCollections()));

        assertEquals(0, unique.getEmployee2Addresses().size());
    }

	@Test
	public void canSpecifyImplementation(){
		ClassWithIPerson classWithIPerson = ObjectG.unique(ClassWithIPerson.class,
				ObjectG.config().when(IPerson.class).useClass(Person.class));

		assertThat(classWithIPerson.getPerson(), IsInstanceOf.instanceOf(Person.class));
	}

	@Test
	public void specifiedImplementationIsUsedDuringWholeGeneration(){
		final Book generated = ObjectG.unique(Book.class, ObjectG.config()
				.onCycle().goDeeper(1)
				.when(BasePage.class).useClass(TextPage.class));

		assertTrue(generated.page instanceof TextPage);
		assertTrue(generated.page.page2Paragraph.page instanceof TextPage);
	}

	@Test
	public void canSpecifyGenerationDepth(){
		ClassA generated = ObjectG.unique(ClassA.class, ObjectG.config().depth(1));

		assertNotNull(generated.classB);
		assertNull(generated.classB.classC);
	}

	@Test
	public void canSpecifyZeroGenerationDepth(){
		ClassA generated = ObjectG.unique(ClassA.class, ObjectG.config().depth(0));

		assertNotNull(generated);
		assertNull(generated.classB);
	}

	@Test
	public void generationDepthGeneratesNativeTypesOnBottom(){
		ClassA generated = ObjectG.unique(ClassA.class, ObjectG.config().depth(1));

		assertNotNull("arrayPrimitive", generated.classB.arrayPrimitive);
		assertNotNull("enumPrimitive", generated.classB.enumPrimitive);
		assertNotNull("intPrimitive", generated.classB.intPrimitive);
		assertNotNull("longRefPrimitive", generated.classB.longRefPrimitive);
	}

	@Test
	public void canConfigureValues(){
		List<Tour> tours = ObjectG.uniqueList(Tour.class, ObjectG.config()
				.when(TourType.class)
				.setValues(TourType.ECONOMIC, TourType.ENTERPRISE)
		, 2);

		Assert.assertEquals(TourType.ECONOMIC, tours.get(0).getTourType());
		Assert.assertEquals(TourType.ENTERPRISE, tours.get(1).getTourType());
	}

	@Test
	public void canRewriteValueInCollectionUsingPropertyExpression(){
		final TourStop expectedTourStop = new TourStop();
		Tour unique = ObjectG.unique(Tour.class
				, ObjectG.config()
				.when("stops[0]")
				.setValue(expectedTourStop));


		assertEquals("first stop should be set by expression", expectedTourStop, unique.getStops().get(0));
	}

	@Test
	public void canUseAccessMethodsOnly(){
		final ClassWithHiddenField unique = ObjectG
				.unique(ClassWithHiddenField.class, ObjectG.config().access().onlyMethods());

		assertEquals(unique.hiddenField, ClassWithHiddenField.HIDDEN);
		assertNotNull(unique.getOpenField());
	}

	@Test
	public void willNotFallIfAccessMethodIsMissing(){
		final ClassWithCombinedAccess unique = ObjectG
				.unique(ClassWithCombinedAccess.class, ObjectG.config().access().onlyMethods());

		assertNull("field without setter should not be set", unique.fieldAndGetterProperty);
		assertNull("field without getter should not be set", unique.fieldAndSetterProperty);
		assertNull("field without setter and getter should not be set", unique.onlyFieldProperty);
		assertNotNull("only field with both setters and getters should be set", unique.setterGetterProperty);
	}

	@Test
	public void canCombineFieldsWithSetterGetter(){
		final ClassWithCombinedAccess unique = ObjectG
				.unique(ClassWithCombinedAccess.class, ObjectG.config().access().preferMethods());

		assertNotNull(unique.onlyFieldProperty);
		assertNotNull(unique.setterGetterProperty);
		assertNotNull(unique.fieldAndGetterProperty);
		assertNotNull(unique.fieldAndSetterProperty);

		assertTrue(ClassWithCombinedAccessFieldAndSetterPropertySetterCalled);
		assertTrue(ClassWithCombinedAccessSetterAndGetterPropertySetterCalled);
	}

	@Test
	@Ignore
	@GoodToHave
	public void canCreateMissingObjectsInPropertyExpression(){
		Tour tour = ObjectG.unique(Tour.class, ObjectG.config()
				.onlyPrimitives()
				.when("season.name").setValue("setByExpression"));

		assertNotNull("season should be generated when property expression was evaluated", tour.getSeason());
		assertEquals("season.name should be configurated with property expression",
				"setByExpression", tour.getSeason().getName());
	}

    @Test
	@Ignore
	@GoodToHave
    public void canInferPropertyTypeForExpressionMatchingCollection(){
		final TourStop expectedTourStop = new TourStop();
		Tour unique = ObjectG.unique(Tour.class
				, ObjectG.config()
				//when property expression is evaluated object "stops[0]" is null, so we must to find
				//another way how to "get" the type of those stops. This test tests this functionality.
				.setObjectsInCollection(0)
				.when("stops[0]")
				.setValue(expectedTourStop));


		assertEquals("first stop should be set by expression", expectedTourStop, unique.getStops().get(0));
    }

	//this 4 flags should be in TestCase because if they were in ClassWithCombinedAccess then ObjectG
	//would mess their values up
	private static boolean ClassWithCombinedAccessFieldAndSetterPropertySetterCalled = false;
	private static boolean ClassWithCombinedAccessSetterAndGetterPropertySetterCalled = false;

	public static class ClassWithCombinedAccess{
		private String onlyFieldProperty;
		private String fieldAndSetterProperty;
		private String fieldAndGetterProperty;
		private String setterGetterProperty;

		public void setFieldAndSetterProperty(final String fieldAndSetterProperty) {
			this.fieldAndSetterProperty = fieldAndSetterProperty;
			ClassWithCombinedAccessFieldAndSetterPropertySetterCalled = true;
		}

		public String getFieldAndGetterProperty() {
			return fieldAndGetterProperty;
		}

		public String getSetterGetterProperty() {
			return setterGetterProperty;
		}

		public void setSetterGetterProperty(final String setterGetterProperty) {
			ClassWithCombinedAccessSetterAndGetterPropertySetterCalled = true;
			this.setterGetterProperty = setterGetterProperty;
		}
	}

	public static class ClassWithHiddenField{
		public static final String HIDDEN = "HIDDEN";
		private String hiddenField = HIDDEN;
		private String openField;

		public String getOpenField() {
			return openField;
		}

		public void setOpenField(final String openField) {
			this.openField = openField;
		}
	}

	public static class ClassA{
		private ClassB classB;
	}
	public static class ClassB{
		private ClassC classC;
		private int intPrimitive;
		private TourType enumPrimitive;
		private byte[] arrayPrimitive;
		private Long longRefPrimitive;
	}
	public static class ClassC{
		private ClassD classD;
	}
	public static class ClassD{
	}

	public static class Book{
		BasePage page;
	}

	public abstract static class BasePage{
		private Page2Paragraph page2Paragraph;
	}

	public static class PicturePage extends BasePage{

	}

	public static class TextPage extends BasePage{

	}

	public static class Page2Paragraph{
		private BasePage page;
		private String paragraph;

		public BasePage getPage() {
			return page;
		}

		public void setPage(final BasePage page) {
			this.page = page;
		}

		public String getParagraph() {
			return paragraph;
		}

		public void setParagraph(final String paragraph) {
			this.paragraph = paragraph;
		}
	}

    public static class ClassWithMap{
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }
}
