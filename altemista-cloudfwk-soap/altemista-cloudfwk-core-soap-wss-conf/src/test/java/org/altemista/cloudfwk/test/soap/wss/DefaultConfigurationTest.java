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


import org.apache.cxf.Bus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Ensures the default configuration of the module is valid
 * @author NTT DATA
 */
public class DefaultConfigurationTest extends AbstractSpringContextTest {

	@Autowired(required = false)
	private Bus cxfBus;
	
	/**
	 * Ensures the default configuration of the module is valid
	 */
	@Test
	public void testDefaultconfiguration() {
		
		// The Apache CXF bus must have been initialized as a Spring bean
		Assert.assertNotNull(this.cxfBus);
	}
}
