package cloud.altemista.fwk.test;

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
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;

/**
 * Convenience base class for unit tests that needs a ACF Spring context.
 * By default, this loads the core ACF Spring application context
 * without aggregator-specific bean (such as <i>webapp</i> or <i>boot</i> modules),
 * and without the application specific configuration files (i.e.: "/spring/*.xml").
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = CloudAltemistafwkApplicationContextInitializer.class,
		locations = "classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml")
public abstract class AbstractSpringContextTest {
	
	//
}
