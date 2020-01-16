package org.altemista.cloudfwk.test.util;

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


import java.io.ByteArrayInputStream;

import org.springframework.util.Base64Utils;
import org.altemista.cloudfwk.common.model.ByteArrayContentReadable;
import org.altemista.cloudfwk.common.model.ContentReadable;
import org.altemista.cloudfwk.common.model.ResourceContentReadable;
import org.altemista.cloudfwk.common.model.StreamContentReadable;
import org.altemista.cloudfwk.common.model.StreamSourceContentReadable;

/**
 * Convenience constants for tests.
 * @author NTT DATA
 */
public final class TestConstants {
	
	/** The standard Lorem Ipsum passage, used since the 1500s */
	public static final String TEXT =
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor "
			+ "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis "
			+ "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
			+ "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu "
			+ "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in "
			+ "culpa qui officia deserunt mollit anim id est laborum";
	
	/** Random picture of 32x32 pixels (@see lorempixel.com), encoded as a Base64 string. */
	public static final String BINARY_BASE64 =
			"/9j/4AAQSkZJRgABAQAAAQABAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBF"
			+ "RyB2NjIpLCBxdWFsaXR5ID0gNzAK/9sAQwAKBwcIBwYKCAgICwoKCw4YEA4NDQ4dFRYRGCMfJSQiHyIh"
			+ "Jis3LyYpNCkhIjBBMTQ5Oz4+PiUuRElDPEg3PT47/9sAQwEKCwsODQ4cEBAcOygiKDs7Ozs7Ozs7Ozs7"
			+ "Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7/8AAEQgAIAAgAwEiAAIRAQMRAf/E"
			+ "AB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUS"
			+ "ITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RV"
			+ "VldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TF"
			+ "xsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgME"
			+ "BQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1Lw"
			+ "FWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKD"
			+ "hIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp"
			+ "6vLz9PX29/j5+v/aAAwDAQACEQMRAD8Adc3Kxdc8dTg4X61RWeedQwJUNjqcAZOMj1qxYWKX1lcXjCSa"
			+ "VdqxmNW2nvnrgnkjngYpkttdxylJFEIOdrcEFvT0ANecrXsepdibplJx04z82SO345pyTeZkEYIJBGOn"
			+ "1pXjbkxybs4wrYHGMk/rUIPmoGTsO3f296dhpmt4c8UjU5Xt47ZYXRRiINkEeoq9LaedNLA9wziUljtx"
			+ "+7PGMccday9L8GRaZcfa/tU8s0fKmM7PwrYe5uooJNgCnoEDBm+p+tc01aXuGkE5R94x7nS5rWZoQxdc"
			+ "AqH5NV1gdXYlywY5xnH8quarFNfmC5ThlTa+5wGwSOnv1qNYb+MMGkjkQrleoJOOB6D8K0UpF+ziluf/"
			+ "2Q==";
	
	/** Random picture of 32x32 pixels, as a byte array */
	public static final byte[] BINARY = Base64Utils.decodeFromString(BINARY_BASE64);
	
	//
	
	/** The content type of TEXT */
	public static final String TEXT_CONTENT_TYPE = "text/plain";
	
	/** The content type of BINARY */
	public static final String BINARY_CONTENT_TYPE = "image/jpeg";
	
	//
	
	/** The file name of TEXT */
	public static final String TEXT_NAME = "lorem_ipsum.txt";
	
	/** The file name of BINARY */
	public static final String BINARY_NAME = "lorem_pixel.jpg";
	
	//
	
	/** A readable text (text/plain) backed by an in-memory byte array */
	public static final ContentReadable TEXT_BYTE_ARRAY_CONTENT_READABLE =
			new ByteArrayContentReadable(TEXT.getBytes(), TEXT_CONTENT_TYPE, TEXT_NAME);
	
	/** A readable binary (image/jpeg) backed by an in-memory byte array */
	public static final ContentReadable BINARY_BYTE_ARRAY_CONTENT_READABLE =
			new ByteArrayContentReadable(BINARY, BINARY_CONTENT_TYPE, BINARY_NAME);
	
	//
	
