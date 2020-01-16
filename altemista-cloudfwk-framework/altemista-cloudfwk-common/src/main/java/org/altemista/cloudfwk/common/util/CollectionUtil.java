/**
 * 
 */
package org.altemista.cloudfwk.common.util;

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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Collection utility class for specific needs not covered by Collections (Java) or CollectionUtil (Apache Commons).
 * @author NTT DATA
 */
public final class CollectionUtil {
	
	/**
	 * Private default constructor (utility class)
	 */
	private CollectionUtil() {
		super();
	}

	/**
	 * Convenience method to ensure a collection is a list, or create a list from the collection otherwise
	 * @param <E> the type of the collection
	 * @param collection Collection
	 * @return the collection casted as List, or a new list created from the collection
	 */
	public static <E> List<E> asList(Collection<E> collection) {
		
		// (sanity check)
		if (collection == null) {
			return Collections.emptyList();
		}

		// If the collection is already a List, return the original collection
		if (collection instanceof List) {
			return (List<E>) collection;
		}
		
		return new ArrayList<E>(collection);
	}
	
	/**
	 * Extracts a page from a collection, range-safe
	 * @param <E> the type of the collection
	 * @param collection The original collection or null
	 * @param offset the start index of the instances to return
	 * @param pageSize the maximum number of instances to return
	 * @return serializable list with the instances from the start index up to a maximum of pageSize values
	 */
	public static <E> List<E> page(Collection<E> collection, int offset, int pageSize) {
		
		// (sanity check)
		if ((collection == null) || collection.isEmpty()) {
			return Collections.emptyList();
		}
		
		return page(asList(collection), offset, pageSize);
	}
	
	/**
	 * Extracts a page from a list, range-safe
	 * @param <E> the type of the collection
	 * @param list The original list or null
	 * @param pOffset the start index of the instances to return
	 * @param pPageSize the maximum number of instances to return
	 * @return serializable list with the instances from the start index up to a maximum of pageSize values
	 */
	public static <E> List<E> page(List<E> list, int pOffset, int pPageSize) {
		
		// (sanity check)
		if ((list == null) || list.isEmpty()) {
			return Collections.emptyList();
		}
		
		// (sanitize input)
		int offset = pOffset;
		if (offset < 0) {
			offset = 0;
		}
		if (offset >= list.size()) {
			offset = list.size();
		}
		int pageSize = pPageSize;
		if (pageSize < 0) {
			pageSize = 0;
		}
		if (offset + pageSize >= list.size()) {
			pageSize = list.size() - offset;
		}
		
		// WORKAROUND: ArrayList.subList is not serializable
		return new ArrayList<E>(list.subList(offset, offset + pageSize));
	}
	
}
