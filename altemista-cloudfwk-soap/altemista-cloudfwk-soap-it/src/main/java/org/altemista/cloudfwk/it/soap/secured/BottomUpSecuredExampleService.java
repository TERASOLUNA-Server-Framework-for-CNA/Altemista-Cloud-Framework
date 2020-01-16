package org.altemista.cloudfwk.it.soap.secured;

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


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.altemista.cloudfwk.it.bean.BottomUpExampleRequest;
import org.altemista.cloudfwk.it.bean.BottomUpExampleResponse;

/**
 * Example bottom-up (contract-last) service definition with basic security and Spring Securtiy authorization
 * @author NTT DATA
 */
@WebService
public interface BottomUpSecuredExampleService {

	@WebMethod
	@WebResult(name = "greetingContainer")
	BottomUpExampleResponse helloAnonymous(@WebParam(name = "nameContainer") BottomUpExampleRequest name);

	@WebMethod
	@WebResult(name = "greetingContainer")
	@PreAuthorize("isAuthenticated()")
	BottomUpExampleResponse helloAuthenticatedUser();

	@WebMethod
	@WebResult(name = "greetingContainer")
	@PreAuthorize("hasRole('USER')")
	BottomUpExampleResponse helloAuthorizedUser();
}
