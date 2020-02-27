package cloud.altemista.fwk.test.async;

/*
 * #%L
 * altemista-cloud asynchronous and scheduled executions CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.async.AsyncStoragePolicy;
import cloud.altemista.fwk.test.AbstractSpringContextTest;
import cloud.altemista.fwk.test.async.service.AsyncService;
import cloud.altemista.fwk.test.async.task.TestTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/cloud-altemistafwk-test-async.xml",
		"classpath:spring/cloud-altemistafwk-test-async-storagePolicy.xml",
	})
public class AsyncStoragePolicyTest extends AbstractSpringContextTest {
	
	@Autowired
	@Qualifier("storagePolicy")
	private AsyncStoragePolicy<Boolean> storagePolicy;
	
	@Autowired
	private AsyncService asyncService;
	
	private static final String ASYNC_ID = "ASYNC_ID";
	
	@Test(timeout=10000)
	public void testAsync() {
		Future<Boolean> asyncFuture = asyncService.longProcessingTask();
		storagePolicy.put(ASYNC_ID,asyncFuture);
		Future<Boolean> future = storagePolicy.get(ASYNC_ID);
		while (!future.isDone()) { // NOSONAR
			// (empty loop for demonstration purposes only)
		}
		
		Assert.assertTrue(future.isDone());	
	}
	
	@Test(timeout=10000)
	public void testScheduler() {
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		
		@SuppressWarnings("unchecked")
		ScheduledFuture<Boolean> schedFuture = (ScheduledFuture<Boolean>)
				service.scheduleAtFixedRate(new TestTask(), 2, 2, TimeUnit.SECONDS);
		
		Assert.assertTrue(!schedFuture.isDone());	
	}

}
