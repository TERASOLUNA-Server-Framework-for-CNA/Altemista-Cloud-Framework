package org.altemista.cloudfwk.core.soap.wss.springsecurity;

/*
 * #%L
 * altemista-cloud SOAP client with WS-Security
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.soap.wss.AbstractPasswordCallbackHandler;

/**
 * {@code CallbackHandler} that uses Spring Security {@code UserDetailsService}
 * to provide the password required by the default WSS4J validator instance ({@code UsernameTokenValidator})
 * and (optionally) performs authentication in the Spring Security context.
 * Unlike {@code AuthenticationManagerUsernameTokenValidator},
 * this class supports {@code passwordType} "PasswordDigest",
 * but coerces the authentication to be based on {@code UserDetailsService}.
 * @author NTT DATA
 * @see InboundSpringSecurityWss4jConfiguration
 * @see OutboundSpringSecurityWss4jConfiguration
 */
public class SpringSecurityCallbackHandler extends AbstractPasswordCallbackHandler {
	
	/** Spring Security UserDetailsService to retrieve the password */
	private UserDetailsService userDetailsService;
	
	/** Flag to perform authentication in the Spring Security context */
	private boolean authenticate;
	
	/**
	 * Constructor
	 * @param userDetailsService Spring Security UserDetailsService to retrieve the password
	 * @param authenticate Flag to perform authentication in the Spring Security context
	 */
	public SpringSecurityCallbackHandler(UserDetailsService userDetailsService, boolean authenticate) {
		super();
		
		Assert.notNull(userDetailsService, "The UserDetailsService is required");
		
		this.userDetailsService = userDetailsService;
		this.authenticate = authenticate;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.soap.wss.AbstractPasswordCallbackHandler#doHandle(org.apache.wss4j.common.ext.WSPasswordCallback)
	 */
	@Override
	protected boolean doHandle(WSPasswordCallback pc) throws IOException {
		
		// Locates the user based on the username
		try {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(pc.getIdentifier());
			pc.setIdentifier(userDetails.getUsername());
			pc.setPassword(userDetails.getPassword());
			
		} catch (UsernameNotFoundException ignored) { // NOSONAR
			// (silently ignores)
			return false;
		}
		
		// Performs authentication in the Spring Security context
		if (this.authenticate) {
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(pc.getIdentifier(), pc.getPassword()));
		}
		
		return true;
	}

	/**
	 * @param userDetailsService the userDetailsService to set
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * @param authenticate the authenticate to set
	 */
	public void setAuthenticate(boolean authenticate) {
		this.authenticate = authenticate;
	}
}
