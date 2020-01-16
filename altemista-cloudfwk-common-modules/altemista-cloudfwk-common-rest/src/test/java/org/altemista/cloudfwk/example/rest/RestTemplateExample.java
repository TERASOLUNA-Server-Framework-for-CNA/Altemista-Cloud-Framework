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


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateExample {

	private String url = "app/service";

	public String get() {
		
		RestTemplate restTemplate = new RestTemplate();
		// tag::example[]
		// GET
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		// end::example[]
		return response.getBody();
	}

	public String post() {
		
		RestTemplate restTemplate = new RestTemplate();
		// tag::example[]
		
		// POST
		User request = new User();
		request.setName("Johnathan M Smith");
		request.setUser("JS01");
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		// end::example[]
		return response.getBody();
	}

	class User {
		private String name;
		private String user;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}
	}

}
