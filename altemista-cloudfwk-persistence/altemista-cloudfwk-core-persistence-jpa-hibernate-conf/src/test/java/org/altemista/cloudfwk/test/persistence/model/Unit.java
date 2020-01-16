/**
 * 
 */
package org.altemista.cloudfwk.test.persistence.model;

/*
 * #%L
 * altemista-cloud persistence: JPA (Hibernate provider) CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Measurement unit (example database)
 * @author NTT DATA
 */
@Entity
@Table(name = "T_UNIT")
public class Unit implements Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = 3684654109887779273L;

	@Id
	@Column(nullable = false)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int grams;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the grams
	 */
	public int getGrams() {
		return grams;
	}

	/**
	 * @param grams the grams to set
	 */
	public void setGrams(int grams) {
		this.grams = grams;
	}
}
