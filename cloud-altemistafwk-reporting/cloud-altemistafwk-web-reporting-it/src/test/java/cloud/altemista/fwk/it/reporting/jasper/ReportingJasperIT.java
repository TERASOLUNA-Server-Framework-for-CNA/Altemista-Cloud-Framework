/**
 * 
 */
package cloud.altemista.fwk.it.reporting.jasper;

/*
 * #%L
 * altemista-cloud web reporting integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.web.reporting.controller.JasperReportController;

/**
 * Integration test that invokes the REST controller to test ExceptionCounterAspectImpl
 * @author NTT DATA
 */
public class ReportingJasperIT extends AbstractIT {
	
	@Test
	public void testExceptionCounterController() {
		
		this.testMappings(JasperReportController.MAPPING);
	}
}
