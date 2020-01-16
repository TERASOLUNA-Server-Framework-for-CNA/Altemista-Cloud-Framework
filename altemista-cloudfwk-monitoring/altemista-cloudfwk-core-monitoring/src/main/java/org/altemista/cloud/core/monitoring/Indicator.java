package org.altemista.cloudfwk.core.monitoring;

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


import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;

/**
 * A monitoring indicator that registers information of one resource.
 * @author NTT DATA
 */
public interface Indicator {
	
	/**
	 * @return unique identifier of the indicator
	 */
	public String getId();
	
	/**
	 * @return description of the indicator (i.e.: for logging purposes)
	 */
	public String getDescription();
	
	/**
	 * Executes the indicator
	 * @return MonitoringInfo with the information of the execution
	 */
	public MonitoringInfo execute();
}
