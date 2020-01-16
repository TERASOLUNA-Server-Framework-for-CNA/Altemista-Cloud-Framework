/**
 * 
 */
package org.altemista.cloudfwk.it.workflow.activiti.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.core.workflow.WorkflowHistoryService;
import org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

/**
 * Tests the historic workflows service Activiti implementation
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = WorkflowHistoryServiceController.MAPPING, method = RequestMethod.GET)
public class WorkflowHistoryServiceController {
	
	/** MAPPING String */
	public static final String MAPPING = "/workflowHistoryService";
	
	/** The historic workflows service interface */
	@Autowired
	private WorkflowHistoryService historyService;
	
	/**
	 * Tests the findTasks method
	 * @param processInstanceId String
	 * @return WorkflowHistoricProcessInstance
	 */
	@RequestMapping("/getProcessInstance/{id}")
	public WorkflowHistoricProcessInstance testfindTasks(@PathVariable("id") String processInstanceId) {
		
		final WorkflowHistoricProcessInstance historicInstance = this.historyService.getProcessInstance(processInstanceId);
		ITControllerUtil.assertNotNull(historicInstance);
		return historicInstance;
	}

}
