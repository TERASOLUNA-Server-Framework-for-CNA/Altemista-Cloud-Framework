// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.hystrix;

/*-
 * #%L
 * altemista-cloud Microservices Hystrix CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix // <1>
public class AltemistaCloudfwkMicroserviceHystrix {

}
// end::annotation[]
