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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Recipe (example database)
 * @author NTT DATA
 */
@Entity
@Table(name = "T_RECIPE")
public class Recipe implements Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = 5785027561475483165L;

	@Id
	@Column(nullable = false)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "BAKING_TEMP", nullable = false)
	private int bakingTemperature;
	
	@Column(name = "BAKING_TIME", nullable = false)
	private int bakingTime;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe")
	private List<RecipeIngredient> ingredients;

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
	 * @return the bakingTemperature
	 */
	public int getBakingTemperature() {
		return bakingTemperature;
	}

	/**
	 * @param bakingTemperature the bakingTemperature to set
	 */
	public void setBakingTemperature(int bakingTemperature) {
		this.bakingTemperature = bakingTemperature;
	}

	/**
	 * @return the bakingTime
	 */
	public int getBakingTime() {
		return bakingTime;
	}

	/**
	 * @param bakingTime the bakingTime to set
	 */
	public void setBakingTime(int bakingTime) {
		this.bakingTime = bakingTime;
	}

	/**
	 * @return the ingredients
	 */
	public List<RecipeIngredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(List<RecipeIngredient> ingredients) {
		this.ingredients = ingredients;
	}

}
