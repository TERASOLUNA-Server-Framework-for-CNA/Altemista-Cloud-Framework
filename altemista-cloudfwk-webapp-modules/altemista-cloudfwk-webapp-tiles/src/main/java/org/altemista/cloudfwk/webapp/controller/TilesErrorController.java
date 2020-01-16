package org.altemista.cloudfwk.webapp.controller;

import org.springframework.stereotype.Controller;

/*
 * #%L
 * altemista-cloud presentation layer: Apache Tiles support
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

/**
 * Controller to redirect error URIs to the actual error views 
 * @author NTT DATA
 */
@Controller
@RequestMapping(value = "/error", method = RequestMethod.GET)
public class TilesErrorController {
	
	@RequestMapping("resourceNotFoundError")
	public String resourceNotFoundError() {
		
		// (forces a ResourceNotFoundException to be catch by SimpleMappingExceptionResolver)
		throw new ResourceNotFoundException("");
	}

	@RequestMapping("businessError")
	public String businessError() {
		
		return "error/businessError"; // NOSONAR
	}
	
	@RequestMapping("transactionTokenError")
	public String transactionTokenError() {
		
		return "error/transactionTokenError"; // NOSONAR
	}
	
	@RequestMapping("dataAccessError")
	public String dataAccessError() {
		
		return "error/dataAccessError"; // NOSONAR
	}

	@RequestMapping("systemError")
	public String systemError() {
		
		return "error/systemError"; // NOSONAR
	}
}
