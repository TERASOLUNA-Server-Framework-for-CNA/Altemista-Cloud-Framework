/**
 * 
 */
package cloud.altemista.fwk.it;

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


import org.junit.Test;
import cloud.altemista.fwk.it.controller.WebFlowAndTilesAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebFlowAndTilesAliveIT extends AbstractIT {
	
	/**
	 * Validates the application is deployed
	 */
	@Test
	public void alive() {
		
		this.testMapping(WebFlowAndTilesAliveController.MAPPING);
	}
}
