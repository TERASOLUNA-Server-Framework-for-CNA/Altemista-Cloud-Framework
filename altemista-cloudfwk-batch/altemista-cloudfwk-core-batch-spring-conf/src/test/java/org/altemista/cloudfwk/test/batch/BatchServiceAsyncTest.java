package org.altemista.cloudfwk.test.batch;

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


import java.util.Date;
import java.util.List;

import javax.batch.runtime.JobExecution;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.core.batch.BatchService;
import org.altemista.cloudfwk.core.batch.model.BatchParameter;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the main interface for the batch module,
 * completing the tests done in BatchServiceAsyncTest but using the default asynchronous executor
 * @author NTT DATA
 * @see org.altemista.cloudfwk.test.batch.BatchServiceTest
 */
@ContextConfiguration({ "classpath:/spring/altemista-cloudfwk-test-batch.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatchServiceAsyncTest extends AbstractSpringContextTest {
	
	/**
	 * The ignore message when the exception is caused by Spring Batch issue 1767
	 * @see https://jira.spring.io/browse/BATCH-1767
	 */
	private static final String SPRING_BATCH_ISSUE = "Ignored due Spring Batch issue";

	/** The name of the existing infinite job */
	private static final String JOB_INFINITE_NAME = "sampleInfiniteJob";
	
	/** Main interface for the batch module */
	@Autowired
	private BatchService service;
	
	/**
	 * Ensures all the inifinite jobs are stopped before going to the next test
	 */
	@After
	public void after() {
		
		List<JobExecution> executions = this.service.getRunningExecutions(JOB_INFINITE_NAME);
		for (JobExecution execution : executions) {
			try {
				this.service.stop(execution.getExecutionId());
				
			} catch (OptimisticLockingFailureException ignored) {
				Assume.assumeNoException(SPRING_BATCH_ISSUE, ignored);
			}
		}
	}
	
	//
	
	/**
	 * Tests the getRunningExecutions method
	 */
	@Test
	public void testGetRunningExecutions() {
		
		Long executionId = this.service.launch(JOB_INFINITE_NAME, this.exampleBatchParameter());
		Assume.assumeNotNull(executionId);
		
		// It is expected the execution is still running
		final List<JobExecution> executions = this.service.getRunningExecutions(JOB_INFINITE_NAME);
		Assert.assertNotNull(executions);
		Assert.assertFalse(executions.isEmpty());
		for (JobExecution execution : executions) {
			if (execution.getExecutionId() == executionId.longValue()) {
				return;
			}
		}
		// (if no execution matched the executionId, then fails)
		Assert.fail("ExecutionId " + executionId + " not in running executions");
	}
	
	/**
	 * Tests the stop method
	 */
	@Test
	public void testStop() {
		
		Long executionId = this.service.launch(JOB_INFINITE_NAME, this.exampleBatchParameter());
		Assume.assumeNotNull(executionId);
		
		try {
			// A running execution can be stopped
			Assert.assertTrue(this.service.stop(executionId));
			
			// An already stopped execution can not be stopped (but does not throw exception)
			Assert.assertFalse(this.service.stop(executionId));
			
		} catch (OptimisticLockingFailureException ignored) {
			Assume.assumeNoException(SPRING_BATCH_ISSUE, ignored);
		}
	}
	
	/**
	 * Tests the restart method
	 */
	@Test
	public void testRestart() {
		
		Long executionId = this.service.launch(JOB_INFINITE_NAME, this.exampleBatchParameter());
		Assume.assumeNotNull(executionId);
		
		try {
			// An already running execution can not be restarted (but does not throw exception)
			Assert.assertNull(this.service.restart(executionId));
			
			Assume.assumeTrue(this.service.stop(executionId));
			
			try {
				Thread.sleep(500); // NOSONAR
			} catch (InterruptedException e) {
				// (ignored)
			}
			
			// An stopped execution can be restarted
			Assert.assertNotNull(this.service.restart(executionId));
			
		} catch (OptimisticLockingFailureException ignored) {
			Assume.assumeNoException(SPRING_BATCH_ISSUE, ignored);
		}
	}
	
	/**
	 * Tests the abandon method
	 */
	@Test
	public void testAbandon() {
		
		Long executionId = this.service.launch(JOB_INFINITE_NAME, this.exampleBatchParameter());
		Assume.assumeNotNull(executionId);
		
		try {
			// An already running execution can not be abandoned (but does not throw exception)
			Assert.assertNull(this.service.abandon(executionId));
			
			Assume.assumeTrue(this.service.stop(executionId));
			
			// An stopped execution can be abandoned
			Assert.assertNotNull(this.service.abandon(executionId));
			
		} catch (OptimisticLockingFailureException ignored) {
			Assume.assumeNoException(SPRING_BATCH_ISSUE, ignored);
		}
	}
	
	/**
	 * Convenience method to create a batch parameter
	 * @return BatchParameter
	 */
	private BatchParameter<?> exampleBatchParameter() {
		
		// Uses a DateParameter to avoid test failures due "repeated execution")
		return new BatchParameter.DateParameter("bar", new Date(), true);
	}
}
