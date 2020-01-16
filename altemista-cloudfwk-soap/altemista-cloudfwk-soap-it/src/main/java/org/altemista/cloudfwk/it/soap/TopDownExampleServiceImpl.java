package org.altemista.cloudfwk.it.soap;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.altemista.cloudfwk.it.service.ExampleBusinessService;
import org.altemista.cloudfwk.it.topdownexample.TopDownExample;
import org.altemista.cloudfwk.it.topdownexample.TopDownExampleRequest;
import org.altemista.cloudfwk.it.topdownexample.TopDownExampleResponse;

/**
 * Example top-down (contract-first) service implementation
 * @author NTT DATA
 */
@Component
public class TopDownExampleServiceImpl implements TopDownExample {
	
	/** The example business service */
	@Autowired
	private ExampleBusinessService businessService;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.topdownexample.TopDownExample#helloAuthorizedUser()
	 */
	@Override
	public TopDownExampleResponse helloAuthorizedUser() {
		
		TopDownExampleResponse response = new TopDownExampleResponse();
		response.setGreeting(this.businessService.helloAuthorizedUser());
		return response;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.topdownexample.TopDownExample#helloAuthenticatedUser()
	 */
	@Override
	public TopDownExampleResponse helloAuthenticatedUser() {
		
		TopDownExampleResponse response = new TopDownExampleResponse();
		response.setGreeting(this.businessService.helloAuthenticatedUser());
		return response;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.topdownexample.TopDownExample#helloAnonymous(org.altemista.cloudfwk.it.topdownexample.TopDownExampleRequest)
	 */
	@Override
	public TopDownExampleResponse helloAnonymous(TopDownExampleRequest nameContainer) {
		
		TopDownExampleResponse response = new TopDownExampleResponse();
		response.setGreeting(this.businessService.hello(nameContainer.getName()));
		return response;
	}

}
