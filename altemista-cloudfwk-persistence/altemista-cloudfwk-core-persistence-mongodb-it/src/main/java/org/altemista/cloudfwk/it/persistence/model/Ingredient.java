/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.model;

/*
 * #%L
 * altemista-cloud persistence: MongoDb integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Ingredient (example database)
 * 
 * @author NTT DATA
 */

@Document
public class Ingredient implements Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = -1703025722018451199L;

	@Id
	private long id;

	private String name;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
