package cloud.altemista.fwk.web.soap.wss.springsecurity;

/*
 * #%L
 * altemista-cloud SOAP server with WS-Security
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
import org.springframework.security.core.userdetails.UserDetailsService;
import cloud.altemista.fwk.core.soap.wss.springsecurity.SpringSecurityCallbackHandler;
import cloud.altemista.fwk.web.soap.wss.Wss4jInInterceptorFeature;

/**
 * {@link Wss4jInInterceptorFeature} that automatically sets up the proper {@link SpringSecurityCallbackHandler}
 * if a {@link UserDetailsService} bean named {@code "inboundwssUserDetailsService"} is present
 * @author NTT DATA
 */
public class SpringSecurityAwareWss4jInInterceptorFeature extends Wss4jInInterceptorFeature
		implements InitializingBean {

	/** The name of the autowired {@link UserDetailsService} bean */
	public static final String INBOUND_WSS_USER_DETAILS_SERVICE = "inboundwssUserDetailsService";
	
	/** Spring Security UserDetailsService to retrieve the password */
	@Autowired(required = false)
	@Qualifier(INBOUND_WSS_USER_DETAILS_SERVICE)
	private UserDetailsService userDetailsService;
	
	/**
	 * Flag to perform authentication in the Spring Security context.
	 * @see SpringSecurityCallbackHandler#authenticate
	 */
	private boolean authenticate = true;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if (this.userDetailsService != null) {
			this.setPasswordCallbackRef(new SpringSecurityCallbackHandler(this.userDetailsService, this.authenticate));
		}
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
