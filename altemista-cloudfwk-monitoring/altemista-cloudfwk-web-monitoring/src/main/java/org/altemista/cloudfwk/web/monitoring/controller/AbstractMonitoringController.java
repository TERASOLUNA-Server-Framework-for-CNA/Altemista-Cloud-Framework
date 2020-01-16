package org.altemista.cloudfwk.web.monitoring.controller;

/*
 * #%L
 * altemista-cloud monitoring: web part
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;

/**
 * Abstract Spring MVC Controller that allows exposing a {@link MonitoringService} as a REST API.
 * @author NTT DATA
 */
public abstract class AbstractMonitoringController implements InitializingBean {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// Service cannot be null
		if (this.getService() == null) {
			throw new MonitoringException("invalid_initialization_monitoring_controller");
		}
	}

	/**
	 * {@code GET "/{mapping}/"} retrieves the information of prior executions
	 * of all the indicators kwnown to this service.
	 * @return Map of indicator identifiers and the list with the stored information of prior executions
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<MonitoringInfo>> getAll() {
		
		return this.getService().getAll();
	}

	/**
	 * {@code GET "/{mapping}/{indicatorId}/"} retrieves the information of prior executions of the specified indicator
	 * @param id unique identifier of the indicator to retrieve
	 * @return list with the stored information of prior executions of the indicator
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<MonitoringInfo> get(@PathVariable("id") String id) {
		
		return this.getService().get(id);
	}

	/**
	 * {@code POST "/{mapping}/"} executes all the indicators known to this service and return the execution results
	 * @return List with the information about the execution of the indicators
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String, List<MonitoringInfo>> executeAll() {
		
		List<MonitoringInfo> list = this.getService().executeAll();
		
		// (list to map for homogeneity with getAll)
		Map<String, List<MonitoringInfo>> map = new LinkedHashMap<String, List<MonitoringInfo>>();
		for (MonitoringInfo info : list) {
			map.put(info.getIndicatorId(), Collections.<MonitoringInfo> singletonList(info));
		}
		return map;
	}

	/**
	 * {@code GET "/{mapping}/{indicatorId}/"} retrieves the information of prior executions of the specified indicator
	 * @param id unique identifier of the indicator to execute
	 * @return List of one MonitoringInfo with the information of the execution
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ResponseBody
	public List<MonitoringInfo> execute(@PathVariable("id") String id) {
		
		// (list for homogeneity with get)
		MonitoringInfo info = this.getService().execute(id);
		return (info != null)
				? Collections.<MonitoringInfo> singletonList(info)
				: Collections.<MonitoringInfo> emptyList();
	}

	/**
	 * @return the MonitoringService to expose as a REST API
	 */
	protected abstract MonitoringService getService();
}
