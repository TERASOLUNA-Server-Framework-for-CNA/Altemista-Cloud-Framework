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


import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

/**
 * Read-only List&lt;E&gt; implementation backed by a Map&lt;Integer, E&gt;
 * where the keys of the map are the indexes of the elements in the list 
 * @param <E> the type of elements in this sparse list
 * @author NTT DATA
 */
public class SparseList<E> // NOSONAR "Override this superclass equals' method"
		extends AbstractList<E>
		implements List<E>, RandomAccess, Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -1856211286260612425L;

	/**
	 * The map into which the elements of the SparseList are stored.
	 * The key is the index of the element in the list.
	 * (serializable by construction: either java.util.HashMap or java.util.Collections.EmptyMap are serializable)
	 */
	private Map<Integer, E> elements; // NOSONAR
	
	/** Index of the first element; typically zero */
	private int offset;
	
	/** The size of the SparseList (the maximum index number it contains). */
	private int size;
	
	/**
	 * Constructor
	 * @param elements the map into which the elements of the SparseList are stored
	 */
	public SparseList(Map<Integer, E> elements) {
		this(elements, 0);
	}
	
	/**
	 * Constructor
	 * @param elements the map into which the elements of the SparseList are stored
	 * @param offset index of the first element; typically zero.
	 * The offset can be negative but cannot be less than the minimum index found in element
	 */
	public SparseList(Map<Integer, E> elements, int offset) {
		super();
		
		if (elements == null) {
			this.elements = Collections.emptyMap();
			this.offset = 0;
			this.size = 0;
			
		} else {
			final int minIndex = Collections.min(elements.keySet());
			if (minIndex < offset) {
				throw new IndexOutOfBoundsException("Index: " + minIndex + ", Offset: " + offset);
			}
			
			this.elements = new HashMap<Integer, E>(elements);
			this.offset = offset;
			this.size = Collections.max(elements.keySet()) - offset + 1;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public E get(int index) {
		
		if ((index < 0) || (index >= this.size)) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
		}
		
		return this.elements.get(Integer.valueOf(index + this.offset));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		
		return this.size;
	}
}
