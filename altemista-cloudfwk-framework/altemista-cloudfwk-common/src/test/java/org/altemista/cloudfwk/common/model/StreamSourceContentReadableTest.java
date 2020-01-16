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
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.InputStreamSource;
import org.altemista.cloudfwk.common.TestConstants;

/**
 * Tests for the ContentReadable implementation backed by an InputStreamSource
 * @author NTT DATA
 */
public class StreamSourceContentReadableTest {

	@Test
	public void sanityCheck() {
		
		Assert.assertNull(new StreamSourceContentReadable().getInputStream());
		Assert.assertNull(new StreamSourceContentReadable(null).getInputStream());
	}
	
	@Test
	public void testDefaultConstructor() {
		
		StreamSourceContentReadable readable = new StreamSourceContentReadable();
		Assert.assertNull(readable.getInputStream());
		
		readable.setInputStreamSource(new InputStreamSource() {
			
			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(TestConstants.BINARY);
			}
		});
		Assert.assertNotNull(readable.getInputStream());
	}
	
	@Test
	public void testFreshInputStream() {
		
		ContentReadable readable = TestConstants.binaryStreamSourceContentReadable();
		Assert.assertNotNull(readable.getInputStream());
		Assert.assertNotSame(readable.getInputStream(), readable.getInputStream());
	}

}
