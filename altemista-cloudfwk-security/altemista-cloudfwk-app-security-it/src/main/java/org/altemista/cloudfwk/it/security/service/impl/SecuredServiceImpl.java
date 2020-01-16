/**
 * 
 */
package org.altemista.cloudfwk.it.security.service.impl;

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


import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.security.service.SecuredService;

/**
 * Implementation of the example service for security unit tests
 * @author NTT DATA
 */
@Service
public class SecuredServiceImpl implements SecuredService {
	
	/** The "Hello, world!" String */
	private static final String HELLO_WORLD = "Hello, world!";

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#notSecured()
	 */
	@Override
	public String notSecured() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#springSecuredToAnonymous()
	 */
	@Override
	public String springSecuredToAnonymous() {
		return HELLO_WORLD;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#springSecuredToRoleAdmin()
	 */
	@Override
	public String springSecuredToRoleAdmin() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#jsr250PermitAll()
	 */
	@Override
	public String jsr250PermitAll() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#jsr250RolesAllowedAdmin()
	 */
	@Override
	public String jsr250RolesAllowedAdmin() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#jsr250DenyAll()
	 */
	@Override
	public String jsr250DenyAll() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#springExpressionSecuredToAuthenticated()
	 */
	@Override
	public String springExpressionSecuredToAuthenticated() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.security.service.SecuredService#springExpressionSecuredToRoleAdmin()
	 */
	@Override
	public String springExpressionSecuredToRoleAdmin() {
		return HELLO_WORLD;
	}
}
