package cloud.altemista.fwk.core.monitoring;

/*
 * #%L
 * altemista-cloud monitoring
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;

/**
 * Defines an storage for prior executions of indicators
 * @author NTT DATA
 */
public interface MonitoringStorage {
	
	/**
	 * Saves an execution
	 * @param info MonitoringInfo to add
	 */
	void add(MonitoringInfo info);
	
	/**
	 * Retrieves the prior executions of an indicator
	 * @param id unique identifier of the indicator to retrieve
	 * @return list with the stored information of prior executions of the indicator
	 */
	List<MonitoringInfo> get(String id);
}
