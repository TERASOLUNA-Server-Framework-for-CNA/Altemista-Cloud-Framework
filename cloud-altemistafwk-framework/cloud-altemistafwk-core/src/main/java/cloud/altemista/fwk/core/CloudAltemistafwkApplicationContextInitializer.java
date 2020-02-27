package cloud.altemista.fwk.core;

/*-
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import cloud.altemista.fwk.core.properties.EnvironmentAwarePropertiesRegistrar;

/**
 * Initialize the TSS+ application context.
 * @author NTT DATA
 */
public class CloudAltemistafwkApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		
		// Registers the environment-aware property sources
		EnvironmentAwarePropertiesRegistrar.registerEnvironmentAwarePropertySources(applicationContext);
	}
}
