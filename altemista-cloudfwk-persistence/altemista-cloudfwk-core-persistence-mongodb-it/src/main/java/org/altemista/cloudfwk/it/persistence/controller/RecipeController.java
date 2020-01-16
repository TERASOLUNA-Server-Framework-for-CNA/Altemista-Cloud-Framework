/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.controller;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.it.persistence.service.RecipeService;

/**
 * 
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = RecipeController.MAPPING, method = RequestMethod.GET)
public class RecipeController {
	
	/** MAPPING String */
	public static final String MAPPING = "/recipe";
	
	@Autowired
	private RecipeService service;
	
	@RequestMapping("/1")
	public void addRecipe() {
		this.service.addRecipe();
	}
	
	@RequestMapping("/2")
	public void findRecipes() {
		this.service.findRecipes();
	}
	
	@RequestMapping("/3")
	public void deleteRecipe() {
		this.service.deleteRecipe();
	}
	
	@RequestMapping("/4")
	public void queryDslPredicateExecutorExamples() {
		this.service.queryDslPredicateExecutorExamples();
	}

}
