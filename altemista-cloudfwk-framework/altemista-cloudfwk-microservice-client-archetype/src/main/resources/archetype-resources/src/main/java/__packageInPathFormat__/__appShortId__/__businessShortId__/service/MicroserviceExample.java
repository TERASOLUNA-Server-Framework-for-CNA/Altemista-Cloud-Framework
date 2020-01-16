package ${package}.${appShortId}.${businessShortId}.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ${package}.${appShortId}.${businessShortId}.request.MicroserviceExampleRequest;
import ${package}.${appShortId}.${businessShortId}.response.MicroserviceExampleResponse;

@FeignClient("${applicationName}")
public interface MicroserviceExample {

	@RequestMapping(value = "/exampleMethod", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public MicroserviceExampleResponse exampleMethod(@RequestBody MicroserviceExampleRequest request);

}
