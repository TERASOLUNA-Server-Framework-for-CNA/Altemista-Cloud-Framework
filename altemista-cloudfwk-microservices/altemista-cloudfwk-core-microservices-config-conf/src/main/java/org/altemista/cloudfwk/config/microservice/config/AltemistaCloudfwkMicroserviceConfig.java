// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.config;
//end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Config Server CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

//tag::annotation[]
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigServer // <1>
public class AltemistaCloudfwkMicroserviceConfig {

}
// end::annotation[]
