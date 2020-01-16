package org.altemista.cloudfwk.it.service;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.altemista.cloudfwk.it.model.DemoTable;

/**
 * A service to illustrate the performance module
 * @author NTT DATA
 */
public interface ExampleService {

	/**
	 * A simple service method
	 * @return String
	 */
	String simpleMethod();

	/**
	 * A simple service method with arguments
	 * @param argument String
	 * @param anotherArgument int
	 * @return String
	 */
	String simpleMethodWithArguments(String argument, int anotherArgument);

	/**
	 * A hierarchical service method (calls another service) with arguments
	 * @param argument String
	 * @param anotherArgument int
	 * @return String
	 */
	String hierarchicalMethodWithArguments(String argument, int anotherArgument);
	
	/**
	 * A service method that inserts something in the database
	 * @return DemoTable
	 */
	DemoTable methodWithDatabaseInsert();
	
	/**
	 * A service method that queries something from the database
	 * @return v
	 */
	List<DemoTable> methodWithDatabaseSelect();
	
	/**
	 * A service method that queries something from the database with pagination
	 * @param pageable Pageable
	 * @return Page
	 */
	Page<DemoTable> methodWithDatabaseSelect(Pageable pageable);
}
