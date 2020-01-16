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


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.it.AbstractWebDriverIT;
import org.altemista.cloudfwk.it.WebDriverItRequest;
import org.altemista.cloudfwk.it.workflow.activiti.controller.WorkflowExecutionServiceController;
import org.altemista.cloudfwk.it.workflow.activiti.controller.WorkflowHistoryServiceController;
import org.altemista.cloudfwk.it.workflow.activiti.controller.WorkflowHumanTaskServiceController;

/**
 * Tests the workflow execution service Activiti implementation
 * @author NTT DATA
 */
public class WorkflowIT extends AbstractWebDriverIT {
	
	/** Extracts the first "taskId" value from a JSON */
	private static final Pattern PATTERN_TASK_ID = Pattern.compile(".*?\"taskId\"\\s*:\\s*\"([^\"]*)\".*");
	
	/** Extracts the first "active" value from a JSON */
	private static final Pattern PATTERN_ACTIVE = Pattern.compile(".*?\"active\"\\s*:\\s*(\\w*).*");
	
	/**
	 * Validates a rather simplistic workflow execution
	 */
	@Test
	public void workflowTests() {
		
		// Starts a workflow instance (with login)
		WebDriverItRequest request = this.navigateAndLogin(
				WorkflowExecutionServiceController.MAPPING + "/start", "user", "password");
		
		// Ensures the response was a valid process instance id
		String processInstanceId = request.getResponseBody();
		Assert.assertNotNull(processInstanceId);
		Assert.assertNotEquals(StringUtils.EMPTY, processInstanceId);
		Assert.assertTrue(StringUtils.isAlphanumeric(processInstanceId));
		
		// Retrieves a list of the tasks
		String tasks = this.testMapping(
				WorkflowHumanTaskServiceController.MAPPING + "/findTasks").getResponseBody();
		final Matcher taskIdMatcher = PATTERN_TASK_ID.matcher(tasks);
		Assert.assertTrue(taskIdMatcher.matches());
		Assert.assertTrue(taskIdMatcher.groupCount() >= 1);
		final String taskId = taskIdMatcher.group(1);
		Assert.assertNotNull(taskId);
		
		// Retrieves the process instance
		String processInstance = this.testMapping(
				WorkflowHistoryServiceController.MAPPING + "/getProcessInstance/" + processInstanceId).getResponseBody();
		Matcher activeMatcher = PATTERN_ACTIVE.matcher(processInstance);
		Assert.assertTrue(activeMatcher.matches());
		Assert.assertTrue(activeMatcher.groupCount() >= 1);
		
		// The process instance should be active at this point
		Assert.assertTrue(BooleanUtils.toBoolean(activeMatcher.group(1)));
		
		// Completes the task
		this.testMapping(WorkflowHumanTaskServiceController.MAPPING + "/complete/" + taskId);
		
		// Retrieves the process instance again
		processInstance = this.testMapping(
				WorkflowHistoryServiceController.MAPPING + "/getProcessInstance/" + processInstanceId).getResponseBody();
		activeMatcher = PATTERN_ACTIVE.matcher(processInstance);
		Assert.assertTrue(activeMatcher.matches());
		Assert.assertTrue(activeMatcher.groupCount() >= 1);
		
		// The process instance should be no longer active at this point
		Assert.assertFalse(BooleanUtils.toBoolean(activeMatcher.group(1)));
	}
}
