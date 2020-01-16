/**
 * 
 */
package org.altemista.cloudfwk.it.service.impl;

/*
 * #%L
 * altemista-cloud web application: Spring Web Flow + JSF integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.altemista.cloudfwk.it.model.Example;
import org.altemista.cloudfwk.it.service.ExampleService;

/**
 * Spring Web Flow service example
 * @author NTT DATA
 */
@Component("exampleService")
public class ExampleServiceImpl implements ExampleService {
	
	/** The SLF4J Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleServiceImpl.class);

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.ExampleService#confirmName(org.altemista.cloudfwk.it.model.Example)
	 */
	public String exampleMethod(Example example) {
		
		LOGGER.debug("Confirmed: {}", example.getName());
		
		return String.format("Congratulations, %s!", example.getName());
	}

}
