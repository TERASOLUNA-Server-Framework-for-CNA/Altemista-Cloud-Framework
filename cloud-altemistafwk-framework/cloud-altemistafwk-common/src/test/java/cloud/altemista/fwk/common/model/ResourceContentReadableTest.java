/**
 * 
 */
package cloud.altemista.fwk.common.model;

/*
 * #%L
 * altemista-cloud common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.common.TestConstants;
import cloud.altemista.fwk.common.exception.CommonException;

/**
 * Tests for the ContentReadable implementation backed by a Spring Resource.
 * @author NTT DATA
 */
public class ResourceContentReadableTest {

	@Test
	public void sanityCheck() {
		
		Assert.assertNotNull(new ResourceContentReadable());
		Assert.assertNotNull(new ResourceContentReadable(null));
		Assert.assertNotNull(new ResourceContentReadable(""));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheck1() {
		
		Assert.assertNotNull(new ResourceContentReadable().getInputStream());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sanityCheck2() {
		
		Assert.assertNotNull(new ResourceContentReadable(null).getInputStream());
	}
	
	@Test
	public void testDefaultConstructor() {
		
		ResourceContentReadable readable = new ResourceContentReadable();
		try {
			readable.getInputStream();
			Assert.fail();
			
		} catch (IllegalArgumentException e) {
			// (expected; ignored)
		}
		
		readable.setResourceLocation(TestConstants.BINARY_RESOURCE_LOCATION);
		Assert.assertNotNull(readable.getInputStream());
	}
	
	@Test
	public void testFreshInputStream() {
		
		ContentReadable readable = TestConstants.BINARY_RESOURCE_CONTENT_READABLE;
		Assert.assertNotNull(readable.getInputStream());
		Assert.assertNotSame(readable.getInputStream(), readable.getInputStream());
	}
	
	@Test(expected = CommonException.class)
	public void testResourceNotFound() {
		
		new ResourceContentReadable("foo").getInputStream();
	}
}
