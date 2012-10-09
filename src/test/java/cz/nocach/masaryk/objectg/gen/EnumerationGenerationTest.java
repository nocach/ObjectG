package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.fixtures.Tour;
import cz.nocach.masaryk.objectg.fixtures.TourType;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: __nocach
 * Date: 9.10.12
 */
public class EnumerationGenerationTest {

    @Test
    public void canGenerateEnumeration(){
        TourType generated = ObjectG.unique(TourType.class);

        assertNotNull(generated);
    }

    @Test
    public void canGenerateDifferentEnumerationValues(){
        TourType value1 = ObjectG.unique(TourType.class);
        TourType value2 = ObjectG.unique(TourType.class);

        assertNotNull("value1", value1);
        assertNotNull("value2", value2);
        assertNotSame("value1 must not be equal to value2", value1, value2);
    }

    @Test
    public void forEmptyEnumerationNullIsGenerated(){
        EmptyEnum generated = ObjectG.unique(EmptyEnum.class);
        assertNull("for empty enumeration only null can be generated", generated);
    }

    @Test
    public void generatedEnumConstantsAreCyclic(){
        TwoValuesEnum value1 = ObjectG.unique(TwoValuesEnum.class);
        TwoValuesEnum value2 = ObjectG.unique(TwoValuesEnum.class);
        TwoValuesEnum value3 = ObjectG.unique(TwoValuesEnum.class);

        assertEquals(value1, value3);
        assertNotSame(value1, value2);
        assertNotSame(value2, value3);
    }

    @Test
    public void canGenerateEnumsAsFields(){
        Tour generated = ObjectG.unique(Tour.class);

        assertNotNull(generated.getTourType());
    }

    public static enum EmptyEnum{

    }

    public static enum TwoValuesEnum{
        FIRST, SECOND
    }
}
