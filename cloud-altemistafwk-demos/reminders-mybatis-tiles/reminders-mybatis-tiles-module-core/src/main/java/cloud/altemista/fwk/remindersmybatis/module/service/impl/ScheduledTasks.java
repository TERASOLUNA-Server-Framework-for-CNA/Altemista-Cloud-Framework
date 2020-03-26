package cloud.altemista.fwk.remindersmybatis.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import cloud.altemista.fwk.common.monitoring.MonitoringService;

@Component
public class ScheduledTasks {

	@Autowired
	private MonitoringService service;
	
	@Scheduled(fixedDelay = 30000)
	public void checkAllIndicators() {
		this.service.executeAll();
	}
}
