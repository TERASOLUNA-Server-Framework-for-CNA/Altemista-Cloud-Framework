// tag::annotation[]
package org.altemista.cloudfwk.config.microservice;
// end::annotation[]

/*-
 * #%L
 * altemita-cloud Microservice application
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

// tag::annotation[]
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackageClasses = AltemistaCloudfwkMicroservice.class) // <1>
@EnableEurekaClient // <2>
public @interface AltemistaCloudfwkMicroservice {

}
// end::annotation[]
