/**
 * 
 */
package cloud.altemista.fwk.it.persistence.model;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ingredient (example database)
 * @author NTT DATA
 */
@Entity
@Table(name = "T_INGREDIENT")
public class Ingredient implements Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -1703025722018451199L;

	@Id
	@Column(nullable = false)
	private long id;
	
	@Column(nullable = false)
	private String name;

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

}
