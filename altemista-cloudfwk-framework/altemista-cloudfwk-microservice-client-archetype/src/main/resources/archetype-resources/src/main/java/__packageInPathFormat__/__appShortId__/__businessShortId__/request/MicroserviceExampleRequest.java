package ${package}.${appShortId}.${businessShortId}.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MicroserviceExampleRequest {

	private String param;
	
	public MicroserviceExampleRequest() {
		
	}
	
	@JsonCreator
	public MicroserviceExampleRequest(
			@JsonProperty("param") String param) {
		this.param = param;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	@Override
	public String toString() {
		return "ExampleProviderRequest [param=" + param + "]";
	}
	
}
