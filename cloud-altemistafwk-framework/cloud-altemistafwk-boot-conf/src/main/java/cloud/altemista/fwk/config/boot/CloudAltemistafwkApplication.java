/**
 * 
 */
package cloud.altemista.fwk.config.boot;

/*
 * #%L
 * altemista-cloud Spring Boot application
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.web.exception.ExceptionLoggingFilter;

/**
 * Spring Boot application that uses the automatic configuration of Altemista Cloud Framework.
 * Aims to mimic the configuration provided by {@code altemista-cloud-webapp-conf} where possible:<ul>
 * <li>Imports the core configuration of the framework
 * ({@code "cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml"})</li>
 * <li>Sets up the servlet filter class for log output of exception ({@link ExceptionLoggingFilter})</li>
 * <li>If running on an embedded Servlet container, enables scanning for Servlet components
 * ({@code @ServletComponentScan("cloud.altemista.fwk.config.webapp")})</li>
 * <li>Imports the beans and the configuration of the included Spring Boot modules
 * (implicit {@code @ComponentScan})</li>
 * <li>Imports the application configuration files
 * ({@code "spring/*.xml"})</li>
 * </ul>
 * @author NTT DATA
 * @see CloudAltemistafwkApplicationBuilder
 */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		OAuth2ClientAutoConfiguration.class
	})
@ServletComponentScan("cloud.altemista.fwk.config.webapp")
@ImportResource(locations = {
		"classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml",
		"classpath*:cloud/altemista/fwk/config/app/cloud-altemistafwk-app-*.xml",
		"classpath*:spring/*.xml"
})
public class CloudAltemistafwkApplication {
	
	/**
	 * Servlet filter class for log output of exception
	 * @param exceptionLogger ExceptionLogger
	 * @return ExceptionLoggingFilter
	 */
	@Bean
	public ExceptionLoggingFilter exceptionLoggingFilter(ExceptionLogger exceptionLogger) {
		
		ExceptionLoggingFilter exceptionLoggingFilter = new ExceptionLoggingFilter();
		exceptionLoggingFilter.setExceptionLogger(exceptionLogger);
		return exceptionLoggingFilter;
	}
}
