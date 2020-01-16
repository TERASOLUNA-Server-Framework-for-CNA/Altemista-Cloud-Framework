package org.altemista.cloudfwk.example.rest;

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


import org.springframework.web.client.RestTemplate;
import org.altemista.cloudfwk.core.rest.security.DigestAuthRestTemplate;

public class DigestAuthRestTemplateExample {

	private static final String url = "app/service";

	public void get1() {

		// tag::example[]
		RestTemplate restTemplate = new DigestAuthRestTemplate("user", "password");
		restTemplate.getForObject(url, String.class);
		// end::example[]
	}

	// tag::example2[]
	private RestTemplate restTemplate =
			new DigestAuthRestTemplate("user", "password");
	
	public String get() {

		return this.restTemplate.getForObject(url, String.class);
	}
	// end::example2[]

}
