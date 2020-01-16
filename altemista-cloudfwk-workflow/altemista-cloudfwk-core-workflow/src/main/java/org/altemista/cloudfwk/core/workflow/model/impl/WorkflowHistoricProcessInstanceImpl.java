/**
 * 
 */
package org.altemista.cloudfwk.core.workflow.model.impl;

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


import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.altemista.cloudfwk.common.util.DefensiveUtil;
import org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance;

/**
 * A serializable implementation of an historic (either ongoing or past) process instance.
 * Different provider implementations are encouraged to return this class (or subclasses),
 * rather than bridge implementations to their native classes in order to allow serialization. 
 * @author NTT DATA
 */
public class WorkflowHistoricProcessInstanceImpl implements WorkflowHistoricProcessInstance, Serializable {

	/** The serialVersionUID long */
	private static final long serialVersionUID = 6365223293443523282L;

	/** The process definition ID */
	private String procesDefinitionId;

	/** The process instance ID */
	private String processInstanceId;
	
	/** Is the process instance active or finished? */
	private boolean active;
	
	/** The time the process instance was started */
	private Date startTime;
	
	/** The time the process instance ended, if the process instance is not active */
	private Date endTime;
	
	/** The reason for aborting */
	private String abortReason;

	/**
	 * The map of process variables for the process instance of the task.
	 * (serializable by construction using {@link DefensiveUtil#unmodifiableMap(Map<String, Object>)})
	 */
	private Map<String, Object> processInstanceVariables; // NOSONAR
	
	/**
	 * Default constructor
	 */
	public WorkflowHistoricProcessInstanceImpl() {
		super();
	}
	
	/**
	 * Constructor
	 * @param that WorkflowHistoricProcessInstance that will be cloned
	 */
	public WorkflowHistoricProcessInstanceImpl(WorkflowHistoricProcessInstance that) {
		this();
		
		this.setProcesDefinitionId(that.getProcesDefinitionId());
		this.setProcessInstanceId(that.getProcessInstanceId());
		this.setActive(that.isActive());
		this.setStartTime(that.getStartTime());
		this.setEndTime(that.getEndTime());
		this.setAbortReason(that.getAbortReason());
		this.setProcessInstanceVariables(that.getProcessInstanceVariables());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcesDefinitionId()
	 */
	@Override
	public String getProcesDefinitionId() {
		return this.procesDefinitionId;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcessInstanceId()
	 */
	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#isActive()
	 */
	@Override
	public boolean isActive() {
		return this.active;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		return DefensiveUtil.copyOf(this.startTime);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		return DefensiveUtil.copyOf(this.endTime);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getAbortReason()
	 */
	@Override
	public String getAbortReason() {
		return this.abortReason;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcessInstanceVariables()
	 */
	@Override
	public Map<String, Object> getProcessInstanceVariables() {
		return DefensiveUtil.unmodifiableMap(this.processInstanceVariables);
	}

	/**
	 * @param procesDefinitionId the procesDefinitionId to set
	 */
	public void setProcesDefinitionId(String procesDefinitionId) {
		this.procesDefinitionId = procesDefinitionId;
	}

	/**
	 * @param processInstanceId the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = DefensiveUtil.copyOf(startTime);
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = DefensiveUtil.copyOf(endTime);
	}

	/**
	 * @param abortReason the abortReason to set
	 */
	public void setAbortReason(String abortReason) {
		this.abortReason = abortReason;
	}

	/**
	 * @param processInstanceVariables the processInstanceVariables to set
	 */
	public void setProcessInstanceVariables(Map<String, Object> processInstanceVariables) {
		this.processInstanceVariables = DefensiveUtil.unmodifiableMap(processInstanceVariables);
	}
}
