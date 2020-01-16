
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
 * The workflow execution service interface for the methods that can be executed with authentication awareness
 * (i.e.: can be executed without user, with any specified user or with the current user)
 * @author NTT DATA
 */
public interface WorkflowAuthenticationAwareExecutionService {

	/**
	 * Start a new process instance
	 * @param processDefinitionId the process definition ID
	 * @return the process instance
	 */
	String start(String processDefinitionId);

	/**
	 * Start a new process instance with the given parameters
	 * @param processDefinitionId the process definition ID
	 * @param parameters the parameters
	 * @return the process instance
	 */
	String start(String processDefinitionId, Map<String, Object> parameters);

	/**
	 * Suspends the process instance by the current user
	 * @param processInstanceId the process instance ID
	 */
	void suspendInstance(String processInstanceId);

	/**
	 * Continues (activates) the suspended process instance by the current user
	 * @param processInstanceId the process instance ID
	 */
	void continueInstance(String processInstanceId);

	/**
	 * Aborts the process instance by the current user
	 * @param processInstanceId the process instance ID
	 * @param reason for aborting; can be null
	 */
	void abortInstance(String processInstanceId, String reason);
}
