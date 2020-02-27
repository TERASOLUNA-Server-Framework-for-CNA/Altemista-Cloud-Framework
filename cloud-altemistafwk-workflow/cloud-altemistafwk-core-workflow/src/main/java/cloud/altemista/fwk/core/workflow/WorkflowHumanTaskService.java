
package cloud.altemista.fwk.core.workflow;

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


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import cloud.altemista.fwk.core.workflow.model.WorkflowTask;
import cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment;

/**
 * The human task service.
 * This service will take care of the human task life cycle if human actors participate in the process.
 * @author NTT DATA
 */
public interface WorkflowHumanTaskService
		extends WorkflowAuthenticationAwareHumanTaskService,
		AuthenticableWorkflowService<WorkflowAuthenticationAwareHumanTaskService> {
	
	/** Retrieves tasks ordered by task ID (ascending order) */
	Order TASK_ID_ASC = new Order(Direction.ASC, "taskId");

	/** Retrieves tasks ordered by task ID (descending order) */
	Order TASK_ID_DESC = new Order(Direction.DESC, "taskId");
	
	/** Retrieves tasks ordered by process instance ID (ascending order) */
	Order PROCESS_INSTANCE_ID_ASC = new Order(Direction.ASC, "processInstanceId");

	/** Retrieves tasks ordered by process instance ID (descending order) */
	Order PROCESS_INSTANCE_ID_DESC = new Order(Direction.DESC, "processInstanceId");
	
	/** Retrieves tasks ordered by name (ascending order) */
	Order NAME_ASC = new Order(Direction.ASC, "name");

	/** Retrieves tasks ordered by name (descending order) */
	Order NAME_DESC = new Order(Direction.DESC, "name");
	
	/** Retrieves tasks ordered by subject (ascending order) */
	Order SUBJECT_ASC = new Order(Direction.ASC, "subject");

	/** Retrieves tasks ordered by subject (descending order) */
	Order SUBJECT_DESC = new Order(Direction.DESC, "subject");
	
	/** Retrieves tasks ordered by description (ascending order) */
	Order DESCRIPTION_ASC = new Order(Direction.ASC, "description");

	/** Retrieves tasks ordered by description (descending order) */
	Order DESCRIPTION_DESC = new Order(Direction.DESC, "description");
	
	/** Retrieves tasks ordered by priority (ascending order) */
	Order PRIORITY_ASC = new Order(Direction.ASC, "priority");

	/** Retrieves tasks ordered by priority (descending order) */
	Order PRIORITY_DESC = new Order(Direction.DESC, "priority");
	
	/** Retrieves tasks ordered by asignee user id (ascending order) */
	Order ASIGNEE_ASC = new Order(Direction.ASC, "asignee");

	/** Retrieves tasks ordered by asignee user id (descending order) */
	Order ASIGNEE_DESC = new Order(Direction.DESC, "asignee");
	
	/** Retrieves tasks ordered by owner user id (ascending order) */
	Order OWNER_ASC = new Order(Direction.ASC, "owner");

	/** Retrieves tasks ordered by owner user id (descending order) */
	Order OWNER_DESC = new Order(Direction.DESC, "owner");
	
	/** Retrieves tasks ordered by creator (ascending order) */
	Order CREATOR_ASC = new Order(Direction.ASC, "creator");

	/** Retrieves tasks ordered by creator (descending order) */
	Order CREATOR_DESC = new Order(Direction.DESC, "creator");
	
	/** Retrieves tasks ordered by expiration date (ascending order) */
	Order EXPIRATION_DATE_ASC = new Order(Direction.ASC, "expirationDate");

	/** Retrieves tasks ordered by expiration date (descending order) */
	Order EXPIRATION_DATE_DESC = new Order(Direction.DESC, "expirationDate");
	
	/** Retrieves tasks ordered by creation date (ascending order) */
	Order CREATION_DATE_ASC = new Order(Direction.ASC, "creationDate");

	/** Retrieves tasks ordered by creation date (descending order) */
	Order CREATION_DATE_DESC = new Order(Direction.DESC, "creationDate");
	
	//
	
	/**
	 * Gets a task by the task ID
	 * @param taskId the task id
	 * @return the task
	 */
	WorkflowTask getTask(String taskId);

	/**
	 * Gets the tasks of one or more process instances
	 * @param pageable pagination information
	 * @param processInstanceIds the optional process instance IDs to filter the tasks
	 * @return Page of tasks
	 */
	Page<WorkflowTask> findProcessTasks(Pageable pageable, String... processInstanceIds);

	/**
	 * Gets the tasks
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of tasks
	 */
	Page<WorkflowTask> findTasks(Pageable pageable, String... processDefinitionIds);

	/**
	 * Gets the tasks assigned to the current user
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of assigned tasks
	 */
	Page<WorkflowTask> findAssignedTasks(Pageable pageable, String... processDefinitionIds);

	/**
	 * Gets the tasks assigned to the specified user
	 * @param pageable pagination information
	 * @param userId the user ID
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of tasks assigned to the specified user
	 */
	Page<WorkflowTask> findTasksAssignedTo(Pageable pageable, String userId, String... processDefinitionIds);

	/**
	 * Gets the tasks assigned to the specified users
	 * @param pageable pagination information
	 * @param userIds the user IDs
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of tasks assigned to the specified users
	 */
	Page<WorkflowTask> findTasksAssignedTo(Pageable pageable, String[] userIds, String... processDefinitionIds);
	
	/**
	 * Gets the tasks owned by the current user
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of owned tasks
	 */
	Page<WorkflowTask> findOwnedTasks(Pageable pageable, String... processDefinitionIds);
	
	/**
	 * Gets the tasks owned by the specified user
	 * @param pageable pagination information
	 * @param userId the user ID
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of tasks owned by the specified user
	 */
	Page<WorkflowTask> findTasksOwnedBy(Pageable pageable, String userId, String... processDefinitionIds);
	
	/**
	 * Gets the tasks owned by the specified users
	 * @param pageable pagination information
	 * @param userIds the user IDs
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of tasks owned by the specified users
	 */
	Page<WorkflowTask> findTasksOwnedBy(Pageable pageable, String[] userIds,  String... processDefinitionIds);
	
	/**
	 * Gets the tasks candidate to be assigned to the current user,
	 * because either the user itself or his groups are candidates for the task
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of candidate tasks
	 */
	Page<WorkflowTask> findCandidateTasks(Pageable pageable, String... processDefinitionIds);
	
	/**
	 * Gets the tasks candidate to be assigned to a set of users or to groups
	 * @param pageable pagination information
	 * @param userIds the user IDs
	 * @param groupIds the group IDs
	 * @param processDefinitionIds the optional process definition IDs to filter the tasks
	 * @return Page of candidate tasks
	 */
	Page<WorkflowTask> findCandidateTasks(Pageable pageable, String[] userIds, String[] groupIds,  String... processDefinitionIds);

	/**
	 * Starts the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().startTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void startTask(String taskId);
	
	/**
	 * Completes the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().completeTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void completeTask(String taskId);
	
	/**
	 * Completes the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().completeTask(taskId, taskVariables)</code>
	 * @param taskId the task ID
	 * @param taskVariables the values used to complete the task
	 */
	@Override
	void completeTask(String taskId, Map<String, Object> taskVariables);

	/**
	 * Stops (or cancels) the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().stopTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void stopTask(String taskId);

	/**
	 * Suspends the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().suspendTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void suspendTask(String taskId);

	/**
	 * Resumes the execution of a suspended task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().resumeTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void resumeTask(String taskId);

	/**
	 * Skips the execution of the task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().skipTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void skipTask(String taskId);
	
	//
	
	/**
	 * Adds a user as a candidate for the task
	 * @param taskId the task ID
	 * @param userIds the user IDs
	 */
	void addCandidateUser(String taskId, String... userIds);
	
	/**
	 * Adds a group as a candidate for the task
	 * @param taskId the task ID
	 * @param groupIds the group IDs
	 */
	void addCandidateGroup(String taskId, String... groupIds);
	
	/**
	 * Releases a task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().releaseTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void releaseTask(String taskId);

	/**
	 * Assign the task to another user anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().delegateTask(taskId, delegatedUserId)</code>
	 * @param taskId the task ID
	 * @param delegatedUserId the user ID that receives the task (will become the assignee for the task)
	 */
	@Override
	void delegateTask(String taskId, String delegatedUserId);

	/**
	 * Resolves the delegated task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().resolveTask(taskId)</code>
	 * @param taskId the task ID
	 */
	@Override
	void resolveTask(String taskId);
	
	/**
	 * Gets all the comments for the task
	 * @param taskId the task ID
	 * @return the comments
	 */
	List<WorkflowTaskComment> getComments(String taskId);
	
	/**
	 * Adds a simple comment to the specified task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().addComment(taskId, commentTest)</code>
	 * @param taskId the task ID 
	 * @param commentText the comment text
	 */
	@Override
	void addComment(String taskId, String commentText);

	/**
	 * Deletes a comment from the specified task anonymously.<br>
	 * This is equivalent to invoke <code>WorkflowHumanTaskService.anonymously().deleteComment(taskId, commentId)</code>
	 * @param taskId the task ID
	 * @param commentId the comment ID
	 */
	@Override
	void deleteComment(String taskId, String commentId);

}
