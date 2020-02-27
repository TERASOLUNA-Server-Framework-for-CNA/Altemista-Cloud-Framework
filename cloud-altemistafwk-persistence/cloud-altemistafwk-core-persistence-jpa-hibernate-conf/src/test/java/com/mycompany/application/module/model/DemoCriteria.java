package com.mycompany.application.module.model;

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


public class DemoCriteria {
	
	private Long minId;
	
	private Long maxId;
	
	private String name;

	/**
	 * @return the minId
	 */
	public Long getMinId() {
		return minId;
	}

	/**
	 * @param minId the minId to set
	 */
	public void setMinId(Long minId) {
		this.minId = minId;
	}

	/**
	 * @return the maxId
	 */
	public Long getMaxId() {
		return maxId;
	}

	/**
	 * @param maxId the maxId to set
	 */
	public void setMaxId(Long maxId) {
		this.maxId = maxId;
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
