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

/**
 * Tests for the ContentType class
 * @author NTT DATA
 */
public class ContentTypeTest {
	
	/**
	 * Checks the class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertNull(ContentType.fromContentType(null));
		Assert.assertNull(ContentType.fromContentType(""));
		Assert.assertNull(ContentType.fromContentType("   "));
		Assert.assertNull(ContentType.fromFilename(null));
		Assert.assertNull(ContentType.fromFilename(""));
		Assert.assertNull(ContentType.fromFilename("   "));
		Assert.assertNull(ContentType.fromExtension(null));
		Assert.assertNull(ContentType.fromExtension(""));
		Assert.assertNull(ContentType.fromExtension("   "));
		
		Assert.assertFalse(ContentType.CSV.isProperFilename(null));
		Assert.assertFalse(ContentType.CSV.isProperFilename(""));
		Assert.assertFalse(ContentType.CSV.isProperFilename("   "));
		Assert.assertFalse(ContentType.CSV.isProperExtension(null));
		Assert.assertFalse(ContentType.CSV.isProperExtension(""));
		Assert.assertFalse(ContentType.CSV.isProperExtension("   "));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks1() {
		
		 ContentType.register(null, "foo");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks2() {
		
		ContentType.register("", "foo");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks3() {
		
		ContentType.register("   ", "foo");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks4() {
		
		 ContentType.register("text/plain");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks5() {
		
		 ContentType.register("text/plain", (String) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks6() {
		
		 ContentType.register("text/plain", (String[]) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks7() {
		
		 ContentType.register("text/plain", "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void registerSanityChecks8() {
		
		 ContentType.register("text/plain", "   ");
	}
	
	@Test
	public void testFromContentType() {
		
		 Assert.assertEquals(ContentType.HTML, ContentType.fromContentType("text/html"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromContentType(" text/html "));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromContentType("TEXT/HTML"));
	}
	
	@Test
	public void testFromFilename() {
		
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("index.htm"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("INDEX.HTM"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("index.html"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("INDEX.HTML"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("/path/to/index.htm"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("/path/to/INDEX.HTM"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("/path/to/index.html"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromFilename("/path/to/INDEX.HTML"));
		 
		 Assert.assertNull(ContentType.fromFilename("index"));
		 Assert.assertNull(ContentType.fromFilename("path/to/index"));
	}
	
	@Test
	public void testFromExtension() {
		
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("htm"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("HTM"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("html"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("HTML"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension(" htm"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("  HTM"));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("html "));
		 Assert.assertEquals(ContentType.HTML, ContentType.fromExtension("HTML  "));
	}
}
