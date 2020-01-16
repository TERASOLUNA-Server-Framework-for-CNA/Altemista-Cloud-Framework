/**
 * 
 */
package org.altemista.cloudfwk.it.rest.service.impl;

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


import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.rest.service.SecuredService;

/**
 * Implementation of the example service for security unit tests
 * @author NTT DATA
 */
@Service
public class SecuredServiceImpl implements SecuredService {
	
	/** The "Hello, world!" String */
	private static final String HELLO_WORLD = "Hello, %s";
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.rest.service.SecuredService#forEveryone(java.lang.String)
	 */
	@Override
	public String forEveryone(String argument) {
		
		return String.format(HELLO_WORLD, argument);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.rest.service.SecuredService#onlyForAdminUsers(java.lang.String)
	 */
	@Override
	public String onlyForAdminUsers(String argument) {
		
		return String.format(HELLO_WORLD, argument);
	}
}
