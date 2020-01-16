package org.altemista.cloudfwk.it.cache.service;

/*
 * #%L
 * altemista-cloud cache: Ehcache integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.core.cache.annotation.CacheableServiceMethod;

@Service
public class ApplicationCacheableService {
	
	private int counterCacheable = 0;
	private int counterNonCacheable = 0;
	
	public int getCounterNonCacheable() {
		return counterNonCacheable;
	}
	
	public int getCounterCacheable() {
		return counterCacheable;
	}

	@CacheableServiceMethod
	public int getCacheableAccount(Integer id) {
		counterCacheable++;
		return counterCacheable;
	}
	
	public int getNonCacheableAccount(Integer id) {
		counterNonCacheable++;
		return counterNonCacheable;
	}
}
