/**
 * 
 */
package cloud.altemista.fwk.it.monitoring;

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


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import cloud.altemista.fwk.common.monitoring.model.Status;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.monitoring.controller.ApplicationMonitoringController;

/**
 * Integration test that invokes the REST controller to test ApplicationMonitorableResourceImpl
 * @author NTT DATA
 */
public class ApplicationMonitorableControllerIT extends AbstractIT {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private static int numberIndicators = 6;
	
	private static String indicatorID = "httpServer";
	
	
	/**
	 * POST /
	 */
	@Test
	public void testExecuteAll() {
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING));
		ResponseEntity<?> response = restTemplate.postForEntity(url, null, Map.class, HttpStatus.OK);
		
		@SuppressWarnings("unchecked")
		Map<String, List<MonitoringView>> data = (Map<String, List<MonitoringView>>) response.getBody();
		Assert.assertEquals(numberIndicators, data.size());
	}
	
	/**
	 * POST /{id}
	 */
	@Test
	public void testExecuteOne() {
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING + String.format("/%s", indicatorID)));
		ResponseEntity<MonitoringView[]> response = restTemplate.postForEntity(url, null, MonitoringView[].class, HttpStatus.OK);
		List<MonitoringView> data = Arrays.asList(response.getBody());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals(Status.FAILED, data.get(0).getStatus());
	}
	
	/**
	 * POST /{mongo}
	 */
	@Test
	public void testExecuteMongo() {
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING + String.format("/%s", "mongo")));
		ResponseEntity<MonitoringView[]> response = restTemplate.postForEntity(url, null, MonitoringView[].class, HttpStatus.OK);
		List<MonitoringView> data = Arrays.asList(response.getBody());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals(Status.OK, data.get(0).getStatus());
	}
	
	/**
	 * GET /	
	 */
	@Test
	public void testGetAllMonitoringInfo() {
		
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING));
		ResponseEntity<?> response = restTemplate.getForEntity(url, Map.class, HttpStatus.OK);
		
		@SuppressWarnings("unchecked")
		Map<String, List<MonitoringView>> data = (Map<String, List<MonitoringView>>) response.getBody();
		
		Assert.assertEquals(numberIndicators, data.size()); // The map contains an entry for each defined indicator
	}
	
	/**
	 * GET /{id} (valid)
	 */
	@Test
	public void testGetMonitoringInfo() {
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING + String.format("/%s", indicatorID)));
		ResponseEntity<MonitoringView[]> response = restTemplate.getForEntity(url, MonitoringView[].class, HttpStatus.OK);
		List<MonitoringView> data = Arrays.asList(response.getBody());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals(Status.FAILED, data.get(0).getStatus());
	}
	
	/**
	 * GET /{id} (invalid)
	 */
	@Test
	public void testGetMonitoringInfoInvalidIndicator() {
		String url = this.resolver.getUrl(this.resolver.getUri(ApplicationMonitoringController.MAPPING + "/invalidIndicator"));
		ResponseEntity<MonitoringView[]> response = restTemplate.getForEntity(url, MonitoringView[].class, HttpStatus.OK);
		List<MonitoringView> data = Arrays.asList(response.getBody());
		Assert.assertEquals(0, data.size());
	}
}
