/**
 * 
 */
package cloud.altemista.fwk.it;

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


import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.cache.controller.ApplicationCacheableResourceController;

/**
 * Simple, base integration test to check the cloud-altemistafwk-core-cache module.
 * @author NTT DATA
 */
public class ApplicationCacheableResourceIT extends AbstractIT {
	
	/**
	 * Validates all the controller options
	 */
	@Test
	public void tests() {		
		this.testMappings(ApplicationCacheableResourceController.MAPPING);
	}
	
}
