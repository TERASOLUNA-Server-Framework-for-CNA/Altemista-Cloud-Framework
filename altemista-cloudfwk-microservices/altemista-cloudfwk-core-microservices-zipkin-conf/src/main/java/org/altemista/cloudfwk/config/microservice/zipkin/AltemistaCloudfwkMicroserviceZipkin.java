// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.zipkin;
// end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Zipkin CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

// tag::annotation[]
import org.springframework.context.annotation.Configuration;

import zipkin.server.EnableZipkinServer;

@Configuration
@EnableZipkinServer
public class AltemistaCloudfwkMicroserviceZipkin {

}
// end::annotation[]
