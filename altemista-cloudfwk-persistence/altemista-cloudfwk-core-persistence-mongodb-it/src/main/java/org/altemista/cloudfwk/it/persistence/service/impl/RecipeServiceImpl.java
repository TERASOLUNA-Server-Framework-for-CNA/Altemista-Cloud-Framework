package org.altemista.cloudfwk.it.persistence.service.impl;

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


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.persistence.model.Ingredient;
import org.altemista.cloudfwk.it.persistence.model.QRecipe;
import org.altemista.cloudfwk.it.persistence.model.Recipe;
import org.altemista.cloudfwk.it.persistence.repository.RecipeRepository;
import org.altemista.cloudfwk.it.persistence.service.RecipeService;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

//tag::example[]
@Service
public class RecipeServiceImpl implements RecipeService {
	
	@Autowired
	private RecipeRepository repository;

//end::example[]	
	
	@Override
	public void addRecipe() {
		long before = this.repository.count();
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>(0);
		Ingredient ing1 = new Ingredient();
		ing1.setName("potato");
		ing1.setId(6L);
		ingredients.add(ing1);
		
		Recipe myRecipe = new Recipe();
		myRecipe.setBakingTemperature(500);
		myRecipe.setId(4);
		myRecipe.setBakingTime(120);
		myRecipe.setIngredients(ingredients);
		myRecipe.setName("Something New");
		
		this.repository.insert(myRecipe);
		long after = this.repository.count();
		
		ITControllerUtil.assertTrue(after > before);
	}
	
	@Override
	public void findRecipes() {
		List<Recipe> recipes = this.repository.findAll();
		ITControllerUtil.assertNotNull(recipes);
		ITControllerUtil.assertNotNull(recipes.get(0).getIngredients().get(0));
	}
	
	@Override
	public void deleteRecipe() {
		long id = 3;
		long before = this.repository.count();
		this.repository.deleteById(id);
		long after = this.repository.count();
		
		ITControllerUtil.assertTrue(after < before);
	}
	
	@Override
	public void update() {
		// TODO
	}
	
	@Override
	public void paging() {
		// TODO
	}
	
	@Override
	public void queryDslPredicateExecutorExamples() {
		ITControllerUtil.assertNotNull(
				//tag::queryDSL[]
				this.repository.findOne(QRecipe.recipe.name.contains("cake"))
				//end::queryDSL[]
				);
		
		ITControllerUtil.assertNull(
				//tag::queryDSL[]
				this.repository.findOne(QRecipe.recipe.name.contains("cyanide"))
				//end::queryDSL[]
				);
		
		//tag::queryDSL[]
		Iterable<Recipe> recipes = this.repository.findAll(QRecipe.recipe.ingredients.isNotEmpty());
		//end::queryDSL[]
		for (Recipe recipe : recipes) {
			ITControllerUtil.assertFalse(recipe.getIngredients().isEmpty());
		}
		//tag::queryDSL[]
		recipes = this.repository.findAll(QRecipe.recipe.ingredients.isEmpty());
		//end::queryDSL[]
		for (Recipe recipe : recipes) {
			ITControllerUtil.assertTrue(recipe.getIngredients().isEmpty());
		}
	}
	//end::queryDSL[]
}
