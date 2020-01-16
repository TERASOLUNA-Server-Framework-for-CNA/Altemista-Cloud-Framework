
package org.altemista.cloudfwk.core.workflow;

/*
 * #%L
 * altemista-cloud workflow
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Map;

/**
 * The workflow execution service interface.
 * This service is the main interface of the workflow module and allows you to execute business processes.
 * @author NTT DATA
 */
public interface WorkflowExecutionService
		extends WorkflowAuthenticationAwareExecutionService,
		AuthenticableWorkflowService<WorkflowAuthenticationAwareExecutionService> {

	/**
	 * Start a new process instance anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowExecutionService.anonymously().start(processDefinitionId)</code>
	 * @param processDefinitionId the process definition ID
	 * @return the process instance
	 */
	@Override
	String start(String processDefinitionId);

	/**
	 * Start a new process instance anonymously with the given parameters.<br>
	 * This is equivalent to invoke <code>WorkflowExecutionService.anonymously().start(processDefinitionId, parameters)</code>
	 * @param processDefinitionId the process definition ID
	 * @param parameters the parameters
	 * @return the process instance
	 */
	@Override
	String start(String processDefinitionId, Map<String, Object> parameters);
	
	/**
	 * Returns the map of process variables for the given process Instance
	 * @param processInstanceId the process instance ID
	 * @return the variables
	 */
	Map<String, Object> getInstanceVariables(String processInstanceId);

	/**
	 * Returns the value of the variable with the given identifier.
	 * @param processInstanceId the process instance ID
	 * @param key the variable identifier
	 * @return the variable value
	 */
	Object getInstanceVariable(String processInstanceId, String key);

	/**
	 * Creates or overwrites a variable value in the referenced processInstance.
	 * @param processInstanceId the process instance ID
	 * @param key the variable identifier
	 * @param value the variable value
	 */
	void setInstanceVariable(String processInstanceId, String key, Object value);

	/**
	 * Creates or overwrites a variable value in the referenced processInstance.
	 * @param processInstanceId the process instance ID
	 * @param key the variable identifier
	 */
	void removeInstanceVariable(String processInstanceId, String key);

	/**
	 * Suspends the process instance anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowExecutionService.anonymously().suspendInstance(processInstanceId)</code>
	 * @param processInstanceId the process instance ID
	 */
	@Override
	void suspendInstance(String processInstanceId);

	/**
	 * Continues (activates) the suspended process instance anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowExecutionService.anonymously().continueInstance(processInstanceId)</code>
	 * @param processInstanceId the process instance ID
	 */
	@Override
	void continueInstance(String processInstanceId);

	/**
	 * Aborts the process instance anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowExecutionService.anonymously().abortInstance(processInstanceId, reason)</code>
	 * @param processInstanceId the process instance ID
	 * @param reason for aborting; can be null
	 */
	@Override
	void abortInstance(String processInstanceId, String reason);
}
