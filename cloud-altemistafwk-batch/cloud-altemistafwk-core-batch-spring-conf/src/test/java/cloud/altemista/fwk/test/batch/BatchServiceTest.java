package cloud.altemista.fwk.test.batch;

/*
 * #%L
 * cloud-altemistafwk batch: Spring Batch implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.batch.runtime.JobExecution;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import cloud.altemista.fwk.core.batch.BatchService;
import cloud.altemista.fwk.core.batch.model.BatchParameter;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Tests the main interface for the batch module,
 * using a synchronous executor to simplify the tests 
 * @author NTT DATA
 * @see cloud.altemista.fwk.test.batch.BatchServiceAsyncTest
 */
@ContextConfiguration({
	"classpath:/spring/cloud-altemistafwk-test-batch.xml",
	"classpath:/spring/cloud-altemistafwk-test-batch-sync.xml"
})
public class BatchServiceTest extends AbstractSpringContextTest {
	
	/** The name of the existing simple job */
	private static final String JOB_NAME = "sampleJob";
	
	/** The name of the existing infinite job */
	private static final String JOB_INFINITE_NAME = "sampleInfiniteJob";
	
	/** The name of the existing tasklet-based job */
	private static final String JOB_NAME_TASKLET = "sampleTaskletJob";
	
	/** The name of the existing job with incrementer */
	private static final String JOB_NAME_WITH_INCREMENTER = "sampleIncrementableJob";

	/** The names of the existing jobs*/
	private static final String[] JOB_NAMES = new String[]{
			JOB_NAME, JOB_INFINITE_NAME, JOB_NAME_TASKLET, JOB_NAME_WITH_INCREMENTER };
	
	/** Main interface for the batch module */
	@Autowired
	private BatchService service;
	
	/**
	 * Tests the countJobs method
	 */
	@Test
	public void testCountJobs() {
		
		Assert.assertEquals(JOB_NAMES.length, this.service.countJobs());
	}
	
