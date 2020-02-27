package cloud.altemista.fwk.it.service;

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


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Example business service implementation
 * @author NTT DATA
 */
@Service
public class ExampleBusinessServiceImpl implements ExampleBusinessService {

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.ExampleBusinessService#hello(java.lang.String)
	 */
	@Override
	public String hello(String name) {
		
		return "Hello, anonymous " + name + "!";
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.ExampleBusinessService#helloAuthenticatedUser()
	 */
	@Override
	public String helloAuthenticatedUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException("No authentication");
		}
		if (!(authentication.getPrincipal() instanceof UserDetails)) {
			throw new IllegalStateException("The principal is not an instance of UserDetails: " + authentication.getPrincipal());
		}
		
		final String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		return "Hello, authenticated " + username + "!";
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.ExampleBusinessService#helloAuthorizedUser()
	 */
	@Override
	public String helloAuthorizedUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException("No authentication");
		}
		if (!(authentication.getPrincipal() instanceof UserDetails)) {
			throw new IllegalStateException("The principal is not an instance of UserDetails: " + authentication.getPrincipal());
		}
		
		final String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		return "Hello, authorized " + username + "!";
	}

}
