/**
 * 
 */
package org.altemista.cloudfwk.it.persistence.controller;

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
	public void crudRepositoryExamples() {
		
		this.service.crudRepositoryExamples();
	}
	
	@RequestMapping("/2")
	public void pagingAndSortingRepositoryExamples() {
		
		this.service.pagingAndSortingRepositoryExamples();
	}
	
	@RequestMapping("/3")
	public void queryDslPredicateExecutorExamples() {
		
		this.service.queryDslPredicateExecutorExamples();
	}

}
