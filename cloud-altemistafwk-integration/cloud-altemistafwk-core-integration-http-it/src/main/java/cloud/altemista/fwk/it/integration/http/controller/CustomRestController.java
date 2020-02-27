package cloud.altemista.fwk.it.integration.http.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.it.integration.http.request.RequestGateway;

@RestController
@RequestMapping(CustomRestController.MAPPING)
public class CustomRestController {

	public static final String MAPPING = "/integrationhttp";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RequestGateway requestGateway;

	@RequestMapping(value="test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,String>> testMethod() {
		Map<String,String> result = new HashMap<String,String>();
		String responseValue = requestGateway.echo("OK!!!");
		result.put("result",responseValue);
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value="response", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String responseMethod(@RequestBody String message) {
		logger.info(" ======> responseMethod init!!!");
		return "responseMethod return: " + message;
	}
}
