package org.altemista.cloudfwk.test;

/*
 * #%L
 * Test dependency for altemista-cloud framework and application unit tests
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;

/**
 * Convenience base class for unit tests that needs a ACF Spring context.
 * By default, this loads the core ACF Spring application context
 * without aggregator-specific bean (such as <i>webapp</i> or <i>boot</i> modules),
 * and without the application specific configuration files (i.e.: "/spring/*.xml").
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = AltemistaCloudfwkApplicationContextInitializer.class,
		locations = "classpath:org/altemista/cloudfwk/config/core/altemista-cloudfwk-core.xml")
public abstract class AbstractSpringContextTest {
	
	//
}
