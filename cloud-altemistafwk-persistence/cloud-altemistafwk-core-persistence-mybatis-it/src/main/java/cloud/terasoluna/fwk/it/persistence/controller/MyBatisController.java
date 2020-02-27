/**
 * 
 */
package cloud.altemista.fwk.it.persistence.controller;

/*
 * #%L
 * altemista-cloud persistence: MyBatis integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.repository.DemoTableMapper;

/**
 * 
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = MyBatisController.MAPPING, method = RequestMethod.GET)
public class MyBatisController {
	
	/** MAPPING String */
	public static final String MAPPING = "/mybatis";
	
	@Autowired
	private DemoTableMapper repository;
	
	@RequestMapping("/1")
	public Object testSelectAll() {
		
		return repository.selectByExample(null);
	}

}
