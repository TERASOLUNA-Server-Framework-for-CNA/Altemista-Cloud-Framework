package ${groupId}.${appShortId}.${businessShortId}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.common.monitoring.MonitoringService;
import cloud.altemista.fwk.web.monitoring.controller.AbstractMonitoringController;

@RestController
@RequestMapping("/${businessShortId}/health")
public class MonitoringController extends AbstractMonitoringController {
	
	/** The monitoring service of the ${businessShortId} module. */
	@Autowired
	@Qualifier("${businessShortId}MonitoringService")
	private MonitoringService service;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.web.monitoring.controller.AbstractMonitoringController#getService()
	 */
	@Override
	protected MonitoringService getService() {
		
		return this.service;
	}
}
