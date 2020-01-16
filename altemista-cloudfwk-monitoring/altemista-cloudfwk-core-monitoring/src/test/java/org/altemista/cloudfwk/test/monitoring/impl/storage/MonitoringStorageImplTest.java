package org.altemista.cloudfwk.test.monitoring.impl.storage;

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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests for the default implementation of the storage for prior executions of indicators (MonitoringStorage)
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-default.xml")
@DirtiesContext
public class MonitoringStorageImplTest extends AbstractSpringContextTest {
	
	@Autowired
	private MonitoringStorage storage;

	@Test
	public void testAddSanityChecks() {
		
		this.storage.add(null);
	}

	@Test
	public void testGetSanityChecks() {
		
		List<MonitoringInfo> list = this.storage.get(null);
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
		
		list = this.storage.get("");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
		
		list = this.storage.get(" ");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testAddThenGet() {
		
		MonitoringInfo expectedInfo = new MonitoringInfo("foo", "bar", Status.OK);
		
		this.storage.add(expectedInfo);
		
		List<MonitoringInfo> list = this.storage.get("foo");
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		
		MonitoringInfo info = list.iterator().next();
		Assert.assertEquals(expectedInfo.getIndicatorId(), info.getIndicatorId());
		Assert.assertEquals(expectedInfo.getExecTime(), info.getExecTime());
		Assert.assertEquals(expectedInfo.getResponseTime(), info.getResponseTime());
		Assert.assertEquals(expectedInfo.getStatus(), info.getStatus());
		Assert.assertEquals(expectedInfo.getMessage(), info.getMessage());
	}
}
