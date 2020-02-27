package cloud.altemista.fwk.common.monitoring;

/*
 * #%L
 * altemista-cloud monitoring: common interfaces
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;
import java.util.Map;

import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;

/**
 * Main interface of the monitoring feature
 * @author NTT DATA
 */
public interface MonitoringService {

	/**
	 * Executes all the indicators known to this service
	 * and returns a list with the information of the executions
	 * @return List with the information about the execution of the indicators
	 */
	List<MonitoringInfo> executeAll();

	/**
	 * Executes one indicator kwnown to this service
	 * @param id unique identifier of the indicator to execute
	 * @return MonitoringInfo with the information of the execution
	 */
	MonitoringInfo execute(String id);

	/**
	 * Retrieves the information of prior executions of all the indicators kwnown to this service
	 * @return Map of indicator identifiers and the list with the stored information of prior executions
	 */
	Map<String, List<MonitoringInfo>> getAll();

	/**
	 * Retrieves the information of prior executions of one indicator kwnown to this service
	 * @param id unique identifier of the indicator to retrieve
	 * @return list with the stored information of prior executions of the indicator
	 */
	List<MonitoringInfo> get(String id);
}
