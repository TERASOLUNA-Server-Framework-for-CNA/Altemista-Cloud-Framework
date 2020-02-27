package cloud.altemista.fwk.common.util;

import java.io.Serializable;

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


import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Utility class with "defensive copying" techniques
 * to avoid security concerns and vulnerabilities (such as "array is stored directly"
 * and "malicious code may expose internal representation by returning reference to mutable object")
 * @author NTT DATA
 */
public final class DefensiveUtil {
	
	/**
	 * Private default constructor.
	 */
	private DefensiveUtil() {
		super();
	}

	/**
	 * Copies the specified array, null-safe.
	 * The resulting array is of exactly the same class as the original array.
	 * @param <T> the type of the array
	 * @param original the array to be copied or null
	 * @return a copy of the original array or null
	 */
	public static <T> T[] copyOf(T[] original) {
		
		if (original == null) {
			return null; // NOSONAR
		}
		return Arrays.copyOf(original, original.length);
	}
	
	/**
	 * Copies the specified date, null-safe.
	 * @param original the date to be copied or null
	 * @return a copy of the original date or null
	 */
	public static Date copyOf(Date original) {
		
		if (original == null) {
			return null;
		}
		return new Date(original.getTime());
	}
	
	/**
	 * Returns an unmodifiable view of the specified list, null-safe 
	 * @param <L> the type of the actual list
	 * @param <E> the type of the elements of the list
	 * @param original the list for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the specified list.
	 */
	@SuppressWarnings("unchecked")
	public static <L extends List<E> & Serializable, E> L unmodifiableList(List<E> original) {
		
		if (original == null) {
			return null;
		}
		
		// (both Collection#UnmodifiableRandomAccessList and Collection#UnmodifiableList implement Serializable)
		return (L) Collections.unmodifiableList(original);
	}

	/**
	 * Converts a primitive array to a serializable ArrayList, null-safe
	 * @param <L> the type of the actual list
	 * @param <T> the type of the elements of the list
	 * @param array The original array or null 
	 * @return the ArrayList or null
	 * @see #copyOf(Object[])
	 */
	public static <L extends List<T> & Serializable, T> L unmodifiableList(T[] array) {
		
		if (array == null) {
			return null; // NOSONAR
		}
		
		return unmodifiableList(Arrays.asList(array));
	}

	/**
	 * Returns a unmodifiable and serializable view of the specified map, null-safe.
	 * The resulting map will be a SortedMap if the original was a SortedMap.
	 * @param <M> the type of the actual map
	 * @param <K> the type of the keys of the map
	 * @param <V> the type of the values of the map
	 * @param original the map to be copied or null
	 * @return unmodifiable and serializable view of the specified map or null
	 */
	@SuppressWarnings("unchecked")
	public static <M extends Map<K, V> & Serializable, K, V> M unmodifiableMap(Map<K, V> original) {
		
		if (original == null) {
			return null;
		}
		
		// (both Collections#UnmodifiableSortedMap and Collection#unmodifiableMap implement Serializable)
		return (M) ((original instanceof SortedMap)
				? Collections.unmodifiableSortedMap((SortedMap<K, V>) original)
				: Collections.unmodifiableMap(original));
	}
}
