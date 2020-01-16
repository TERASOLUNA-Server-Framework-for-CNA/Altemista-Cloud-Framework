
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


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance;

/**
 * The historic workflows service interface; contains information about ongoing and past process instances.
 * @author NTT DATA
 */
public interface WorkflowHistoryService {
	
	/** Retrieves tasks ordered by process instance ID (ascending order) */
	Order PROCESS_INSTANCE_ID_ASC = new Order(Direction.ASC, "processInstanceId");

	/** Retrieves tasks ordered by process instance ID (descending order) */
	Order PROCESS_INSTANCE_ID_DESC = new Order(Direction.DESC, "processInstanceId");
	
	/** Retrieves tasks ordered by process instance ID (ascending order) */
	Order PROCESS_DEFINITION_ID_ASC = new Order(Direction.ASC, "processInstanceId");

	/** Retrieves tasks ordered by process instance ID (descending order) */
	Order PROCESS_DEFINITION_ID_DESC = new Order(Direction.DESC, "processInstanceId");
	
	/** Retrieves tasks ordered by start time (ascending order) */
	Order START_TIME_ASC = new Order(Direction.ASC, "startTime");

	/** Retrieves tasks ordered by start time (descending order) */
	Order START_TIME_DESC = new Order(Direction.DESC, "startTime");
	
	/** Retrieves tasks ordered by end time (ascending order) */
	Order END_TIME_ASC = new Order(Direction.ASC, "endTime");

	/** Retrieves tasks ordered by end time (descending order) */
	Order END_TIME_DESC = new Order(Direction.DESC, "endTime");
	
	/** Retrieves tasks ordered by duration (ascending order) */
	Order DURATION_ASC = new Order(Direction.ASC, "duration");

	/** Retrieves tasks ordered by duration (descending order) */
	Order DURATION_DESC = new Order(Direction.DESC, "duration");
	
	//
	
	/**
	 * Gets an ongoing or past process by the process instance ID
	 * @param processInstanceId the process instance ID
	 * @return WorkflowHistoricProcessInstance
	 */
	WorkflowHistoricProcessInstance getProcessInstance(String processInstanceId);
	
	/**
	 * Gets ongoing or past process instances for a set of specific process definitions
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs for filtering
	 * @return Page of WorkflowHistoricProcessInstance
	 */
	Page<WorkflowHistoricProcessInstance> findAllProcessInstances(
			Pageable pageable, String... processDefinitionIds);
	
	/**
	 * Gets ongoing process instances for a set of specific process definitions
	 * @param pageable pagination information
	 * @param processDefinitionIds the optional process definition IDs for filtering
	 * @return Page of WorkflowHistoricProcessInstance
	 */
	Page<WorkflowHistoricProcessInstance> findActiveProcessInstances(
			Pageable pageable, String... processDefinitionIds);
	
	/**
	 * Gets past process instances for a set of specific process definitions
	 * @param pageable pagination information
	 * @param processDefinitionIds the process definition IDs for filtering
	 * @return Page of WorkflowHistoricProcessInstance
	 */
	Page<WorkflowHistoricProcessInstance> findFinishedProcessInstances(
			Pageable pageable, String... processDefinitionIds);
	
	/**
	 * Clears one past processes by the process instance ID
	 * @param processInstanceId the process instance ID
	 */
	void deleteFinishedProcessInstance(String processInstanceId);
	
	/**
	 * Clears all the past processes, or the past processes of a set of specific process definition
	 * @param processDefinitionIds the optional process definition IDs for filtering
	 */
	void deleteFinishedProcessInstances(String... processDefinitionIds);
}
