package com.mycompany.application.microservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.microservice.service.MicroserviceExample;
import com.mycompany.application.microservice.request.MicroserviceExampleRequest;
import com.mycompany.application.microservice.response.MicroserviceExampleResponse;

@RestController
public class MicroserviceExampleController implements MicroserviceExample {
	
	private static Logger logger = LoggerFactory.getLogger(MicroserviceExampleController.class);

	@Override
	@RequestMapping(value = "/exampleMethod", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public MicroserviceExampleResponse exampleMethod(@RequestBody MicroserviceExampleRequest request) {
		if (logger.isInfoEnabled()){
			logger.info("init exampleProviderMethod");
			logger.info(request.toString());
		}
		String result = "Response from exampleMethod: " + request.getParam();
		MicroserviceExampleResponse response = new MicroserviceExampleResponse(result);
		if (logger.isInfoEnabled()){
			logger.info(response.toString());
			logger.info("end exampleProviderMethod");
		}
		return response;
	}

}
