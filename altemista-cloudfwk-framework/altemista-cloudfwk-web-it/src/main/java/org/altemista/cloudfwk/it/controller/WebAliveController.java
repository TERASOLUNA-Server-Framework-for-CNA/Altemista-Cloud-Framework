package org.altemista.cloudfwk.it.controller;

/*
 * #%L
 * altemista-cloud web integration tests
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
 * Simple REST controller to check the application has been successfully
 * deployed
 * 
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = WebAliveController.MAPPING, method = RequestMethod.GET)
public class WebAliveController {

	/** MAPPING String */
	public static final String MAPPING = "/webalive";

	/**
	 * Simple REST service to check the application has been successfully deployed
	 * @return Object
	 */
	@RequestMapping
	public Object alive() {

		return "I'm alive!";
	}
}
