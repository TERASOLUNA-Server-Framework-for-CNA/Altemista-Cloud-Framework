/**
 * 
 */
package org.altemista.cloudfwk.common.model;

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


import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.common.TestConstants;

/**
 * Tests for the ContentReadable  implementation backed by an InputStream
 * @author NTT DATA
 */
public class StreamContentReadableTest {

	@Test
	public void sanityCheck() {
		
		Assert.assertNull(new StreamContentReadable().getInputStream());
		Assert.assertNull(new StreamContentReadable(null).getInputStream());
	}
	
	@Test
	public void testDefaultConstructor() {
		
		StreamContentReadable readable = new StreamContentReadable();
		Assert.assertNull(readable.getInputStream());
		
		readable.setInputStream(new ByteArrayInputStream(TestConstants.BINARY));
		Assert.assertNotNull(readable.getInputStream());
	}
	
	@Test
	public void testSameInputStream() {
		
		ContentReadable readable = TestConstants.binaryStreamContentReadable();
		Assert.assertNotNull(readable.getInputStream());
		Assert.assertSame(readable.getInputStream(), readable.getInputStream());
	}
}
