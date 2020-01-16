package org.altemista.cloudfwk.it.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;

/*
 * #%L
 * altemista-cloud cache: Spring integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.it.cache.application.ApplicationCacheable;
import org.altemista.cloudfwk.it.cache.element.ApplicationCacheableDefaultElement;
import org.altemista.cloudfwk.it.cache.repository.ApplicationCacheableRepository;
import org.altemista.cloudfwk.it.cache.service.ApplicationCacheableService;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

/**
 * Simple REST controller to check the cache module
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = ApplicationCacheableResourceController.MAPPING, method = RequestMethod.GET)
public class ApplicationCacheableResourceController {
	
	/** MAPPING String */
	public static final String MAPPING = "/applicationCacheableResource";
	
	/** The default application cacheable resource. */
	@Autowired
	private ApplicationCacheable app;
	
	@Autowired
	private ApplicationCacheableService service;
	
	@Autowired
	private ApplicationCacheableRepository repository;
	
	@Autowired
	private ApplicationCacheableDefaultElement defaultElement;
	
	
	@RequestMapping("/1")
	public void getCacheableServiceElement() {
		ApplicationCacheableService resolver = this.service;
		checkCacheable(resolver.getCounterCacheable(), resolver.getCacheableAccount(987));
	}
	
	@RequestMapping("/2")
	public void getNonCacheableServiceElement() { 
		ApplicationCacheableService resolver = this.service;
		checkNonCacheable(resolver.getCounterNonCacheable(), resolver.getNonCacheableAccount(987));
	}
	
	@RequestMapping("/3")
	public void getCacheableApplicationElement() {
		ApplicationCacheable resolver = this.app;
		checkCacheable(resolver.getCounterCacheable(), resolver.getCacheableAccount(987));
	}
	
	@RequestMapping("/4")
	public void getNonCacheableApplicationElement() { 
		ApplicationCacheable resolver = this.app;
		checkNonCacheable(resolver.getCounterNonCacheable(), resolver.getNonCacheableAccount(987));
	}
	
	@RequestMapping("/5")
	public void getCacheableRepositoryElement() {
		ApplicationCacheableRepository resolver = this.repository;
		checkCacheable(resolver.getCounterCacheable(), resolver.getCacheableAccount(987));
	}
	
	@RequestMapping("/6")
	public void getNonCacheableRepositoryElement() { 
		ApplicationCacheableRepository resolver = this.repository;
		checkNonCacheable(resolver.getCounterNonCacheable(), resolver.getNonCacheableAccount(987));
	}
	
	@RequestMapping("/7")
	public void getCacheableDefaultElement() {
		ApplicationCacheableDefaultElement resolver = this.defaultElement;
		checkCacheable(resolver.getCounterCacheable(), resolver.getCacheableAccount(987));
	}
	
	@RequestMapping("/8")
	public void getNonCacheableDefaultElement() {
		ApplicationCacheableDefaultElement resolver = this.defaultElement;
		checkNonCacheable(resolver.getCounterNonCacheable(), resolver.getNonCacheableAccount(987));
	}
	
	private void checkCacheable(int lastCounterValue, int response){
		if (lastCounterValue == 0) { // First time
			ITControllerUtil.assertTrue(response == 1);
		}
		else { // Next times
			ITControllerUtil.assertTrue(response == lastCounterValue);
		}
	}
	
	private void checkNonCacheable(int lastCounterValue, int response){
		ITControllerUtil.assertTrue(response == lastCounterValue + 1);
	}
	

}
