package org.altemista.cloudfwk.it.security.oauth2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple REST controller to check the application has been successfully deployed
 * @author NTT DATA
 */
@RestController
@RequestMapping(method = RequestMethod.GET)
public class OAuth2AliveController {

	/** MAPPING String */
	public static final String MAPPING = "/oauth2alive";

	/** The ALIVE_MESSAGE String */
	public static final String ALIVE_MESSAGE = "I'm alive!";
	
	/**
	 * Simple REST service to check the application has been successfully deployed 
	 * @return Object
	 */
	@RequestMapping(MAPPING)
	public Object alive() {

		return ALIVE_MESSAGE;
	}
}
