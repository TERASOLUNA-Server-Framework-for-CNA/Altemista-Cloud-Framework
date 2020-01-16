package org.altemista.cloudfwk.test.soap.wss;

/*
 * #%L
 * altemista-cloud SOAP client with WS-Security CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.annotation.security.PermitAll;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Example service interface with security annotations
 * @author NTT DATA
 */
@WebService
public interface ExampleSecuredService {
	
	@WebMethod
	@PermitAll
	void exampleMethod();

}
