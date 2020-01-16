package com.mycompany.altemistamoviesui.webapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GatewayConfiguration {

	@Value("${spring.cloud.gateway.protocol}")
	private String protocol;
	
	@Value("${spring.cloud.gateway.host}")
	private String host;
	
	@Value("${spring.cloud.gateway.port}")
	private Integer port;

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

}
