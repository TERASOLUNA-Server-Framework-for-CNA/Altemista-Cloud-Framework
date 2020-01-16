package org.altemista.cloudfwk.common.cryptography.util;

/*
 * #%L
 * altemista-cloud common: cryptography utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Iterator;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.altemista.cloudfwk.common.cryptography.exception.CryptographyException;
import org.altemista.cloudfwk.common.cryptography.model.KeyStoreBean;
import org.altemista.cloudfwk.common.util.ResourceUtil;

/**
 * Java key store-related utilities
 * @author NTT DATA
 */
public final class KeyStoreUtil {

	private static final String ALGORITHM = "SunX509";

	/**
	 * Default private constructor (utility class)
	 */
	private KeyStoreUtil() {
		super();
	}

	/**
	 * Reads a Java key store.
	 * @param bean POJO with a key or trust store information
	 * @return the key store
	 */
	public static KeyStore getKeyStore(KeyStoreBean bean) {

		// (sanity checks)
		if (bean == null) {
			throw new CryptographyException("no_keystore");
		}
		if (StringUtils.isEmpty(bean.getResourceLocation())) {
			throw new CryptographyException("no_keystore_resource");
		}
		if (ArrayUtils.isEmpty(bean.getPassword())) {
			throw new CryptographyException("no_keystore_password");
		}
		if (StringUtils.isEmpty(bean.getType())) {
			throw new CryptographyException("no_keystore_type");
		}
		
		InputStream is = null;
		try {
			// Opens the key store 
			is = ResourceUtil.getInputStream(bean.getResourceLocation());

			// Actually loads the key store
			KeyStore keyStore = KeyStore.getInstance(bean.getType());
			keyStore.load(is, bean.getPassword());
			return keyStore;
			
		} catch (KeyStoreException e) {
			throw new CryptographyException("invalid_keystore_type", new Object[]{ bean.getType() }, e);
			
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException("invalid_keystore_algorithm", e);
			
		} catch (CertificateException e) {
			throw new CryptographyException("invalid_keystore_certificate", e);
			
		} catch (IOException e) {
			throw new CryptographyException("io_exception", e);
			
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * Reads the JSSE key managers from a Java key store
	 * @param bean POJO with a key or trust store information
	 * @return the JSSE key managers
	 */
	public static KeyManager[] getKeyManagers(KeyStoreBean bean) {

		// Reads the key store
		KeyStore keyStore = getKeyStore(bean);
		
		// Extracts the key managers 
		try {
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(ALGORITHM);
			keyManagerFactory.init(keyStore, bean.getPassword());
			return keyManagerFactory.getKeyManagers();
			
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException("invalid_keymanager_algorithm", new Object[]{ ALGORITHM }, e);
			
		} catch (KeyStoreException e) {
			throw new CryptographyException("invalid_keymanager_keystore", e);
			
		} catch (UnrecoverableKeyException e) {
			throw new CryptographyException("invalid_keymanager_key", e);
		} 
	}
	
	/**
	 * Reads the JSSE trust managers from a Java key store
	 * @param bean POJO with a key or trust store information
	 * @return the JSSE trust managers
	 */
	public static TrustManager[] getTrustManagers(KeyStoreBean bean) {

		// Reads the key store
		KeyStore keyStore = getKeyStore(bean);
		
		// Extracts the trust managers 
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(ALGORITHM);
			trustManagerFactory.init(keyStore);
			return trustManagerFactory.getTrustManagers();
			
		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException("invalid_trustmanager_algorithm", new Object[]{ ALGORITHM }, e);
			
		} catch (KeyStoreException e) {
			throw new CryptographyException("invalid_trustmanager_keystore", e);
		} 
	}
	
	/**
	 * Reads a public key from a certificate stored in a Java key store
	 * @param bean POJO with a key or trust store information
	 * @param pAlias the alias for the certificate. If null or empty, the first public certificate will be read
	 * @return the public key read
	 */
	public static PublicKey getPublicKey(KeyStoreBean bean, String pAlias) {

		// Reads the key store
		KeyStore keyStore = getKeyStore(bean);

		// No alias specified: looks for the first public certificate
		try {
			// Returns the public certificate with the specified alias
			// or looks for the first public certificate (if no alias specified) 
			for (Iterator<String> it = StringUtils.isEmpty(pAlias)
						? new EnumerationIterator<String>(keyStore.aliases())
						: new SingletonIterator<String>(pAlias); it.hasNext(); ) {
				String alias = it.next();
				
				Certificate certificate = keyStore.getCertificate(alias);
				
				// Returns the public certificate
				if (certificate != null) {
					return certificate.getPublicKey();
				}
			}

		} catch (KeyStoreException e) {
			throw new CryptographyException("publickey_invalid_keystore",
					new Object[] { bean.getResourceLocation() }, e);
		}
		
		// No public certificate found
		throw new CryptographyException("publickey_notfound", new Object[] { pAlias, bean.getResourceLocation() });
	}
	
	/**
	 * Reads a private key from a certificate stored in a Java key store
	 * @param bean POJO with a key or trust store information
	 * @param pAlias the alias for the certificate. If null or empty, the first private certificate will be read
	 * @param pPassword char[] the password for the certificate. If null or empty, the key store password will be used
	 * @return the private key read
	 */
	public static PrivateKey getPrivateKey(KeyStoreBean bean, String pAlias, char[] pPassword) {

		// Reads the key store
		KeyStore keyStore = getKeyStore(bean);
		
		char[] password = ObjectUtils.defaultIfNull(pPassword, bean.getPassword());

		try {
			// Returns the private key of the certificate with the specified alias
			// or looks for the first private key (if no alias specified) 
			for (Iterator<String> it = StringUtils.isEmpty(pAlias)
						? new EnumerationIterator<String>(keyStore.aliases())
						: new SingletonIterator<String>(pAlias); it.hasNext(); ) {
				String alias = it.next();
				
				PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
				
				// Returns the private key
				if (privateKey != null) {
					return privateKey;
				}
			}

		} catch (NoSuchAlgorithmException e) {
			throw new CryptographyException("privatekey_algorithm", e);
			
		} catch (UnrecoverableKeyException e) {
			throw new CryptographyException("privatekey_invalid_key", e);
			
		} catch (KeyStoreException e) {
			throw new CryptographyException("privatekey_invalid_keystore",
					new Object[] { bean.getResourceLocation() },  e);
		}

		// No private key found
		throw new CryptographyException("privatekey_notfound", new Object[] { pAlias, bean.getResourceLocation() });
	}
}
