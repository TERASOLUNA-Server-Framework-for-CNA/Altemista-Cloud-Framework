
package cloud.altemista.fwk.core.workflow.model;

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


import java.util.Date;
import java.util.Map;


/**
 * An human task.
 * @author NTT DATA
 */
public interface WorkflowTask {

	/**
	 * Gets the task ID
	 * @return the task ID
	 */
	String getTaskId();

	/**
	 * Returns the map of task variables
	 * @return the variables
	 */
	Map<String, Object> getTaskVariables();

	/**
	 * Gets the process instance ID
	 * @return the process instance ID
	 */
	String getProcessInstanceId();

	/**
	 * Returns the map of process variables for the process instance of the task
	 * @return the variables
	 */
	Map<String, Object> getProcessInstanceVariables();

	/**
	 * Gets the internal name of the task
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the subject of the task
	 * (optional operation: some providers can return the name as the subject)
	 * @return the subject
	 */
	String getSubject();

	/**
	 * Gets the description of the task
	 * @return the description
	 */
	String getDescription();

	/**
	 * Gets the priority of the task
	 * @return the priority
	 */
	int getPriority();

	/**
	 * Checks if the task can be skipped
	 * @return true if the task can be skipped
	 */
	boolean isSkippable();
	
	/**
	 * Checks if the task is currently delegated
	 * @return true if the task is delegated
	 */
	boolean isDelegated();
	
	/**
	 * Checks if the task is currently suspended
	 * @return true if the task is suspended
	 */
	boolean isSuspended();

	/**
	 * Gets the current user ID responsible for this task or which this task is delegated
	 * @return the current user ID responsible for this task
	 */
	String getAssigneeId();

	/**
	 * Gets the user ID responsible for this task, without taking delegation in account
	 * @return the user ID responsible for this task
	 */
	String getOwnerId();

	/**
	 * Gets the user ID that created this task
	 * @return the user ID that created this task
	 */
	String getCreatorId();

	/**
	 * Gets the moment when this task expires
	 * @return the expiration date
	 */
	Date getExpirationDate();

	/**
	 * Gets the moment when the task was created
	 * @return the creation date
	 */
	Date getCreationDate();
}
