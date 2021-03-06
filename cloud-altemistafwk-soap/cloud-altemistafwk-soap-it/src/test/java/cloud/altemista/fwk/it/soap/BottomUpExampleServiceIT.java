package cloud.altemista.fwk.it.soap;

/*
 * #%L
 * altemista-cloud SOAP client/server integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


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
import cloud.altemista.fwk.it.bean.BottomUpExampleResponse;

/**
 * Verifies the example bottom-up (contract-last) service is deployed
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core-soap.xml",
		"classpath:spring/it-app-soap-test.xml"
	})
public class BottomUpExampleServiceIT extends AbstractIT {

	/** Example bottom-up (contract-last) service client */
	@Autowired
	private BottomUpExampleService service;
	
	@Test
	public void testAutowired() {
		
		Assert.assertNotNull(this.service);
	}

	@Test
	public void testHelloAnonymous() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		BottomUpExampleRequest request = new BottomUpExampleRequest();
		request.setName("Foo");
		
		BottomUpExampleResponse response = this.service.helloAnonymous(request);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getGreeting());
		Assert.assertEquals("Hello, anonymous Foo!", response.getGreeting());
	}

	@Test
	public void testHelloAuthorizedUser() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		try {
			this.service.helloAuthorizedUser();
			Assert.fail("JaxWsSoapFaultException expected (Access is denied)");
			
		} catch (JaxWsSoapFaultException e) {
			Assert.assertEquals("Access is denied", e.getFaultString());
		}
	}

	@Test
	public void testHelloAuthenticatedUser() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		try {
			this.service.helloAuthenticatedUser();
			Assert.fail("JaxWsSoapFaultException expected (Access is denied)");
			
		} catch (JaxWsSoapFaultException e) {
			Assert.assertEquals("Access is denied", e.getFaultString());
		}
	}
}
