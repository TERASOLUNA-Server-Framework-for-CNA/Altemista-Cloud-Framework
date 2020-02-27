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

/**
 * Tests for the ContentReadable implementation backed by an in-memory byte array
 * @author NTT DATA
 */
public class ByteArrayContentReadableTest {

	@Test
	public void sanityCheck() {
		
		Assert.assertNotNull(new ByteArrayContentReadable().getInputStream());
		Assert.assertNotNull(new ByteArrayContentReadable(null).getInputStream());
	}
	
	@Test
	public void testDefaultConstructor() {
		
		ByteArrayContentReadable readable = new ByteArrayContentReadable();
		readable.setBuffer(TestConstants.BINARY);
		Assert.assertNotNull(readable.getInputStream());
	}
	
	@Test
	public void testFreshInputStream() {
		
		ContentReadable readable = TestConstants.BINARY_BYTE_ARRAY_CONTENT_READABLE;
		Assert.assertNotNull(readable.getInputStream());
		Assert.assertNotSame(readable.getInputStream(), readable.getInputStream());
	}
}
