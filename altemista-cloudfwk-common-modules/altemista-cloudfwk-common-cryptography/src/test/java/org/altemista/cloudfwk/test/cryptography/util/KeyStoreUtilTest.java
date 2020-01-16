package org.altemista.cloudfwk.test.cryptography.util;

/*
 * #%L
 * altemista-cloudfwk common: cryptography utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.common.cryptography.exception.CryptographyException;
import org.altemista.cloudfwk.common.cryptography.model.KeyStoreBean;
import org.altemista.cloudfwk.common.cryptography.util.KeyStoreUtil;
import org.altemista.cloudfwk.common.exception.CommonException;

public class KeyStoreUtilTest {
	
	@Test
	public void testGetKeyStoreSanityChecks() {

		KeyStoreBean bean = null;
		
		// no_keystore
		
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore", e.getCode());
		}
		bean = new KeyStoreBean();
		
		// no_keystore_resource
		
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_resource CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_resource", e.getCode());
		}
		bean.setResourceLocation("");
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_resource CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_resource", e.getCode());
		}
		bean.setResourceLocation("foo");
		
		// no_keystore_password
		
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_password CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_password", e.getCode());
		}
		bean.setPassword(new char[]{});
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_password CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_password", e.getCode());
		}
		bean.setPassword("bar".toCharArray());
		
		// no_keystore_type
		
		bean.setType(null);
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_type CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_type", e.getCode());
		}
		bean.setType("");
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("no_keystore_type CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.no_keystore_type", e.getCode());
		}
		bean.setType(KeyStoreBean.DEFAULT_TYPE);
		
		// invalid_keystore_resource
		
		try {
			KeyStoreUtil.getKeyStore(bean);
			Assert.fail("resource_not_found CommonException expected");

		} catch (CommonException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.exception.resource_not_found", e.getCode());
		}
	}
	
	//
	
	@Test
	public void testGetKeyStore1() throws KeyStoreException {
		
		// core-certificate.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-certificate.jks");
		bean.setPassword("pubks-key".toCharArray());
		
		KeyStore keyStore = KeyStoreUtil.getKeyStore(bean);
		Assert.assertNotNull(keyStore);
		Assert.assertTrue(keyStore.containsAlias("core"));
	}
	
	@Test
	public void testGetKeyStore2() throws KeyStoreException {
		
		// core-key.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-key.jks");
		bean.setPassword("privks-key".toCharArray());
		
		KeyStore keyStore = KeyStoreUtil.getKeyStore(bean);
		Assert.assertNotNull(keyStore);
		Assert.assertTrue(keyStore.containsAlias("core-key"));
	}
	
	@Test
	public void testGetKeyStore3() throws KeyStoreException {
		
		// rsa-keypair.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/rsa-keypair.jks");
		bean.setPassword("kspassword".toCharArray());
		
		KeyStore keyStore = KeyStoreUtil.getKeyStore(bean);
		Assert.assertNotNull(keyStore);
		Assert.assertTrue(keyStore.containsAlias("myalias"));
	}
	
	//
	
	@Test
	public void testGetKeyManagers1() throws KeyStoreException {
		
		// core-certificate.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-certificate.jks");
		bean.setPassword("pubks-key".toCharArray());
		
		KeyManager[] keyManagers = KeyStoreUtil.getKeyManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(keyManagers));
	}
	
	@Test
	public void testGetKeyManagers2() throws KeyStoreException {
		
		// core-key.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-key.jks");
		bean.setPassword("privks-key".toCharArray());
		
		KeyManager[] keyManagers = KeyStoreUtil.getKeyManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(keyManagers));
	}
	
	@Test
	public void testGetKeyManagers3() throws KeyStoreException {
		
		// rsa-keypair.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/rsa-keypair.jks");
		bean.setPassword("kspassword".toCharArray());
		
		KeyManager[] keyManagers = KeyStoreUtil.getKeyManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(keyManagers));
	}
	
	//
	
	@Test
	public void testGetTrustManagers1() throws KeyStoreException {
		
		// core-certificate.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-certificate.jks");
		bean.setPassword("pubks-key".toCharArray());
		
		TrustManager[] trustManagers = KeyStoreUtil.getTrustManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(trustManagers));
	}
	
	@Test
	public void testGetTrustManagers2() throws KeyStoreException {
		
		// core-key.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-key.jks");
		bean.setPassword("privks-key".toCharArray());
		
		TrustManager[] trustManagers = KeyStoreUtil.getTrustManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(trustManagers));
	}
	
	@Test
	public void testGetTrustManagers3() throws KeyStoreException {
		
		// rsa-keypair.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/rsa-keypair.jks");
		bean.setPassword("kspassword".toCharArray());
		
		TrustManager[] trustManagers = KeyStoreUtil.getTrustManagers(bean);
		Assert.assertTrue(ArrayUtils.isNotEmpty(trustManagers));
	}
	
	//
	
	@Test
	public void testGetPublicKey1() throws KeyStoreException {
		
		// core-certificate.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-certificate.jks");
		bean.setPassword("pubks-key".toCharArray());
		
		PublicKey publicKey = KeyStoreUtil.getPublicKey(bean, "core");
		Assert.assertNotNull(publicKey);
	}
	
	@Test
	public void testGetPublicKey2() throws KeyStoreException {
		
		// core-key.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-key.jks");
		bean.setPassword("privks-key".toCharArray());
		
		try {
			KeyStoreUtil.getPublicKey(bean, "core");
			Assert.fail("publickey_notfound CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.publickey_notfound", e.getCode());
		}

		PublicKey publicKey = KeyStoreUtil.getPublicKey(bean, "core-key");
		Assert.assertNotNull(publicKey);
	}
	
	@Test
	public void testGetPublicKey3() throws KeyStoreException {
		
		// rsa-keypair.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/rsa-keypair.jks");
		bean.setPassword("kspassword".toCharArray());
		
		PublicKey publicKey = KeyStoreUtil.getPublicKey(bean, "myalias");
		Assert.assertNotNull(publicKey);
	}
	
	//
	
	@Test
	public void testGetPrivateKey1() throws KeyStoreException {
		
		// core-certificate.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-certificate.jks");
		bean.setPassword("pubks-key".toCharArray());

		try {
			KeyStoreUtil.getPrivateKey(bean, "core", null);
			Assert.fail("privatekey_notfound CryptographyException expected");
			
		} catch (CryptographyException e) {
			Assert.assertEquals("e.org.altemista.cloudfwk.common.cryptography.exception.privatekey_notfound", e.getCode());
		}
	}
	
	@Test
	public void testGetPrivateKey2() throws KeyStoreException {
		
		// core-key.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/core-key.jks");
		bean.setPassword("privks-key".toCharArray());
		
		PrivateKey privateKey = KeyStoreUtil.getPrivateKey(bean, "core-key", null);
		Assert.assertNotNull(privateKey);
	}
	
	@Test
	public void testGetPrivateKey3() throws KeyStoreException {
		
		// rsa-keypair.jks
		
		KeyStoreBean bean = new KeyStoreBean();
		bean.setResourceLocation("classpath:keystores/rsa-keypair.jks");
		bean.setPassword("kspassword".toCharArray());
		
		PrivateKey privateKey = KeyStoreUtil.getPrivateKey(bean, "myalias", null);
		Assert.assertNotNull(privateKey);
	}
}
