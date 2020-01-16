package org.altemista.cloudfwk.core.monitoring.impl.storage.wrapper;

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
import java.util.List;

import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.core.monitoring.MonitoringStorage;

/**
 * Base class for using the decorator pattern for storages of prior executions of indicators
 * (e.g.: to add logging, mail on error, etc.)
 * @author NTT DATA
 */
public abstract class AbstractMonitoringStorageWrapperImpl implements MonitoringStorage {
	
	/** The original MeasuresStorage that will be wrapped */
	private MonitoringStorage target;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.MonitoringStorage#add(org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo)
	 */
	@Override
	public void add(MonitoringInfo info) {
		
		// (simply delegates to target)
		if (this.target != null) {
			this.target.add(info);
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.MonitoringStorage#get(java.lang.String)
	 */
	@Override
	public List<MonitoringInfo> get(String id) {
		
		// (simply delegates to target)
		return (this.target != null)
				? this.target.get(id)
				: Collections.<MonitoringInfo> emptyList();
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(MonitoringStorage target) {
	
		this.target = target;
	}
}
