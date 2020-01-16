package org.altemista.cloudfwk.test.integration.ip;

public class IntegrationIpEchoService {

	public String test(String input) {
		if ("FAIL".equals(input)) {
			throw new RuntimeException("Failure Demonstration");
		}
		return "echo:" + input;
	}

}
