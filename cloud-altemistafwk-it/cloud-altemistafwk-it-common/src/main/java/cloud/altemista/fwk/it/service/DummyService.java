package cloud.altemista.fwk.it.service;

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


/**
 * Dummy service definition for use in integration tests
 * @author NTT DATA
 */
public interface DummyService {

	/**
	 * Dummy method that does not return values
	 */
	void voidMethod();
	
	/**
	 * Dummy method that always returns null
	 * @return null
	 */
	Object alwaysReturnNull();

	/**
	 * Dummy method that always returns an object
	 * @return Object, never null
	 */
	Object alwaysReturnObject();
	
	/**
	 * Dummy method that always throws a DummyApplicationException
	 */
	void alwaysThrowsApplicationException();

}
