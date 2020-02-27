package cloud.altemista.fwk.common.connection.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/*
 * #%L
 * cloud-altemistafwk common: connectivity utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import cloud.altemista.fwk.common.connection.exception.ConnectionException;
import cloud.altemista.fwk.common.cryptography.model.KeyStoreBean;
import cloud.altemista.fwk.common.cryptography.util.KeyStoreUtil;

/**
 * SSLSocketFactory generator. Use it for SSL URL connections with the right certificates and keys
 * @author NTT DATA
 */
public final class SSLSocketFactoryUtil {

	/**
	 * Default private constructor (utility class)
	 */
	private SSLSocketFactoryUtil() {
		super();
	}
	
	/**
	 * Creates a new SSLSocketFactory and sets it as the default SSLSocketFactory for new HTTPS connections. 
	 * @param keyStore the key store to use
	 * @param trustStore the trust store to use
	 */
	public static void set(KeyStoreBean keyStore, KeyStoreBean trustStore) {
		
		if (keyStore == null) {
			throw new ConnectionException("no_keystore");
		}
		if (trustStore == null) {
			throw new ConnectionException("no_truststore");
		}
		
		try {
			KeyManager[] keyMgr = KeyStoreUtil.getKeyManagers(keyStore);
			TrustManager[] trustMgr = KeyStoreUtil.getTrustManagers(trustStore);
			
			SSLContext sslCtx = SSLContext.getInstance("SSL");
			sslCtx.init(keyMgr, trustMgr, null);
			SSLSocketFactory sslSocketFactory = sslCtx.getSocketFactory();
			
			HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
			
		} catch (NoSuchAlgorithmException e) {
			throw new ConnectionException("ssl_socket_factory", e);
			
		} catch (KeyManagementException e) {
			throw new ConnectionException("ssl_socket_factory", e);
		}
	}
}
