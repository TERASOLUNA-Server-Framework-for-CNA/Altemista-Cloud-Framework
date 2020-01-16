package org.altemista.cloudfwk.it.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;
import org.altemista.cloudfwk.it.util.ITControllerUtil;

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
 * REST controller to test ApplicationMonitorableResourceImpl
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = ApplicationTestingMonitoringController.MAPPING, method = RequestMethod.GET)
public class ApplicationTestingMonitoringController {
	
	/** MAPPING String */
	public static final String MAPPING = "/applicationMonitorableResource";
	
	/** The default application monitorable resource. */
	@Autowired
	@Qualifier("monitoringServiceDefaultStorage")
	private MonitoringService service;
	
	/**
	 * It checks HTTP Indicator is booted up ok and it is creating a valid MonitoringInfo DTO
	 */
	@RequestMapping("/1")
	public MonitoringInfo checkHttpIndicator() {	
		MonitoringInfo info = this.service.execute("httpServer");
		this.checkMonitoringInfo(info);
		return info;

	}
	
	/**
	 * It checks Database Indicator is booted up ok and it is creating a valid MonitoringInfo DTO
	 */
	@RequestMapping("/2")
	public MonitoringInfo checkDatabaseIndicator() {	
		MonitoringInfo info = this.service.execute("database");
		this.checkMonitoringInfo(info);
		ITControllerUtil.assertEquals(Status.OK, info.getStatus());
		return info;
	}
	
	/**
	 * It checks Socket Indicator is booted up ok and it is creating a valid MonitoringInfo DTO 
	 */
	@RequestMapping("/3")
	public MonitoringInfo checkSocketIndicator() {	
		MonitoringInfo info = this.service.execute("socket");
		ITControllerUtil.assertEquals(Status.FAILED, info.getStatus());
		this.checkMonitoringInfo(info);
		return info;
	}
	
	/**
	 * It checks LDAP Indicator is booted up ok and it is creating a valid MonitoringInfo DTO
	 */
	@RequestMapping("/4")
	public MonitoringInfo checkLDAPIndicator() {	
		MonitoringInfo info = this.service.execute("ldap");
		ITControllerUtil.assertEquals(Status.FAILED, info.getStatus());
		this.checkMonitoringInfo(info);
		return info;
	}
	
	/**
	 * It checks that the MonitoringInfo it received is not null as well as its attributes.
	 * @param info MonitoringInfo
	 */
	private void checkMonitoringInfo(MonitoringInfo info) { 
		ITControllerUtil.assertNotNull(info);
		ITControllerUtil.assertNotNull(info.getStatus());
		ITControllerUtil.assertNotNull(info.getExecTime());
		ITControllerUtil.assertNotNull(info.getMessage());
		ITControllerUtil.assertNotNull(info.getResponseTime());
	}

	

}
