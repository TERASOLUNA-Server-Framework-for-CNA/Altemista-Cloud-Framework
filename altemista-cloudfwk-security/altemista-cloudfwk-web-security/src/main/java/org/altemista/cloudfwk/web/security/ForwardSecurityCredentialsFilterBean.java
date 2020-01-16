
package org.altemista.cloudfwk.web.security;

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


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

/**
 * Filter class to forward the Spring Security Credentials to external systems (e.g.: via REST, web services, etc.).
 * <br>
 * Spring Security removes the password from the authentication object,
 * but this password may be needed to consume external services forwarding the authentication
 * (e.g.: in the RestTemplate used to to consume REST services).<br>
 * Include the in the proper &lt;security:http&gt; element, before <code>FORM_LOGIN_FILTER</code>,
 * that MUST have form-login based authentication (i.e.: &lt;security:form-login&gt;):
 * <pre>&lt;security:http ...&gt;
 *     ...
 *     &lt;security:custom-filter ref="forwardSecurityCredentialsFilter" before="FORM_LOGIN_FILTER" /&gt;
 *     &lt;security:form-login /&gt;
 * &lt;/security:http&gt;</pre>
 * @author NTT DATA
 * @deprecated This functionality, although useful in development stages, is quite limited;
 * and will be removed in future versions if a Single sign-on or OAuth/OAuth 2.0 module becomes available 
 */
@Deprecated
public class ForwardSecurityCredentialsFilterBean extends GenericFilterBean {
	
	/** The password parameter name. */
	private String passwordParameter = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
	 */
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		
		Assert.hasText(this.passwordParameter, "The password parameter name can not be null");
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		final String password = request.getParameter(this.passwordParameter);
		
		if (password != null) {
			// Publish the credentials object into session for the application.
			// If the credentials are invalid and Spring Security refuses them,
			// the session attribute will be overridden when the user introduces the right credentials.
			WebUtils.setSessionAttribute((HttpServletRequest) request, this.passwordParameter, password);
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @param passwordParameter the passwordParameter to set
	 */
	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}
}
