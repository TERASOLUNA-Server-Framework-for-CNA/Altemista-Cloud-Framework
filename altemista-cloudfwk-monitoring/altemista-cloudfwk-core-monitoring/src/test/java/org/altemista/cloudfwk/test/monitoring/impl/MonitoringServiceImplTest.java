package org.altemista.cloudfwk.test.monitoring.impl;

/*
 * #%L
 * altemista-cloud monitoring
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
//import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the default implementation of the main interface of Monitoring feature (MonitoringService)
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-default.xml")
@DirtiesContext
public class MonitoringServiceImplTest extends AbstractSpringContextTest {

	/** Number of indicator defined in configuration file */
	private static int EXPECTED_INDICATOR_COUNT = 7;

	@Autowired
	protected MonitoringService service;
	
	@Test
	public void testExecuteAll() {
		
		List<MonitoringInfo> list = this.service.executeAll();
		Assert.assertNotNull(list);
		Assert.assertEquals(EXPECTED_INDICATOR_COUNT, list.size());

		// Checks every element retrieved is correct.
		for (MonitoringInfo info : list) {
			Assert.assertNotNull(info);
			Assert.assertTrue(StringUtils.isNotBlank(info.getIndicatorId()));
			Assert.assertNotNull(info.getStatus());
			Assert.assertTrue(StringUtils.isNotBlank(info.getMessage()));
		}
	}

	@Test
	public void testExecuteSanityChecks() {

		Assert.assertNull(this.service.execute(null));
		Assert.assertNull(this.service.execute(""));
		Assert.assertNull(this.service.execute("   "));
		
		// (unknown indicator)
		Assert.assertNull(this.service.execute("foo"));
	}

	@Test
	public void testExecuteUnknownIndicator() {
		
		// (unknown indicator)
		Assert.assertNull(this.service.execute("foo"));
	}

	@Test
	public void testExecuteOk() {

		MonitoringInfo info = this.service.execute("alive");
		Assert.assertNotNull(info);
		Assert.assertEquals("alive", info.getIndicatorId());
		Assert.assertEquals(Status.OK, info.getStatus());
	}

	@Test
	public void testExecuteFailed() {

		MonitoringInfo info = this.service.execute("server1");
		Assert.assertNotNull(info);
		Assert.assertEquals("server1", info.getIndicatorId());
		Assert.assertEquals(Status.FAILED, info.getStatus());
	}
	
	@Test
	public void testServiceGetAll() {
		
		// (populates the storage)
		MonitoringInfo expectedAlive = this.service.execute("alive");
		Assert.assertNotNull(expectedAlive);
		Assert.assertEquals("alive", expectedAlive.getIndicatorId());
		Assert.assertEquals(Status.OK, expectedAlive.getStatus());
		
		MonitoringInfo expectedServer1 = this.service.execute("server1");
		Assert.assertNotNull(expectedServer1);
		Assert.assertEquals("server1", expectedServer1.getIndicatorId());
		Assert.assertEquals(Status.FAILED, expectedServer1.getStatus());
		
		// Retireves the information
		Map<String, List<MonitoringInfo>> allInfo = this.service.getAll();
		Assert.assertNotNull(allInfo);
		Assert.assertEquals(EXPECTED_INDICATOR_COUNT, allInfo.size());
		
		// Checks the "alive" information has been stored
		Assert.assertTrue(allInfo.containsKey("alive"));
		Assert.assertNotNull(allInfo.get("alive"));
		Assert.assertFalse(allInfo.get("alive").isEmpty());
		MonitoringInfo alive = allInfo.get("alive").iterator().next();
		
		// Checks the "alive" information is the same as in the execution
		Assert.assertEquals(expectedAlive.getIndicatorId(), alive.getIndicatorId());
		Assert.assertEquals(expectedAlive.getExecTime(), alive.getExecTime());
		Assert.assertEquals(expectedAlive.getResponseTime(), alive.getResponseTime());
		Assert.assertEquals(expectedAlive.getStatus(), alive.getStatus());
		Assert.assertEquals(expectedAlive.getMessage(), alive.getMessage());
		
		// Checks the "server1" information has been stored
		Assert.assertTrue(allInfo.containsKey("server1"));
		Assert.assertNotNull(allInfo.get("server1"));
		Assert.assertFalse(allInfo.get("server1").isEmpty());
		MonitoringInfo server1 = allInfo.get("server1").iterator().next();
		
		// Checks the "server1" information is the same as in the execution
		Assert.assertEquals(expectedServer1.getIndicatorId(), server1.getIndicatorId());
		Assert.assertEquals(expectedServer1.getExecTime(), server1.getExecTime());
		Assert.assertEquals(expectedServer1.getResponseTime(), server1.getResponseTime());
		Assert.assertEquals(expectedServer1.getStatus(), server1.getStatus());
		Assert.assertEquals(expectedServer1.getMessage(), server1.getMessage());
	}

	@Test
	public void testGetUnknownIndicator() {
		
		// (unknown indicator)
		List<MonitoringInfo> list = this.service.get("foo");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	public void testGet() {

		// (populates the storage)
		MonitoringInfo expectedAlive = this.service.execute("alive");
		Assert.assertNotNull(expectedAlive);
		Assert.assertEquals("alive", expectedAlive.getIndicatorId());
		Assert.assertEquals(Status.OK, expectedAlive.getStatus());
		
		// Retireves the information
		List<MonitoringInfo> list = this.service.get("alive");
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		MonitoringInfo alive = list.iterator().next();
		
		// Checks the "alive" information is the same as in the execution
		Assert.assertEquals(expectedAlive.getIndicatorId(), alive.getIndicatorId());
		Assert.assertEquals(expectedAlive.getExecTime(), alive.getExecTime());
		Assert.assertEquals(expectedAlive.getResponseTime(), alive.getResponseTime());
		Assert.assertEquals(expectedAlive.getStatus(), alive.getStatus());
		Assert.assertEquals(expectedAlive.getMessage(), alive.getMessage());
		
		// Runs a new execution
		try {
			Thread.sleep(100L); // NOSONAR
		} catch (InterruptedException ignored) { // NOSONAR
			// (this exception is silently ignored)
		}
		this.service.execute("alive");
		
		// Retireves the new information
		list = this.service.get("alive");
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		alive = list.iterator().next();
		
		// Checks it is a different execution
		Assert.assertEquals(expectedAlive.getIndicatorId(), alive.getIndicatorId());
		Assert.assertNotEquals(expectedAlive.getExecTime(), alive.getExecTime());
	}
}
