package cloud.altemista.fwk.test.soap;

/*
 * #%L
 * altemista-cloud SOAP client CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Example service interface
 * @author NTT DATA
 */
@WebService
public interface ExampleService {
	
	@WebMethod
	void exampleMethod();

}
