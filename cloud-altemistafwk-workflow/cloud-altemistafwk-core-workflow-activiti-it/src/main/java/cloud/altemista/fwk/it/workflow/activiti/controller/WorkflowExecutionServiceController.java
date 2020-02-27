/**
 * 
 */
package cloud.altemista.fwk.it.workflow.activiti.controller;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.core.workflow.WorkflowExecutionService;
import cloud.altemista.fwk.it.util.ITControllerUtil;

/**
 * Tests the workflow execution service Activiti implementation
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = WorkflowExecutionServiceController.MAPPING, method = RequestMethod.GET)
public class WorkflowExecutionServiceController {
	
	/** MAPPING String */
	public static final String MAPPING = "/workflowExecutionService";
	
	/** The process definition declared in "oneTaskProcess.bpmn20.xml" */
	private static final String DEFINITION_ID = "oneTaskProcess";
	
	/** The workflow execution service */
	@Autowired
	private WorkflowExecutionService executionService;
	
	/**
	 * Tests the start method
	 * @return instaceId
	 */
	@RequestMapping(path = "/start", produces = "text/plain")
	public String testStartWorkflow() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		ITControllerUtil.assertNotNull(instanceId);
		return instanceId;
	}
}
