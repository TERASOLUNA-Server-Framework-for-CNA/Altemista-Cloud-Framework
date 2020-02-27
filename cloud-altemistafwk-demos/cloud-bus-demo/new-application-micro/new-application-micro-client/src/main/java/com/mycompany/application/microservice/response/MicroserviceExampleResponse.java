package com.mycompany.application.microservice.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MicroserviceExampleResponse {

	private String result;
	
	public MicroserviceExampleResponse() {
		
	}

	@JsonCreator
	public MicroserviceExampleResponse(
			@JsonProperty("result") String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "ExampleProviderResponse [result=" + result + "]";
	}
	
}
