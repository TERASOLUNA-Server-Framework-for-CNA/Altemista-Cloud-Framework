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


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.altemista.cloudfwk.core.soap.wss.Wss4jOutInterceptorFeature;
import org.altemista.cloudfwk.core.soap.wss.Wss4jOutInterceptorFeature.UserProvider;

/**
 * {@link Wss4jOutInterceptorFeature} that automatically sets up the proper {@link SpringSecurityCallbackHandler}
 * if a {@link UserDetailsService} bean named {@code "outgoingWssUserDetailsService"} is present
 * @author NTT DATA
 */
public class SpringSecurityAwareWss4jOutInterceptorFeature extends Wss4jOutInterceptorFeature
		implements InitializingBean {

	/** The name of the autowired {@link UserDetailsService} bean */
	public static final String OUTGOING_WSS_USER_DETAILS_SERVICE = "outgoingwssUserDetailsService";
	
	/** Spring Security UserDetailsService to retrieve the password */
	@Autowired(required = false)
	@Qualifier(OUTGOING_WSS_USER_DETAILS_SERVICE)
	private UserDetailsService userDetailsService;
	
	/**
	 * {@link UserProvider} that returns the name of the current authenticated user in the Spring Security Context
	 * @author NTT DATA
	 */
	public static final UserProvider SECURITY_CONTEXT_USER_PROVIDER = new UserProvider() {
		
		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.soap.wss.Wss4jOutInterceptorFeature.UserProvider#getUser()
		 */
		@Override
		public String getUser() {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return (authentication == null) ? null : authentication.getName();
		}
	};
	
	
	/**
	 * Flag to use the name of the current authenticated user in the Spring Security Context as the user
	 * (unless a different {@link UserProvider} is manually injected)
	 * @see Wss4jOutInterceptorFeature#SECURITY_CONTEXT_USER_PROVIDER
	 */
	private boolean forwardUser = true;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if (this.userDetailsService != null) {
			this.setPasswordCallbackRef(new SpringSecurityCallbackHandler(this.userDetailsService, false));
		}
		
		if ((this.userProvider == null) && this.forwardUser) {
			this.setUserProvider(SECURITY_CONTEXT_USER_PROVIDER);
		}
	}

	/**
	 * @param userDetailsService the userDetailsService to set
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * @param forwardUser the forwardUser to set
	 */
	public void setForwardUser(boolean forwardUser) {
		this.forwardUser = forwardUser;
	}
}
