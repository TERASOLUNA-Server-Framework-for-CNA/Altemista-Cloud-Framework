
package org.altemista.cloudfwk.test.cryptography.util;

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


import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.altemista.cloudfwk.test.util.TestConstants;

public class AESExampleTest {
	
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
		String password = "Q726QuDoXN19yXrvijRP";
		String salt = "691eca1d48eb7cd7";
		TextEncryptor aesTexTencryptor = Encryptors.text(password, salt); //<1>
		
		String encryptedText = aesTexTencryptor.encrypt(clearText); //<2>
		
		String decryptedText = aesTexTencryptor.decrypt(encryptedText); //<3>
		//end::text[]
		
		Assert.assertEquals(clearText, decryptedText);
	}

	@Test
	public void binaryExample() {
		
		byte[] rawBytes = TestConstants.BINARY;
		
		String password = "A4JChmMgP5e4NKVD1oEn";
		String salt = "28c93eebf9ee3b4c";
		//tag::binary[]
		BytesEncryptor aesBytesEncryptor = Encryptors.standard(password, salt); //<1>
		
		byte[] encryptedBytes = aesBytesEncryptor.encrypt(rawBytes);
		
		byte[] decryptedBytes = aesBytesEncryptor.decrypt(encryptedBytes);
		//end::binary[]
		
		Assert.assertArrayEquals(rawBytes, decryptedBytes);
	}
}
