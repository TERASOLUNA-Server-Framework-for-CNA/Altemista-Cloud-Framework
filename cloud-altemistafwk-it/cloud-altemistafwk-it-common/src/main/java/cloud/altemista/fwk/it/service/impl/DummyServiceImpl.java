package cloud.altemista.fwk.it.service.impl;

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
import cloud.altemista.fwk.it.exception.DummyApplicationException;
import cloud.altemista.fwk.it.service.DummyService;

/**
 * Simple implementation of the dummy service for use in integration tests
 * @author NTT DATA
 */
@Service
public class DummyServiceImpl implements DummyService {
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.DummyService#voidMethod()
	 */
	@Override
	public void voidMethod() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.DummyService#alwaysReturnNull()
	 */
	@Override
	public Object alwaysReturnNull() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.DummyService#alwaysReturnObject()
	 */
	@Override
	public Object alwaysReturnObject() {
		
		return new Object();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.it.service.DummyService#alwaysThrowsApplicationException()
	 */
	@Override
	public void alwaysThrowsApplicationException() {
		
		throw new DummyApplicationException();
	}
}
