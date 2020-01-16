package org.altemista.cloudfwk.workflow.activiti.model.impl;

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

import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance;

/**
 * An historic (either ongoing or past) process instance implementation, based on Activiti
 * @author NTT DATA
 */
public class ActivitiWorkflowHistoricProcessInstanceImpl implements WorkflowHistoricProcessInstance {
	
	/** The Activiti historic process instance */
	private final HistoricProcessInstance instance;
	
	/**
	 * Constructor
	 * @param instance the Activiti historic process instance
	 */
	public ActivitiWorkflowHistoricProcessInstanceImpl(HistoricProcessInstance instance) {
		super();
		Assert.notNull(instance, "The HistoricProcessInstance is required");
		
		this.instance = instance;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcesDefinitionId()
	 */
	@Override
	public String getProcesDefinitionId() {
		
		return this.instance.getProcessDefinitionKey();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcessInstanceId()
	 */
	@Override
	public String getProcessInstanceId() {
		
		return this.instance.getId();
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#isActive()
	 */
	@Override
	public boolean isActive() {
		
		return this.instance.getEndTime() == null;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		
		return this.instance.getStartTime();
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		
		return this.instance.getEndTime();
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getAbortReason()
	 */
	@Override
	public String getAbortReason() {
		
		return this.instance.getDeleteReason();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance#getProcessInstanceVariables()
	 */
	@Override
	public Map<String, Object> getProcessInstanceVariables() {
		
		return this.instance.getProcessVariables();
	}
}
