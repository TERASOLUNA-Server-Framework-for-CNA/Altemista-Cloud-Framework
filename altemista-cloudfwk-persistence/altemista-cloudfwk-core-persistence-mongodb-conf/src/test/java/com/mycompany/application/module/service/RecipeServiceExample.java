package com.mycompany.application.module.service;

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


import java.util.List;

import com.mycompany.application.module.model.RecipeExample;

public interface RecipeServiceExample {
	
	void addRecipe();
	
	List<RecipeExample> findRecipes();

	void deleteRecipe();

	void update();

	void paging();

	Iterable<RecipeExample> queryDslPredicateExecutorExamples(boolean fast);

}
