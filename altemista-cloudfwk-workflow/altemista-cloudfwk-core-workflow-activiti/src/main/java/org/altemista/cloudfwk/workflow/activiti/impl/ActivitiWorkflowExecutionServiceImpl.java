
package org.altemista.cloudfwk.workflow.activiti.impl;

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


import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService;
import org.altemista.cloudfwk.core.workflow.WorkflowExecutionService;
import org.altemista.cloudfwk.core.workflow.exception.WorkflowException;
import org.altemista.cloudfwk.core.workflow.impl.AbstractAuthenticableWorkflowService;

/**
 * Workflow execution service implementation with Activiti
 * @author NTT DATA
 */
@Component
public class ActivitiWorkflowExecutionServiceImpl
		extends AbstractAuthenticableWorkflowService<WorkflowAuthenticationAwareExecutionService>
		implements WorkflowExecutionService {

	/** Activiti runtime service */
	private final RuntimeService runtimeService;
	
	/** Activiti identity service */
	private final IdentityService identityService;

	/**
	 * Constructor
	 * @param processEngine Provides access to all the Activiti services
	 */
	@Autowired
	public ActivitiWorkflowExecutionServiceImpl(ProcessEngine processEngine) {
		super();
		
		Assert.notNull(processEngine, "ProcessEngine must not be null");
		
		this.runtimeService = processEngine.getRuntimeService();
		this.identityService = processEngine.getIdentityService();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.AuthenticableWorkflowService#anonymously()
	 */
	@Override
	public WorkflowAuthenticationAwareExecutionService anonymously() {
		
		return this;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.AuthenticableWorkflowService#withUser(java.lang.String)
	 */
	@Override
	public WorkflowAuthenticationAwareExecutionService withUser(String userId) {
		
		Assert.hasText(userId, "The user ID is required");
		
		return new WithUser(userId); 
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#start(java.lang.String)
	 */
	@Override
	public String start(String processDefinitionId) {
		
		return this.start(processDefinitionId, null);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#start(java.lang.String, java.util.Map)
	 */
	@Override
	public String start(String processDefinitionId, Map<String, Object> parameters) {
		
		// Sanity check
		Assert.hasText(processDefinitionId, "The process definition ID is required");
		
		try {
			ProcessInstance instance = this.runtimeService.startProcessInstanceByKey(processDefinitionId, parameters);
			return instance.getProcessInstanceId();
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_definition_not_found", new Object[]{ processDefinitionId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#getInstanceVariables(java.lang.String)
	 */
	@Override
	public Map<String, Object> getInstanceVariables(String processInstanceId) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		try {
			return this.runtimeService.getVariables(processInstanceId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#getInstanceVariable(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getInstanceVariable(String processInstanceId, String key) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		Assert.hasText(key, "The key for the variable is required");
		
		try {
			return this.runtimeService.getVariable(processInstanceId, key);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#setInstanceVariable(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInstanceVariable(String processInstanceId, String key, Object value) {
		
		if (value == null) {
			this.removeInstanceVariable(processInstanceId, key);
			return;
		}
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		Assert.hasText(key, "The key for the variable is required");
		
		try {
			this.runtimeService.setVariable(processInstanceId, key, value);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#removeInstanceVariable(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeInstanceVariable(String processInstanceId, String key) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		Assert.hasText(key, "The key for the variable is required");
		
		try {
			this.runtimeService.removeVariable(processInstanceId, key);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#suspendInstance(java.lang.String)
	 */
	@Override
	public void suspendInstance(String processInstanceId) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		try {
			this.runtimeService.suspendProcessInstanceById(processInstanceId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
			
		} catch (ActivitiException e) {
			throw new WorkflowException("invalid_process_instance_state", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#continueInstance(java.lang.String)
	 */
	@Override
	public void continueInstance(String processInstanceId) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		try {
			this.runtimeService.activateProcessInstanceById(processInstanceId);
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
			
		} catch (ActivitiException e) {
			throw new WorkflowException("invalid_process_instance_state", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.WorkflowExecutionService#abortInstance(java.lang.String, java.lang.String)
	 */
	@Override
	public void abortInstance(String processInstanceId, String reason) {
		
		// Sanity check
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		try {
			this.runtimeService.deleteProcessInstance(processInstanceId, StringUtils.trimToNull(reason));
			
		} catch (ActivitiObjectNotFoundException e) {
			throw new WorkflowException("process_instance_not_found", new Object[]{ processInstanceId }, e);
		}
	}

	/**
	 * Wraps the authentication aware methods with Activiti authentication of the specified user
	 * @author NTT DATA
	 */
	private class WithUser implements WorkflowAuthenticationAwareExecutionService {
		
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
		 * @see org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService#start(java.lang.String)
		 */
		@Override
		public String start(String processDefinitionId) {
			
			try {
				ActivitiWorkflowExecutionServiceImpl.this.identityService.setAuthenticatedUserId(this.userId);
				return ActivitiWorkflowExecutionServiceImpl.this.start(processDefinitionId);
				
			} finally {
				ActivitiWorkflowExecutionServiceImpl.this.identityService.setAuthenticatedUserId(null);
			}
		}

		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService#start(java.lang.String, java.util.Map)
		 */
		@Override
		public String start(String processDefinitionId, Map<String, Object> parameters) {
			
			try {
				ActivitiWorkflowExecutionServiceImpl.this.identityService.setAuthenticatedUserId(this.userId);
				return ActivitiWorkflowExecutionServiceImpl.this.start(processDefinitionId, parameters);
				
			} finally {
				ActivitiWorkflowExecutionServiceImpl.this.identityService.setAuthenticatedUserId(null);
			}
		}

		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService#suspendInstance(java.lang.String)
		 */
		@Override
		public void suspendInstance(String processInstanceId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowExecutionServiceImpl.this.suspendInstance(processInstanceId);
		}

		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService#continueInstance(java.lang.String)
		 */
		@Override
		public void continueInstance(String processInstanceId) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowExecutionServiceImpl.this.continueInstance(processInstanceId);
		}

		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.workflow.WorkflowAuthenticationAwareExecutionService#abortInstance(java.lang.String, java.lang.String)
		 */
		@Override
		public void abortInstance(String processInstanceId, String reason) {
			
			// (Activiti does not use authentication in this operation)
			ActivitiWorkflowExecutionServiceImpl.this.abortInstance(processInstanceId, reason);
		}

	}
}
