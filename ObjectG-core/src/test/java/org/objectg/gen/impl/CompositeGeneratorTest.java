package org.objectg.gen.impl;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.objectg.BaseObjectGTest;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.CompositeGenerator;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.Generator;

import static junit.framework.Assert.assertNotSame;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class CompositeGeneratorTest extends BaseObjectGTest {
    private static final CompositeGenerator generator = new CompositeGenerator(
            new DummyGenerator(),
            new PrimitiveGenerator());

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
            Object firstValue = generator.generate(new GenerationConfiguration(), GenerationContext.createRoot(Integer.class));
            Object secondValue = generator.generate(new GenerationConfiguration(), GenerationContext.createRoot(Integer.class));
            assertNotSame(firstValue, secondValue);
        }
    }

    private static class DummyGenerator extends Generator {

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
