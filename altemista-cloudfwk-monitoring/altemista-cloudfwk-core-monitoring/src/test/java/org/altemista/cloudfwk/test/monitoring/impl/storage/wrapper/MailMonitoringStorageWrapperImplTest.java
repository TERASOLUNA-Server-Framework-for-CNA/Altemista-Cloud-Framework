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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;
import org.altemista.cloudfwk.core.monitoring.impl.storage.wrapper.MailMonitoringStorageWrapperImpl;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

/**
 * Tests for the mail-on-error wrapper of the storage for prior executions of indicators
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-mailWrapper.xml")
public class MailMonitoringStorageWrapperImplTest extends AbstractSpringContextTest {
	
	@Value("${mail.port:25}")
	private int port;
	
	@Autowired
	private MonitoringService service;
	
	@Autowired
	private MonitoringStorage storage;

	@Test
	public void testSpringContext() {
		
		// (nothing to be explicitly tested; simpy checks the spring context set up)
		Assert.assertNotNull(this.service);
		Assert.assertNotNull(this.storage);
		Assert.assertTrue(this.storage instanceof MailMonitoringStorageWrapperImpl);
	}

	@Test
	public void testOk() {

		SmtpServer server = this.startServer();
		try {
			int expectedMailCount = server.getEmailCount();

			// AliveIndicator (ok)
			MonitoringInfo info = this.service.execute("alive");
			Assert.assertNotNull(info);
			Assert.assertEquals(Status.OK, info.getStatus());

			// No mail has been sent
			Assert.assertEquals(expectedMailCount, server.getEmailCount());

		} finally {
			server.stop();
		}
	}

	@Test
	public void testFailed() {

		SmtpServer server = this.startServer();
		try {
			int unexpectedMailCount = server.getEmailCount();

			// HttpIndicator (failed)
			MonitoringInfo info = this.service.execute("server1");
			Assert.assertNotNull(info);
			Assert.assertEquals(Status.FAILED, info.getStatus());

			// At least one mail has been sent
			Assert.assertNotEquals(unexpectedMailCount, server.getEmailCount());

		} finally {
			server.stop();
		}
	}

	/**
	 * Convenience method to start the fake SMTP server in each test
	 * @return the started SmtpServer
	 */
	protected SmtpServer startServer() {

		// Starts the fake SMTP server in the specified port
		ServerOptions options = new ServerOptions();
		options.port = this.port;
		return SmtpServerFactory.startServer(options);
	}

}
