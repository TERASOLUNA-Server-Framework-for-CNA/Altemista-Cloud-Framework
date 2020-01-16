/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.model;

/*
 * #%L
 * altemista-cloud persistence: JPA (Hibernate provider) integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

//tag::example[]
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//end::example[]
/**
 * Measurement unit (example database)
 * @author NTT DATA
 */
//tag::example[]
@Entity
@Table(name = "T_UNIT")
public class Unit implements Serializable {
	
	//end::example[]
	/** The serialVersionUID long */
	private static final long serialVersionUID = 3684654109887779273L;

	//tag::example[]
	@Id
	@Column(nullable = false)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	//end::example[]
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
