/**
 * 
 */
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


import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.core.batch.BatchService;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the default configuration of the batch module 
 * @author NTT DATA
 */
@ContextConfiguration({
	"classpath:/spring/altemista-cloudfwk-test-batch.xml",
	"classpath:/spring/altemista-cloudfwk-test-batch-sync.xml"
})
public class SpringContextTest extends AbstractSpringContextTest {
	
	/** Main interface for the batch module */
	@Autowired
	private BatchService service;
	
	/**
	 * Tests the default configuration of the batch module 
	 */
	@Test
	public void testSpringContext() {
		
		Assert.assertNotNull(this.service);
	}
	
	/**
	 * Tests the automatic loading of job definitions 
	 * @throws NoSuchJobException if fails
	 */
	@Test
	public void testAutomaticJobRegistrar() throws NoSuchJobException {
		
		// "sampleJob" is defined in test-jobs.xml
		Assert.assertNotNull(this.service.isLaunchable("sampleJob"));
	}

}
