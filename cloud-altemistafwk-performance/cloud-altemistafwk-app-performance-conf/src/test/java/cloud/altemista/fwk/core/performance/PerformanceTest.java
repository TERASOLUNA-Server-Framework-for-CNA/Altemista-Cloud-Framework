
package cloud.altemista.fwk.core.performance;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.performance.aop.PerformanceAspect;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;
import cloud.altemista.fwk.demo.performance.FirstTestService;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * The Class PerformanceTest.
 * 
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/cloud-altemistafwk-test-performance.xml")
public class PerformanceTest extends AbstractSpringContextTest {

	/** The aspect PerformanceAspect */
	@Autowired
	private PerformanceAspect aspect;

	/** The storage MeasuresStorage */
	@Autowired
	@Qualifier("methodExecutionMeasuresStorage")
	private MeasuresStorage storage;
	
	/** An example class to test performance aspect. */
	@Autowired
	private FirstTestService firstService;
	
	/**
	 * Nothing to explicitly test here; just that the default module configuration is not incomplete
	 */
	@Test
	public void testApplicationContext() {
		
		Assert.assertNotNull(this.aspect);
		Assert.assertNotNull(this.storage);
		Assert.assertNotNull(this.firstService);
	}
	
	/**
	 * Test some executions
	 */
	@Test
	public void testPerformanceAspect() {
		
		// (fills the storage with some example executions)
		for (int i = 0, n = 10; i < n; i++) {
			this.firstService.exampleMethod();
			this.firstService.exampleMethodWithHiddenValues("NOT VISIBLE", "VISIBLE");
		}
		
		// Tests the storage
		List<MeasuredTask> list = this.storage.get();
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		
		// Ensures the longest executions are shown first
		long totalTimeMillis = Long.MAX_VALUE;
		for (MeasuredTask task : list) {
			Assert.assertTrue(task.getTotalTimeMillis() <= totalTimeMillis);
			totalTimeMillis = task.getTotalTimeMillis();
		}
		
		// Ensures no hidden value is shown in the task description
		for (MeasuredTask task : list) {
			Assert.assertFalse(task.getDescription().contains("NOT VISIBLE"));
		}
	}
}
