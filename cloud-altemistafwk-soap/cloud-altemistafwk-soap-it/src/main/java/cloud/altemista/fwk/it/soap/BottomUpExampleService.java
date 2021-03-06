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


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import cloud.altemista.fwk.it.bean.BottomUpExampleRequest;
import cloud.altemista.fwk.it.bean.BottomUpExampleResponse;

/**
 * Example bottom-up (contract-last) service definition
 * @author NTT DATA
 */
@WebService
public interface BottomUpExampleService {

	@WebMethod
	@WebResult(name = "greetingContainer")
	BottomUpExampleResponse helloAnonymous(@WebParam(name = "nameContainer") BottomUpExampleRequest name);

	@WebMethod
	@WebResult(name = "greetingContainer")
	BottomUpExampleResponse helloAuthorizedUser();

	@WebMethod
	@WebResult(name = "greetingContainer")
	BottomUpExampleResponse helloAuthenticatedUser();
}
