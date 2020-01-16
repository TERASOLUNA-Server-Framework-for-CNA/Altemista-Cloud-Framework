package org.altemista.cloudfwk.common.connection.util;

/*
 * #%L
 * altemista-cloud common: connectivity utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.connection.model.ProxyBean;

/**
 * ProxyBean selector that can be configured with a fixed proxy per thread.
 * @author NTT DATA
 */
public final class ThreadLocalProxySelector extends ProxySelector {
	
	/** Singleton instance. */
	private static final ThreadLocalProxySelector INSTANCE = new ThreadLocalProxySelector();
	
	/** URI Scheme component: "http". */
	private static final String HTTP_SCHEME = "http";
	
	/** URI Scheme component: "https". */
	private static final String HTTPS_SCHEME = "https";
	
	/** A list that contains one element of type Proxy that represents a direct connection. */
	private static final List<Proxy> NO_PROXY_LIST = Collections.singletonList(Proxy.NO_PROXY);
	
	/** The thread local proxy. */
	private static ThreadLocal<ProxyBean> threadLocalProxy = new ThreadLocal<ProxyBean>();

	/** The previous default proxy selector. */
	private static ProxySelector defaultProxySelector;
	
	/**
	 * Private default constructor (singleton).
	 */
	private ThreadLocalProxySelector() {
		super();
	}
	
	/**
	 * Resets the proxy to use
	 */
	public static void resetProxy() {
		
		// Removes the stored proxy
		threadLocalProxy.remove();
		
		// Restores the previous proxy selector as the default one
		if (defaultProxySelector != null) {
			ProxySelector.setDefault(defaultProxySelector);
		}
		defaultProxySelector = null;
	}
	
	/**
	 * Sets the proxy to use
	 * @param proxyBean ProxyBean
	 */
	public static void setProxy(ProxyBean proxyBean) {
		
		// Stores the proxy
		threadLocalProxy.set(proxyBean);
		
		// Registers the previous proxy selector, then sets this instance as the default proxy selector
		if (defaultProxySelector == null) {
			defaultProxySelector = ProxySelector.getDefault();
		}
		ProxySelector.setDefault(INSTANCE);
	}
	
	/* (non-Javadoc)
	 * @see java.net.ProxySelector#select(java.net.URI)
	 */
	@Override
	public List<Proxy> select(URI uri) {
		Assert.notNull(uri);
		
		final ProxyBean proxyBean = threadLocalProxy.get();
		
		// If no proxy is configured, used the previous default proxy selector or direct connection
		if (proxyBean == null) {
			if (defaultProxySelector != null) {
				return defaultProxySelector.select(uri);
			}
			return NO_PROXY_LIST;
		}
		
		String scheme = uri.getScheme();
		if (HTTP_SCHEME.equals(scheme) || HTTPS_SCHEME.equals(scheme)) {
			this.setDefaultAuthenticator();
			return Collections.singletonList(this.asProxy(Proxy.Type.HTTP));
		}
	
		return this.defaultProxy(uri);
	}
	
	/**
	 * If the username of the proxy is set,
	 * changes the authenticator that will be used by the networking code
     * when a proxy or an HTTP server asks for authentication.
	 */
	private void setDefaultAuthenticator() {
		
		final ProxyBean proxyBean = threadLocalProxy.get();
		if (proxyBean.getUsername() == null) {
			return;
		}
		
		// This use of Authenticator is valid for proxy configuration.
		// Its assumed that there is only one proxy for all applications deployed in an app server.
		Authenticator.setDefault(new ProxyAuthenticator(this.asPasswordAuthentication()));
	}
	
	/**
	 * Uses the the previous default proxy selector
	 * or returns a list that contains one element of type Proxy that represents a direct connection 
	 * @param uri The URI that a connection is required to
	 * @return a List of Proxies
	 */
	private List<Proxy> defaultProxy(URI uri) {
		
		if (defaultProxySelector != null) {
			return defaultProxySelector.select(uri);
		}
		return NO_PROXY_LIST;
	}
	
	/**
	 * Returns the ProxyBean as proxy settings (Proxy class).
	 * @param type the proxy type 
	 * @return Proxy
	 */
	private Proxy asProxy(Proxy.Type type) {
		
		final ProxyBean proxyBean = threadLocalProxy.get();
		return new Proxy(type, new InetSocketAddress(proxyBean.getHostname(), proxyBean.getPort()));
	}
	
	/**
	 * Returns the username and password of the ProxyBean as a PasswordAuthentication
	 * @return PasswordAuthentication or null if no username or password are set
	 */
	private PasswordAuthentication asPasswordAuthentication() {
		
		final ProxyBean proxyBean = threadLocalProxy.get();
		if ((proxyBean.getUsername() == null) || (proxyBean.getPassword() == null)) {
			return null;
		}
		
		return new PasswordAuthentication(proxyBean.getUsername(), proxyBean.getPassword().toCharArray());
	}

	/* (non-Javadoc)
	 * @see java.net.ProxySelector#connectFailed(java.net.URI, java.net.SocketAddress, java.io.IOException)
	 */
	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		//
	}

	/**
	 * Implementation of Authenticator that uses a password authentication when the requestor is a proxy.
	 */
	private static final class ProxyAuthenticator extends Authenticator {

		/** The password authentication to use in the proxy. */
		private PasswordAuthentication passwordAuthentication;

		/**
		 * Constructor.
		 * @param passwordAuthentication to use in the proxy
		 */
		public ProxyAuthenticator(PasswordAuthentication passwordAuthentication) {
			super();
			this.passwordAuthentication = passwordAuthentication;
		}

		/* (non-Javadoc)
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {

			if (this.getRequestorType() == RequestorType.PROXY) {
				return this.passwordAuthentication;
			}
			
			return super.getPasswordAuthentication();
		}
	}
}
