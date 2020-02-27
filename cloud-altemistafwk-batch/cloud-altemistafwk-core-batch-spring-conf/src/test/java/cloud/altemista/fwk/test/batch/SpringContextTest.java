/**
 * 
 */
package cloud.altemista.fwk.test.batch;

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
import cloud.altemista.fwk.core.batch.BatchService;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Tests the default configuration of the batch module 
 * @author NTT DATA
 */
@ContextConfiguration({
	"classpath:/spring/cloud-altemistafwk-test-batch.xml",
	"classpath:/spring/cloud-altemistafwk-test-batch-sync.xml"
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
