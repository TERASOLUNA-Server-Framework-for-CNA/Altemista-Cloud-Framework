/**
 * 
 */
package cloud.altemista.fwk.it.monitoring;

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
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.monitoring.controller.ApplicationTestingMonitoringController;

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
