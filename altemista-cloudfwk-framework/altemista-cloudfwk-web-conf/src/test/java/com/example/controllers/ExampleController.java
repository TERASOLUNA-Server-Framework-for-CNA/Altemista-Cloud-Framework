package com.example.controllers;

/*-
 * #%L
 * altemista-cloud web CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//tag::example[]
@RestController
@RequestMapping(path = "example")
public class ExampleController {
	
	@RequestMapping
	public String exampleMethod() {
		return "Hello, World!";
	}
}
//end::example[]
