/**
 * 
 */
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;


/**
 * Wrapper around storage policy that logs MonitoringInfo when they are being added to the storage.
 * @author NTT DATA
 */
public class LoggerMonitoringStorageWrapperImpl extends AbstractMonitoringStorageWrapperImpl implements InitializingBean {

	/** The name of the logger where the performance information will be written */
	private String loggerName = "org.altemista.cloudfwk.MONITORING";
	
	/** The logger Logger */
	private Logger logger;
	
	/**	The format of the trace. */
	private String format = "Executed {} with status {} in {}ms: {}";
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// Obtains the logger
		Assert.notNull(this.loggerName);
		this.logger = LoggerFactory.getLogger(this.loggerName);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.impl.storage.wrapper.AbstractMonitoringStorageWrapperImpl#add(org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo)
	 */
	@Override
	public void add(MonitoringInfo info) {
		super.add(info);
		
		if (info.getStatus().equals(Status.OK)) {
			this.logger.info(this.format,
					info.getIndicatorId(), info.getStatus(), info.getResponseTime(), info.getMessage());
			
		} else {
			this.logger.error(this.format,
					info.getIndicatorId(), info.getStatus(), info.getResponseTime(), info.getMessage());	
		}
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		
		this.loggerName = loggerName;
	}
}
