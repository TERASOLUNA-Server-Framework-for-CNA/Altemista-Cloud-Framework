
package org.altemista.cloudfwk.test.ws.service;

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

import org.springframework.security.access.annotation.Secured;

//tag::example[]
/**
 * Example service for WS integration tests
 * 
 * @author NTT DATA
 */
@WebService
public interface SecuredExampleService {

	@Secured("ROLE_ADMIN")
	String saySecuredHi();
}
// end::example[]
