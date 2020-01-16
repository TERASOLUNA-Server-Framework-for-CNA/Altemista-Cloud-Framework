/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud web application: JSF implementation integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class JsfIT extends AbstractIT {
	
	@Test
	public void testJsfPage() {
		
		// Returns /welcome.jsf 
		String responseBody = this.testUri("/welcome.jsf").getResponseBody();
		
		// This page have been rendered by JSF
		Assert.assertTrue(responseBody.contains("<section>"));
	}
	
	@Test
	public void testContainerRoot() {
		
		// Returns /welcome.jsf (<welcome-file>welcome.jsf</welcome-file>)
		String responseBody = this.testUri("/").getResponseBody();
		
		// This page have been rendered by JSF
		Assert.assertTrue(responseBody.contains("<section>"));
	}
	
	@Test
	public void testSpringRoot() {
		
		// Returns /welcome.jsf (*.welcome.file=forward:/welcome.jsf)
		String responseBody = this.testMapping("/").getResponseBody();
		
		// This page have been rendered by JSF
		Assert.assertTrue(responseBody.contains("<section>"));
	}
}
