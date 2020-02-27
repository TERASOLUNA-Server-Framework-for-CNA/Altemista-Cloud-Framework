/**
 * 
 */
package cloud.altemista.fwk.workflow.activiti.model.impl;

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


import java.util.Date;
import java.util.Map;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.workflow.model.WorkflowTask;

/**
 * Human task implementation, based on Activiti.
 * @author NTT DATA
 */
public class ActivitiWorkflowTaskImpl implements WorkflowTask {
	
	/** The Activiti task */
	private final Task task;
	
	/**
	 * Constructor
	 * @param task the Activiti task
	 */
	public ActivitiWorkflowTaskImpl(Task task) {
		super();
		Assert.notNull(task, "The Task is required");
		
		this.task = task;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getTaskId()
	 */
	@Override
	public String getTaskId() {
		
		return this.task.getId();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getTaskVariables()
	 */
	@Override
	public Map<String, Object> getTaskVariables() {
		
		return this.task.getTaskLocalVariables();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getProcessInstanceId()
	 */
	@Override
	public String getProcessInstanceId() {
		
		return this.task.getProcessInstanceId();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getProcessInstanceVariables()
	 */
	@Override
	public Map<String, Object> getProcessInstanceVariables() {
		
		return this.task.getProcessVariables();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getName()
	 */
	@Override
	public String getName() {
		
		return this.task.getName();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getSubject()
	 */
	@Override
	public String getSubject() {
		
		// (no subject field in Activiti: returns the internal name)
		return this.getName();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return this.task.getDescription();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getPriority()
	 */
	@Override
	public int getPriority() {
		
		return this.task.getPriority();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isSkippable()
	 */
	@Override
	public boolean isSkippable() {
		
		// Activiti does not support skipping tasks
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isDelegated()
	 */
	@Override
	public boolean isDelegated() {
		
		// A task is delegated if its delegationState is PENDING
		return DelegationState.PENDING.equals(this.task.getDelegationState());
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isSuspended()
	 */
	@Override
	public boolean isSuspended() {
		
		// (note that Activiti does not support suspending tasks but process instances)
		return this.task.isSuspended();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getAssigneeId()
	 */
	@Override
	public String getAssigneeId() {
		
		return this.task.getAssignee();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getOwnerId()
	 */
	@Override
	public String getOwnerId() {
		
		// Activiti does not inform the owner after claiming a task but the assignee;_
		// use the assignee if the owner is null
		return ObjectUtils.firstNonNull(this.task.getOwner(), this.task.getAssignee());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getCreatorId()
	 */
	@Override
	public String getCreatorId() {
		
		// (not supported by Activiti)
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getExpirationDate()
	 */
	@Override
	public Date getExpirationDate() {
		
		return this.task.getDueDate();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getCreationDate()
	 */
	@Override
	public Date getCreationDate() {
		
		return this.task.getCreateTime();
	}
}
