/**
 * 
 */
package org.altemista.cloudfwk.test.persistence.repository;

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


import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.persistence.controller.RecipeController;

public class RecipeRepositoryIT extends AbstractIT {
	
	@Test
	public void jpaRepositoryTest() {
		
		this.testMappings(RecipeController.MAPPING);
	}
}
