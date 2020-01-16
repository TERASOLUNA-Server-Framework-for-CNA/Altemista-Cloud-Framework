//tag::example[]
package com.mycompany.application.module.service;
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.model.IngredientExample;
import com.mycompany.application.module.model.QRecipeExample;
import com.mycompany.application.module.model.RecipeExample;
import com.mycompany.application.module.repository.RecipeRepositoryExample;
import com.querydsl.core.types.dsl.BooleanExpression;
//tag::example[]

@Service
public class RecipeServiceExampleImpl implements RecipeServiceExample {
	
	@Autowired
	private RecipeRepositoryExample repository; //<1>

	//end::example[]	
	
	@Override
	public void addRecipe() {
		long before = this.repository.count();
		
		List<IngredientExample> ingredients = new ArrayList<IngredientExample>(0);
		IngredientExample ing1 = new IngredientExample();
		ing1.setName("potato");
		ing1.setId(6L);
		ingredients.add(ing1);
		
		RecipeExample myRecipe = new RecipeExample();
		myRecipe.setBakingTemperature(500);
		myRecipe.setId(4);
		myRecipe.setBakingTime(120);
		myRecipe.setIngredients(ingredients);
		myRecipe.setName("Something New");
		
		this.repository.insert(myRecipe);
		long after = this.repository.count();
	}
	
	@Override
	
	//tag::example[]
	public List<RecipeExample> findRecipes() {
		return this.repository.findAll(); //<2>
	}

	public RecipeExample findMyRecipe() {
		return this.repository.findRecipeByName("lemonade"); //<3>
	}
	//end::example[]	

	@Override
	public void deleteRecipe() {
		long id = 3;
		long before = this.repository.count();
		this.repository.deleteById(id);
		long after = this.repository.count();
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
	//tag::queryDSL[]
	public Iterable<RecipeExample> queryDslPredicateExecutorExamples(boolean fast) {
		
		BooleanExpression predicate = QRecipeExample.recipeExample.name.contains("cake"); // <1> <2>
		if (fast) {
			predicate = predicate.and(QRecipeExample.recipeExample.bakingTime.loe(60)); // <3>
		}
		return this.repository.findAll(predicate);
	}
	//end::queryDSL[]
}
