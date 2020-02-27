package cloud.altemista.fwk.example.rest;

/*
 * #%L
 * altemista-cloud common: REST consumer utilitites
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import cloud.altemista.fwk.core.rest.security.BasicAuthRestTemplate;

public class BasicAuthRestTemplateExample {

	private static final String url = "app/service";

	public void get1() {

		// tag::example[]
		RestTemplate restTemplate = new BasicAuthRestTemplate("user", "password");
		restTemplate.getForObject(url, String.class);
		// end::example[]
	}

	// tag::example2[]
	@Autowired
	private RestTemplate restTemplate;
	
	public String get() {
		return this.restTemplate.getForObject(url, String.class);
	}
	// end::example2[]

}
