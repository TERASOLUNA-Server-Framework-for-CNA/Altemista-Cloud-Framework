package org.altemista.cloudfwk.it.security.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

/**
 * OAuth 2.0 client (the "front-end")
 * @author NTT DATA
 */
@RestController
@RequestMapping(path = OAuth2ClientController.MAPPING)
public class OAuth2ClientController {

	/** MAPPING String */
	public static final String MAPPING = "/client";

	/** The "hello user" message from the front-end */
	public static final String HELLO_MESSAGE = "Hello! I'm the client";
	
	@Autowired
	@Qualifier("authorizationCodeRestTemplate") 
	private RestOperations restTemplate;
	
	@Value("${integration-test.url:http://localhost:8080}/api/hello")
	private String backEndUrl;
	
	/**
	 * Simple REST service that does not invoke any OAuth 2.0 resource
	 * @return String
	 */
	@RequestMapping(path = "/hello", method = RequestMethod.GET)
	public String helloDirect() {
		
		return HELLO_MESSAGE;
	}
	
	/**
	 * Simple REST service that invokes an OAuth 2.0 resource
	 * @return String
	 */
	@RequestMapping(path = "/hello/api", method = RequestMethod.GET)
	public String helloApi() {
		
		final String backEndResponse = this.restTemplate.getForObject(this.backEndUrl, String.class);
		return backEndResponse;
	}
}