	/**
	 * Test the listJobs method
	 */
	@Test
	public void testListJobs() {
		
		final List<String> list = this.service.listJobs(0, 10);
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.containsAll(Arrays.asList(JOB_NAMES)));
	}
	
	/**
	 * Test the listJobs method when the start is beyond the last element (should be clipped)
	 */
	@Test
	public void testListJobsBeyondLast() {
		
		final List<String> list = this.service.listJobs(1000, 10);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
	
	/**
	 * Test the listJobs method when the start is a negative number (should be clipped)
	 */
	@Test
	public void testListJobsNegativeStart() {
		
		final List<String> list = this.service.listJobs(-1000, 10);
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.containsAll(Arrays.asList(JOB_NAMES)));
	}
	
	/**
	 * Test the listJobs method when the count is zero (should return no elements)
	 */
	@Test
	public void testListJobsNoPageSize() {
		
		final List<String> list = this.service.listJobs(0, 0);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
	
	/**
	 * Test the listJobs method when the count is a negative number (should return no elements)
	 */
	@Test
	public void testListJobsNegativePageSize() {
		
		final List<String> list = this.service.listJobs(0, -10);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
	
	/**
	 * Test the isLaunchable method with an existing job name
	 */
	@Test
	public void testIsLaunchable() {
		
		Assert.assertTrue(this.service.isLaunchable(JOB_NAME));
		Assert.assertTrue(this.service.isLaunchable(JOB_NAME_TASKLET));
		Assert.assertTrue(this.service.isLaunchable(JOB_NAME_WITH_INCREMENTER));
	}
	
	/**
	 * Test the isLaunchable method with a wrong job name
	 */
	@Test
	public void testIsNotLaunchable() {
		
		Assert.assertFalse(this.service.isLaunchable("foo"));
	}
	
	/**
	 * Test the launch method 
	 */
	@Test
	public void testLaunch() {
		
		Assume.assumeTrue(this.service.isLaunchable(JOB_NAME));
		Long executionId = this.service.launch(JOB_NAME);
		Assert.assertNotNull(executionId);
	}
	
	/**
	 * Test the launch method 
	 */
	@Test
	public void testLaunchWithParameters() {
		
		Assume.assumeTrue(this.service.isLaunchable(JOB_NAME));
		Long executionId = this.service.launch(JOB_NAME, this.exampleBatchParameter());
		Assert.assertNotNull(executionId);
	}
	
	/**
	 * Test the launch method with a job that is not launchable 
	 */
	@Test
	public void testLaunchNotLaunchable() {
		
		Assume.assumeFalse(this.service.isLaunchable("foo"));
		
		// With no parameters
		Assert.assertNull(this.service.launch("foo"));
		
		// With parameters
		Assert.assertNull(this.service.launch("foo", this.exampleBatchParameter()));
	}
	
	/**
	 * Test the isLaunchableNextInstance method with a job that is incrementable
	 */
	@Test
	public void testIsLaunchableNextInstance() {
		
		Assert.assertTrue(this.service.isLaunchableNextInstance(JOB_NAME_WITH_INCREMENTER));
	}
	
	/**
	 * Test the isLaunchableNextInstance method with a job that is not incrementable
	 */
	@Test
	public void testIsNotLaunchableNextInstance() {
		
		Assert.assertFalse(this.service.isLaunchableNextInstance(JOB_NAME));
		Assert.assertFalse(this.service.isLaunchableNextInstance(JOB_NAME_TASKLET));
	}
	
	/**
	 * Test the launchNextInstance method
	 */
	@Test
	public void testLaunchNextInstance() {
		
		Assert.assertNull(this.service.launchNextInstance(JOB_NAME));
		
		Assert.assertNotNull(this.service.launchNextInstance(JOB_NAME_WITH_INCREMENTER));
	}
	
	//
	
	/**
	 * Tests the countJobInstances method
	 */
	@Test
	public void testCountJobInstances() {
		
		Assert.assertNotEquals(0, this.service.countJobInstances(JOB_NAME));
	}
	
	/**
	 * Tests the listJobInstances method
	 */
	@Test
	public void testListJobInstances() {
		
		final List<Long> list = this.service.listJobInstances(JOB_NAME, 0, 10);
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
	}
	
	//
	
	/**
	 * Tests the getExecutions method
	 */
	@Test
	public void testGetExecutions() {
		
		final List<Long> instanceIds = this.service.listJobInstances(JOB_NAME, 0, 10);
		Assume.assumeNotNull(instanceIds);
		Assume.assumeNotNull(instanceIds.isEmpty());
		Long instanceId = instanceIds.iterator().next();
		Assume.assumeNotNull(instanceId);
		
		List<JobExecution> executions = this.service.getExecutions(instanceId);
		Assert.assertNotNull(executions);
	}
	
	/**
	 * Tests the getRunningExecutions method
	 */
	@Test
	public void testGetRunningExecutions() {
		
		// No running executions
		final List<JobExecution> before = this.service.getRunningExecutions(JOB_NAME);
		Assert.assertNotNull(before);
		Assert.assertTrue(before.isEmpty());
		
		this.service.launch(JOB_NAME);
		
		// Also, no running executions as the launcher is synchronous
		final List<JobExecution> after = this.service.getRunningExecutions(JOB_NAME);
		Assert.assertNotNull(after);
		Assert.assertTrue(after.isEmpty());
	}
	
	/**
	 * Tests the getParameters method
	 */
	@Test
	public void testGetParameters() {
		
		Assume.assumeTrue(this.service.isLaunchable(JOB_NAME));
		BatchParameter<?> exampleParameter = this.exampleBatchParameter();
		Long executionId = this.service.launch(JOB_NAME, exampleParameter);
		Assume.assumeNotNull(executionId);
		
		// Asserts over the parameters
		List<BatchParameter<?>> parameters = this.service.getParameters(executionId);
		Assert.assertNotNull(parameters);
		Assert.assertFalse(parameters.isEmpty());
		BatchParameter<?> parameter = parameters.iterator().next();
		Assert.assertEquals(exampleParameter.getName(), parameter.getName());
		Assert.assertTrue(exampleParameter.getValue() instanceof Date);
		Assert.assertEquals(exampleParameter.isIdentifier(), parameter.isIdentifier());
	}
	
//	JobExecution abandon(long executionId);
//	
//	Long restart(long executionId);
//	
//	boolean stop(long executionId);
	
	/**
	 * Convenience method to create a batch parameter
	 * @return BatchParameter
	 */
	private BatchParameter<?> exampleBatchParameter() {
		
		// Uses a DateParameter to avoid test failures due "repeated execution")
		return new BatchParameter.DateParameter("bar", new Date(), true);
	}
}
