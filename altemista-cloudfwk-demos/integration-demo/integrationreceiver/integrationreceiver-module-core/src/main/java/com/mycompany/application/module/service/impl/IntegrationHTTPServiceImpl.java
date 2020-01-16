package com.mycompany.application.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.http.config.RequestGateway;
import com.mycompany.application.module.service.IntegrationHTTPService;

@Service
public class IntegrationHTTPServiceImpl implements IntegrationHTTPService {

	@Autowired 
	private RequestGateway requestGateway;
	
	@Override
	public String send(String message) {
		return requestGateway.echo(message);
	}

}
