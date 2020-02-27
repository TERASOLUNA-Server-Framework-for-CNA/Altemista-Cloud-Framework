package cloud.altemista.fwk.core.properties;

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


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;
import cloud.altemista.fwk.core.util.ApplicationContextUtil;

/**
 * Tests that property values can be configured in multi-environment properties files.
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = CloudAltemistafwkApplicationContextInitializer.class,
		locations = "classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml")
public class EnvironmentAwarePropertiesTest {
	
	@SuppressWarnings("unused")
	public void exampleMethod() {
		//tag::example[]
		
		String remoteUrl = ApplicationContextUtil.getEnvironment().getProperty("remote.system.url");
		//end::example[]
	}

	@Test(expected = NullPointerException.class)
	public void testSanityChecksKeyNull() {
		
		ApplicationContextUtil.getEnvironment().getProperty(null);
	}
	
	@Test
	public void testSanityChecksKeyNotNull() {
		
		ApplicationContextUtil.getEnvironment().getProperty("");
	}
	
	@Test
	public void testDefaultValues() {
		
		Assert.assertNull(ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertNull(ApplicationContextUtil.getEnvironment().getProperty("props.property0", (String) null));
		Assert.assertEquals("", ApplicationContextUtil.getEnvironment().getProperty("props.property0", ""));
		Assert.assertEquals("value", ApplicationContextUtil.getEnvironment().getProperty("props.property0", "value"));
		
		Assert.assertNotNull(ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertNotNull(ApplicationContextUtil.getEnvironment().getProperty("props.property1", (String) null));
		Assert.assertNotEquals("", ApplicationContextUtil.getEnvironment().getProperty("props.property1", ""));
		Assert.assertNotEquals("value", ApplicationContextUtil.getEnvironment().getProperty("props.property1", "value"));
	}
	
	@Test
	public void testPriorities() {
		
		/*
		 * No.	env.	no-prefix	global	Which should prevail?
		 * 0	no		no			no		not exists
		 * 1	yes		no			no		local
		 * 2	no		yes			no		no-prefix
		 * 3	yes		yes			no		local
		 * 4	no		no			yes		global
		 * 5	yes		no			yes		local
		 * 6	no		yes			yes		no-prefix
		 * 7	yes		yes			yes		local
		 */
		// Specific environment properties (maximum priority)
		Assert.assertNull(						ApplicationContextUtil.getEnvironment().getProperty("props.property0"));
		Assert.assertEquals("property1_local",	ApplicationContextUtil.getEnvironment().getProperty("props.property1"));
		Assert.assertEquals("property2",		ApplicationContextUtil.getEnvironment().getProperty("props.property2"));
		Assert.assertEquals("property3_local",	ApplicationContextUtil.getEnvironment().getProperty("props.property3"));
		Assert.assertEquals("property4_global",	ApplicationContextUtil.getEnvironment().getProperty("props.property4"));
		Assert.assertEquals("property5_local",	ApplicationContextUtil.getEnvironment().getProperty("props.property5"));
		Assert.assertEquals("property6",		ApplicationContextUtil.getEnvironment().getProperty("props.property6"));
		Assert.assertEquals("property7_local",	ApplicationContextUtil.getEnvironment().getProperty("props.property7"));
	}
}
