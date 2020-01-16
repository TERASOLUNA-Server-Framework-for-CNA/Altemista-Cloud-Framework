//tag::example[]
package com.mycompany.application.module.model;
//end::example[]

/*
 * #%L
 * altemista-cloud persistence: MongoDB (Spring Data) CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
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
 * @author NTT DATA
 */

//tag::example[]

@Document(collection = "kitchen") //<1>
public class RecipeExample implements Serializable {

	//end::example[]
	/** The serialVersionUID long */
	private static final long serialVersionUID = 5785027561475483165L;

	//tag::example[]
	@Id //<3>
	private long id; //<2>

	private String name; //<2>
	private int bakingTemperature; //<2>
	private int bakingTime; //<2>

	@DBRef
	private List<IngredientExample> ingredients;  //<4>
	//end::example[]
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBakingTemperature() {
		return bakingTemperature;
	}

	public void setBakingTemperature(int bakingTemperature) {
		this.bakingTemperature = bakingTemperature;
	}

	public int getBakingTime() {
		return bakingTime;
	}

	public void setBakingTime(int bakingTime) {
		this.bakingTime = bakingTime;
	}

	public List<IngredientExample> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<IngredientExample> ingredients) {
		this.ingredients = ingredients;
	}
}
