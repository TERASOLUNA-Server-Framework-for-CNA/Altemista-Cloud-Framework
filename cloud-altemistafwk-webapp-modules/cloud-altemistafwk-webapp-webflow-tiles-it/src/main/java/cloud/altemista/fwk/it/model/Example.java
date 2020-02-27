/**
 * 
 */
package cloud.altemista.fwk.it.model;

/*
 * #%L
 * altemista-cloud web application: Spring Web Flow + Apache Tiles integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

/**
 * Spring Web Flow variable example
 * @author NTT DATA
 */
public class Example implements Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -925197558959301344L;
	
	/** The name */
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
