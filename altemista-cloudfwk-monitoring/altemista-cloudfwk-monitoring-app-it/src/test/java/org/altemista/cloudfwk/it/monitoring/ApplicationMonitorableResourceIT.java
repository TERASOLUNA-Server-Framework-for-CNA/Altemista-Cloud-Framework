/**
 * 
 */
package org.altemista.cloudfwk.it.monitoring;

/*
 * #%L
 * altemista-cloud monitoring integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.monitoring.controller.ApplicationTestingMonitoringController;

/**
 * Integration test that invokes the REST controller to test ApplicationMonitorableResourceImpl
 * @author NTT DATA
 */
public class ApplicationMonitorableResourceIT extends AbstractIT {
	
	@Test
	public void testMonitorableResourceController() {
		this.testMappings(ApplicationTestingMonitoringController.MAPPING);
	}
	
}
