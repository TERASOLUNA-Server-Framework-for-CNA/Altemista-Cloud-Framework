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


import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the array utility class for specific needs not covered by Arrays (Java) or ArrayUtils (Apache Commons).
 * @author NTT DATA
 */
public class ArrayUtilTest {
	
	/**
	 * Checks the utility class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertNull(ArrayUtil.toObjectArray(null));
		Assert.assertNull(ArrayUtil.asList(null));
	}
	
	/**
	 * ChecksArrayUtil.toObjectArray is handling wrogn values properly (e.g.: not an array) 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void sanityChecks2() {
		
		Assert.assertNull(ArrayUtil.toObjectArray("foo"));
	}
	
	@Test
	public void testToObjectArray() {
		
		final Object[] objectArray = new Object[]{ "foo" };
		Assert.assertSame(objectArray, ArrayUtil.toObjectArray(objectArray));
		
		Assert.assertTrue(ArrayUtil.toObjectArray(new boolean[]{ true  }) instanceof Boolean[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new byte[]{    42    }) instanceof Byte[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new char[]{    42    }) instanceof Character[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new double[]{  42.0d }) instanceof Double[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new float[]{   42.0f }) instanceof Float[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new int[]{     42    }) instanceof Integer[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new long[]{    42L   }) instanceof Long[]);
		Assert.assertTrue(ArrayUtil.toObjectArray(new short[]{   42    }) instanceof Short[]);
	}
}
