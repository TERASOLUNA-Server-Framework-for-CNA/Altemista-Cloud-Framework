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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;

/**
 * Tests the simple database-backed properties provider
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = CloudAltemistafwkApplicationContextInitializer.class,
		locations = {
			"classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml",
			"classpath:cloud/altemista/fwk/config/test/cloud-altemistafwk-test-databasePropertiesProvider.xml"
		})
public class DatabasePropertiesProviderTest {
	
	@Autowired
	private PropertiesProvider propertiesProvider;
	
	@Test
	public void testInjection() {
		Assert.assertNotNull(this.propertiesProvider);
	}
	
	@Test
	public void testExampleValue1() {
		
		// @see com/jevers/core/properties/test-data.sql
		
		String fooValue = this.propertiesProvider.getProperty("foo");
		Assert.assertNotNull(fooValue);
		Assert.assertEquals("bar", fooValue);
	}
	
	@Test
	public void testExampleValue2() {
		
		// @see com/jevers/core/properties/test-data.sql
		
		String quxValue = this.propertiesProvider.getProperty("qux");
		Assert.assertNotNull(quxValue);
		Assert.assertEquals("norf", quxValue);
	}
	
	@Test
	public void testNonExistentValue() {
		
		// @see com/jevers/core/properties/test-data.sql
		
		String barValue = this.propertiesProvider.getProperty("bar");
		Assert.assertNull(barValue);
	}
	
	@Test
	public void testNonExistentValueWithDefault() {
		
		// @see com/jevers/core/properties/test-data.sql
		
		String barValue = this.propertiesProvider.getProperty("bar", "baz");
		Assert.assertNotNull(barValue);
		Assert.assertEquals("baz", barValue);
	}

}
