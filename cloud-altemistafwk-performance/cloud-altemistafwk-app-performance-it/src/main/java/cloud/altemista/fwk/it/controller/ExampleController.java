/**
 * 
 */
package cloud.altemista.fwk.it.controller;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.it.model.DemoTable;
import cloud.altemista.fwk.it.service.ExampleService;

/**
 * A controller with methods that illustrates the performance module
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = ExampleController.MAPPING, method = RequestMethod.GET)
public class ExampleController {
	
	/** MAPPING String */
	public static final String MAPPING = "/example";
	
	@Autowired
	private ExampleService service;
	
	/**
	 * A simple method (no service involved)
	 * @return String
	 */
	@RequestMapping("/1")
	public String simpleMethod() {
		
		return "Hello, world!";
	}
	
	/**
	 * A simple service method
	 * @return String
	 */
	@RequestMapping("/2")
	public String simpleServiceMethod() {
		
		return this.service.simpleMethod();
	}
	
	/**
	 * A simple service method with arguments
	 * @return String
	 */
	@RequestMapping("/3")
	public String simpleServiceMethodWithArguments() {
		
		return this.service.simpleMethodWithArguments(
				RandomStringUtils.randomAscii(8), RandomUtils.nextInt(1, 8));
	}
	
	/**
	 * A hierarchical service method (calls another service) with arguments
	 * @return String
	 */
	@RequestMapping("/4")
	public String hierarchicalMethodWithArguments() {
		
		return this.service.hierarchicalMethodWithArguments(
				RandomStringUtils.randomAscii(8), RandomUtils.nextInt(1, 4));
	}
	
	/**
	 * A service method that inserts something in the database
	 * @return DemoTable
	 */
	@RequestMapping("/5")
	@ResponseBody
	public DemoTable methodWithDatabaseInsert() {
		
		return this.service.methodWithDatabaseInsert();
	}
	
	/**
	 * A service method that queries something from the database
	 * @return DemoTable
	 */
	@RequestMapping("/6")
	@ResponseBody
	public List<DemoTable> methodWithDatabaseSelect() {
		
		return this.service.methodWithDatabaseSelect();
	}
	
	/**
	 * A service method that queries something from the database with pagination
	 * @return DemoTable
	 */
	@RequestMapping("/7")
	@ResponseBody
	public Page<DemoTable> methodWithDatabaseSelectPageable() {
		
		return this.service.methodWithDatabaseSelect(new PageRequest(0, 10));
	}
}
