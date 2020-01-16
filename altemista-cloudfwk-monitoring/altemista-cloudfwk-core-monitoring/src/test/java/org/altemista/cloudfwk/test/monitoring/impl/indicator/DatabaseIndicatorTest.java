package org.altemista.cloudfwk.test.monitoring.impl.indicator;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.core.monitoring.impl.indicator.DatabaseIndicator;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests for the {@link DatabaseIndicator}
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-default.xml")
@DirtiesContext
public class DatabaseIndicatorTest extends AbstractSpringContextTest {
	
	@Autowired
	protected MonitoringService service;

	@Test
	public void testOk() {
		
		MonitoringInfo info = this.service.execute("database");
		Assert.assertNotNull(info);
		Assert.assertEquals("database", info.getIndicatorId());
		Assert.assertEquals(Status.OK, info.getStatus());
	}
}
