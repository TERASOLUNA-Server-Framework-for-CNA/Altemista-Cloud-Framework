package org.altemista.cloudfwk.it.persistence.service;

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


public interface RecipeService {
	
	void addRecipe();
	
	void findRecipes();

	void deleteRecipe();

	void update();

	void paging();

	void queryDslPredicateExecutorExamples();

}
