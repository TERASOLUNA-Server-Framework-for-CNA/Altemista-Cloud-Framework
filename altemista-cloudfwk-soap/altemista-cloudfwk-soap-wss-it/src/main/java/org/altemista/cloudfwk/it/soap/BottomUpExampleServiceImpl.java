package org.altemista.cloudfwk.it.soap;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.altemista.cloudfwk.it.bean.BottomUpExampleRequest;
import org.altemista.cloudfwk.it.bean.BottomUpExampleResponse;
import org.altemista.cloudfwk.it.service.ExampleBusinessService;

/**
 * Example bottom-up (contract-last) service implementation
 * @author NTT DATA
 */
@Component
public class BottomUpExampleServiceImpl implements BottomUpExampleService {
	
	/** The example business service */
	@Autowired
	private ExampleBusinessService businessService;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.soap.BottomUpExampleService#helloAnonymous(org.altemista.cloudfwk.it.bean.BottomUpExampleRequest)
	 */
	@Override
	public BottomUpExampleResponse helloAnonymous(BottomUpExampleRequest name) {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.hello(name.getName()));
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.soap.simple.BottomUpExampleService#helloAuthorizedUser()
	 */
	@Override
	public BottomUpExampleResponse helloAuthorizedUser() {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.helloAuthorizedUser());
		return response;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.soap.BottomUpExampleService#helloAuthenticatedUser()
	 */
	@Override
	public BottomUpExampleResponse helloAuthenticatedUser() {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.helloAuthenticatedUser());
		return response;
	}
}
