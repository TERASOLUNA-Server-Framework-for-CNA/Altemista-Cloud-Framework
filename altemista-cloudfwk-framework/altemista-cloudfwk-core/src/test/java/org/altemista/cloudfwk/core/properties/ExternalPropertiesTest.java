package org.altemista.cloudfwk.core.properties;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;
import org.altemista.cloudfwk.core.util.ApplicationContextUtil;
import org.altemista.cloudfwk.core.util.EnvironmentUtil;

/**
 * Tests for PropertiesLocationsFactoryBean.
 * @author NTT DATA
 */
public class ExternalPropertiesTest {
	
	/** The Spring context configuration location */
	private static final String CONTEXT_CONFIGURATION_PATHS =
			"classpath:org/altemista/cloudfwk/config/core/altemista-cloudfwk-core.xml";

	/** Name of the system property that optionally contains the external path */
	private static final String EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME = "external.configuration.path";

	/** The "classpath:" version of the additional base path */
	private static final String CLASSPATH_BASE_PATH = "classpath:externalConfig/classpath";

	/** The "file:" version of the additional base path */
	private static final String FILE_SYSTEM_BASE_PATH =
			StringUtils.appendIfMissing(
					ExternalPropertiesTest.class.getResource("/").toExternalForm(),
					SystemUtils.FILE_SEPARATOR) + "externalConfig/filesystem";

	//
	
	@Before
	@After
	public void reset() {
		
		System.clearProperty(EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME);
		System.clearProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY);
	}
	
	@Test	
	public void testDefaultConfiguration() {

		this.initializeSpringContextBeans(null, null);
		
		// (no property overriden)
		Assert.assertNull(ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertNotEquals("property1_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertNotEquals("property2_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertNotEquals("property4_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertNotEquals("property5_dev_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertNotEquals("property5_pro_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
	}

	@Test	
	public void testAltFromClasspath() {

		this.initializeSpringContextBeans(CLASSPATH_BASE_PATH, null);
		
		Assert.assertEquals("property0_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertEquals("property1_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertEquals("property2_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertEquals("property4_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertNotEquals("property5_dev_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertNotEquals("property5_pro_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
	}

	@Test	
	public void testAltFromClasspathDevEnvironment() {

		this.initializeSpringContextBeans(CLASSPATH_BASE_PATH, "dev");
		
		Assert.assertEquals("property0_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertEquals("property1_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertEquals("property2_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertEquals("property4_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertEquals("property5_dev_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertNotEquals("property5_pro_classpath", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
	}

	@Test	
	public void testAltFromFileSystem() {

		this.initializeSpringContextBeans(FILE_SYSTEM_BASE_PATH, null);
		
		Assert.assertEquals("property0_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertEquals("property1_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertEquals("property2_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertEquals("property4_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertNotEquals("property5_dev_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertNotEquals("property5_pro_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
	}

	@Test	
	public void testAltFromFileSystemProEnvironment() {

		this.initializeSpringContextBeans(FILE_SYSTEM_BASE_PATH, "pro");
		
		Assert.assertEquals("property0_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertEquals("property1_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertEquals("property2_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertEquals("property4_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertNotEquals("property5_dev_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertEquals("property5_pro_filesystem", ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
	}
	
	/**
	 * Initializes the Spring context and loads the beans required by the test
	 * simulating the values of the system properties
	 * @param externalBasePath String value for the system property that optionally contains the external base path
	 * @param environment String value for the system property that holds the host name
	 */
	private void initializeSpringContextBeans(String externalBasePath, String environment) {
		
		// Simulates the values of the system properties
		if (StringUtils.isNotBlank(externalBasePath)) {
			System.setProperty(EXTERNAL_LOCATION_SYSTEM_PROPERTY_NAME, externalBasePath);
		}
		if (StringUtils.isNotBlank(environment)) {
			System.setProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY, environment);
		}
		
		// Initializes the Spring context
		ConfigurableApplicationContext beanFactory = new ClassPathXmlApplicationContext(CONTEXT_CONFIGURATION_PATHS);
		new AltemistaCloudfwkApplicationContextInitializer().initialize(beanFactory);
	}
}
