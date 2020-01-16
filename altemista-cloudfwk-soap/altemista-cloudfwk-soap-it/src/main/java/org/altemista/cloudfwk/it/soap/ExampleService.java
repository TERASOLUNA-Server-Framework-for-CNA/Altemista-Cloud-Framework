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


import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Minimal example bottom-up (contract-last) service definition
 * @author NTT DATA
 */
@WebService
public interface ExampleService {

	@WebMethod
	void doNothing();
}
