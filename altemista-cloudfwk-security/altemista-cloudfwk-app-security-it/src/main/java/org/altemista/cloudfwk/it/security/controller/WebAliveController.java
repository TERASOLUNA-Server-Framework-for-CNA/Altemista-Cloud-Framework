package org.altemista.cloudfwk.it.security.controller;

/*
 * #%L
 * altemista-cloud web and Spring Boot-based applications security integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple REST controller to check the application has been successfully deployed
 * @author NTT DATA
 */
@RestController
@RequestMapping(method = RequestMethod.GET)
public class WebAliveController {

	/** MAPPING String */
	public static final String MAPPING = "/webalive";

	/** MAPPING String */
	public static final String MAPPING_SECURE = "/secure/webalive";

	/** The ALIVE_MESSAGE String */
	public static final String ALIVE_MESSAGE = "I'm alive and not secured!";
	
	/** The SECURE_ALIVE_MESSAGE String */
	public static final String SECURE_ALIVE_MESSAGE = "I'm alive and secured!";
	
	/**
	 * Simple REST service to check the application has been successfully deployed 
	 * @return Object
	 */
	@RequestMapping(MAPPING)
	public Object alive() {
		
		return ALIVE_MESSAGE;
	}
	
	/**
	 * Simple REST service to check the application has been successfully deployed 
	 * @return Object
	 */
	@RequestMapping(MAPPING_SECURE)
	public Object aliveSecured() {
		
		return SECURE_ALIVE_MESSAGE;
	}
}
