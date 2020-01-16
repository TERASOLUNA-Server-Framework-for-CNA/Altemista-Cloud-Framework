package org.altemista.cloudfwk.it.security.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth 2.0 resources (the "back-end")
 * @author NTT DATA
 */
@RestController
@RequestMapping(path = OAuth2ApiController.MAPPING)
public class OAuth2ApiController {

	/** MAPPING String */
	public static final String MAPPING = "/api";

	/** The "hello user" message from the back-end */
	public static final String HELLO_MESSAGE = "Hello, %s! I'm the back-end (resource server)";
	
	/**
	 * Simple REST service that represents a resource 
	 * @return String
	 */
	@RequestMapping(path = "/hello", method = RequestMethod.GET)
	public String hello() {
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String name = authentication.getName();
		
		return String.format(HELLO_MESSAGE, name);
	}
}
