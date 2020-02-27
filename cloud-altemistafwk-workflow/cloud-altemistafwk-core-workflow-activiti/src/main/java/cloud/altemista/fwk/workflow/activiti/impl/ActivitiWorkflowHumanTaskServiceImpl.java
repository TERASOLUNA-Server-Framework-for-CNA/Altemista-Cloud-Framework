/**
 * 
 */
package cloud.altemista.fwk.workflow.activiti.impl;

/*
 * #%L
 * altemista-cloud workflow: Activiti implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService;
import cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService;
import cloud.altemista.fwk.core.workflow.exception.WorkflowException;
import cloud.altemista.fwk.core.workflow.impl.AbstractAuthenticableWorkflowService;
import cloud.altemista.fwk.core.workflow.model.WorkflowTask;
import cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment;
import cloud.altemista.fwk.core.workflow.model.impl.WorkflowTaskCommentImpl;
import cloud.altemista.fwk.core.workflow.model.impl.WorkflowTaskImpl;
import cloud.altemista.fwk.workflow.activiti.model.impl.ActivitiWorkflowTaskCommentImpl;
import cloud.altemista.fwk.workflow.activiti.model.impl.ActivitiWorkflowTaskImpl;

/**
 * Human task service implementation with Activiti
 * @author NTT DATA
 */
