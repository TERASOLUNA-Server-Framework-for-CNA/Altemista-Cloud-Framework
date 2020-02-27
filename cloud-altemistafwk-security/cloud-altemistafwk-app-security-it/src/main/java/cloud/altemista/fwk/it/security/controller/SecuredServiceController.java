package cloud.altemista.fwk.it.security.controller;

import org.springframework.beans.factory.annotation.Autowired;

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
import cloud.altemista.fwk.it.security.service.SecuredService;

/**
 * Simple REST controller to check the application has been successfully deployed
 * @author NTT DATA
 */
@RestController
@RequestMapping(path = SecuredServiceController.MAPPING, method = RequestMethod.GET)
public class SecuredServiceController {

	/** MAPPING String */
	public static final String MAPPING = "/service";
	
	@Autowired
	private SecuredService service;
	
	@RequestMapping("/notSecured")
	public String notSecured() {
		return this.service.notSecured();
	}
	
	@RequestMapping("/springSecuredToAnonymous")
	public String springSecuredToAnonymous() {
		return this.service.springSecuredToAnonymous();
	}
	
	@RequestMapping("/springSecuredToRoleAdmin")
	public String springSecuredToRoleAdmin() {
		return this.service.springSecuredToRoleAdmin();
	}
	
	@RequestMapping("/jsr250PermitAll")
	public String jsr250PermitAll() {
		return this.service.jsr250PermitAll();
	}
	
	@RequestMapping("/jsr250RolesAllowedAdmin")
	public String jsr250RolesAllowedAdmin() {
		return this.service.jsr250RolesAllowedAdmin();
	}
	
	@RequestMapping("/jsr250DenyAll")
	public String jsr250DenyAll() {
		return this.service.jsr250DenyAll();
	}
	
	@RequestMapping("/springExpressionSecuredToAuthenticated")
	public String springExpressionSecuredToAuthenticated() {
		return this.service.springExpressionSecuredToAuthenticated();
	}
	
	@RequestMapping("/springExpressionSecuredToRoleAdmin")
	public String springExpressionSecuredToRoleAdmin() {
		return this.service.springExpressionSecuredToRoleAdmin();
	}
}
