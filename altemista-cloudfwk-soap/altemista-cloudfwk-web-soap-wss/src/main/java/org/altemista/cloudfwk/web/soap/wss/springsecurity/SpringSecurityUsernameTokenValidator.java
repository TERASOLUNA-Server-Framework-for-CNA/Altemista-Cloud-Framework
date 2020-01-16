package org.altemista.cloudfwk.web.soap.wss.springsecurity;

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


import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.wss4j.dom.validate.UsernameTokenValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * This WSS4J validator validates a processed {@code UsernameToken}
 * using a Spring Security {@code AuthenticationManager}
 * and performs authentication in the Spring Security context.
 * Only the {@code passwordType} value {@code "PasswordText"} is supported.
 * @author NTT DATA
 * @see org.altemista.cloudfwk.core.soap.wss.InSpringSecurityWsSecurityBean
 */
public class SpringSecurityUsernameTokenValidator extends UsernameTokenValidator implements InitializingBean {
	
	/** The name of the AuthenticationManager bean that will be autowired */
	public static final String WSS_AUTHENTICATION_MANAGER_BEAN_NAME = "inboundwssAuthenticationManager";

	/**
	 * Flag that allows reverting to the default UsernameTokenValidator behavior.
	 * Enabled by default (this class will override the UsernameTokenValidator behavior).
	 */
	private boolean enabled = true;
	
	/**
	 * The Spring Security AuthenticationManager
	 * Autowired to allow not injecting it if the feature is not enabled (e.g.: by configuration) 
	 */
	@Autowired(required = false)
	@Qualifier(WSS_AUTHENTICATION_MANAGER_BEAN_NAME)
	private AuthenticationManager authenticationManager;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		// If this validator is enabled, the AuthenticationManager is mandatory
		if (this.enabled) {
			Assert.notNull(this.authenticationManager, "The AuthenticationManager is required");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wss4j.dom.validate.UsernameTokenValidator#verifyPlaintextPassword(org.apache.wss4j.dom.message.token.UsernameToken, org.apache.wss4j.dom.handler.RequestData)
	 */
	@Override
	protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException {
		
		// Reverts to the default UsernameTokenValidator behavior if not enabled
		if (!this.enabled) {
			super.verifyPlaintextPassword(usernameToken, data);
			return;
		}

		final String user = usernameToken.getName();
		final String password = usernameToken.getPassword();
		final String pwType = usernameToken.getPasswordType();

		// Performs the authentication using the Spring Security AuthenticationManager
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Invokes the CallbackHandlers as usual 
		try {
			WSPasswordCallback pwCb = new WSPasswordCallback(user, null, pwType, WSPasswordCallback.USERNAME_TOKEN);
			data.getCallbackHandler().handle(new Callback[] { pwCb });

		} catch (IOException e) {
			throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, e);
			
		} catch (UnsupportedCallbackException e) {
			throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wss4j.dom.validate.UsernameTokenValidator#verifyDigestPassword(org.apache.wss4j.dom.message.token.UsernameToken, org.apache.wss4j.dom.handler.RequestData)
	 */
	@Override
	protected void verifyDigestPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException {
		
		// Reverts to the default UsernameTokenValidator behavior if not enabled
		if (!this.enabled) {
			super.verifyDigestPassword(usernameToken, data);
			return;
		}

		// Only the passwordType "PasswordText" is supported
		throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param authenticationManager the authenticationManager to set
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
