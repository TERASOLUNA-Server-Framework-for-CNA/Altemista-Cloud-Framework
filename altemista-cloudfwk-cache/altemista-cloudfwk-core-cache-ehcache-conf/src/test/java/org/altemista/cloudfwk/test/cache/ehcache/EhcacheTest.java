
package org.altemista.cloudfwk.test.cache.ehcache;

/*
 * #%L
 * altemista-cloud cache: Ehcache implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.example.cache.CachedService;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Verifies the Ehcache-based cache configuration
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-cache-ehcache.xml")
public class EhcacheTest extends AbstractSpringContextTest {
	
	/** The cached service */
	@Autowired
	public CachedService service;
	
	@Test
	public void testSpringContext() {
		
		// (simply ensures the Spring Context configuration is complete)
		Assert.assertNotNull(this.service);
	}
	
	@Test
	public void testNonCachedMethod() {
		
		Assume.assumeNotNull(this.service);
		
		String firstResult = this.service.nonCachedMethod();
		Assert.assertNotNull(firstResult);
		
		String secondResult = this.service.nonCachedMethod();
		Assert.assertNotNull(secondResult);
		Assert.assertNotEquals(firstResult, secondResult);
	}
	
	@Test
	public void testCachedMethod() {
		
		Assume.assumeNotNull(this.service);
		
		String firstResult = this.service.cachedMethod();
		Assert.assertNotNull(firstResult);
		
		String secondResult = this.service.cachedMethod();
		Assert.assertNotNull(secondResult);
		Assert.assertEquals(firstResult, secondResult);
	}
}
