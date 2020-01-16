package org.altemista.cloudfwk.core.soap.wss;

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


import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.xml.ws.WebServiceFeature;

import org.apache.commons.collections4.MapUtils;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.springframework.util.StringUtils;

/**
 * Base adapter of CXF-specific {@link WSS4JInInterceptor} or {@link WSS4JOutInterceptor}
 * as a standard {@link WebServiceFeature} (e.g.: to be used in Spring {@code AbstractJaxWsServiceExporter})
 * @author NTT DATA
 */
public abstract class Wss4jInterceptorFeature extends AbstractFeature {
	
	/** The properties that will be set in the {@link WSS4JInInterceptor} or {@link WSS4JOutInterceptor} */
	protected Map<String, Object> properties;
	
	/**
	 * Default constructor
	 */
	public Wss4jInterceptorFeature() {
		super();
		
		// (this WebServiceFeature is enabled by default)
		this.enabled = true;
		this.properties = new HashMap<String, Object>();
	}
	
	/**
	 * @param enabled the enabled to set
	 * @see WebServiceFeature#enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		
		this.properties.clear();
		if (MapUtils.isNotEmpty(properties)) {
			this.properties.putAll(properties);
		}
	}
	
	/**
	 * Convenience method to set the action
	 * @param action the action parameter to set
	 * @see org.apache.wss4j.common.ConfigurationConstants#ACTION
	 */
	public void setAction(String action) {
		
		if (StringUtils.isEmpty(action)) {
			this.properties.remove(ConfigurationConstants.ACTION);
		} else {
			this.properties.put(ConfigurationConstants.ACTION, action);
		}
	}
	
	/**
	 * Convenience method to set the password type
	 * @param passwordType the password type to set; one of: "PasswordDigest", "PasswordText" or "PasswordNone"
	 * @see org.apache.wss4j.common.ConfigurationConstants#PASSWORD_TYPE
	 * @see org.apache.wss4j.common.WSS4JConstants#PW_DIGEST
	 * @see org.apache.wss4j.common.WSS4JConstants#PW_TEXT
	 * @see org.apache.wss4j.common.WSS4JConstants#PW_NONE
	 */
	public void setPasswordType(String passwordType) {
		
		if (StringUtils.isEmpty(passwordType)) {
			this.properties.remove(ConfigurationConstants.PASSWORD_TYPE);
		} else {
			this.properties.put(ConfigurationConstants.PASSWORD_TYPE, passwordType);
		}
	}
	
	/**
	 * Convenience method to set the callback handler
	 * @param callbackHandler the callbackHandler to set
	 * @see org.apache.wss4j.common.ConfigurationConstants#PW_CALLBACK_REF
	 */
	public void setPasswordCallbackRef(CallbackHandler callbackHandler) {
		
		if (callbackHandler == null) {
			this.properties.remove(ConfigurationConstants.PW_CALLBACK_REF);
		} else {
			this.properties.put(ConfigurationConstants.PW_CALLBACK_REF, callbackHandler);
		}
	}
}
