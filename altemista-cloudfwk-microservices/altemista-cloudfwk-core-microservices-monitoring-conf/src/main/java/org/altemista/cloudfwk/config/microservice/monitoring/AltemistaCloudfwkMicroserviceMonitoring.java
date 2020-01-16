// tag::annotation[]
package org.altemista.cloudfwk.config.microservice.monitoring;
// end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Monitoring CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

//tag::annotation[]
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Configuration;

import com.github.vanroy.cloud.dashboard.config.EnableCloudDashboard;

@Configuration
@EnableHystrixDashboard // <1>
//@EnableCloudDashboard // <2>
@EnableTurbine // <3>
public class AltemistaCloudfwkMicroserviceMonitoring {

}
// end::annotation[]
