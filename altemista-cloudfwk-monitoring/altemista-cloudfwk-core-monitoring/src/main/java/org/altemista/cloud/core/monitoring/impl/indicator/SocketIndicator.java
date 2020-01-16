
package org.altemista.cloudfwk.core.monitoring.impl.indicator;

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

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;

/**
 * Indicator to check the status of a socket address (e.g.: a SMTP server).
 * @author NTT DATA
 */
public class SocketIndicator extends AbstractIndicator implements InitializingBean {
	
	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketIndicator.class);

	/** The host to check */
	private String host;

	/** The port to check */
	private int port;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.impl.indicator.AbstractIndicator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (StringUtils.isEmpty(this.host)) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "host", this.getId() });
		}
		if (this.port <= 0) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "port", this.getId() });
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return String.format("socket %s:%d", this.host, this.port);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {

		MonitoringInfo info = new MonitoringInfo(this.getId(), this.getDescription());
		
		// Creates the socket and checks the connection
		Socket socket = null;
		try {
			// Creates the socket
			socket = new Socket(this.host, this.port);
			
			// Checks the connection
			return socket.isConnected()
					? info.update(Status.OK)
					: info.update(Status.FAILED, this.getDescription() + ": not connected");

		} catch (IOException e) {
			LOGGER.error(this.getDescription() + ": I/O exception", e);
			return info.update(Status.FAILED, this.getDescription() + ": unable to create scoket");

		} finally {
			IOUtils.closeQuietly(socket);
		}
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
	
		this.host = host;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
	
		this.port = port;
	}
}
