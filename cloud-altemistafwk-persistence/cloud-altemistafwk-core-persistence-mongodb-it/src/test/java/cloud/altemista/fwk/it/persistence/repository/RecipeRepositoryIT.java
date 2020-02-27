/**
 * 
 */
package cloud.altemista.fwk.it.persistence.repository;

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


import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.persistence.controller.RecipeController;

public class RecipeRepositoryIT extends AbstractIT {
	
	@Test
	public void readOnlyCrudExample() {
		
		this.testMappings(RecipeController.MAPPING);
	}
	
}
