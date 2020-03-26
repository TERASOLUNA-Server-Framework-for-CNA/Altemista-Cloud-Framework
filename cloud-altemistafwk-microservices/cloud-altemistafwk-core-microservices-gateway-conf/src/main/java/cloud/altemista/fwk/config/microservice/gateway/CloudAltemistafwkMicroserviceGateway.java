// tag::annotation[]
package cloud.altemista.fwk.config.microservice.gateway;
// end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Gateway CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

// tag::annotation[]
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cloud.altemista.fwk.config.microservice.gateway.filters.CloudAltemistafwkMicroserviceGatewayAuthenticationFilter;

@Configuration
@EnableZuulProxy // <1>
public class CloudAltemistafwkMicroserviceGateway {

	@Bean
	@ConditionalOnClass({OAuth2ClientAutoConfiguration.class})
	public CloudAltemistafwkMicroserviceGatewayAuthenticationFilter cloudAltemistafwkMicroserviceGatewayAuthenticationFilter(){
		return new CloudAltemistafwkMicroserviceGatewayAuthenticationFilter();
	}
}
// end::annotation[]
