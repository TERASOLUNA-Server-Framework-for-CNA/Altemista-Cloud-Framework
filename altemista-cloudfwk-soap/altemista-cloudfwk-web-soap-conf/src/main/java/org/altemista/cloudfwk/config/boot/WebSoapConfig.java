/**
 * 
 */
package org.altemista.cloudfwk.config.boot;

/*
 * #%L
 * altemista-cloud SOAP server CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sets up Apache CXF in Spring Boot 
 * @author NTT DATA
 */
@Configuration
public class WebSoapConfig {
	
	/**
	 * The CXFServlet will process all SOAP requests at /soap/*
	 * @return ServletRegistrationBean
	 */
	@Bean
	public ServletRegistrationBean cxfServletRegistration() {
		
		return new ServletRegistrationBean(new CXFServlet(), "/soap/*");
	}

}
