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


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.common.TestConstants;
import org.altemista.cloudfwk.common.annotation.HiddenValue;

/**
 * Tests for the uility methods for obtaining string representations of objects.
 * @author NTT DATA
 */
public class ReflectionToStringUtilTest {
	
	/**
	 * Checks the utility class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertEquals("null", ReflectionToStringUtil.toString(null));
		Assert.assertEquals("null", ReflectionToStringUtil.toString(null, 200, 3, 1000));
		Assert.assertEquals("null", ReflectionToStringUtil.toString(null, 0, 0, 0));
		Assert.assertEquals("null", ReflectionToStringUtil.toString(null, -1, -1, -1));
	}
	
	/**
	 * Checks the null-safe, <code>@HiddenValue</code>-aware toString() using reflection
	 */
	@Test
	public void testReflectionToString() {
		
		// Tests the string representation of the example class
		String string = ReflectionToStringUtil.toString(new NormalClass());
		
		Assert.assertTrue("Class name not printed",
				StringUtils.contains(string, "ReflectionToStringUtilTest.NormalClass"));
		
		Assert.assertTrue("Wrong start marker", StringUtils.startsWith(string, "<"));
		Assert.assertTrue("Wrong end marker", StringUtils.endsWith(string, ">"));
		
		Assert.assertTrue("Array field not printed", StringUtils.contains(string, "array="));
		Assert.assertTrue("Wrong start marker for array field", StringUtils.contains(string, "array=<"));
		Assert.assertTrue("Array type not printed", StringUtils.contains(string, "int[]"));
		Assert.assertTrue("Array value not printed", StringUtils.contains(string, "[1, 2, 3]"));
		Assert.assertTrue("Wrong end marker for array field", StringUtils.contains(string, "3]>"));
		
		Assert.assertTrue("Byte array field not printed", StringUtils.contains(string, "byteArray="));
		Assert.assertTrue("Byte array value not printed", StringUtils.contains(string, "<[961 bytes]>"));
		
		Assert.assertTrue("Char array field not printed", StringUtils.contains(string, "charArray="));
		Assert.assertTrue("Char array value not printed", StringUtils.contains(string, "<[444 chars]>"));
		
		Assert.assertTrue("Collection field not printed", StringUtils.contains(string, "collection="));
		Assert.assertTrue("Wrong start marker for collection field", StringUtils.contains(string, "collection=<"));
		Assert.assertTrue("Collection type not printed", StringUtils.contains(string, "Arrays.ArrayList"));
		Assert.assertTrue("Collection value not printed", StringUtils.contains(string, "[4, 5, 6]"));
		Assert.assertTrue("Wrong end marker for array field", StringUtils.contains(string, "6]>"));
		
		Assert.assertTrue("Map field not printed", StringUtils.contains(string, "map="));
		Assert.assertTrue("Wrong start marker for map field", StringUtils.contains(string, "map=<"));
		Assert.assertTrue("Map type not printed", StringUtils.contains(string, "HashMap"));
		Assert.assertTrue("Map value not printed", StringUtils.contains(string, "{7=7.0, 8=8.0, 9=9.0}"));
		Assert.assertTrue("Wrong end marker for array field", StringUtils.contains(string, "9=9.0}>"));
		
		Assert.assertTrue("Hidden class field not printed", StringUtils.contains(string, "hiddenClass="));
		Assert.assertFalse("Hidden class not hidden", StringUtils.contains(string, "Hidden Class toString()"));
		Assert.assertTrue("Hidden class placeholder not printed", StringUtils.contains(string, "<hidden class>"));
		
		Assert.assertTrue("Hidden field not printed", StringUtils.contains(string, "hiddenField="));
		Assert.assertTrue("Hidden field placeholder not printed",
				StringUtils.contains(string, "<hidden nested class>"));
		
		Assert.assertTrue("JDK class toString() not printed",
				StringUtils.contains(string, "jdkField=java.lang.Object@"));
		
		Assert.assertTrue("Nested field not printed", StringUtils.contains(string, "nested="));
		Assert.assertTrue("Nested field type not printed", StringUtils.contains(string, "<hidden nested class>"));
		Assert.assertFalse("Nested field toString() printed", StringUtils.contains(string, "Bad implementation"));
		
		Assert.assertTrue("Null field not printed", StringUtils.contains(string, "nullField="));
		Assert.assertTrue("Null field value not printed", StringUtils.contains(string, "nullField=null"));
		
		
		// Tests the string representation of the example class with maximum depth 2
		string = ReflectionToStringUtil.toString(new NormalClass(), Integer.MAX_VALUE, 2, Integer.MAX_VALUE);
		Assert.assertTrue("Array not abbreviated", StringUtils.contains(string, "array=<int[]:[3]>"));
		
		Assert.assertTrue("Collection not abbreviated",
				StringUtils.contains(string, "collection=<Arrays.ArrayList:[3]>"));
		
		Assert.assertTrue("Nested field not abbreviated",
				StringUtils.contains(string, "nested=<ReflectionToStringUtilTest.NestedClass:2>"));
		
		
		// Tests the string representation of the example class with maximum depth 1
		string = ReflectionToStringUtil.toString(new NormalClass(), Integer.MAX_VALUE, 1, Integer.MAX_VALUE);
		Assert.assertEquals("<ReflectionToStringUtilTest.NormalClass:1>", string);

		
		// A class annotated with @HiddenValue should use the placeholder
		Assert.assertEquals("<hidden class>", ReflectionToStringUtil.toString(new HiddenClass()));
	}
	
	/**
	 * Example class annotated with <code>@HiddenValue</code>
	 */
	@HiddenValue("<hidden class>")
	private static class HiddenClass {
		
		@Override
		public String toString() {
			
			return "Hidden Class toString()";
		}
	}
	
	/**
	 * Example class
	 */
	private static class NormalClass {
		
		/** A primitive value */
		@SuppressWarnings("unused")
		private int primitiveField = 42;
		
		/** A JDK class */
		@SuppressWarnings("unused")
		private Object jdkField = new Object();
		
		/** An array */
		@SuppressWarnings("unused")
		private int[] array = new int[]{ 1, 2, 3};
		
		/** A byte array */
		@SuppressWarnings("unused")
		private byte[] byteArray = TestConstants.BINARY;
		
		/** A char array */
		@SuppressWarnings("unused")
		private char[] charArray = TestConstants.TEXT.toCharArray();
		
		/** A collection */
		@SuppressWarnings("unused")
		private List<Long> collection = Arrays.asList(new Long[]{ 4L, 5L, 6L });
		
		/** A map */
		private Map<Integer, Float> map = new HashMap<Integer, Float>();
		
		/** A nested class */
		@SuppressWarnings("unused")
		private NestedClass nested = new NestedClass();
		
		/** A nested class with <code>@HiddenValue</code> at field level */
		@HiddenValue("<hidden nested class>")
		private NestedClass hiddenField = new NestedClass();
		
		/** A nested class with <code>@HiddenValue</code> at class level */
		@SuppressWarnings("unused")
		private HiddenClass hiddenClass = new HiddenClass();
		
		/**
		 * Constructor
		 */
		public NormalClass() {
			super();
			
			this.map.put(7, 7.0f);
			this.map.put(8, 8.0f);
			this.map.put(9, 9.0f);
		}
	}
	
	/**
	 * Example class
	 */
	private static class NestedClass {
		
		/** An uninitialized class */
		@SuppressWarnings("unused")
		private Object nullField;
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Bad implementation";
		}
	}
}
