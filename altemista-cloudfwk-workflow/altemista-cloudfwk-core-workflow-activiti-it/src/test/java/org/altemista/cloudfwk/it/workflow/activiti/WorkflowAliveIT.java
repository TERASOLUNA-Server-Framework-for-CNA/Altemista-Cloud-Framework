/**
 * 
 */
package org.altemista.cloudfwk.it.workflow.activiti;

/*
 * #%L
 * altemista-cloud workflow integration tests (Activiti implementation)
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.workflow.activiti.controller.WorkflowAliveController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WorkflowAliveIT extends AbstractIT {
	
	/**
	 * Validates the application is deployed
	 */
	@Test
	public void alive() {
		
		this.testMapping(WorkflowAliveController.MAPPING);
	}
}
