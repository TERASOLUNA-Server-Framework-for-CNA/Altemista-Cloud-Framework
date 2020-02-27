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


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import cloud.altemista.fwk.it.controller.ExampleController;
import cloud.altemista.fwk.it.controller.PerformanceController;

/**
 * Tests the performance module
 * @author NTT DATA
 */
public class JdbcPerformanceIT extends AbstractIT {
	
	/**
	 * Clears the information of the MeasuresStorage before each test
	 */
	@Before
	public void clearJdbcMeasuresStorage() {
		
		this.testMapping(PerformanceController.MAPPING + "/jdbc/clear/json");
	}
	
	/**
	 * Tests a method that does not feed the MeasuresStorage
	 */
	@Test
	public void testJdbcMeasuresStorage1() {
		
		final String jdbcMeasuresStorageMapping = PerformanceController.MAPPING + "/jdbc/json";
		
		// Initial status
		final String emptyStorage = this.testMapping(jdbcMeasuresStorageMapping).getResponseBody();
		
		// These methods does not feed the JDBC measures storage
		this.testMapping(ExampleController.MAPPING + "/2");
		this.testMapping(ExampleController.MAPPING + "/3");
		this.testMapping(ExampleController.MAPPING + "/4");

		// Checks the response is the same
		String responseBody = this.testMapping(jdbcMeasuresStorageMapping).getResponseBody();
		Assert.assertEquals(emptyStorage, responseBody);
	}
	
	/**
	 * Tests a method that does feed the MeasuresStorage
	 */
	@Test
	public void testJdbcMeasuresStorage2() {
		
		final String jdbcMeasuresStorageMapping = PerformanceController.MAPPING + "/jdbc/json";
		
		int currentLength = StringUtils.length(this.testMapping(jdbcMeasuresStorageMapping).getResponseBody());
		
		// These methods does feed the method measures storage
		for (String mapping : new String[]{ "/5", "/6", "/7" }) {
			
			// Executes the method
			this.testMapping(ExampleController.MAPPING + mapping);
			
			// Checks the response is now longer
			int length = StringUtils.length(this.testMapping(jdbcMeasuresStorageMapping).getResponseBody());
			Assert.assertTrue("Expected longer response after invoking URI: " + ExampleController.MAPPING + mapping,
					length > currentLength);
			
			// (for the next iteration)
			currentLength = length;
		}
	}

}
