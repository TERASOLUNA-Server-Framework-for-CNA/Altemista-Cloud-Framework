/**
 * 
 */
package org.altemista.it;

/*
 * #%L
 * altemista-cloud web integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.http.HttpStatus;
import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.controller.WebAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebAliveIT extends AbstractIT {
	
	/**
	 * Validates the application is deployed
	 */
	@Test
	public void testAlive() {
		
		this.testMapping(WebAliveController.MAPPING);
	}
	
	/**
	 * If this test fails means the integration tests are not working as expected
	 */
	@Test
	public void testNotFound() {
		
		this.testMapping("/invalidMapping", HttpStatus.SC_NOT_FOUND);
	}
}
