
package org.altemista.cloudfwk.it.web.reporting.model;

/*
 * #%L
 * altemista-cloud web reporting integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * The persistent class for the t_account database table.
 * 
 * @author NTT DATA
 * 
 */
public class Account {

	/** The entity id. */
	private int entityId;

	/** The name. */
	private String name;

	/** The number. */
	private String number;

	/**
	 * @return the entityId
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
}
