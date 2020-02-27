package cloud.altemista.fwk.common.connection.model;

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


import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.util.Assert;

/**
 * POJO with a proxy information.
 * @author NTT DATA
 */
public class ProxyBean implements Serializable {

	/** serialVersionUID long */
	private static final long serialVersionUID = 993559251710118636L;

	/** The IP address of the proxy. */
	private String hostname;

	/** The port number of the proxy. */
	private int port;

	/** The username to use in the proxy. */
	private String username;

	/** The password to use in the proxy. */
	private String password;
	
	/**
	 * Default constructor.
	 */
	public ProxyBean() {
		super();
	}
	
	/**
	 * Validates there is a hostname
	 */
	@PostConstruct
	public void validate() {
		Assert.hasText(this.hostname);
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
