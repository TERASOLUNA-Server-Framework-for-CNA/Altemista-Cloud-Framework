/**
 * 
 */
package cloud.altemista.fwk.test.security.service.impl;

/*
 * #%L
 * altemista-cloud security: annotation-based authorization CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.stereotype.Service;
import cloud.altemista.fwk.test.security.service.SecuredService;

/**
 * Implementation of the example service for security unit tests
 * @author NTT DATA
 */
@Service
public class SecuredServiceImpl implements SecuredService {
	
	/** The "Hello, world!" String */
	private static final String HELLO_WORLD = "Hello, world!";

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#notSecured()
	 */
	@Override
	public String notSecured() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#springSecuredToAnonymous()
	 */
	@Override
	public String springSecuredToAnonymous() {
		return HELLO_WORLD;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#springSecuredToRoleAdmin()
	 */
	@Override
	public String springSecuredToRoleAdmin() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#jsr250PermitAll()
	 */
	@Override
	public String jsr250PermitAll() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#jsr250RolesAllowedAdmin()
	 */
	@Override
	public String jsr250RolesAllowedAdmin() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#jsr250DenyAll()
	 */
	@Override
	public String jsr250DenyAll() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#springExpressionSecuredToAuthenticated()
	 */
	@Override
	public String springExpressionSecuredToAuthenticated() {
		return HELLO_WORLD;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.test.security.service.SecuredService#springExpressionSecuredToRoleAdmin()
	 */
	@Override
	public String springExpressionSecuredToRoleAdmin() {
		return HELLO_WORLD;
	}

}
