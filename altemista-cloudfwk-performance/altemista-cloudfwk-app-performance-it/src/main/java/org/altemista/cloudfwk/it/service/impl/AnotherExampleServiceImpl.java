package org.altemista.cloudfwk.it.service.impl;

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


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.service.AnotherExampleService;
import org.altemista.cloudfwk.it.util.PerformanceUtil;

/**
 * The implementatino of the other service to illustrate the performance module
 * @author NTT DATA
 */
@Service
public class AnotherExampleServiceImpl implements AnotherExampleService {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.it.service.AnotherExampleService#simpleMethodWithArgument(java.lang.String)
	 */
	@Override
	public String simpleMethodWithArgument(String argument) {
		
		PerformanceUtil.randomSleep(10, 110);
		return StringUtils.lowerCase(argument) + " " + StringUtils.upperCase(argument);
	}

}
