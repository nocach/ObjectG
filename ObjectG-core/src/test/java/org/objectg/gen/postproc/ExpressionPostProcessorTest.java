package org.objectg.gen.postproc;

import org.junit.Ignore;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.fixtures.domain.Person;
import org.objectg.gen.GenerationContext;

import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 27.10.12
 */
public class ExpressionPostProcessorTest extends BaseObjectGTest implements ExpressionPostProcessor.Handler {
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

    @Test(expected = PostProcessingException.class)
    public void throwsWhenNotExistingProperty(){
		ExpressionPostProcessor expressionPostProcessor = new ExpressionPostProcessor(
				"notExistingProperty", this);

		Person person = ObjectG.unique(Person.class);
		expressionPostProcessor.process(new GenerationConfiguration(), person);
    }

	@Test(expected = PostProcessingException.class)
	public void throwsWhenEmptyPropertyExpression(){
		ExpressionPostProcessor expressionPostProcessor = new ExpressionPostProcessor(
				"", this);
		Person person = ObjectG.unique(Person.class);
		expressionPostProcessor.process(new GenerationConfiguration(), person);
	}


}
