/**
 * 
 */
package org.altemista.cloudfwk.it.security.service;

/*
 * #%L
 * altemista-cloud web and Spring Boot-based applications security integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Example service for security integration tests
 * @author NTT DATA
 */
public interface SecuredService {
	
	/**
	 * Not secured method
	 * @return dummy value
	 */
	String notSecured();
	
	/**
	 * Method secured by Spring Security <code>@Secured</code> annotation.
	 * All users (even anonymously authenticated ones) are allowed invoke this method.
	 * @return dummy value
	 */
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	String springSecuredToAnonymous();
	
	/**
	 * Method secured by Spring Security <code>@Secured</code> annotation.
	 * Only the users with the role <code>ROLE_ADMIN</code> are allowed invoke this method.
	 * @return dummy value
	 */
	@Secured("ROLE_ADMIN")
	String springSecuredToRoleAdmin();
	
	/**
	 * Method secured by JSR 250 annotation.
	 * All security roles are allowed to invoke this method
	 * @return dummy value
	 */
	@PermitAll
	String jsr250PermitAll();
	
	/**
	 * Method secured by JSR 250 annotation.
	 * Only the users with the role <code>ROLE_ADMIN</code> are allowed invoke this method.
	 * @return dummy value
	 */
	@RolesAllowed("ROLE_ADMIN")
	String jsr250RolesAllowedAdmin();
	
	/**
	 * Method secured by JSR 250 annotation.
	 * No security roles are allowed to invoke this method.
	 * @return dummy value
	 */
	@DenyAll
	String jsr250DenyAll();
	
	/**
	 * Method secured by Spring Security <code>@Secured</code> annotation with expression-based syntax.
	 * All authenticated users are allowed invoke this method.
	 * @return dummy value
	 */
	@PreAuthorize("isAuthenticated()")
	String springExpressionSecuredToAuthenticated();
	
	/**
	 * Method secured by Spring Security <code>@Secured</code> annotation with expression-based syntax.
	 * Only the users with the role <code>ROLE_ADMIN</code> are allowed invoke this method.
	 * @return dummy value
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	String springExpressionSecuredToRoleAdmin();
}
