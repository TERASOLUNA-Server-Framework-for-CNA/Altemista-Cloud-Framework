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

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.workflow.WorkflowHistoryService;
import cloud.altemista.fwk.core.workflow.exception.WorkflowException;
import cloud.altemista.fwk.core.workflow.model.WorkflowHistoricProcessInstance;
import cloud.altemista.fwk.core.workflow.model.impl.WorkflowHistoricProcessInstanceImpl;
import cloud.altemista.fwk.workflow.activiti.model.impl.ActivitiWorkflowHistoricProcessInstanceImpl;

/**
 * Historic workflows service implementation with Activiti
 * @author NTT DATA
 */
@Component
public class ActivitiWorkflowHistoryServiceImpl implements WorkflowHistoryService {
	
	/** Activiti ongoing and past process instances service */
	private final HistoryService historyService;

	/**
	 * Constructor
	 * @param processEngine Provides access to all the Activiti services
	 */
	@Autowired
	public ActivitiWorkflowHistoryServiceImpl(ProcessEngine processEngine) {
		super();
		
		Assert.notNull(processEngine, "ProcessEngine must not be null");
		
		this.historyService = processEngine.getHistoryService();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHistoryService#getProcessInstance(java.lang.String)
	 */
	@Override
	public WorkflowHistoricProcessInstance getProcessInstance(String processInstanceId) {
		
		// (sanity checks)
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		// Queries the historic service
		try {
			HistoricProcessInstanceQuery query =
					this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId);
			HistoricProcessInstance historicProcessInstance = query.singleResult();
			return this.toWorkflowHistoricProcessInstance(historicProcessInstance);
			
		} catch (ActivitiException e) {
			throw new WorkflowException("not_unique_task", new Object[]{ processInstanceId }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHistoryService#findAllProcessInstances(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowHistoricProcessInstance> findAllProcessInstances(Pageable pageable, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the historic service
		final HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHistoryService#findActiveProcessInstances(org.springframework.data.domain.Pageable, java.lang.String[])
	 */
	@Override
	public Page<WorkflowHistoricProcessInstance> findActiveProcessInstances(Pageable pageable, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the historic service
		final HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery()
				.unfinished();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}

	@Override
	public Page<WorkflowHistoricProcessInstance> findFinishedProcessInstances(Pageable pageable, String... processDefinitionIds) {
		
		// (sanity checks)
		Assert.notNull(pageable, "The pageable must not be null");
		
		// Queries the historic service
		final HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery()
				.finished();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		return this.execute(query, pageable);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHistoryService#deleteFinishedProcessInstance(java.lang.String)
	 */
	@Override
	public void deleteFinishedProcessInstance(String processInstanceId) {
		
		// (sanity checks)
		Assert.hasText(processInstanceId, "The process instance ID is required");
		
		// Queries the historic service
		this.historyService.deleteHistoricProcessInstance(processInstanceId);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.WorkflowHistoryService#deleteFinishedProcessInstances(java.lang.String)
	 */
	@Override
	public void deleteFinishedProcessInstances(String... processDefinitionIds) {
		
		// Queries the historic service
		HistoricProcessInstanceQuery query = this.historyService.createHistoricProcessInstanceQuery()
				.finished();
		if (ArrayUtils.isNotEmpty(processDefinitionIds)) {
			query.processDefinitionKeyIn(Arrays.asList(processDefinitionIds));
		}
		for (HistoricProcessInstance processInstance : query.list()) {
			this.historyService.deleteHistoricProcessInstance(processInstance.getId());
		}
	}
	
	/**
	 * Convenience method to execute a paginated HistoricProcessInstanceQuery and convert the result
	 * @param query HistoricProcessInstanceQuery
	 * @param pageable Pageable
	 * @return Page of WorkflowHistoricProcessInstance
	 */
	private Page<WorkflowHistoricProcessInstance> execute(HistoricProcessInstanceQuery query, Pageable pageable) {
		
		final long count = query.count();
		this.order(query, pageable.getSort());
		final List<HistoricProcessInstance> list = query
				.includeProcessVariables()
				.listPage(Math.toIntExact(pageable.getOffset()), pageable.getPageSize());
		return new PageImpl<WorkflowHistoricProcessInstance>(
				this.toWorkflowHistoricProcessInstances(list), pageable, count);
	}
	
	/**
	 * Convenience method to append order clauses to a HistoricProcessInstanceQuery
	 * @param query HistoricProcessInstanceQuery
	 * @param sort Sort
	 */
	private void order(HistoricProcessInstanceQuery query, Sort sort) {
		
		if (sort == null) {
			return;
		}
		
		for (Order order : sort) {
			
			if (PROCESS_INSTANCE_ID_ASC.equals(order)) {
				query.orderByProcessInstanceId().asc();
			} else if (PROCESS_INSTANCE_ID_DESC.equals(order)) {
				query.orderByProcessInstanceId().desc();
				
			} else if (PROCESS_DEFINITION_ID_ASC.equals(order)) {
				query.orderByProcessDefinitionId().asc();
			} else if (PROCESS_DEFINITION_ID_DESC.equals(order)) {
				query.orderByProcessDefinitionId().desc();
				
			} else if (START_TIME_ASC.equals(order)) {
				query.orderByProcessInstanceStartTime().asc();
			} else if (START_TIME_DESC.equals(order)) {
				query.orderByProcessInstanceStartTime().desc();
				
			} else if (END_TIME_ASC.equals(order)) {
				query.orderByProcessInstanceEndTime().asc();
			} else if (END_TIME_DESC.equals(order)) {
				query.orderByProcessInstanceEndTime().desc();
				
			} else if (DURATION_ASC.equals(order)) {
				query.orderByProcessInstanceDuration().asc();
			} else if (DURATION_DESC.equals(order)) {
				query.orderByProcessInstanceDuration().desc();
			}
		}
	}
	
	/**
	 * Convenience method to convert a list of tasks
	 * @param list List of HistoricProcessInstance
	 * @return List of WorkflowHistoricProcessInstance
	 */
	private List<WorkflowHistoricProcessInstance> toWorkflowHistoricProcessInstances(List<HistoricProcessInstance> list) {
		
		// (sanity checks)
		if (list == null) {
			return null; // NOSONAR
		}
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Returns a list of ActivitiWorkflowHistoricProcessInstanceImpl
		List<WorkflowHistoricProcessInstance> ret = new ArrayList<WorkflowHistoricProcessInstance>();
		for (HistoricProcessInstance task : list) {
			ret.add(this.toWorkflowHistoricProcessInstance(task));
		}
		return ret;
	}
	
	/**
	 * Convenience method to convert a task
	 * @param historicProcessInstance HistoricProcessInstance
	 * @return WorkflowHistoricProcessInstance
	 */
	private WorkflowHistoricProcessInstance toWorkflowHistoricProcessInstance(
			HistoricProcessInstance historicProcessInstance) {
		
		// (sanity checks)
		if (historicProcessInstance == null) {
			return null;
		}
		
		// Instantiates a serializable WorkflowHistoricProcessInstanceImpl from a new ActivitiWorkflowHistoricProcessInstanceImpl
		return new WorkflowHistoricProcessInstanceImpl(
				new ActivitiWorkflowHistoricProcessInstanceImpl(historicProcessInstance));
	}
}