	/** The resource location of the resource version of TEXT */
	public static final String TEXT_RESOURCE_LOCATION = "classpath:lorem_ipsum.txt";
	
	/** The resource location of the resource version of BINARY */
	public static final String BINARY_RESOURCE_LOCATION = "classpath:lorem_pixel.jpg";
	
	//
	
	/** A readable text file (text/plain) backed by a Spring Resource */
	public static final ContentReadable TEXT_RESOURCE_CONTENT_READABLE =
			new ResourceContentReadable(TEXT_RESOURCE_LOCATION, TEXT_CONTENT_TYPE);
	
	/** A readable binary file (image/jpeg) backed by a Spring Resource */
	public static final ContentReadable BINARY_RESOURCE_CONTENT_READABLE =
			new ResourceContentReadable(BINARY_RESOURCE_LOCATION, BINARY_CONTENT_TYPE);
	
	//
	
	/**
	 * Returns a readable text (text/plain), readable once
	 * @return a ContentReadable with a text file
	 */
	public static final ContentReadable textStreamContentReadable() {

		return new StreamContentReadable(new ByteArrayInputStream(TEXT.getBytes()), TEXT_CONTENT_TYPE, TEXT_NAME);
	}
	
	/**
	 * Returns a binary (image/jpeg), readable once
	 * @return a ContentReadable with a binary file
	 */
	public static final ContentReadable binaryStreamContentReadable() {

		return new StreamContentReadable(new ByteArrayInputStream(BINARY), BINARY_CONTENT_TYPE, BINARY_NAME);
	}
	
	//
	
	/**
	 * Returns a slowly readable text (text/plain), readable once
	 * @return a slow ContentReadable with a text file
	 */
	public static final ContentReadable textSlowByteArrayContentReadable() {

		return new StreamContentReadable(new SlowByteArrayInputStream(TEXT.getBytes()), TEXT_CONTENT_TYPE, TEXT_NAME);
	}
	
	/**
	 * Returns a slowly binary (image/jpeg), readable once
	 * @return a slow ContentReadable with a binary file
	 */
	public static final ContentReadable binarySlowByteArrayContentReadable() {

		return new StreamContentReadable(new SlowByteArrayInputStream(BINARY), BINARY_CONTENT_TYPE, BINARY_NAME);
	}
	
//	//
//	
//	/**
//	 * Returns a readable text (text/plain), readable multiple times
//	 * @return a ContentReadable with a text file
//	 */
//	public static final ContentReadable textStreamSourceContentReadable() {
//
//		return new StreamSourceContentReadable(
//				new InputStreamSource() {
//					
//					@Override
//					public InputStream getInputStream() throws IOException {
//						return new SlowByteArrayInputStream(TEXT.getBytes());
//					}
//					
//				}, TEXT_CONTENT_TYPE, TEXT_NAME);
//	}
//	
//	/**
//	 * Returns a binary (image/jpeg), readable multiple times
//	 * @return a ContentReadable with a binary file
//	 */
//	public static final ContentReadable binaryStreamSourceContentReadable() {
//
//		return new StreamSourceContentReadable(
//				new InputStreamSource() {
//					
//					@Override
//					public InputStream getInputStream() throws IOException {
//						return new SlowByteArrayInputStream(BINARY);
//					}
//					
//				}, BINARY_CONTENT_TYPE, BINARY_NAME);
//	}
	
	//
	
	/**
	 * Returns a slowly readable text (text/plain), readable multiple times
	 * @return a slow ContentReadable with a text file
	 */
	public static final ContentReadable textSlowStreamSourceContentReadable() {

		return new StreamSourceContentReadable(
				new SlowInputStreamSource(TEXT.getBytes()), TEXT_CONTENT_TYPE, TEXT_NAME);
	}
	
	/**
	 * Returns a slowly binary (image/jpeg), readable multiple times
	 * @return a slow ContentReadable with a binary file
	 */
	public static final ContentReadable binarySlowStreamSourceContentReadable() {

		return new StreamSourceContentReadable(
				new SlowInputStreamSource(BINARY), BINARY_CONTENT_TYPE, BINARY_NAME);
	}
	
	//
	
	/**
	 * Default private constructor
	 */
	private TestConstants() {
		super();
	}

}
