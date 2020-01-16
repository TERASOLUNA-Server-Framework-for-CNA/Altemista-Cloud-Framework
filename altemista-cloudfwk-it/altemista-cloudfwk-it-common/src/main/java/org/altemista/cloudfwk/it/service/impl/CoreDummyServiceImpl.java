package org.altemista.cloudfwk.it.service.impl;

import java.util.Arrays;

/*
 * #%L
 * altemista-cloud common: integration tests common utilities
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.core.exception.CoreException;
import org.altemista.cloudfwk.it.service.CoreDummyService;

/**
 * Simple implementation of the dummy service for use in integration tests
 * @author NTT DATA
 */
@Service
public class CoreDummyServiceImpl implements CoreDummyService {
	

	@Override
	public void alwaysThrowsApplicationExceptionWithMessage() {
		throw new CoreException("dummy", Arrays.asList(new Integer(100)).toArray());
	}


}
