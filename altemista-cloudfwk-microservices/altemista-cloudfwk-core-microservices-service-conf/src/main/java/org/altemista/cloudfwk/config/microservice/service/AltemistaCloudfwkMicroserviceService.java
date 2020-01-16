// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

/*-
 * #%L
 * altemista-cloud Microservices Service CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.context.annotation.Configuration;

@Configuration
public class AltemistaCloudfwkMicroserviceService {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
// end::annotation[]
