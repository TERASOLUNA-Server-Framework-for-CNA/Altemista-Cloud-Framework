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
 * Example request bean for the bottom-up (contract-last) service
 * @author NTT DATA
 */
public class BottomUpExampleRequest implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** The name */
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
