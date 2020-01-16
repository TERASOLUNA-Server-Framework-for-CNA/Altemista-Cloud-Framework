package org.altemista.cloudfwk.it.service;

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


import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Example business service
 * @author NTT DATA
 */
public interface ExampleBusinessService {

	/**
	 * This method is not secured and does not require an authenticated user
	 * @param name String
	 * @return String
	 */
	String hello(String name);

	/**
	 * This method is secured to authenticated users.
	 * @return String
	 */
	@PreAuthorize("isAuthenticated()")
	String helloAuthenticatedUser();

	/**
	 * This method is secured to an specific role.
	 * @return String
	 */
	@PreAuthorize("hasRole('USER')")
	String helloAuthorizedUser();
}
