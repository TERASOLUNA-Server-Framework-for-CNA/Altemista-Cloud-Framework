package org.altemista.cloudfwk.test.soap;

/*
 * #%L
 * altemista-cloud SOAP client CONF
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
 * Ensures the default configuration of the module is valid when there is at least one client
 * @author NTT DATA
 */
@ContextConfiguration(locations = { "classpath:spring/example-soap-client.xml" })
public class DefaultConfigurationClientTest extends AbstractSpringContextTest {

	@Autowired(required = false)
	private ExampleService client;
	
	/**
	 * Ensures the default configuration of the module is valid when there is at least one client
	 */
	@Test
	public void testDefaultconfiguration() {
		
		// (ensures no runtime dependencies are missing)
		Assert.assertNotNull(this.client);
	}
}
