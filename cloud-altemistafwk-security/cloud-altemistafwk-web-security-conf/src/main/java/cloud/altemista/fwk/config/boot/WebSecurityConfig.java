/**
 * 
 */
package cloud.altemista.fwk.config.boot;

/*
 * #%L
 * altemista-cloud security: Web/HTTP authorization CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.terasoluna.gfw.security.web.logging.UserIdMDCPutFilter;

/**
 * Spring Boot support for the web application security module
 * @author NTT DATA
 * @see META-INF/web-fragment.xml
 */
@Configuration
public class WebSecurityConfig {
	
	/**
	 * Set authentication user name of Spring Security in MDC
	 * @param userIdMDCPutFilter UserIdMDCPutFilter
	 * @return FilterRegistrationBean
	 */
	@Autowired
	@Bean
	public FilterRegistrationBean userIdMDCPutFilterRegistration(UserIdMDCPutFilter userIdMDCPutFilter) {
		
		return new FilterRegistrationBean(userIdMDCPutFilter); 
	}
}

