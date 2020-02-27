/**
 * 
 */
package cloud.altemista.fwk.it.service.impl;

/*
 * #%L
 * altemista-cloud web application: Spring Web Flow + Apache Tiles integration tests
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
import cloud.altemista.fwk.it.model.Example;
import cloud.altemista.fwk.it.service.ExampleService;

/**
 * Spring Web Flow service example
 * @author NTT DATA
 */
@Component("exampleService")
public class ExampleServiceImpl implements ExampleService {
	
	/** The SLF4J Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleServiceImpl.class);

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.ExampleService#confirmName(cloud.altemista.fwk.it.model.Example)
	 */
	public String exampleMethod(Example example) {
		
		LOGGER.debug("Confirmed: {}", example.getName());
		
		return String.format("Congratulations, %s!", example.getName());
	}

}
