package cloud.altemista.fwk.it.controller;

import org.springframework.stereotype.Controller;

/*
 * #%L
 * altemista-cloud web application: Spring Web Flow + Apache Tiles integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple REST controller to check the application has been successfully deployed
 * @author NTT DATA
 */
@Controller
@RequestMapping(value = WebFlowAndTilesAliveController.MAPPING, method = RequestMethod.GET)
public class WebFlowAndTilesAliveController {
	
	/** MAPPING String */
	public static final String MAPPING = "/alive";
	
	/**
	 * Simple REST service to check the application has been successfully deployed 
	 * @return Object
	 */
	@RequestMapping
	public String alive() {
		
		return "blank";
	}

}
