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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.core.workflow.WorkflowHumanTaskService;
import org.altemista.cloudfwk.core.workflow.model.WorkflowTask;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

/**
 * Tests the human task service Activiti implementation
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = WorkflowHumanTaskServiceController.MAPPING, method = RequestMethod.GET)
public class WorkflowHumanTaskServiceController {
	
	/** MAPPING String */
	public static final String MAPPING = "/workflowHumanTaskService";
	
	/** No pagination */
	private static final Pageable NO_PAGINATION = new PageRequest(0, Integer.MAX_VALUE);
	
	/** The human task service */
	@Autowired
	private WorkflowHumanTaskService taskService;
	
	/**
	 * Tests the findTasks method
	 * @return Page of WorkflowTask
	 */
	@RequestMapping("/findTasks")
	public Page<WorkflowTask> testfindTasks() {
		
		final Page<WorkflowTask> page = this.taskService.findTasks(NO_PAGINATION);
		ITControllerUtil.assertNotNull(page);
		return page;
	}
	
	/**
	 * Tests the complete method
	 * @param taskId String
	 */
	@RequestMapping("/complete/{id}")
	public void testComplete(@PathVariable("id") String taskId) {
		
		this.taskService.completeTask(taskId);
	}
}
