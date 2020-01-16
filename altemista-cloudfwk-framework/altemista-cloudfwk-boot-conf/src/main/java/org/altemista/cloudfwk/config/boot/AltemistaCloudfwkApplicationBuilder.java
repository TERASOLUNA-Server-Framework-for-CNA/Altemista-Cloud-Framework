package org.altemista.cloudfwk.config.boot;

/*-
 * #%L
 * altemista-cloud Spring Boot application
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;

/**
 * Spring Boot-based ACF application builder.<br>
 * Typical usage:<pre>public static void main(String[] args) {
  new AltemistaCloudfwkApplicationBuilder().run(args);
}</pre>
 * @author NTT DATA
 * @see AltemistaCloudfwkApplication
 */
public class AltemistaCloudfwkApplicationBuilder extends SpringApplicationBuilder {

	Logger logger = LoggerFactory.getLogger(AltemistaCloudfwkApplicationBuilder.class);

	/**
	 * Default constructor
	 */
	public AltemistaCloudfwkApplicationBuilder() {

		// Spring Boot-based ACF application
		super(AltemistaCloudfwkApplication.class);

		// Initializers, equivalent to the following section of altemita-cloudfwk-webapp-conf Servlet 3.0 web-fragment:
		// <context-param>
		//   <param-name>contextInitializerClasses</param-name>
		//   <param-value>org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer</param-value>
		// </context-param>
		this.initializers(new AltemistaCloudfwkApplicationContextInitializer());
	}

}
