package cloud.altemista.fwk.web.soap.wss;

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


import javax.xml.ws.WebServiceFeature;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import cloud.altemista.fwk.core.soap.wss.Wss4jInterceptorFeature;

/**
 * Adapter of CXF-specific {@link WSS4JInInterceptor} as a standard {@link WebServiceFeature}
 * @author NTT DATA
 */
public class Wss4jInInterceptorFeature extends Wss4jInterceptorFeature {
	
	/** @see WSS4JInInterceptor#setIgnoreActions(boolean) */
	protected boolean ignoreActions;
	
	/**
	 * Default constructor
	 */
	public Wss4jInInterceptorFeature() {
		super();
		
		this.ignoreActions = false;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.cxf.feature.AbstractFeature#initializeProvider(org.apache.cxf.interceptor.InterceptorProvider, org.apache.cxf.Bus)
	 */
	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		super.initializeProvider(provider, bus);
		
		if (this.isEnabled()) {
			WSS4JInInterceptor interceptor = new WSS4JInInterceptor();
			interceptor.setIgnoreActions(ignoreActions);
			interceptor.setProperties(this.properties);
			provider.getInInterceptors().add(interceptor);
		}
	}

	/**
	 * @param ignoreActions the ignoreActions to set
	 * @see WSS4JInInterceptor#setIgnoreActions(boolean)
	 */
	public void setIgnoreActions(boolean ignoreActions) {
		this.ignoreActions = ignoreActions;
	}
}
