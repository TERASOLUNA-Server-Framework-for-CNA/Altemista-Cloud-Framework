/**
 * 
 */
package cloud.altemista.fwk.it;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.controller.ExampleController;

/**
 * Tests the controller with methods that illustrates the performance module
 * @author NTT DATA
 */
public class ExampleIT extends AbstractIT {
	
	/**
	 * Simply tests the methods that illustrates the performance module;
	 * if this test fails, the results of PerformanceIT will be not reliable
	 */
	@Test
	public void testExampleController() {
		
		this.testMappings(ExampleController.MAPPING);
	}
}
