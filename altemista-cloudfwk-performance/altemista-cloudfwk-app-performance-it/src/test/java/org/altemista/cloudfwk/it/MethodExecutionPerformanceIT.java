/**
 * 
 */
package org.altemista.cloudfwk.it;

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


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.altemista.cloudfwk.it.controller.ExampleController;
import org.altemista.cloudfwk.it.controller.PerformanceController;

/**
 * Tests the performance module
 * @author NTT DATA
 */
public class MethodExecutionPerformanceIT extends AbstractIT {
	
	/**
	 * Clears the information of the MeasuresStorage before each test
	 */
	@Before
	public void clearMethodExecutionMeasuresStorage() {
		
		this.testMapping(PerformanceController.MAPPING + "/method/clear/json");
	}
	
	/**
	 * Tests a method that does not feed the MeasuresStorage
	 */
	@Test
	public void testMethodExecutionMeasuresStorage1() {
		
		final String methodExecutionMeasuresStorageMapping = PerformanceController.MAPPING + "/method/json";
		
		// Initial status
		final String emptyStorage = this.testMapping(methodExecutionMeasuresStorageMapping).getResponseBody();
		
		// This method does not feed the method execution measures storage
		this.testMapping(ExampleController.MAPPING + "/1");

		// Checks the response is the same
		Assert.assertEquals(emptyStorage, this.testMapping(methodExecutionMeasuresStorageMapping).getResponseBody());
	}
	
	/**
	 * Tests a method that does feed the MeasuresStorage
	 */
	@Test
	public void testMethodExecutionMeasuresStorage2() {
		
		final String methodExecutionMeasuresStorageMapping = PerformanceController.MAPPING + "/method/json";
		
		int currentLength = StringUtils.length(
				this.testMapping(methodExecutionMeasuresStorageMapping).getResponseBody());
		
		// These methods does feed the method measures storage
		for (String mapping : new String[]{"/2", "/3", "/4", "/5", "/6", "/7"}) {
			
			// Executes the method
			this.testMapping(ExampleController.MAPPING + mapping);
			
			// Checks the response is now longer
			int length = StringUtils.length(this.testMapping(methodExecutionMeasuresStorageMapping).getResponseBody());
			Assert.assertTrue("Expected longer response after invoking URI: " + ExampleController.MAPPING + mapping,
					length > currentLength);
			
			// (for the next iteration)
			currentLength = length;
		}
	
	}

}
