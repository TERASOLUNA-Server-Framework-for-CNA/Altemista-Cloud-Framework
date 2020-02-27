/**
 * 
 */
package com.mycompany.application.module.service;

/*
 * #%L
 * altemista-cloud persistence: MyBatis CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.application.module.repository.DemoMapper;

//tag::example[]
@Service
@Transactional //<1>
public class DemoService {

	@Autowired
	private DemoMapper repository; //<2>
	
	public String helloWorld() {
		return this.repository.selectSalutation("World");
	}
}
// end::example[]
