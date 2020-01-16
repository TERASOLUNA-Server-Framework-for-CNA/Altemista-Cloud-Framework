/**
 * 
 */
package org.altemista.cloudfwk.common.util;

import java.util.Arrays;
import java.util.List;

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


import org.apache.commons.lang3.ArrayUtils;

/**
 * Array utility class for specific needs not covered by Arrays (Java) or ArrayUtils (Apache Commons).
 * @author NTT DATA
 */
public final class ArrayUtil {

	/**
	 * Private default constructor (utility class)
	 */
	private ArrayUtil() {
		super();
	}

	/**
	 * Converts a primitive array to an Object array, null-safe
	 * @param array The original array or null 
	 * @return the original array (if it was null or already an Object array),
	 * or the corresponding Object array if the original array was a primitive array
	 * @throws IllegalArgumentException if the argument is not an array
	 */
	public static Object[] toObjectArray(Object array) { // NOSONAR "Cyclomatic Complexity"
		
		// (sanity checks)
		if (array == null) {
			return null; // NOSONAR
		}
		if (!array.getClass().isArray()) {
			throw new IllegalArgumentException("The agurment is not an array");
		}
		
		// No conversion needed
		if (array instanceof Object[]) {
			return (Object[]) array;
		}
		
		// Converts from primitive array to Object array
		if (array instanceof boolean[]) {
			return ArrayUtils.toObject((boolean[]) array);
		}
		if (array instanceof byte[]) {
			return ArrayUtils.toObject((byte[]) array);
		}
		if (array instanceof char[]) {
			return ArrayUtils.toObject((char[]) array);
		}
		if (array instanceof double[]) {
			return ArrayUtils.toObject((double[]) array);
		}
		if (array instanceof float[]) {
			return ArrayUtils.toObject((float[]) array);
		}
		if (array instanceof int[]) {
			return ArrayUtils.toObject((int[]) array);
		}
		if (array instanceof long[]) {
			return ArrayUtils.toObject((long[]) array);
		}
		if (array instanceof short[]) {
			return ArrayUtils.toObject((short[]) array);
		}
		
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Converts an array to a serializable List, null-safe
	 * @param <T> the type of the array
	 * @param array the array or null
	 * @return the List or null
	 */
	public static <T> List<T> asList(T[] array) {
		
		if (array == null) {
			return null; // NOSONAR
		}
		
		return Arrays.asList(array);
	}
}
