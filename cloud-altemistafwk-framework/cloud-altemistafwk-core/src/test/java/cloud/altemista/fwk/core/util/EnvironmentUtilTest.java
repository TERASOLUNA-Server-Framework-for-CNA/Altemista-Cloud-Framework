package cloud.altemista.fwk.core.util;

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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import cloud.altemista.fwk.common.util.ResourceUtil;

/**
 * Test for the utility class for core modules depending on Locale or host name environment variable.
 * @author NTT DATA
 */
public class EnvironmentUtilTest {

	@Test
	public void currentEnvironmentNotSet() {
		
		System.clearProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY);
		
		Assert.assertEquals("local", EnvironmentUtil.getCurrentEnvironment());
	}

	@Test
	public void currentEnvironmentSet() {
		
		final String fakeEnvironment = "dev";

		System.setProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY, fakeEnvironment);
		
		Assert.assertEquals(fakeEnvironment, EnvironmentUtil.getCurrentEnvironment());
	}
	
	/**
	 * Checks the utility class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		EnvironmentUtil.getFilename(null);
		EnvironmentUtil.getFilename("");
		
		EnvironmentUtil.getInputStream(null);
		EnvironmentUtil.getInputStream("");
	}
	
	@Test
	public void getFilenamesEnvironmentNotSet() {
		
		System.clearProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY);
		
		Assert.assertEquals("file.local", EnvironmentUtil.getFilename("file"));
		Assert.assertEquals("file.local.ext", EnvironmentUtil.getFilename("file.ext"));
		Assert.assertEquals("path/to/file.local", EnvironmentUtil.getFilename("path/to/file"));
		Assert.assertEquals("path/to/file.local.ext", EnvironmentUtil.getFilename("path/to/file.ext"));
		Assert.assertEquals("file://file.local", EnvironmentUtil.getFilename("file://file"));
		Assert.assertEquals("file://file.local.ext", EnvironmentUtil.getFilename("file://file.ext"));
		Assert.assertEquals("file://path/to/file.local", EnvironmentUtil.getFilename("file://path/to/file"));
		Assert.assertEquals("file://path/to/file.local.ext", EnvironmentUtil.getFilename("file://path/to/file.ext"));

	}
	
	@Test
	public void getFilenamesEnvironmentSet() {
		
		System.setProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY, "dev");
		
		Assert.assertEquals("file.dev", EnvironmentUtil.getFilename("file"));
		Assert.assertEquals("file.dev.ext", EnvironmentUtil.getFilename("file.ext"));
		Assert.assertEquals("path/to/file.dev", EnvironmentUtil.getFilename("path/to/file"));
		Assert.assertEquals("path/to/file.dev.ext", EnvironmentUtil.getFilename("path/to/file.ext"));
		Assert.assertEquals("file://file.dev", EnvironmentUtil.getFilename("file://file"));
		Assert.assertEquals("file://file.dev.ext", EnvironmentUtil.getFilename("file://file.ext"));
		Assert.assertEquals("file://path/to/file.dev", EnvironmentUtil.getFilename("file://path/to/file"));
		Assert.assertEquals("file://path/to/file.dev.ext", EnvironmentUtil.getFilename("file://path/to/file.ext"));

	}
	
	@Test
	public void getDefaultFile() throws IOException {
		
		System.clearProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY);
		
		InputStream referenceIs = null;
		InputStream testIs = null;
		try {
			referenceIs = ResourceUtil.getInputStream("pom.xml");
			Assume.assumeNotNull(referenceIs);
			
			// Checks the default file is read
			testIs = EnvironmentUtil.getInputStream("pom.xml");
			Assert.assertNotNull(testIs);
			Assert.assertTrue(IOUtils.contentEquals(testIs, referenceIs));
			
		} finally {
			IOUtils.closeQuietly(referenceIs);
			IOUtils.closeQuietly(testIs);
		}
	}
	
	@Test
	public void getDefaultEnvironmentFile() throws IOException {
		
		System.clearProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY);
		
		InputStream referenceIs = null;
		InputStream testIs = null;
		try {
			referenceIs = ResourceUtil.getInputStream("classpath:logback/logback.xml");
			Assume.assumeNotNull(referenceIs);
			
			// Checks the specific file for the default environment is read
			testIs = EnvironmentUtil.getInputStream("classpath:logback/logback.xml");
			Assert.assertNotNull(testIs);
			Assert.assertTrue(IOUtils.contentEquals(testIs, referenceIs));
			
		} finally {
			IOUtils.closeQuietly(referenceIs);
			IOUtils.closeQuietly(testIs);
		}
	}
	
	@Test
	public void getNonDefaultEnvironmentFile() throws IOException {
		
		System.setProperty(EnvironmentUtil.ENVIRONMENT_SYSTEM_PROPERTY, "example");
		
		InputStream referenceIs = null;
		InputStream testIs = null;
		try {
			referenceIs = ResourceUtil.getInputStream("classpath:logback/logback.example.xml");
			Assume.assumeNotNull(referenceIs);
			
			// Checks the specific file for the current environment is read
			testIs = EnvironmentUtil.getInputStream("classpath:logback/logback.xml");
			Assert.assertNotNull(testIs);
			Assert.assertTrue(IOUtils.contentEquals(testIs, referenceIs));
			
		} finally {
			IOUtils.closeQuietly(referenceIs);
			IOUtils.closeQuietly(testIs);
		}
	}
}
