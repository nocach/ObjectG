package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import static junit.framework.Assert.assertNotSame;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class CompositeGeneratorTest {
    private static final CompositeGenerator generator = new CompositeGenerator(
            new DummyGenerator(),
            new NativeClassGenerator());

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
            Object firstValue = generator.generate(new GenerationConfiguration(), new GenerationContext<Integer>(Integer.class));
            Object secondValue = generator.generate(new GenerationConfiguration(), new GenerationContext<Integer>(Integer.class));
            assertNotSame(firstValue, secondValue);
        }
    }

    private static class DummyGenerator extends Generator{

        @Override
        public Object generateValue(GenerationConfiguration configuration, GenerationContext type) {
            return null;
        }

        @Override
        public boolean supportsType(Class type) {
            return false;
        }
    }
}