@Component
public class ActivitiWorkflowHumanTaskServiceImpl
		extends AbstractAuthenticableWorkflowService<WorkflowAuthenticationAwareHumanTaskService>
		implements WorkflowHumanTaskService {
	

	/** Activiti task service */
	private final TaskService taskService;
	
	/** Activiti identity service */
	private final IdentityService identityService;

	/**
	 * Constructor
	 * @param processEngine Provides access to all the Activiti services
	 */
	@Autowired
	public ActivitiWorkflowHumanTaskServiceImpl(ProcessEngine processEngine) {
		super();
		
		// (sanity checks)
		Assert.notNull(processEngine, "ProcessEngine must not be null");
		
		this.taskService = processEngine.getTaskService();
		this.identityService = processEngine.getIdentityService();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.AuthenticableWorkflowService#anonymously()
	 */
	@Override
	public WorkflowAuthenticationAwareHumanTaskService anonymously() {
		
		return this;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.AuthenticableWorkflowService#withUser(java.lang.String)
	 */
	@Override
	public WorkflowAuthenticationAwareHumanTaskService withUser(String userId) {
		
		Assert.hasText(userId, "The user ID is required");
		
		return new WithUser(userId); 
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#getTask(java.lang.String)
	 */
	@Override
	public WorkflowTask getTask(String taskId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		
		// Queries the task service
		try {
			TaskQuery query = this.taskService.createTaskQuery().taskId(taskId);
			Task task = query.singleResult();
			return this.toWorkflowTask(task);
			
		} catch (ActivitiException e) {
			throw new WorkflowException("not_unique_task", new Object[]{ taskId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findProcessTasks(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findProcessTasks(Pageable pageable, String... processInstanceIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the task service
		TaskQuery query = this.taskService.createTaskQuery();
		if (ArrayUtils.isNotEmpty(processInstanceIds)) {
			query.processInstanceIdIn(Arrays.asList(processInstanceIds));
		}
		return this.execute(query, pageable);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findTasks(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findTasks(Pageable pageable, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the task service
		TaskQuery query = this.taskService.createTaskQuery();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findAssignedTasks(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findAssignedTasks(Pageable pageable, String... processDefinitionIds) {
		
		return this.findTasksAssignedTo(pageable, this.getCurrentUserId(), processDefinitionIds);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findTasksAssignedTo(org.springframework.data.domain.Pageable, java.lang.String, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findTasksAssignedTo(Pageable pageable, String userId, String... processDefinitionIds) {
		
		return this.findTasksAssignedTo(pageable, new String[]{ userId }, processDefinitionIds);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findTasksAssignedTo(org.springframework.data.domain.Pageable, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findTasksAssignedTo(Pageable pageable, String[] userIds, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		Assert.notEmpty(userIds, "At least one user ID is required");
		
		// Queries the task service
		TaskQuery query = this.taskService.createTaskQuery().or();
		for (String userId : userIds) {
			query.taskAssignee(userId);
		}
		query.endOr();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findOwnedTasks(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findOwnedTasks(Pageable pageable, String... processDefinitionIds) {
		
		return this.findTasksOwnedBy(pageable, this.getCurrentUserId(), processDefinitionIds);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findTasksOwnedBy(org.springframework.data.domain.Pageable, java.lang.String, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findTasksOwnedBy(Pageable pageable, String userId, String... processDefinitionIds) {
		
		return this.findTasksOwnedBy(pageable, new String[]{ userId }, processDefinitionIds);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findTasksOwnedBy(org.springframework.data.domain.Pageable, java.lang.String[], java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findTasksOwnedBy(Pageable pageable, String[] userIds, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		Assert.notEmpty(userIds, "At least one user ID is required");
		
		// Queries the task service
		TaskQuery query = this.taskService.createTaskQuery().or();
		for (String userId : userIds) {
			query.taskOwner(userId);
		}
		query.endOr();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findCandidateTasks(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findCandidateTasks(Pageable pageable, String... processDefinitionIds) {
		
		return this.findCandidateTasks(pageable,
				new String[] { this.getCurrentUserId() }, this.getCurrentGroupIds(), processDefinitionIds);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#findCandidateTasks(org.springframework.data.domain.Pageable, java.lang.String[], java.lang.String[], java.lang.String[])
	 */
	@Override
	public Page<WorkflowTask> findCandidateTasks(Pageable pageable, String[] userIds, String[] groupIds, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the task service
		TaskQuery query = this.taskService.createTaskQuery().or();
		if (!ObjectUtils.isEmpty(userIds)) {
			for (String userId : userIds) {
				query.taskCandidateUser(userId);
			}
		}
		if (!ObjectUtils.isEmpty(groupIds)) {
			for (String groupId : groupIds) {
				query.taskCandidateGroup(groupId);
			}
		}
		query.endOr();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	/**
	 * Convenience method to execute a paginated TaskQuery and convert the result
	 * @param query TaskQuery
	 * @param pageable Pageable
	 * @return Page of WorkflowTask
	 */
	private Page<WorkflowTask> execute(TaskQuery query, Pageable pageable) {
		
		long count = query.count();
		this.order(query, pageable.getSort());
		List<Task> list = query
				.includeProcessVariables()
				.includeTaskLocalVariables()
				.listPage(Math.toIntExact(pageable.getOffset()), pageable.getPageSize());
		return new PageImpl<WorkflowTask>(this.toWorkflowTasks(list), pageable, count);
	}
	
	/**
	 * Convenience method to append order clauses to a TaskQuery
	 * @param query TaskQuery
	 * @param sort Sort
	 */
	private void order(TaskQuery query, Sort sort) { // NOSONAR "Cyclomatic complexity"
		
		if (sort == null) {
			return;
		}
		
		for (Order order : sort) {
			
			if (TASK_ID_ASC.equals(order)) {
				query.orderByTaskId().asc();
			} else if (TASK_ID_DESC.equals(order)) {
				query.orderByTaskId().desc();
				
			} else if (PROCESS_INSTANCE_ID_ASC.equals(order)) {
				query.orderByProcessInstanceId().asc();
			} else if (PROCESS_INSTANCE_ID_DESC.equals(order)) {
				query.orderByProcessInstanceId().desc();
				
			} else if (NAME_ASC.equals(order) || SUBJECT_ASC.equals(order)) {
				query.orderByTaskName().asc();
			} else if (NAME_DESC.equals(order) || SUBJECT_DESC.equals(order)) {
				query.orderByTaskName().desc();
				
			} else if (DESCRIPTION_ASC.equals(order)) {
				query.orderByTaskDescription().asc();
			} else if (DESCRIPTION_DESC.equals(order)) {
				query.orderByTaskDescription().desc();
				
			} else if (PRIORITY_ASC.equals(order)) {
				query.orderByTaskPriority().asc();
			} else if (PRIORITY_DESC.equals(order)) {
				query.orderByTaskPriority().desc();
				
			} else if (ASIGNEE_ASC.equals(order)) {
				query.orderByTaskAssignee().asc();
			} else if (ASIGNEE_DESC.equals(order)) {
				query.orderByTaskAssignee().desc();
				
			} else if (OWNER_ASC.equals(order)) {
				query.orderByTaskOwner().asc();
			} else if (OWNER_DESC.equals(order)) {
				query.orderByTaskOwner().desc();
				
			} else if (EXPIRATION_DATE_ASC.equals(order)) {
				query.orderByTaskDueDate().asc();
			} else if (EXPIRATION_DATE_DESC.equals(order)) {
				query.orderByTaskDueDate().desc();
				
			} else if (CREATION_DATE_ASC.equals(order)) {
				query.orderByTaskCreateTime().asc();
			} else if (CREATION_DATE_DESC.equals(order)) {
				query.orderByTaskCreateTime().desc();
			}
		}
	}
	
	/**
	 * Convenience method to convert a list of tasks
	 * @param list List of Task
	 * @return List of WorkflowTask
	 */
	private List<WorkflowTask> toWorkflowTasks(List<Task> list) {
		
		// (sanity checks)
		if (list == null) {
			return null; // NOSONAR
		}
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Returns a list of ActivitiWorkflowTaskImpl
		List<WorkflowTask> ret = new ArrayList<WorkflowTask>();
		for (Task task : list) {
			ret.add(this.toWorkflowTask(task));
		}
		return ret;
	}
	
	/**
	 * Convenience method to convert a task
	 * @param task Task
	 * @return WorkflowTask
	 */
	private WorkflowTask toWorkflowTask(Task task) {
		
		// (sanity checks)
		if (task == null) {
			return null;
		}
		
		// Instantiates a serializable WorkflowTaskImpl from a new ActivitiWorkflowTaskImpl
		return new WorkflowTaskImpl(new ActivitiWorkflowTaskImpl(task));
	}
	
	//
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#startTask(java.lang.String)
	 */
	@Override
	public void startTask(String taskId) {
	
		// (no implementation in Activiti)
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#completeTask(java.lang.String)
	 */
	@Override
	public void completeTask(String taskId) {
		
		this.completeTask(taskId, null);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#completeTask(java.lang.String, java.util.Map)
	 */
	@Override
	public void completeTask(String taskId, Map<String, Object> taskVariables) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		
		try {
			this.taskService.complete(taskId, taskVariables);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("task_not_found", new Object[]{ taskId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#stopTask(java.lang.String)
	 */
	@Override
	public void stopTask(String taskId) {
	
		// (no implementation in Activiti)
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#suspendTask(java.lang.String)
	 */
	@Override
	public void suspendTask(String taskId) {
	
		// (no implementation in Activiti)
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#resumeTask(java.lang.String)
	 */
	@Override
	public void resumeTask(String taskId) {
	
		// (no implementation in Activiti)
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#skipTask(java.lang.String)
	 */
	@Override
	public void skipTask(String taskId) {
		
		throw new WorkflowException("unsupported_operation");
	}

	//
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#addCandidateUser(java.lang.String, java.lang.String[])
	 */
	@Override
	public void addCandidateUser(String taskId, String... userIds) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		Assert.notEmpty(userIds, "At least one user ID is required");
		
		for (String userId : userIds) {
			this.taskService.addCandidateUser(taskId, userId);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#addCandidateGroup(java.lang.String, java.lang.String[])
	 */
	@Override
	public void addCandidateGroup(String taskId, String... groupIds) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		Assert.notEmpty(groupIds, "At least one group ID is required");
		
		for (String groupId : groupIds) {
			this.taskService.addCandidateGroup(taskId, groupId);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#claimTask(java.lang.String)
	 */
	@Override
	public void claimTask(String taskId) {
		
		// (human tasks cannot be claimed anonymously)
		throw new WorkflowException("unsupported_operation");
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#releaseTask(java.lang.String)
	 */
	@Override
	public void releaseTask(String taskId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		
		try {
			this.taskService.unclaim(taskId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("task_not_found", new Object[]{ taskId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#delegateTask(java.lang.String, java.lang.String)
	 */
	@Override
	public void delegateTask(String taskId, String delegatedUserId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		Assert.hasText(delegatedUserId, "The delegated user ID is required");
		
		try {
			this.taskService.delegateTask(taskId, delegatedUserId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("task_not_found", new Object[]{ taskId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#resolveTask(java.lang.String)
	 */
	@Override
	public void resolveTask(String taskId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		
		try {
			this.taskService.resolveTask(taskId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("task_not_found", new Object[]{ taskId }, e);
		}
	}
	
	//

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#getComments(java.lang.String)
	 */
	@Override
	public List<WorkflowTaskComment> getComments(String taskId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		
		List<Comment> list = this.taskService.getTaskComments(taskId);
		return this.toWorkflowTaskComments(list);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#addComment(java.lang.String, java.lang.String)
	 */
	@Override
	public void addComment(String taskId, String commentText) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		Assert.hasText(commentText, "The comment text is required");
		
		final String processInstanceId = this.getTask(taskId).getProcessInstanceId();
		this.taskService.addComment(taskId, processInstanceId, commentText);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService#deleteComment(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteComment(String taskId, String commentId) {
		
		// (sanity checks)
		Assert.hasText(taskId, "The task ID is required");
		Assert.hasText(commentId, "The comment ID is required");

		try {
			this.taskService.deleteComment(commentId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("comment_not_found", new Object[]{ commentId }, e);
		}
	}
	
	/**
	 * Convenience method to convert a list of comments
	 * @param list List of Comment
	 * @return List of WorkflowTaskComment
	 */
	private List<WorkflowTaskComment> toWorkflowTaskComments(List<Comment> list) {
		
		// (sanity checks)
		if (list == null) {
			return null; // NOSONAR
		}
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Returns a list of WorkflowTaskComment
		List<WorkflowTaskComment> ret = new ArrayList<WorkflowTaskComment>();
		for (Comment comment : list) {
			ret.add(this.toWorkflowTaskComment(comment));
		}
		return ret;
	}
	
	/**
	 * Convenience method to convert a comment
	 * @param comment Comment
	 * @return WorkflowTaskComment
	 */
	private WorkflowTaskComment toWorkflowTaskComment(Comment comment) {
		
		// (sanity checks)
		if (comment == null) {
			return null;
		}
		
		// Instantiates a serializable WorkflowTaskCommentImpl from a new ActivitiWorkflowTaskCommentImpl
		return new WorkflowTaskCommentImpl(new ActivitiWorkflowTaskCommentImpl(comment));
	}
	
	/**
	 * Wraps the authentication aware methods with Activiti authentication of the specified user
	 * @author NTT DATA
	 */
	public class WithUser implements WorkflowAuthenticationAwareHumanTaskService {
		
		/** The user ID */
		private String userId;
		
		/**
		 * Constructor for the specific user instances
		 * @param userId the user ID
		 */
		private WithUser(String userId) {
			super();
			
			Assert.hasText(userId, "The user ID is required");
			
			this.userId = userId;
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#startTask(java.lang.String)
		 */
		@Override
		public void startTask(String taskId) {
			
			// (no implementation in Activiti)
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#completeTask(java.lang.String)
		 */
		@Override
		public void completeTask(String taskId) {
			
			try {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(this.userId);
				ActivitiWorkflowHumanTaskServiceImpl.this.completeTask(taskId);
				
			} finally {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(null);
			}
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#completeTask(java.lang.String, java.util.Map)
		 */
		@Override
		public void completeTask(String taskId, Map<String, Object> taskVariables) {
			
			try {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(this.userId);
				ActivitiWorkflowHumanTaskServiceImpl.this.completeTask(taskId, taskVariables);
				
			} finally {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(null);
			}
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#stopTask(java.lang.String)
		 */
		@Override
		public void stopTask(String taskId) {
			
			// (no implementation in Activiti)
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#suspendTask(java.lang.String)
		 */
		@Override
		public void suspendTask(String taskId) {
			
			// (no implementation in Activiti)
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#resumeTask(java.lang.String)
		 */
		@Override
		public void resumeTask(String taskId) {
			
			// (no implementation in Activiti)
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#skipTask(java.lang.String)
		 */
		@Override
		public void skipTask(String taskId) {
			
			throw new WorkflowException("unsupported_operation");
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#claimTask(java.lang.String)
		 */
		@Override
		public void claimTask(String taskId) {
			
			// (sanity checks)
			Assert.hasText(taskId, "The task ID is required");
			Assert.hasText(this.userId, "The user ID is required");
			
			try {
				taskService.claim(taskId, this.userId);
				
			} catch (ActivitiObjectNotFoundException e) {
				throw new WorkflowException("task_not_found", new Object[]{ taskId }, e);
				
			} catch (ActivitiTaskAlreadyClaimedException e) {
				throw new WorkflowException("invalid_task_state", new Object[]{ taskId }, e);
			}
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#releaseTask(java.lang.String)
		 */
		@Override
		public void releaseTask(String taskId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowHumanTaskServiceImpl.this.releaseTask(taskId);
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#delegateTask(java.lang.String, java.lang.String)
		 */
		@Override
		public void delegateTask(String taskId, String delegatedUserId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowHumanTaskServiceImpl.this.delegateTask(taskId, delegatedUserId);
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#resolveTask(java.lang.String)
		 */
		@Override
		public void resolveTask(String taskId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowHumanTaskServiceImpl.this.resolveTask(taskId);
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#addComment(java.lang.String, java.lang.String)
		 */
		@Override
		public void addComment(String taskId, String commentText) {
			
			try {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(this.userId);
				ActivitiWorkflowHumanTaskServiceImpl.this.addComment(taskId, commentText);
				
			} finally {
				ActivitiWorkflowHumanTaskServiceImpl.this.identityService.setAuthenticatedUserId(null);
			}
		}
	
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService#deleteComment(java.lang.String, java.lang.String)
		 */
		@Override
		public void deleteComment(String taskId, String commentId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowHumanTaskServiceImpl.this.deleteComment(taskId, commentId);
		}
	
	}
}
