package cloud.altemista.fwk.it.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.common.monitoring.MonitoringService;
import cloud.altemista.fwk.web.monitoring.controller.AbstractMonitoringController;

/*
 * #%L
 * altemista-cloud monitoring integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */



/**
 * REST controller to test AbstractMonitoringController
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
