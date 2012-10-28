package cz.nocach.masaryk.objectg.gen.postproc;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.fixtures.domain.Person;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * User: __nocach
 * Date: 27.10.12
 */
public class ExpressionPostProcessorTest implements ExpressionPostProcessor.Handler{
    private GenerationConfiguration handleConfiguration;
    private Object handleGeneratedObject;
    private Object handleContextForExpression;

    @Override
    public Object handle(GenerationConfiguration configuration, Object generatedObject, GenerationContext contextForExpression) {
        this.handleConfiguration = configuration;
        this.handleGeneratedObject = generatedObject;
        handleContextForExpression = contextForExpression;
        return Long.valueOf(1L);
    }

    @Test
    public void handlerIsCalled(){
        ExpressionPostProcessor expressionPostProcessor = new ExpressionPostProcessor(
                "employee2Addresses[0].id", this);

        Person person = ObjectG.unique(Person.class);
        expressionPostProcessor.process(new GenerationConfiguration(), person);

        assertEquals("handleGeneratedObject", person, handleGeneratedObject);
        assertNotNull("expressionGenerationContext", handleContextForExpression);
    }

    @Test
    @Ignore
    public void boundConditions(){
        fail("think about empty expression and invalid expressions");
    }
}
