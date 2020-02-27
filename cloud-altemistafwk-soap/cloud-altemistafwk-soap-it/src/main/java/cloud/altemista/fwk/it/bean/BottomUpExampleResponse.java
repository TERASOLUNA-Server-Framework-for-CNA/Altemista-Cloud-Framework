package cloud.altemista.fwk.it.bean;

/*
 * #%L
 * altemista-cloud SOAP client/server integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

/**
 * Example response bean for the bottom-up (contract-last) service
 * @author NTT DATA
 */
public class BottomUpExampleResponse implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** The greeting */
	private String greeting;

	/**
	 * @return the greeting
	 */
	public String getGreeting() {
		return this.greeting;
	}

	/**
	 * @param greeting the greeting to set
	 */
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
}
