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
public interface CoreDummyService {
	
	/**
	 * Dummy method that always throws a DummyApplicationException
	 */
	void alwaysThrowsApplicationExceptionWithMessage();

}
