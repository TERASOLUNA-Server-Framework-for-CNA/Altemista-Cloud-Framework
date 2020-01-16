package org.altemista.cloudfwk.config.microservice;

/*-
 * #%L
 * altemista-cloud Microservice application
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class AltemistaCloudfwkMicroserviceBuilder extends SpringBootServletInitializer {

    protected static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder, Class<?> clazz) {
        return builder.sources(clazz, AltemistaCloudfwkMicroserviceBuilder.class);
    }
	
}
