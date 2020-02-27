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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.it.topdownexample.TopDownExample;
import cloud.altemista.fwk.it.topdownexample.TopDownExampleRequest;
import cloud.altemista.fwk.it.topdownexample.TopDownExampleResponse;

/**
 * Verifies the example top-down (contract-first) service is deployed
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core-soap.xml",
		"classpath:spring/it-app-soap-test.xml"
	})
public class TopDownExampleServiceIT {

	/** Example top-down (contract-first) service client */
	@Autowired
	private TopDownExample service;
	
	@Test
	public void testAutowired() {
		
		Assert.assertNotNull(this.service);
	}

	@Test
	public void testHelloAnonymous() throws Exception {
		
		Assume.assumeNotNull(this.service);
		
		TopDownExampleRequest request = new TopDownExampleRequest();
		request.setName("Bar");
		
		TopDownExampleResponse response = this.service.helloAnonymous(request);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getGreeting());
		Assert.assertEquals("Hello, anonymous Bar!", response.getGreeting());
	}
}
