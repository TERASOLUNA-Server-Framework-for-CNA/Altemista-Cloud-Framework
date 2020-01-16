package ${package}.${appShortId}.microservice.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MicroserviceExampleMethodResponse {

	private String result;
	private Long providerPort;

	@JsonCreator
	public MicroserviceExampleMethodResponse(
			@JsonProperty("result") String result,
			@JsonProperty("providerPort") Long providerPort) {
		this.result = result;
		this.providerPort = providerPort;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Long getProviderPort() {
		return providerPort;
	}

	public void setProviderPort(Long providerPort) {
		this.providerPort = providerPort;
	}

	@Override
	public String toString() {
		return "ExampleProviderMethodResponse [result=" + result + ", providerPort=" + providerPort + "]";
	}
	
}
