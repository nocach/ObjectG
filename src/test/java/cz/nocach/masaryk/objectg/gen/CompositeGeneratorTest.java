package cz.nocach.masaryk.objectg.gen;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotSame;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class CompositeGeneratorTest {
    private static final CompositeGenerator generator = new CompositeGenerator(
            new DummyGenerator(),
            new PrimitiveUniqueGenerator());

    @Test
    public void testConcurrent(){
        JUnitCore.runClasses(ParallelComputer.classes(),
                CompositeGeneratorSimpleTest.class,
                CompositeGeneratorSimpleTest.class,
                CompositeGeneratorSimpleTest.class
        );
    }

    public static class CompositeGeneratorSimpleTest{
        @Test
        public void test(){
            System.out.println("test of "+this);
            assertNotSame(generator.generate(Integer.class), generator.generate(Integer.class));
        }
    }

    private static class DummyGenerator implements Generator{

        @Override
        public Object generate(Class type) {
            return null;
        }

        @Override
        public boolean supportsType(Class type) {
            return false;
        }
    }
}
