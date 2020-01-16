
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
 * The human task service for the methods that can be executed with authentication awareness
 * (i.e.: can be executed without user, with any specified user or with the current user)
 * @author NTT DATA
 */
public interface WorkflowAuthenticationAwareHumanTaskService {
	
	/**
	 * Starts the execution of the task
	 * @param taskId the task ID
	 */
	void startTask(String taskId);
	
	/**
	 * Completes the execution of the task
	 * @param taskId the task ID
	 */
	void completeTask(String taskId);
	
	/**
	 * Completes the execution of the task
	 * @param taskId the task ID
	 * @param taskVariables the values used to complete the task
	 */
	void completeTask(String taskId, Map<String, Object> taskVariables);

	/**
	 * Stops (or cancels) the execution of the task
	 * @param taskId the task ID
	 */
	void stopTask(String taskId);

	/**
	 * Suspends the execution of the task
	 * @param taskId the task ID
	 */
	void suspendTask(String taskId);

	/**
	 * Resumes the execution of a suspended task
	 * @param taskId the task ID
	 */
	void resumeTask(String taskId);

	/**
	 * Skips the execution of the task
	 * @param taskId the task ID
	 */
	void skipTask(String taskId);
	
	//
	
	/**
	 * Claims responsibility for a task
	 * @param taskId the task ID
	 */
	void claimTask(String taskId);
	
	/**
	 * Releases a task
	 * @param taskId the task ID
	 */
	void releaseTask(String taskId);

	/**
	 * Assign the task to another user
	 * @param taskId the task ID
	 * @param delegatedUserId the user ID that receives the task (will become the assignee for the task)
	 */
	void delegateTask(String taskId, String delegatedUserId);

	/**
	 * Resolves the delegated task
	 * @param taskId the task ID
	 */
	void resolveTask(String taskId);
	
	/**
	 * Adds a simple comment to the specified task
	 * @param taskId the task ID 
	 * @param commentText the comment text
	 */
	void addComment(String taskId, String commentText);

	/**
	 * Deletes a comment from the specified task
	 * @param taskId the task ID
	 * @param commentId the comment ID
	 */
	void deleteComment(String taskId, String commentId);

}
