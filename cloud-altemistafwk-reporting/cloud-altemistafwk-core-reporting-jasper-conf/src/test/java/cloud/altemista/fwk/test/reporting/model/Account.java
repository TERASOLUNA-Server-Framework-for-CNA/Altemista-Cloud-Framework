
package cloud.altemista.fwk.test.reporting.model;

/*
 * #%L
 * altemista-cloud reporting: JasperReports CONF
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the t_account database table.
 * 
 * @author NTT DATA
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "searchByNamedName", query = "FROM Account WHERE name =:namevalue"),
		@NamedQuery(name = "searchByName", query = "FROM Account WHERE name = ?"),
		@NamedQuery(name = "searchAll", query = "select a from Account a where true=true") })
@Table(name = "T_ACCOUNT")
public class Account implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The entity id. */
	@Id
	@Column(name = "ID")
	private int entityId;

	/** The name. */
	@Column(name = "NAME")
	private String name;

	/** The number. */
	@Column(name = "NUMBER")
	private String number;
	
	/**
	 * Instantiates a new account.
	 */
	public Account() {
	}

	/**
	 * Instantiates a new account.
	 *
	 * @param number the number
	 * @param name the name
	 */
	public Account(String number, String name) {
		this.number = number;
		this.name = name;
	}

	/**
	 * Gets the entity id.
	 *
	 * @return the entity id
	 */
	public int getEntityId() {
		return this.entityId;
	}

	/**
	 * Sets the entity id.
	 *
	 * @param entityId the new entity id
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public String getNumber() {
		return this.number;
	}

	/**
	 * Sets the number.
	 *
	 * @param number the new number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

}
