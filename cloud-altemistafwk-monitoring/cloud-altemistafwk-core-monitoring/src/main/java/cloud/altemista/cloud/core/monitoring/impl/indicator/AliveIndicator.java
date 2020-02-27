package cloud.altemista.fwk.core.monitoring.impl.indicator;

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


import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;
import cloud.altemista.fwk.common.monitoring.model.Status;

/**
 * Indicator that always returns the status OK.
 * It can be used to simply check the application is deployed.
 * @author NTT DATA
 */
public class AliveIndicator extends AbstractIndicator {
	
	/** The description */
	private static final String DESCRIPTION = "Alive";

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {
		
		return new MonitoringInfo(this.getId(), this.getDescription(), Status.OK);
	}
}
