package cloud.altemista.fwk.it.soap;

/*
 * #%L
 * altemista-cloud SOAP client/server with WS-Security integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.jaxws.JaxWsSoapFaultException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.bean.BottomUpExampleRequest;
import cloud.altemista.fwk.it.soap.secured.BottomUpSecuredExampleService;

/**
 * Verifies the example bottom-up (contract-last) service with basic security is deployed
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core-soap.xml",
		"classpath:spring/it-app-soap-secured-test.xml"
	})
public class BottomUpSecuredExampleServiceIT extends AbstractIT {

	/** Example bottom-up (contract-last) service client with basic security */
	@Autowired
	private BottomUpSecuredExampleService service;
	
	@Test
	public void testAutowired() {
		
		Assert.assertNotNull(this.service);
	}

	@Test
	public void testHelloAnonymous() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		BottomUpExampleRequest request = new BottomUpExampleRequest();
		request.setName("Foo");
		
		try {
			this.service.helloAnonymous(request);
			Assert.fail("JaxWsSoapFaultException expected (...security error...)");
			
		} catch (JaxWsSoapFaultException e) {
			Assert.assertTrue(e.getFaultString(),
					StringUtils.containsIgnoreCase(e.getFaultString(), "security error"));
		}
	}

	@Test
	public void testHelloAuthorizedUser() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		try {
			this.service.helloAuthorizedUser();
			Assert.fail("JaxWsSoapFaultException expected (...security error...)");
			
		} catch (JaxWsSoapFaultException e) {
			Assert.assertTrue(e.getFaultString(),
					StringUtils.containsIgnoreCase(e.getFaultString(), "security error"));
		}
	}

	@Test
	public void testHelloAuthenticatedUser() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		try {
			this.service.helloAuthenticatedUser();
			Assert.fail("JaxWsSoapFaultException expected (...security error...)");
			
		} catch (JaxWsSoapFaultException e) {
			Assert.assertTrue(e.getFaultString(),
					StringUtils.containsIgnoreCase(e.getFaultString(), "security error"));
		}
	}
}
