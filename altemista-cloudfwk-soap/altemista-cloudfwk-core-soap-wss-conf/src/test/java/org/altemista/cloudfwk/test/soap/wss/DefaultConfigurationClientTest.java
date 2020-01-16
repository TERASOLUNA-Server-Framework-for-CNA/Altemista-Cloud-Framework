package org.altemista.cloudfwk.test.soap.wss;

/*
 * #%L
 * altemista-cloud SOAP client with WS-Security CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Ensures the default configuration of the module is valid when there is at least one secured client
 * @author NTT DATA
 */
@ContextConfiguration(locations = { "classpath:spring/example-soap-client.xml" })
public class DefaultConfigurationClientTest extends AbstractSpringContextTest {

	@Autowired(required = false)
	private ExampleSecuredService securedClient;
	
	/**
	 * Ensures the default configuration of the module is valid when there is at least one secured client
	 */
	@Test
	public void testDefaultconfiguration() {
		
		// (ensures no runtime dependencies are missing)
		Assert.assertNotNull(this.securedClient);
	}
}
