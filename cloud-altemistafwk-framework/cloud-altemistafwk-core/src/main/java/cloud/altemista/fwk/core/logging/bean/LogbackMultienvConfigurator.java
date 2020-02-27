package cloud.altemista.fwk.core.logging.bean;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import cloud.altemista.fwk.core.util.EnvironmentUtil;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Environment dependent Logback configurator.
 * Based on a default configuration file name (e.g.: classpath:logback/logback.xml),
 * tries to load first an specific environment configuration file (e.g.: classpath:logback/logback.dev.xml).
 * @author NTT DATA
 */
public class LogbackMultienvConfigurator implements InitializingBean, DisposableBean {
	
	/** The SLF4J Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LogbackMultienvConfigurator.class);
	
	/** The file name used to configure Logback (e.g.: classpath:logback/logback.xml) */
	private String filename;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws JoranException {
		
		if (!StringUtils.hasText(this.filename)) {
			LOGGER.warn("Logback configuration filename not set");
			return;
		}
		
		LoggerContext context = this.getLoggerContext();
		if (context == null) {
			return;
		}
		
		// Gets the specific configuration file to be used
		InputStream inputStream = EnvironmentUtil.getInputStream(this.filename);
		if (inputStream == null) { // NOSONAR
			LOGGER.warn("Could not read Logback configuration file with filename: " + this.filename);
			return;
		}

		// Reset and configure again Logback using the specific files
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			
			// Clear any previous configuration
			context.reset();
			
			configurator.doConfigure(inputStream);
			
		} finally {
			// This also handles JoranExceptions
			StatusPrinter.printInCaseOfErrorsOrWarnings(context);
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() {
		
		// Release the resources
		LoggerContext context = this.getLoggerContext();
		if (context != null) {
			context.stop();
		}
		
	}
	
	/**
	 * Returns the ILoggerFactory of SLF4J when its actually bound to Logback
	 * @return LoggerContext, or null if SLF4J is not bound to Logback
	 */
	private LoggerContext getLoggerContext() {
		
		// Checks if SLF4J is actually bound to Logback
		ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
		if (iLoggerFactory instanceof LoggerContext) {
			return (LoggerContext) iLoggerFactory;
		}
		
		LOGGER.warn("SLF4J is not bound to Logback: " + iLoggerFactory.getClass().getName());
		return null;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
