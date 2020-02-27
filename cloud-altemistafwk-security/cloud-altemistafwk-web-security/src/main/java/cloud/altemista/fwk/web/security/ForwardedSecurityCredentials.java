
package cloud.altemista.fwk.web.security;

/*
 * #%L
 * altemista-cloud security: Web/HTTP authorization
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.apache.http.auth.BasicUserPrincipal;
import org.apache.http.auth.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * User credentials from Spring Security context that can be forwarded to external systems
 * (e.g.: via REST, web services, etc.)
 * @author NTT DATA
 * @see cloud.altemista.fwk.web.security.ForwardSecurityCredentialsFilterBean
 * @deprecated This functionality, although useful in development stages, is quite limited;
 * and will be removed in future versions if a Single sign-on or OAuth/OAuth 2.0 module becomes available 
 */
@Deprecated
public class ForwardedSecurityCredentials implements Credentials, Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = -6996389854244301890L;

	/** The HttpSession. */
	@Autowired
	private transient HttpSession session;
	
	/** The password parameter name. */
	private String passwordParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
	
	/** The principal. */
	private transient Principal principal = null;
	
	/** The password. */
	private transient String password = null;
	
	/* (non-Javadoc)
	 * @see org.apache.http.auth.Credentials#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		
		this.initialize();
		return this.principal;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.auth.Credentials#getPassword()
	 */
	@Override
	public String getPassword() {		
		
		this.initialize();
		return this.password;
	}
	
	/**
	 * Initializes the principal and the password from the session attribute.
	 */
	protected void initialize() {
		
		synchronized (this) {
			if (this.principal == null) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if ((authentication != null) && (authentication.getPrincipal() instanceof UserDetails)) {
					
					// Creates a BasicUserPrincipal from the username of the principal
					String username = ((UserDetails) authentication.getPrincipal()).getUsername();
					this.principal = new BasicUserPrincipal(username);
					
					// Retrieves the password that was published in session by ForwardSecurityCredentialsFilterBean
					// and removes the password from the session (it is no longer needed) 
					this.password = (String) this.session.getAttribute(this.passwordParameter);
					this.session.removeAttribute(this.passwordParameter);
				}
			}
		}
	}

	/**
	 * @param passwordParameter the passwordParameter to set
	 */
	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}
}
