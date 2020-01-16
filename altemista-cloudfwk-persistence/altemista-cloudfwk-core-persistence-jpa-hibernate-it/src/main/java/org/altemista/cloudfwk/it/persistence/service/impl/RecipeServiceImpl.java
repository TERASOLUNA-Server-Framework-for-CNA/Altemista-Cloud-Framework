package org.altemista.cloudfwk.it.persistence.service.impl;

import java.util.List;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.it.persistence.model.QRecipe;
import org.altemista.cloudfwk.it.persistence.model.Recipe;
import org.altemista.cloudfwk.it.persistence.repository.RecipeRepository;
import org.altemista.cloudfwk.it.persistence.service.RecipeService;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
	
	@Autowired
	private RecipeRepository repository;
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.persistence.service.RecipeService#crudRepositoryExamples()
	 */
	@Override
	public void crudRepositoryExamples() {
		
		int count = (int) (this.repository.count());
		if (count == 0) {
			return;
		}
		
		List<Recipe> recipes = this.repository.findAll();
		ITControllerUtil.assertNotNull(recipes);
		ITControllerUtil.assertEquals(count, recipes.size());
		
		Recipe recipe = recipes.iterator().next();
		Recipe recipeFound = this.repository.findRecipeByName(recipe.getName());
		ITControllerUtil.assertEquals(recipe, recipeFound);
		
		recipe.setName("delicious " + recipe.getName());
		ITControllerUtil.assertNotNull(recipe = this.repository.save(recipe));
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.persistence.service.RecipeService#pagingAndSortingRepositoryExamples()
	 */
	@Override
	public void pagingAndSortingRepositoryExamples() {
		
		long count = this.repository.count();
		if (count == 0L) {
			return;
		}
		
		Page<Recipe> page = this.repository.findAll(new PageRequest(1, 1));
		ITControllerUtil.assertNotNull(page);
		ITControllerUtil.assertEquals(1, page.getSize());
		ITControllerUtil.assertEquals(1, page.getNumberOfElements());
		ITControllerUtil.assertEquals(count, page.getTotalElements());
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.persistence.service.RecipeService#queryDslPredicateExecutorExamples()
	 */
	@Override
	public void queryDslPredicateExecutorExamples() {
		
		ITControllerUtil.assertNotNull(this.repository.findOne(QRecipe.recipe.name.containsIgnoreCase("cake")));
		
		Iterable<Recipe> recipes = this.repository.findAll(QRecipe.recipe.ingredients.isNotEmpty());
		for (Recipe recipe : recipes) {
			ITControllerUtil.assertFalse(recipe.getIngredients().isEmpty());
		}
		
		recipes = this.repository.findAll(QRecipe.recipe.ingredients.isEmpty());
		for (Recipe recipe : recipes) {
			ITControllerUtil.assertTrue(recipe.getIngredients().isEmpty());
		}
	}
}
