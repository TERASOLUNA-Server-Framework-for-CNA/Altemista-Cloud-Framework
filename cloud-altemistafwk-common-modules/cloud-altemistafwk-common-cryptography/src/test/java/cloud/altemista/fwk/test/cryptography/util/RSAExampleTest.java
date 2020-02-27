
package cloud.altemista.fwk.test.cryptography.util;

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


import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.rsa.crypto.RsaSecretEncryptor;
import cloud.altemista.fwk.test.util.TestConstants;

public class RSAExampleTest {
	
	/**
	 * Avoids "java.security.InvalidKeyException: Illegal key size"
	 */
	@Before
	public void init() {
		
		try {
			Assume.assumeTrue("Full-strengh JCE installed",
					javax.crypto.Cipher.getMaxAllowedKeyLength("AES") >= 256);
			
		} catch (NoSuchAlgorithmException e) {
			Assume.assumeNoException("AES is a known algorithm", e);
		}
	}
	
	@Test
	public void textExample() {
		
		String clearText = TestConstants.TEXT;
		
		//tag::text[]
		Resource keyStoreResource = new ClassPathResource("keystores/rsa-keypair.jks");
		char[] keyStorePassword = "kspassword".toCharArray(); //<1>
		KeyStoreKeyFactory keyStoreKeyFactory =
				new KeyStoreKeyFactory(keyStoreResource, keyStorePassword); // <2>
		String alias = "myalias";
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias); // <3>
		
		RsaSecretEncryptor rsaSecretEncryptor = new RsaSecretEncryptor(keyPair); //<4>
		
		String encryptedText = rsaSecretEncryptor.encrypt(clearText); //<5>
		
		String decryptedText = rsaSecretEncryptor.decrypt(encryptedText); //<6>
		//end::text[]
		
		Assert.assertEquals(clearText, decryptedText);
	}
	
	@Test
	public void binaryExample() {
		
		byte[] rawBytes = TestConstants.BINARY;
		
		ClassPathResource keyStoreResource = new ClassPathResource("keystores/rsa-keypair.jks");
		char[] keyStorePassword = "kspassword".toCharArray();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStoreResource, keyStorePassword);
		
		String alias = "myalias";
		char[] keyPairPassword = "kspassword".toCharArray();
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keyPairPassword);
		
		//tag::binary[]
		RsaSecretEncryptor rsaSecretEncryptor = new RsaSecretEncryptor(keyPair);
		
		byte[] encryptedBytes = rsaSecretEncryptor.encrypt(rawBytes);
		
		byte[] decryptedBytes = rsaSecretEncryptor.decrypt(encryptedBytes);
		//end::binary[]
		
		Assert.assertArrayEquals(rawBytes, decryptedBytes);
	}
}
