package org.altemista.cloudfwk.test.monitoring.impl.indicator;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

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
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.core.monitoring.impl.indicator.SocketIndicator;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

/**
 * Tests for the {@link SocketIndicator}
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-default.xml")
@DirtiesContext
public class SocketIndicatorTest extends AbstractSpringContextTest {
	
	@Autowired
	protected MonitoringService service;
	
	@Value("${mail.host:localhost}")
	private String host;
	
	@Value("${mail.port:25}")
	private int port;
	
	@Test
	public void testOk() {
		
		ServerOptions options = new ServerOptions();
		options.port = this.port;
		SmtpServer server = SmtpServerFactory.startServer(options);
		try {
			MonitoringInfo info = this.service.execute("socket");
			Assert.assertNotNull(info);
			Assert.assertEquals("socket", info.getIndicatorId());
			Assert.assertEquals(Status.OK, info.getStatus());
			
		} finally {
			server.stop();
		}
	}

	@Test
	public void testFailed() {
		
		Socket socket = null;
		try {
			// (ensures the socket is not open (e.g.: by another test running in parallel))
			try {
				socket = new Socket(this.host, this.port);
			} catch (IOException e) {
				Assume.assumeNoException(e);
			}
			Assume.assumeFalse(socket.isConnected());
			
			// Actual test
			MonitoringInfo info = this.service.execute("socket");
			Assert.assertNotNull(info);
			Assert.assertEquals("socket", info.getIndicatorId());
			Assert.assertEquals(Status.FAILED, info.getStatus());
			
		} finally {
			IOUtils.closeQuietly(socket);
		}
	}
	
}
