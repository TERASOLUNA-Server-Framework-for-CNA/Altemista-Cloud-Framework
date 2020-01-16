package org.altemista.cloudfwk.core.monitoring.impl;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.core.monitoring.Indicator;
import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;

/**
 * Default implementation of the main interface of Monitoring feature
 * @author NTT DATA
 */
public class MonitoringServiceImpl implements MonitoringService, InitializingBean {

	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringServiceImpl.class);

	/** The indicators known to this service */
	private List<Indicator> indicators;

	/** The (optional) storage of information about prior executions */
	private MonitoringStorage storage;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// The list of indicators cannot be empty
		if (CollectionUtils.isEmpty(this.indicators)) {
			throw new MonitoringException("invalid_initialization_monitoring_service");
		}

		// Warn if the list of indicators contains a duplciate ID
		final Set<String> ids = new HashSet<String>();
		for (Indicator indicator : this.indicators) {
			String id = indicator.getId();
			if (!ids.add(id)) {
				LOGGER.warn("Duplicated indicator identifier found: %s", new Object[] { id });
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.monitoring.MonitoringService#executeAll()
	 */
	@Override
	public List<MonitoringInfo> executeAll() {
		
		// Executes all indicators in the system and adds their result to an list
		List<MonitoringInfo> list = new ArrayList<MonitoringInfo>();
		for (Indicator indicator : this.indicators) {
			list.add(this.execute(indicator));
		}
		return list;

	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.monitoring.MonitoringService#execute(java.lang.String)
	 */
	@Override
	public MonitoringInfo execute(String id) {
		
		// (sanity check)
		if (StringUtils.isBlank(id)) {
			return null;
		}
		
		// Executes the indicator with the specified identifier
		for (Indicator indicator : this.indicators) {
			if (StringUtils.equals(indicator.getId(), id)) {
				return this.execute(indicator);
			}
		}
		
		// Warn if not found
		LOGGER.warn("Requested execution of non-existent indicator: {}", new Object[] { id });
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.monitoring.MonitoringService#getAll()
	 */
	@Override
	public Map<String, List<MonitoringInfo>> getAll() {
		
		Map<String, List<MonitoringInfo>> map = new LinkedHashMap<String, List<MonitoringInfo>>();
		for (Indicator indicator : this.indicators) {
			String id = indicator.getId();
			map.put(id, this.get(id));
		}
		
		return map;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.monitoring.MonitoringService#get(java.lang.String)
	 */
	@Override
	public List<MonitoringInfo> get(String id) {
		
		// (sanity check)
		if ((this.storage == null) || StringUtils.isBlank(id)) {
			return Collections.emptyList();
		}
		
		return this.storage.get(id);
	}

	/**
	 * Convenience method to execute one indicator and store the exceution in the optional storage
	 * @param indicator, not null
	 * @return MonitoringInfo
	 */
	private MonitoringInfo execute(Indicator indicator) {
		
		// Executes the indicator
		MonitoringInfo monitoringInfo = indicator.execute();
		
		// Stores the execution in the optional storage
		if ((this.storage != null) && (monitoringInfo != null)) {
			this.storage.add(monitoringInfo);
		}
		
		return monitoringInfo;
	}

	/**
	 * @param indicators the indicators to set
	 */
	public void setIndicators(List<Indicator> indicators) {
	
		this.indicators = indicators;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(MonitoringStorage storage) {
	
		this.storage = storage;
	}
}
