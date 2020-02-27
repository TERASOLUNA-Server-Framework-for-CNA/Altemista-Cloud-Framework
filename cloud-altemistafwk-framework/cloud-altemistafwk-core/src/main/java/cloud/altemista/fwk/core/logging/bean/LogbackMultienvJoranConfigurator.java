package cloud.altemista.fwk.core.logging.bean;

/*-
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloud.altemista.fwk.core.util.EnvironmentUtil;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackMultienvJoranConfigurator extends ContextAwareBase implements Configurator{
	
	private String filename = "classpath:logback/logback.xml";
	
	private Logger LOGGER = LoggerFactory.getLogger(LogbackMultienvJoranConfigurator.class);

	@Override
	public void configure(LoggerContext loggerContext) {
		
		InputStream inputStream = EnvironmentUtil.getInputStream(this.filename);
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			
			// Clear any previous configuration
			loggerContext.reset();
			
			configurator.doConfigure(inputStream);
		} catch (JoranException e) {
			LOGGER.error(e.getMessage());
		} finally {
			// This also handles JoranExceptions
			StatusPrinter.printInCaseOfErrorsOrWarnings(context);
			IOUtils.closeQuietly(inputStream);
		}
		
	}

}
