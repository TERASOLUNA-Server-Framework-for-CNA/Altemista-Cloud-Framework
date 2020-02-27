package cloud.altemista.fwk.test.ws.service.impl;

/*
 * #%L
 * altemista-cloud SOAP server with WS-Security CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.jws.WebService;

import org.springframework.stereotype.Service;
import cloud.altemista.fwk.test.ws.service.SecuredExampleService;
//import cloud.altemista.fwk.web.soap.service.SecuredWS;

//tag::example[]
/**
 * Web service implementation
 */
@WebService(serviceName = "SecuredExampleService", endpointInterface = "cloud.altemista.fwk.test.ws.service.SecuredExampleService")
//@SecuredWS
@Service
public class SecuredExampleServiceImpl implements SecuredExampleService {

	/** The "Hello, world!" String */
	private static final String HELLO_WORLD = "Hello, world!";

	@Override
	public String saySecuredHi() {
		return HELLO_WORLD;
	}
}
// end::example[]
