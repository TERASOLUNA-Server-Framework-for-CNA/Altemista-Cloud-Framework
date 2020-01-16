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


import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the SparseList class
 * @author NTT DATA
 */
public class SparseListTest {
	
	/** A dense map (all the elements of the SparseList will have data) */
	private static final Map<Integer, Object> DENSE = new HashMap<Integer, Object>();
	static {
		DENSE.put(0, "foo");
		DENSE.put(1, "bar");
		DENSE.put(2, "baz");
		DENSE.put(3, "qux");
	}
	
	/** An sparse map (only a few elements of the SparseList will have data) */
	private static final Map<Integer, Object> SPARSE = new HashMap<Integer, Object>();
	static {
		SPARSE.put(1000, "foo");
		SPARSE.put(2000, "bar");
		SPARSE.put(3000, "baz");
		SPARSE.put(4000, "qux");
	}
	
	/**
	 * Checks the class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertNotNull(new SparseList<Object>((Map<Integer, Object>) null));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testReadOnlyAdd() {
		
		new SparseList<Object>(DENSE).add("foo");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testReadOnlyAddIndex() {
		
		new SparseList<Object>(DENSE).add(0, "foo");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testReadOnlySet() {
		
		new SparseList<Object>(DENSE).set(0, "foo");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testReadOnlyRemove() {
		
		new SparseList<Object>(DENSE).remove(0);
	}
	
	@Test
	public void testDenseConstructor() {
		
		Assert.assertNotNull(new SparseList<Object>(DENSE));
		Assert.assertNotNull(new SparseList<Object>(DENSE, 0));
		Assert.assertNotNull(new SparseList<Object>(DENSE, -1));
		Assert.assertNotNull(new SparseList<Object>(DENSE, -1000));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testDenseConstructorOffset() {
		
		Assert.assertNotNull(new SparseList<Object>(DENSE, 1));
	}
	
	@Test
	public void testSparseConstructor() {
		
		Assert.assertNotNull(new SparseList<Object>(SPARSE));
		Assert.assertNotNull(new SparseList<Object>(SPARSE, 0));
		Assert.assertNotNull(new SparseList<Object>(SPARSE, 1));
		Assert.assertNotNull(new SparseList<Object>(SPARSE, -1));
		Assert.assertNotNull(new SparseList<Object>(SPARSE, 1000));
		Assert.assertNotNull(new SparseList<Object>(SPARSE, -1000));
	}
	
	@Test
	public void testDenseSize() {
		
		Assert.assertEquals(4, new SparseList<Object>(DENSE).size());
		Assert.assertEquals(4, new SparseList<Object>(DENSE, 0).size());
		Assert.assertEquals(5, new SparseList<Object>(DENSE, -1).size());
		Assert.assertEquals(1004, new SparseList<Object>(DENSE, -1000).size());
	}
	
	@Test
	public void testSparseSize() {
		
		Assert.assertEquals(4001, new SparseList<Object>(SPARSE).size());
		Assert.assertEquals(4001, new SparseList<Object>(SPARSE, 0).size());
		Assert.assertEquals(4000, new SparseList<Object>(SPARSE, 1).size());
		Assert.assertEquals(4002, new SparseList<Object>(SPARSE, -1).size());
		Assert.assertEquals(3001, new SparseList<Object>(SPARSE, 1000).size());
		Assert.assertEquals(5001, new SparseList<Object>(SPARSE, -1000).size());
	}
	
	/**
	 * Tests that all the elements of the dense list have value
	 */
	@Test
	public void testDenseGet() {
		
		SparseList<Object> list = new SparseList<Object>(DENSE);
		for (int i = 0, n = list.size(); i < n; i++) {
			Assert.assertNotNull(list.get(i));
		}
	}
	@Test
	public void testDenseGetOffset() {
		
		SparseList<Object> list = new SparseList<Object>(DENSE, -1);
		Assert.assertNull(list.get(0));
		Assert.assertNotNull(list.get(DENSE.size()));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testDenseGetNegative() {
		
		new SparseList<Object>(DENSE).get(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testDenseGetOutOfBounds() {
		
		new SparseList<Object>(DENSE).get(DENSE.size());
	}
	
	/**
	 * Tests that only the few elements of the sparse list that have value are the correct ones
	 */
	@Test
	public void testSparseGet() {
		
		SparseList<Object> list = new SparseList<Object>(SPARSE);
		for (int i = 0, n = list.size(); i < n; i++) {
			if (SPARSE.containsKey(Integer.valueOf(i))) {
				Assert.assertNotNull(list.get(i));
			} else {
				Assert.assertNull(list.get(i));
			}
		}
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSparseGetNegative() {
		
		new SparseList<Object>(SPARSE).get(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testSparseGetOutOfBounds() {
		
		new SparseList<Object>(SPARSE).get(4001);
	}
	
	/**
	 * Tests iterating over a dense list 
	 */
	@Test
	public void testDenseIterator() {
		
		SparseList<Object> list = new SparseList<Object>(DENSE);
		int i = 0;
		for(Object o : list) {
			Assert.assertNotNull(o);
			i++;
		}
		Assert.assertEquals(list.size(), i);
	}
	
	/**
	 * Tests iterating over a sparse list 
	 */
	@Test
	public void testSparseIterator() {
		
		SparseList<Object> list = new SparseList<Object>(SPARSE);
		int i = 0;
		for(Object o : list) {
			if (SPARSE.containsKey(Integer.valueOf(i))) {
				Assert.assertNotNull(o);
			} else {
				Assert.assertNull(o);
			}
			i++;
		}
		Assert.assertEquals(list.size(), i);
	}
	
	@Test
	public void testDenseIndexOf() {
		
		SparseList<Object> list = new SparseList<Object>(DENSE);
		for(Map.Entry<Integer, Object> entry : DENSE.entrySet()) {
			Assert.assertEquals(entry.getKey().intValue(), list.indexOf(entry.getValue()));
		}
		Assert.assertEquals(-1, list.indexOf(null));
		Assert.assertEquals(-1, list.indexOf(""));
	}
	
	@Test
	public void testSparseIndexOf() {
		
		SparseList<Object> list = new SparseList<Object>(SPARSE);
		for(Map.Entry<Integer, Object> entry : SPARSE.entrySet()) {
			Assert.assertEquals(entry.getKey().intValue(), list.indexOf(entry.getValue()));
		}
		Assert.assertEquals(0, list.indexOf(null));
		Assert.assertEquals(-1, list.indexOf(""));
	}
}
