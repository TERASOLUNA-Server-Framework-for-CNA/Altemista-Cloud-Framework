
package org.altemista.cloudfwk.test.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*
 * #%L
 * altemista-cloudfwk asynchronous and scheduled executions CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;
import org.altemista.cloudfwk.test.async.service.AsyncService;

@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-async.xml")
public class AsyncServiceTest extends AbstractSpringContextTest {
	
	@Autowired
	private AsyncService asyncService;
	
	@Test
	public void testInjection() {
		
		Assert.assertNotNull(this.asyncService);
	}
	
	/**
	 * Tests the @Async methods are actually asynchronous
	 */
	@Test
	public void testAsynchronousServiceIsAsynchronous() {
		
		Future<Boolean> future = this.asyncService.longProcessingTask();
		
		// The status is still pending
		Assert.assertFalse(future.isCancelled());
		Assert.assertFalse(future.isDone());
	}
	
	/**
	 * Tests the @Async methods do actually finish
	 * @throws InterruptedException
	 * @throws ExecutionException 
	 */
	@Test
	public void testAsynchronousSendIsSent() throws InterruptedException, ExecutionException {
		
		final long wait = 250L; // NOSONAR
		final long maxTotalWait = 2500L; // NOSONAR
		
		Future<Boolean> future = this.asyncService.longProcessingTask();
		
		// The status is still pending
		Assert.assertFalse(future.isCancelled());
		Assert.assertFalse(future.isDone());
	
		// Wait enough for the mail with attachment to be sent
		long totalWait = 0L;
		while (!(future.isCancelled()) && (!future.isDone())) {
			Thread.sleep(wait); // NOSONAR
			
			// (10 seconds)
			totalWait += wait;
			if (totalWait > maxTotalWait) { 
				Assert.fail("the asynchronous method did not finished after 10 seconds");
			}
		}
		
		// The mail has been sent
		Assert.assertFalse(future.isCancelled());
		Assert.assertTrue(future.isDone());
		
		Boolean retValue = future.get();
		Assert.assertTrue(retValue);
	}
	
}
