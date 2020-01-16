/**
 * 
 */
package org.altemista.cloudfwk.test.persistence.repository;

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


import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.test.AbstractPersistenceTest;
import org.altemista.cloudfwk.test.persistence.model.QRecipe;
import org.altemista.cloudfwk.test.persistence.model.Recipe;

@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-jpa.xml")
@Transactional
@Rollback
public class RecipeRepositoryTest extends AbstractPersistenceTest {

	/** The repository RecipeRepository */
	@Autowired
	private RecipeRepository repository;
	
	/**
	 * Rather simplistic tests for the "extends CrudRepository" part
	 */
	@Test
	public void testCrudRepository() {
		
		int count = (int) (this.repository.count());
		Assume.assumeTrue(count != 0);
		
		List<Recipe> recipes = this.repository.findAll();
		Assert.assertNotNull(recipes);
		Assert.assertEquals(count, recipes.size());
		
		Recipe recipe = recipes.iterator().next();
		Recipe recipeFound = this.repository.findRecipeByName(recipe.getName());
		Assert.assertEquals(recipe, recipeFound);
		
		recipe.setName("new name");
		Assert.assertNotNull(recipe = this.repository.save(recipe));
	}
	
	/**
	 * Rather simplistic tests for the "extends PagingAndSortingRepository" part
	 */
	@Test
	public void testPagingAndSortingRepository() {
		
		long count = (int) (this.repository.count());
		Assume.assumeTrue(count != 0L);
		
		Page<Recipe> page = this.repository.findAll(new PageRequest(1, 1));
		Assert.assertNotNull(page);
		Assert.assertEquals(1, page.getSize());
		Assert.assertEquals(1, page.getNumberOfElements());
		Assert.assertEquals(count, page.getTotalElements());
	}
	
	/**
	 * Rather simplistic tests for the "extends QueryDslPredicateExecutor" part
	 */
	@Test
	public void testQueryDslPredicateExecutor() {
		
		Assert.assertNotNull(this.repository.findOne(QRecipe.recipe.name.contains("cake")));
		
		Iterable<Recipe> recipes = this.repository.findAll(QRecipe.recipe.ingredients.isNotEmpty());
		for (Recipe recipe : recipes) {
			Assert.assertFalse(recipe.getIngredients().isEmpty());
		}
		
		recipes = this.repository.findAll(QRecipe.recipe.ingredients.isEmpty());
		for (Recipe recipe : recipes) {
			Assert.assertTrue(recipe.getIngredients().isEmpty());
		}
	}
}
