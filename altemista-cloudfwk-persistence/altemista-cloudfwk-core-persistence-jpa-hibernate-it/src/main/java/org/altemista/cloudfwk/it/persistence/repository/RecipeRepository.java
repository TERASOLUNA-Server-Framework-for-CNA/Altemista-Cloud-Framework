/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.repository;

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


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.it.persistence.model.Recipe;

@Transactional(propagation = Propagation.MANDATORY)
public interface RecipeRepository extends JpaRepository<Recipe, Long>, QuerydslPredicateExecutor<Recipe> {
	
	Recipe findRecipeByName(String name);
}
