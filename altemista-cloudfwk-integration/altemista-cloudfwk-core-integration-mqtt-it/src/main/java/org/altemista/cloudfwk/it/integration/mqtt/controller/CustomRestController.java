package org.altemista.cloudfwk.it.integration.mqtt.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.it.integration.mqtt.config.IntegrationMQTTConfiguration.MqttMsgproducer;

@RestController
@RequestMapping(CustomRestController.MAPPING)
public class CustomRestController {

	public static final String MAPPING = "/integrationmqtt";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = false)
	private MqttMsgproducer producer;
	

	@RequestMapping(value="test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,String>> testMethod() {
		Map<String,String> result = new HashMap<String,String>();
		if (producer != null) {
			producer.sendToMqtt("OK!");
		} else {
			logger.info(" ====> MqttMsgproducer is null!!!");
			logger.info("       Maybe you have to set `integration.mqtt.test.active=true` in `it-app-integration.properties` properties file?");
		}
		result.put("result","OK!");
		return ResponseEntity.ok(result);
	
	}
	
}
