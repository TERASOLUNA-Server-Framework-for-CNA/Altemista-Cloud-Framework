/**
 * Spring MVC controllers of the module business module, served at "/app/*".
 * I.e.: {@code @Conrtoller} annotated classes
 */
package com.mycompany.application.module.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public String listAllUsers() {
		return "OK";
	}
}