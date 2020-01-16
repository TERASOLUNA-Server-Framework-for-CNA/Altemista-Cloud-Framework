/**
 * 
 */
package org.altemista.cloudfwk.it.util;

/*
 * #%L
 * altemista-cloud common: integration tests common utilities
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.altemista.cloudfwk.it.exception.ItApplicationException;

/**
 * Utility class for implementing REST controllers invoked by the actual integration tests.
 * This class methods mimic the names of org.junit.Assert to favor readability.
 * @author NTT DATA
 */
public final class ITControllerUtil {
	
	/**
	 * Private default constructor (utility class)
	 */
	private ITControllerUtil() {
		super();
	}
	
	/**
	 * Asserts that a condition is true.
	 * @param condition condition to be checked
	 */
	public static void assertTrue(boolean condition) {
		
		if (!condition) {
			throw new ItApplicationException("expected:<true>, but was:<" + condition + ">");
		}
	}
	
	/**
	 * Asserts that a condition is true.
     * @param condition condition to be checked
	 * @param message the code to be used to resolve the message of this exception
	 */
	public static void assertTrue(String message, boolean condition) {
		
		if (!condition) {
			throw new ItApplicationException(message);
		}
	}
	
	/**
	 * Asserts that a condition is false.
     * @param condition condition to be checked
	 */
	public static void assertFalse(boolean condition) {
		
		if (condition) {
			throw new ItApplicationException("expected:<false>, but was:<" + condition + ">");
		}
	}
	
	/**
	 * Asserts that a condition is false.
     * @param condition condition to be checked
	 * @param message the code to be used to resolve the message of this exception
	 */
	public static void assertFalse(String message, boolean condition) {
		
		assertTrue(message, !condition);
	}
	
	/**
	 * Asserts that an object is equals to another
	 * @param expected expected value
	 * @param object object to be checked
	 */
	public static void assertEquals(Object expected, Object object) {
		
		if (expected == null) {
			assertNull(object);
			
		} else if (object == null) {
			throw new ItApplicationException("expected:<" + expected + ">, but was null");
			
		} else if (!object.equals(expected)) {
			throw new ItApplicationException("expected:<" + expected + ">, but was:<" + object + ">");
		}
	}
	
	/**
	 * Asserts that an object is equals to another
	 * @param expected expected value
	 * @param object object to be checked
	 * @param message the code to be used to resolve the message of this exception
	 */
	public static void assertEquals(String message, Object expected, Object object) {
		
		if (expected == null) {
			assertNull(message, object);
			
		} else {
			assertNotNull(message, object);
			assertTrue(message, object.equals(expected));
		}
	}
	
	/**
	 * Asserts that an object is null
	 * @param object object to be checked
	 */
	public static void assertNull(Object object) {
		
		if (object != null) {
			throw new ItApplicationException("expected null, but was:<" + object + ">");
		}
	}
	
	/**
	 * Asserts that an object is null
	 * @param object object to be checked
	 * @param message the code to be used to resolve the message of this exception
	 */
	public static void assertNull(String message, Object object) {
		
		assertTrue(message, object == null);
	}
	
	/**
	 * Asserts that an object is not null
	 * @param object object to be checked
	 */
	public static void assertNotNull(Object object) {
		
		if (object == null) {
			throw new ItApplicationException("expected not null, but was null");
		}
	}
	
	/**
	 * Asserts that an object is not null
	 * @param object object to be checked
	 * @param message the code to be used to resolve the message of this exception
	 */
	public static void assertNotNull(String message, Object object) {
		
		assertTrue(message, object != null);
	}
}
