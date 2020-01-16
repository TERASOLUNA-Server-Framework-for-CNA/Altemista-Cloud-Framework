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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;

/**
 * Indicator to check the status of an HTTP connection.
 * @author NTT DATA
 */
public class HttpIndicator extends AbstractIndicator implements InitializingBean {
	
	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpIndicator.class);

	/** The URL to check */
	private String url;

	/** The maximum amount of time to wait when checking the URL address (in milliseconds). */
	private int timeout;
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.impl.indicator.AbstractIndicator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		if (StringUtils.isEmpty(this.url)) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "url", this.getId() });
		}
		if (this.timeout < 0) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "timeout", this.getId() });
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return String.format("HTTP %s", this.url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {
		
		MonitoringInfo info = new MonitoringInfo(this.getId(), this.getDescription());

		// Creates the URL object
		URL urlObject = null;
		try {
			urlObject = new URL(this.url);

		} catch (MalformedURLException e) {
			LOGGER.error(this.getDescription() + ": malformed URL", e);
			return info.update(Status.FAILED, this.getDescription() + ": malformed URL");
		}

		// Establishes the connection and checks the response
		HttpURLConnection connection = null;
		try {
			// Establishes the connection
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setConnectTimeout(this.timeout);

			// Checks the response
			final int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return info.update(Status.OK);
			}
			
			final String responseMessage = connection.getResponseMessage();
			return info.update(Status.FAILED,
					String.format("%s: %d. %s", this.getDescription(), responseCode, responseMessage));

		} catch (IOException e) {
			LOGGER.error(this.getDescription() + ": I/O exception", e);
			return info.update(Status.FAILED, this.getDescription() + ": I/O exception");

		} finally {
			IOUtils.close(connection);
		}
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
	
		this.url = url;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
	
		this.timeout = timeout;
	}
}
