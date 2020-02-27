/**
 * 
 */
package cloud.altemista.fwk.it.service;

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


import cloud.altemista.fwk.it.model.Example;

/**
 * Spring Web Flow service example
 * @author NTT DATA
 */
public interface ExampleService {

	/**
	 * Spring Web Flow method example
	 * @param example variable
	 * @return value
	 */
	String exampleMethod(Example example);
}
