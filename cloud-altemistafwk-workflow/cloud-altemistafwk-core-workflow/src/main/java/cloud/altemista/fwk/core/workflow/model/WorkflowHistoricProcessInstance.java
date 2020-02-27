/**
 * 
 */
package cloud.altemista.fwk.core.workflow.model;

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

/**
 * An historic (either ongoing or past) process instances
 * @author NTT DATA
 */
public interface WorkflowHistoricProcessInstance {

	/**
	 * Gets the process definition ID
	 * @return the process definition ID
	 */
	String getProcesDefinitionId();

	/**
	 * Gets the process instance ID
	 * @return the process instance ID
	 */
	String getProcessInstanceId();
	
	/**
	 * Checks if the process instance is active or finished
	 * @return boolean if the process instance is active (true) or finished (false)
	 */
	boolean isActive();
	
	/**
	 * Gets the time the process instance was started
	 * @return the time the process instance was started
	 */
	Date getStartTime();
	
	/**
	 * Gets the time the process instance ended, if the process instance is not active
	 * @return the time the process instance ended
	 */
	Date getEndTime();
	
	/**
	 * Gets the reason for aborting
	 * @return the reason for aborting
	 * @see org.cloud.altemista.fwk.core.workflow.WorkflowExecutionService#abortInstance(String, String)
	 */
	String getAbortReason();

	/**
	 * Returns the map of process variables for the process instance of the task
	 * @return the variables
	 */
	Map<String, Object> getProcessInstanceVariables();
	
}
