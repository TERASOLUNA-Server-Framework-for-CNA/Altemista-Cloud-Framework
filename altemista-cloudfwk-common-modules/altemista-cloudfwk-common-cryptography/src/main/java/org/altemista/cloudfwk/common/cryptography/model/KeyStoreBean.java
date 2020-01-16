package org.altemista.cloudfwk.common.cryptography.model;

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


import java.io.Serializable;

/**
 * POJO with a key or trust store information.
 * @author NTT DATA
 */
public class KeyStoreBean implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -5625151584673397182L;

	/** Key store type: "pkcs12" (The transfer syntax for personal identity information as defined in PKCS12). */
	public static final String TYPE_PKCS12 = "pkcs12";

	/** Key store type: "jks" (The proprietary keystore implementation provided by the "SUN" provider). */
	public static final String TYPE_JKS = "jks";
	
	/** Key store type: "jceks" (the proprietary keystore implementation provided by the "SunJCE" provider). */
	public static final String TYPE_JCEKS = "jceks";

	/** Default value for the key store type. */
	public static final String DEFAULT_TYPE = TYPE_JKS;

	/**
	 * The resource location to the java key store:
	 * either a "classpath:" pseudo URL, a "file:" URL, or a plain file path.
	 */
	private String resourceLocation;

	/** The password for the java key store. */
	private transient char[] password;
	
	/** The key store type. */
	private String type = DEFAULT_TYPE;

	/**
	 * @return the resourceLocation
	 */
	public String getResourceLocation() {
	
		return this.resourceLocation;
	}

	/**
	 * @param resourceLocation the resourceLocation to set
	 */
	public void setResourceLocation(String resourceLocation) {
	
		this.resourceLocation = resourceLocation;
	}

	/**
	 * @return the password
	 */
	public char[] getPassword() {
	
		return this.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(char[] password) {
	
		this.password = password;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		
		this.type = type;
	}
}
