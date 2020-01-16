/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.repository;

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


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.altemista.cloudfwk.it.persistence.model.Recipe;



//tag::example[]
public interface RecipeRepository extends MongoRepository<Recipe, Long>, QuerydslPredicateExecutor<Recipe> {
	Recipe findRecipeByName(String name);
}
//end::example[]	
