package org.altemista.cloudfwk.remindersmybatis.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.altemista.cloudfwk.common.monitoring.MonitoringService;

@Component
public class ScheduledTasks {

	@Autowired
	private MonitoringService service;
	
	@Scheduled(fixedDelay = 30000)
	public void checkAllIndicators() {
		this.service.executeAll();
	}
}
