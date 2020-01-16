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
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the default implementation of the main interface of Monitoring feature (MonitoringService)
 * when there is no MonitoringStorage (i.e.: get*() methods returns no information)
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-noStorage.xml")
public class MonitoringServiceImplNoStorageTest extends AbstractSpringContextTest {

	/** Number of indicator defined in configuration file */
	private static int EXPECTED_INDICATOR_COUNT = 1;

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
	public void testExecute() {

		MonitoringInfo info = this.service.execute("alive");
		Assert.assertNotNull(info);
		Assert.assertEquals("alive", info.getIndicatorId());
		Assert.assertEquals(Status.OK, info.getStatus());
	}
	
	@Test
	public void testServiceGetAll() {
		
		// (runs some executions)
		this.service.execute("alive");
		
		// Retireves the information
		Map<String, List<MonitoringInfo>> allInfo = this.service.getAll();
		Assert.assertNotNull(allInfo);
		Assert.assertEquals(EXPECTED_INDICATOR_COUNT, allInfo.size());
		
		// Checks the "alive" information has been stored
		Assert.assertTrue(allInfo.containsKey("alive"));
		Assert.assertNotNull(allInfo.get("alive"));
		Assert.assertTrue(allInfo.get("alive").isEmpty());
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
		
		// (runs some executions)
		this.service.execute("alive");
		
		// Retireves the information
		List<MonitoringInfo> list = this.service.get("alive");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
}
