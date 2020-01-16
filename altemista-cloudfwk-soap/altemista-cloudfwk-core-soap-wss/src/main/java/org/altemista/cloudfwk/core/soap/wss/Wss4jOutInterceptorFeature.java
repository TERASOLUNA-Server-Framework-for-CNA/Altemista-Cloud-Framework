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


import javax.xml.ws.WebServiceFeature;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.common.WSS4JConstants;

/**
 * Adapter of CXF-specific {@link WSS4JOutInterceptor} as a standard {@link WebServiceFeature}
 * @author NTT DATA
 */
public class Wss4jOutInterceptorFeature extends Wss4jInterceptorFeature {
	
	/**
	 * The userProvider.
	 * If it is not null and returns a non-empty user, the value of the {@code user} property will be overwritten.
	 * @see #setUser(String)
	 * @see org.apache.wss4j.common.ConfigurationConstants#USER
	 */
	protected UserProvider userProvider;
	
	/**
	 * Constructor
	 */
	public Wss4jOutInterceptorFeature() {
		super();
		
		// (on the Outbound side, the default passwordType value is PW_DIGEST)
		this.setPasswordType(WSS4JConstants.PW_DIGEST);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.cxf.feature.AbstractFeature#initializeProvider(org.apache.cxf.interceptor.InterceptorProvider, org.apache.cxf.Bus)
	 */
	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		super.initializeProvider(provider, bus);
		
		if (this.isEnabled()) {
			WSS4JOutInterceptor interceptor = new WSS4JOutInterceptor();
			interceptor.setProperties(this.properties);
			
			if (this.userProvider != null) {
				String user = this.userProvider.getUser();
				if (StringUtils.isNotEmpty(user)) {
					interceptor.setProperty(ConfigurationConstants.USER, user);
				}
			}
			
			provider.getOutInterceptors().add(interceptor);
		}
	}
	
	/**
	 * Convenience method to set the user
	 * @param user the user to set
	 * @see org.apache.wss4j.common.ConfigurationConstants#USER
	 */
	public void setUser(String user) {
		
		if (StringUtils.isEmpty(user)) {
			this.properties.remove(ConfigurationConstants.USER);
		} else {
			this.properties.put(ConfigurationConstants.USER, user);
		}
	}
	
	/**
	 * Retrieve the user that will be used in the outbound WS-Security (e.g.: constant user, current user, etc.)
	 * @author NTT DATA
	 */
	public interface UserProvider {
		
		/**
		 * @return the user to set
		 */
		String getUser();
	}

	/**
	 * @param userProvider the userProvider to set
	 */
	public void setUserProvider(UserProvider userProvider) {
		this.userProvider = userProvider;
	}
}
