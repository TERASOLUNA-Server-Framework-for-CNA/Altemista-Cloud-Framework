/**
 * 
 */
package org.altemista.cloudfwk.it;

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


import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.controller.WebFlowAndJsfAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebFlowAndJsfAliveIT extends AbstractIT {
	
	/**
	 * Validates the application is deployed
	 */
	@Test
	public void alive() {
		
		this.testMapping(WebFlowAndJsfAliveController.MAPPING);
	}
}
