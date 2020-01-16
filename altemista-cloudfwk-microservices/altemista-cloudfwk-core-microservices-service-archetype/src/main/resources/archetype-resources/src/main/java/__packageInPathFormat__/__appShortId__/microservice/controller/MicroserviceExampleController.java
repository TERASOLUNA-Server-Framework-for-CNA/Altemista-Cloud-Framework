package ${package}.${appShortId}.microservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ${package}.${appShortId}.microservice.request.MicroserviceExampleMethodRequest;
import ${package}.${appShortId}.microservice.response.MicroserviceExampleMethodResponse;

@RestController
public class MicroserviceExampleController {
	
	private static Logger logger = LoggerFactory.getLogger(MicroserviceExampleController.class);

	@RequestMapping(value = "/exampleProviderMethod", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public MicroserviceExampleMethodResponse exampleProviderMethod(@RequestBody MicroserviceExampleMethodRequest request) {
		if (logger.isInfoEnabled()){
			logger.info("init exampleProviderMethod");
			logger.info(request.toString());
		}
		String result = "Response from exampleProviderMethod: " + request.getParam();
		MicroserviceExampleMethodResponse response = new MicroserviceExampleMethodResponse(result);
		if (logger.isInfoEnabled()){
			logger.info(response.toString());
			logger.info("end exampleProviderMethod");
		}
		return response;
	}

}
