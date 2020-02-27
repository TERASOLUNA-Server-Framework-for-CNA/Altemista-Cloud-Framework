/**
 * 
 */
package cloud.altemista.fwk.it.security.jwt;

/*
 * #%L
 * altemista-cloud web and Spring Boot-based applications security integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.security.jwt.controller.OAuth2SrvAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class OAuth2SrvIT extends AbstractIT {
	
	/**
	 * Validates the application has been successfully deployed
	 */
	@Test
	public void alive() {
		
		final String body = this.testMapping(OAuth2SrvAliveController.MAPPING).getResponseBody();
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, OAuth2SrvAliveController.ALIVE_MESSAGE));
	}
}
