// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.registry;
// end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Registry CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

// tag::annotation[]
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEurekaServer // <1>
public class AltemistaCloudfwkMicroserviceEureka {

}
// end::annotation[]
