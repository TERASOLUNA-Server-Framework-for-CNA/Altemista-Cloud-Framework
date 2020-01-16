//tag::example[]
package com.mycompany.application.module.repository;
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

//tag::example[]

import org.springframework.data.mongodb.repository.MongoRepository;
//end::example[]
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.mycompany.application.module.model.RecipeExample;
//tag::example[]

public interface RecipeRepositoryExample
		extends MongoRepository<RecipeExample, Long>, // <1>
			QuerydslPredicateExecutor<RecipeExample> { // <2>
	
	RecipeExample findRecipeByName(String name); // <3>
}
//end::example[]
