package org.altemista.cloudfwk.core.monitoring.impl.indicator;

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


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.core.monitoring.Indicator;

/**
 * convenience base class for implementing Indicators
 * @author NTT DATA
 */
public abstract class AbstractIndicator implements Indicator, InitializingBean {

	/** The unique identifier of the indicator */
	private String id;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		if (StringUtils.isBlank(id)) {
			throw new MonitoringException("invalid_indicator_id", new Object[] { id });
		}
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#getId()
	 */
	@Override
	public String getId() {
		
		return this.id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
	
		this.id = id;
	}

}
