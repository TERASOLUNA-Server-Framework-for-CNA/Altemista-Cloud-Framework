/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud core integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.http.HttpStatus;
import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.controller.AliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class AliveIT extends AbstractIT {
	
	/**
	 * Validates the application is deployed
	 */
	@Test
	public void alive() {
		
		this.testMapping(AliveController.MAPPING);
	}
	
	/**
	 * If this test fails means the integration tests are not working as expected
	 */
	@Test
	public void notFound() {
		
		this.testMapping("/invalidMapping", HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void exception() {
		this.testMapping(AliveController.MAPPING + "/2", HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	
	@Test
	public void i18nDefaultLocale() {
		this.testMapping(AliveController.MAPPING + "/3");
	}
	
	@Test
	public void i18n() {
		this.testMapping(AliveController.MAPPING + "/4");
	}

	@Test
	public void checkCodeListBeans() {
		this.testMapping(AliveController.MAPPING + "/5");
	}
}
