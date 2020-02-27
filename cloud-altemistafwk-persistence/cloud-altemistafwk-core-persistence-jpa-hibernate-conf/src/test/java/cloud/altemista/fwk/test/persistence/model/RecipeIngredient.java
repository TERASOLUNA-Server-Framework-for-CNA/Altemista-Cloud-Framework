/**
 * 
 */
package cloud.altemista.fwk.test.persistence.model;

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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Recipe ingredient (example database)
 * @author NTT DATA
 */
@Entity
@Table(name = "T_RECIPE_INGREDIENT")
public class RecipeIngredient implements Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = 4611489829840179426L;

	@Id
	@Column(nullable = false)
	private long id;
	
	@JoinColumn(name = "RECIPE_ID", nullable = false)
	@ManyToOne(optional = false)
	private Recipe recipe;
	
	@JoinColumn(name = "INGREDIENT_ID", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private Ingredient ingredient;
	
	@Column(nullable = false)
	private long amount;
	
	@JoinColumn(name = "UNIT_ID", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private Unit unit;

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
	 * @return the recipe
	 */
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * @param recipe the recipe to set
	 */
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	/**
	 * @return the ingredient
	 */
	public Ingredient getIngredient() {
		return ingredient;
	}

	/**
	 * @param ingredient the ingredient to set
	 */
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
