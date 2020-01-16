package org.altemista.cloudfwk.test.monitoring.impl.storage.wrapper;

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


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;
import org.altemista.cloudfwk.core.monitoring.impl.storage.wrapper.LoggerMonitoringStorageWrapperImpl;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests for the logger wrapper of the storage for prior executions of indicators
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-loggerWrapper.xml")
public class LoggerMonitoringStorageWrapperImplTest extends AbstractSpringContextTest {
	
	@Autowired
	private MonitoringService service;
	
	@Autowired
	private MonitoringStorage storage;

	@Test
	public void testSpringContext() {
		
		// (nothing to be explicitly tested; simpy checks the spring context set up)
		Assert.assertNotNull(this.service);
		Assert.assertNotNull(this.storage);
		Assert.assertTrue(this.storage instanceof LoggerMonitoringStorageWrapperImpl);
	}
}
