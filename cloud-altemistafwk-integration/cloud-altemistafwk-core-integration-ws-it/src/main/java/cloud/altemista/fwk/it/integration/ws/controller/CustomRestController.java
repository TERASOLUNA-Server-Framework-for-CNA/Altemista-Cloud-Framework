package cloud.altemista.fwk.it.integration.ws.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.it.integration.ws.gateway.RequestGateway;

@RestController
@RequestMapping(CustomRestController.MAPPING)
public class CustomRestController {

	public static final String MAPPING = "/integrationws";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RequestGateway requestGateway;

	@RequestMapping(value="test/{param}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,String>> testMethod(@PathVariable("param") String param) {
		Map<String,String> result = new HashMap<String,String>();
		String responseValue = requestGateway.requestGatewayTestMethod("OK!!!");
		result.put("result",responseValue);
		return ResponseEntity.ok(result);
	}
}
