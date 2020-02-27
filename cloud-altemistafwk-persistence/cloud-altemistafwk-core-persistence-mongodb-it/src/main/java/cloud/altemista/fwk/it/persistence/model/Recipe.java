/**
 * 
 */
package cloud.altemista.fwk.it.persistence.model;

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
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Recipe (example database)
 * 
 * @author NTT DATA
 */

@Document(collection = "kitchen")
public class Recipe implements Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = 5785027561475483165L;

	@Id
	private long id;

	private String name;

	private int bakingTemperature;

	private int bakingTime;

	@DBRef
	private List<Ingredient> ingredients;

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

	/**
	 * @return the bakingTemperature
	 */
	public int getBakingTemperature() {
		return bakingTemperature;
	}

	/**
	 * @param bakingTemperature
	 *            the bakingTemperature to set
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
	 * @param bakingTime
	 *            the bakingTime to set
	 */
	public void setBakingTime(int bakingTime) {
		this.bakingTime = bakingTime;
	}

	/**
	 * @return the ingredients
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients
	 *            the ingredients to set
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

}
