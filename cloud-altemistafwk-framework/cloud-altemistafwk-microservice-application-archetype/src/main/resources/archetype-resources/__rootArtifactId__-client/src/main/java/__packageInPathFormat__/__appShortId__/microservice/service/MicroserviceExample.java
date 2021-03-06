package ${package}.${appShortId}.microservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ${package}.${appShortId}.microservice.request.MicroserviceExampleRequest;
import ${package}.${appShortId}.microservice.response.MicroserviceExampleResponse;

@FeignClient("${rootArtifactId}")
public interface MicroserviceExample {

	@RequestMapping(value = "/exampleMethod", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public MicroserviceExampleResponse exampleMethod(@RequestBody MicroserviceExampleRequest request);

}
