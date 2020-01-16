package org.altemista.cloudfwk.remindersmybatis.module.controller.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.web.monitoring.controller.AbstractMonitoringController;

/*
 * #%L
 * altemista-cloudfwk monitoring integration tests
 * %%
 * Copyright (C) 2016 - 2017 
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */



/**
 * Simple REST controller to access monitoring info throw /health
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = ApplicationMonitoringController.MAPPING)
public class ApplicationMonitoringController extends AbstractMonitoringController {
	
	/** MAPPING String */
	public static final String MAPPING = "/health";
	
	/** The default application monitorable resource. */
	@Autowired
	private MonitoringService service;

	@Override
	protected MonitoringService getService() {
		return this.service;
	}

}
