package cloud.altemista.fwk.core.monitoring.impl.storage;

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


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;
import cloud.altemista.fwk.core.monitoring.MonitoringStorage;

/**
 * Default implementation of an storage for prior executions of indicators.
 * It stores only the last execution of each indicator
 * @author NTT DATA
 */
public class MonitoringStorageImpl implements MonitoringStorage {
	
	/** The actual storage */
	private Map<String, MonitoringInfo> storage;

	/**
	 * Constructor
	 */
	public MonitoringStorageImpl() {
		super();
		
		this.storage = new HashMap<String, MonitoringInfo>();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.MonitoringStorage#add(cloud.altemista.fwk.common.monitoring.model.MonitoringInfo)
	 */
	@Override
	public void add(MonitoringInfo info) {
		
		// (sanity check)
		if (info == null) {
			return;
		}
		
		this.storage.put(info.getIndicatorId(), info);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.MonitoringStorage#get(java.lang.String)
	 */
	@Override
	public List<MonitoringInfo> get(String id) {
		
		// (sanity check)
		if (StringUtils.isBlank(id)) {
			return Collections.emptyList();
		}
		
		MonitoringInfo info = this.storage.get(id);
		return (info != null)
				? Collections.<MonitoringInfo> singletonList(info)
				: Collections.<MonitoringInfo> emptyList();
	}
}
