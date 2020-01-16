package org.altemista.cloudfwk.core.logging;

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


import java.text.MessageFormat;

//tag::definition[]
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerExample {
	
	/** The SLF4J Logger. */
	private static final Logger LOGGER = // <1>
			LoggerFactory.getLogger(LoggerExample.class); // <2>
	// ...
	// end::definition[]
	
	public void loggerExample(String string) {
		
		// tag::basic[]
		LOGGER.info("Entry point"); // <1>
		
		if (LOGGER.isDebugEnabled()) { // <2>
			LOGGER.debug(this.reallyHeavyCalculation());
		}
		
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException exception) {
			LOGGER.error("Error parsing the argument: " + string, exception); // <3>
		}
		// end::basic[]

		Integer integer = 0;
		
		// tag::advanced[]
		LOGGER.info(string + " is " + integer); // <1>
		LOGGER.info(String.format("%s is %d", string, integer)); // <2>
		LOGGER.info(MessageFormat.format("{0} is {1}", string, integer)); // <3>
		LOGGER.info("{} is {}", string, integer); // (preferred) // <4>
		// end::advanced[]
		
	}
	
	private String reallyHeavyCalculation() {
		return "";
	}

}
