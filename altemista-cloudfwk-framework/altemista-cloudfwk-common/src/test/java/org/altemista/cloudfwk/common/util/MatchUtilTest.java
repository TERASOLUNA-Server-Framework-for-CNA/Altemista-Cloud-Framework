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

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the utility class to match classes and class names
 * @author NTT DATA
 */
public class MatchUtilTest {
	
	/**
	 * Checks the utility class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertFalse(MatchUtil.match(null, (Class<?>) null));
		Assert.assertFalse(MatchUtil.match(null, Object.class));
		Assert.assertFalse(MatchUtil.match(null, (String) null));
		Assert.assertFalse(MatchUtil.match(null, ""));
		Assert.assertFalse(MatchUtil.match(null, "java.lang.Object"));
		Assert.assertFalse(MatchUtil.match("", (Class<?>) null));
		Assert.assertFalse(MatchUtil.match("", Object.class));
		Assert.assertFalse(MatchUtil.match("", (String) null));
		Assert.assertFalse(MatchUtil.match("", ""));
		Assert.assertFalse(MatchUtil.match("", "java.lang.Object"));
		Assert.assertNull(MatchUtil.stackTraceElementMatching((String) null));
		Assert.assertNull(MatchUtil.stackTraceElementMatching(""));
		Assert.assertNull(MatchUtil.stackTraceElementMatching((String[]) null));
		Assert.assertNull(MatchUtil.stackTraceElementMatching(new String[0]));
	}
	
	@Test
	public void testMatchClass() {
		
		Class<?> clazz = ArrayList.class;
		
		Assert.assertTrue(MatchUtil.match("java.util.ArrayList", clazz));
		Assert.assertFalse(MatchUtil.match("java.util.HashSet", clazz));
		
		Assert.assertTrue(MatchUtil.match("j?v?.?t?l.?rr?yList", clazz));
		Assert.assertFalse(MatchUtil.match("j?v?.?t?l.?List", clazz));
		
		Assert.assertTrue(MatchUtil.match("java.util.Array*", clazz));
		Assert.assertTrue(MatchUtil.match("java.util.*ay*", clazz));
		Assert.assertTrue(MatchUtil.match("java.util.*List", clazz));
		Assert.assertFalse(MatchUtil.match("java.util.Hash*", clazz));
		Assert.assertFalse(MatchUtil.match("java.util.*sh*", clazz));
		Assert.assertFalse(MatchUtil.match("java.util.*Set", clazz));

		Assert.assertTrue(MatchUtil.match("java.**", clazz));
		Assert.assertFalse(MatchUtil.match("com.**", clazz));
		Assert.assertFalse(MatchUtil.match("java.*", clazz));
		Assert.assertTrue(MatchUtil.match("**.ArrayList", clazz));
		Assert.assertFalse(MatchUtil.match("**.HashSet", clazz));
		Assert.assertFalse(MatchUtil.match("*.ArrayList", clazz));

		Assert.assertTrue(MatchUtil.match("implements(java.util.List)", clazz));
		Assert.assertTrue(MatchUtil.match("implements(java.util.*)", clazz));
		Assert.assertTrue(MatchUtil.match("implements(**.List)", clazz));
		Assert.assertFalse(MatchUtil.match("implements(java.util.Set)", clazz));
		Assert.assertFalse(MatchUtil.match("implements(com.sun.*)", clazz));
		Assert.assertFalse(MatchUtil.match("implements(**.Set)", clazz));
		Assert.assertFalse(MatchUtil.match("implements()", clazz));
	}
	
	@Test
	public void testMatchClassName() {
		
		String className = "java.util.ArrayList";
		
		Assert.assertTrue(MatchUtil.match("java.util.ArrayList", className));
		Assert.assertFalse(MatchUtil.match("java.util.HashSet", className));
		
		Assert.assertTrue(MatchUtil.match("j?v?.?t?l.?rr?yList", className));
		Assert.assertFalse(MatchUtil.match("j?v?.?t?l.?List", className));
		
		Assert.assertTrue(MatchUtil.match("java.util.Array*", className));
		Assert.assertTrue(MatchUtil.match("java.util.*ay*", className));
		Assert.assertTrue(MatchUtil.match("java.util.*List", className));
		Assert.assertFalse(MatchUtil.match("java.util.Hash*", className));
		Assert.assertFalse(MatchUtil.match("java.util.*sh*", className));
		Assert.assertFalse(MatchUtil.match("java.util.*Set", className));

		Assert.assertTrue(MatchUtil.match("java.**", className));
		Assert.assertFalse(MatchUtil.match("com.**", className));
		Assert.assertFalse(MatchUtil.match("java.*", className));
		Assert.assertTrue(MatchUtil.match("**.ArrayList", className));
		Assert.assertFalse(MatchUtil.match("**.HashSet", className));
		Assert.assertFalse(MatchUtil.match("*.ArrayList", className));

		Assert.assertTrue(MatchUtil.match("implements(java.util.List)", className));
		Assert.assertTrue(MatchUtil.match("implements(java.util.*)", className));
		Assert.assertTrue(MatchUtil.match("implements(**.List)", className));
		Assert.assertFalse(MatchUtil.match("implements(java.util.Set)", className));
		Assert.assertFalse(MatchUtil.match("implements(com.sun.*)", className));
		Assert.assertFalse(MatchUtil.match("implements(**.Set)", className));
		Assert.assertFalse(MatchUtil.match("implements()", className));
	}
	
	@Test
	public void testMatchNonExistingClassName() {
		
		String className = "foo.bar.Baz";
		
		Assert.assertTrue(MatchUtil.match("foo.bar.Baz", className));
		Assert.assertTrue(MatchUtil.match("foo.bar.*", className));
		Assert.assertTrue(MatchUtil.match("foo.**", className));
		Assert.assertTrue(MatchUtil.match("**.Baz", className));
		
		Assert.assertFalse(MatchUtil.match("implements(foo.bar.Qux)", className));
		Assert.assertFalse(MatchUtil.match("implements(foo.bar.*)", className));
		Assert.assertFalse(MatchUtil.match("implements(foo.**)", className));
		Assert.assertFalse(MatchUtil.match("implements(**)", className));
	}
	
	@Test
	public void testStackTraceElementMatching() {
		
		Assert.assertNotNull(MatchUtil.stackTraceElementMatching("org.altemista.cloudfwk.common.util.MatchUtilTest"));
		Assert.assertNotNull(MatchUtil.stackTraceElementMatching("org.altemista.cloudfwk.common.util.*Test"));
		Assert.assertNotNull(MatchUtil.stackTraceElementMatching("org.altemista.cloudfwk.**.*Test"));
		Assert.assertNotNull(MatchUtil.stackTraceElementMatching("implements(**.Serializable)"));
		Assert.assertNull(MatchUtil.stackTraceElementMatching("foo.bar.Baz"));
		Assert.assertNull(MatchUtil.stackTraceElementMatching("implements()"));
	}

}
