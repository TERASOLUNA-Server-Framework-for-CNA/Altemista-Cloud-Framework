// tag::annotation[]
package cloud.altemista.fwk.config.microservice.service;

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
public class CloudAltemistafwkMicroserviceService {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
// end::annotation[]
