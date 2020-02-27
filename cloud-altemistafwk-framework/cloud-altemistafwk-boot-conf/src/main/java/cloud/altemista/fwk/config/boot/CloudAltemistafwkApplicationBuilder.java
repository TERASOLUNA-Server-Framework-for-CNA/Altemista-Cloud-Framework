package cloud.altemista.fwk.config.boot;

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
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;

/**
 * Spring Boot-based ACF application builder.<br>
 * Typical usage:<pre>public static void main(String[] args) {
  new CloudAltemistafwkApplicationBuilder().run(args);
}</pre>
 * @author NTT DATA
 * @see CloudAltemistafwkApplication
 */
public class CloudAltemistafwkApplicationBuilder extends SpringApplicationBuilder {

	Logger logger = LoggerFactory.getLogger(CloudAltemistafwkApplicationBuilder.class);

	/**
	 * Default constructor
	 */
	public CloudAltemistafwkApplicationBuilder() {

		// Spring Boot-based ACF application
		super(CloudAltemistafwkApplication.class);

		// Initializers, equivalent to the following section of altemita-cloudfwk-webapp-conf Servlet 3.0 web-fragment:
		// <context-param>
		//   <param-name>contextInitializerClasses</param-name>
		//   <param-value>cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer</param-value>
		// </context-param>
		this.initializers(new CloudAltemistafwkApplicationContextInitializer());
	}

}
