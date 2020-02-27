package cloud.altemista.fwk.it.soap;

/*
 * #%L
 * altemista-cloud SOAP client/server integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.stereotype.Component;

/**
 * Minimal example bottom-up (contract-last) service implementation
 * @author NTT DATA
 */
@Component
public class ExampleServiceImpl implements ExampleService {

	@Override
	public void doNothing() {
		// (does nothing)
	}
}
