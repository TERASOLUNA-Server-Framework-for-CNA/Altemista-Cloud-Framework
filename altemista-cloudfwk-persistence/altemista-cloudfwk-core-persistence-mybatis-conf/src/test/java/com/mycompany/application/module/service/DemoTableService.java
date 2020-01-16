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


import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.model.DemoTable;
import com.mycompany.application.module.model.DemoTableExample;
import com.mycompany.application.module.repository.DemoTableMapper;

@Service
public class DemoTableService {

	@Autowired
	private DemoTableMapper repository; //<1>
	
	public DemoTable findPersonNamed(String name) {
		
		//tag::example[]
		DemoTableExample predicate = new DemoTableExample();
		// end::example[]
		/*
//tag::example[]
		predicate.createCriteria().andNameEqualTo(name);
// end::example[]
		*/
//tag::example[]
		
		return CollectionUtils.extractSingleton(
				this.repository.selectByExample(predicate));
		// end::example[]
	}
}
// end::example[]
