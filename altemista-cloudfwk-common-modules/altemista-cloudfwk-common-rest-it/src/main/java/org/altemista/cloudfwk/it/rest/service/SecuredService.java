/**
 * 
 */
package org.altemista.cloudfwk.it.rest.service;

/*
 * #%L
 * altemista-cloud common: REST consumer utilitites integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.security.access.annotation.Secured;

/**
 * Example service for security integration tests
 * @author NTT DATA
 */
public interface SecuredService {
	
	/**
	 * Not secured service method
	 * @param argument value
	 * @return dummy value
	 */
	String forEveryone(String argument);
	
	/**
	 * Method secured by Spring Security <code>@Secured</code> annotation.
	 * Only the users with the role <code>ROLE_ADMIN</code> are allowed invoke this method.
	 * @param argument value
	 * @return dummy value
	 */
	@Secured("ROLE_ADMIN")
	String onlyForAdminUsers(String argument);
}
