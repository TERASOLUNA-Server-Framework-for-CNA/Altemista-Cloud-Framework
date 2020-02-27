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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cloud.altemista.fwk.it.bean.BottomUpExampleRequest;
import cloud.altemista.fwk.it.bean.BottomUpExampleResponse;
import cloud.altemista.fwk.it.service.ExampleBusinessService;

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
	 * @see cloud.altemista.fwk.it.soap.BottomUpExampleService#helloAnonymous(cloud.altemista.fwk.it.bean.BottomUpExampleRequest)
	 */
	@Override
	public BottomUpExampleResponse helloAnonymous(BottomUpExampleRequest name) {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.hello(name.getName()));
		return response;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.soap.simple.BottomUpExampleService#helloAuthorizedUser()
	 */
	@Override
	public BottomUpExampleResponse helloAuthorizedUser() {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.helloAuthorizedUser());
		return response;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.soap.BottomUpExampleService#helloAuthenticatedUser()
	 */
	@Override
	public BottomUpExampleResponse helloAuthenticatedUser() {
		
		BottomUpExampleResponse response = new BottomUpExampleResponse();
		response.setGreeting(this.businessService.helloAuthenticatedUser());
		return response;
	}
}
