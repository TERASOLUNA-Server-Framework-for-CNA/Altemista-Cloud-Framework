package com.mycompany.application.microservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mycompany.application.microservice.request.MicroserviceExampleRequest;
import com.mycompany.application.microservice.response.MicroserviceExampleResponse;

@FeignClient("new-application-micro")
public interface MicroserviceExample {

	@RequestMapping(value = "/exampleMethod", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public MicroserviceExampleResponse exampleMethod(@RequestBody MicroserviceExampleRequest request);

}
