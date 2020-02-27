/**
 * 
 */
package cloud.altemista.fwk.example.batch;

/*
 * #%L
 * altemista-cloud batch: Spring Batch implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Example Tasklet for testing the batch module
 * @author NTT DATA
 */
@Component("exampleTasklet")
public class ExampleTasklet implements Tasklet {

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		// Do nothing
		return RepeatStatus.FINISHED;
	}

}
