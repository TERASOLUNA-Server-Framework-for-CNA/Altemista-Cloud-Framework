
package cloud.altemista.fwk.core.workflow.model.impl;

import java.io.Serializable;

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

import cloud.altemista.fwk.common.util.DefensiveUtil;
import cloud.altemista.fwk.core.workflow.model.WorkflowTask;

/**
 * A serializable implementation of a human task.
 * Different provider implementations are encouraged to return this class (or subclasses),
 * rather than bridge implementations to their native classes in order to allow serialization. 
 * @author NTT DATA
 */
public class WorkflowTaskImpl implements WorkflowTask, Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -4833945848857010755L;

	/** The task ID */
	private String taskId;
	
	/**
	 * The map of task variables.
	 * (serializable by construction using {@link DefensiveUtil#unmodifiableMap(Map)})
	 */
	private Map<String, Object> taskVariables; // NOSONAR
	
	/** The process instance ID */
	private String processInstanceId;
	
	/**
	 * The process  variables for the process instance of the task.
	 * (serializable by construction using {@link DefensiveUtil#unmodifiableMap(Map)})
	 */
	private Map<String, Object> processInstanceVariables; // NOSONAR

	/** The internal name of the task */
	private String name;

	/** The subject of the task */
	private String subject;

	/** The description of the task */
	private String description;

	/** The priority of the task */
	private int priority;

	/** Can the task be skipped? */
	private boolean skippable;
	
	/** Is the task currently delegated? */
	private boolean delegated;
	
	/** Is the task currently suspended? */
	private boolean suspended;

	/** The current user ID responsible for this task or which this task is delegated */
	private String assigneeId;

	/** The user ID responsible for this task, without taking delegation in account */
	private String ownerId;

	/** The user ID that created this task */
	private String creatorId;

	/** The moment when this task expires */
	private Date expirationDate;

	/** The moment when the task was created */
	private Date creationDate;
	
	/**
	 * Default constructor
	 */
	public WorkflowTaskImpl() {
		super();
	}
	
	/**
	 * Constructor
	 * @param that WorkflowTask that will be cloned
	 */
	public WorkflowTaskImpl(WorkflowTask that) {
		this();
		
		this.setTaskId(that.getTaskId());
		this.setTaskVariables(that.getTaskVariables());
		this.setProcessInstanceId(that.getProcessInstanceId());
		this.setProcessInstanceVariables(that.getProcessInstanceVariables());
		this.setName(that.getName());
		this.setSubject(that.getSubject());
		this.setDescription(that.getDescription());
		this.setPriority(that.getPriority());
		this.setSkippable(that.isSkippable());
		this.setDelegated(that.isDelegated());
		this.setSuspended(that.isSuspended());
		this.setAssigneeId(that.getAssigneeId());
		this.setOwnerId(that.getOwnerId());
		this.setCreatorId(that.getCreatorId());
		this.setExpirationDate(that.getExpirationDate());
		this.setCreationDate(that.getCreationDate());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getTaskId()
	 */
	@Override
	public String getTaskId() {
		
		return this.taskId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getTaskVariables()
	 */
	@Override
	public Map<String, Object> getTaskVariables() {
		
		return DefensiveUtil.unmodifiableMap(this.taskVariables);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getProcessInstanceId()
	 */
	@Override
	public String getProcessInstanceId() {
		
		return this.processInstanceId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getProcessInstanceVariables()
	 */
	@Override
	public Map<String, Object> getProcessInstanceVariables() {
		
		return DefensiveUtil.unmodifiableMap(this.processInstanceVariables);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getName()
	 */
	@Override
	public String getName() {
		
		return this.name;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getSubject()
	 */
	@Override
	public String getSubject() {
		
		return this.subject;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return this.description;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getPriority()
	 */
	@Override
	public int getPriority() {
		
		return this.priority;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isSkippable()
	 */
	@Override
	public boolean isSkippable() {
		
		return this.skippable;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isDelegated()
	 */
	@Override
	public boolean isDelegated() {
		
		return this.delegated;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#isSuspended()
	 */
	@Override
	public boolean isSuspended() {
		
		return this.suspended;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getAssigneeId()
	 */
	@Override
	public String getAssigneeId() {
		
		return this.assigneeId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getOwnerId()
	 */
	@Override
	public String getOwnerId() {
		
		return this.ownerId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getCreatorId()
	 */
	@Override
	public String getCreatorId() {
		
		return this.creatorId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getExpirationDate()
	 */
	@Override
	public Date getExpirationDate() {
		
		return DefensiveUtil.copyOf(this.expirationDate);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTask#getCreationDate()
	 */
	@Override
	public Date getCreationDate() {
		
		return DefensiveUtil.copyOf(this.creationDate);
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @param taskVariables the taskVariables to set
	 */
	public void setTaskVariables(Map<String, Object> taskVariables) {
		this.taskVariables = DefensiveUtil.unmodifiableMap(taskVariables);
	}

	/**
	 * @param processInstanceId the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @param processInstanceVariables the processInstanceVariables to set
	 */
	public void setProcessInstanceVariables(Map<String, Object> processInstanceVariables) {
		this.processInstanceVariables = DefensiveUtil.unmodifiableMap(processInstanceVariables);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @param skippable the skippable to set
	 */
	public void setSkippable(boolean skippable) {
		this.skippable = skippable;
	}

	/**
	 * @param delegated the delegated to set
	 */
	public void setDelegated(boolean delegated) {
		this.delegated = delegated;
	}

	/**
	 * @param suspended the suspended to set
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * @param assigneeId the assigneeId to set
	 */
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = DefensiveUtil.copyOf(expirationDate);
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = DefensiveUtil.copyOf(creationDate);
	}
}
