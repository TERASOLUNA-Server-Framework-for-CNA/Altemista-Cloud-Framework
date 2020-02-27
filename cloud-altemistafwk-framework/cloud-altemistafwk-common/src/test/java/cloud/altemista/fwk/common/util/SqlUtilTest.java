/**
 * 
 */
package cloud.altemista.fwk.common.util;

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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test for the utility class to format SQL queries
 * @author NTT DATA
 */
public class SqlUtilTest {

	@Test
	public void sanityChecks() {
		
		Assert.assertEquals(-1, SqlUtil.findPlaceholder(null, 0));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("", 0));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder(null, -1));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("", -1));
		
		Assert.assertEquals(0, SqlUtil.countPlaceholders(null));
		Assert.assertEquals(0, SqlUtil.countPlaceholders(""));
		Assert.assertEquals(0, SqlUtil.countPlaceholders("'"));
		Assert.assertEquals(0, SqlUtil.countPlaceholders("''"));
		
		Assert.assertEquals("", SqlUtil.replacePlaceholder(null, null));
		Assert.assertEquals("", SqlUtil.replacePlaceholder("", null));
		Assert.assertEquals("", SqlUtil.replacePlaceholder(null, Collections.emptyList()));
		Assert.assertEquals("", SqlUtil.replacePlaceholder("", Collections.emptyList()));

		Assert.assertEquals("", SqlUtil.parametersToString((Collection<?>) null));
		Assert.assertEquals("", SqlUtil.parametersToString(Collections.emptyList()));
	}
	
	@Test
	public void testFindPlaceholder() {
		
		// No placehoders
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("SELECT * FROM foo", 0));
		
		// One placeholder
		Assert.assertEquals(30, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ?", 0));
		Assert.assertEquals(30, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ?", 30));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ?", 31));
		
		// Two placeholders
		Assert.assertEquals(30, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ? AND baz = ?", 0));
		Assert.assertEquals(30, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ? AND baz = ?", 30));
		Assert.assertEquals(42, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = ? AND baz = ?", 31));
		
		// Quoted, false placeholder
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = '?'", 0));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = 'baz''?'", 0));
		Assert.assertEquals(-1, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = 'baz' AND qux = '?'", 0));
		
		// Quoted, false placeholder and actual placeholder
		Assert.assertEquals(44, SqlUtil.findPlaceholder("SELECT * FROM foo WHERE bar = '?' AND bar = ?", 0));
	}
	
	@Test
	public void testCountPlaceholders() {
		
		Assert.assertEquals(0, SqlUtil.countPlaceholders("SELECT * FROM foo"));
		Assert.assertEquals(1, SqlUtil.countPlaceholders("SELECT * FROM foo WHERE bar = ?"));
		Assert.assertEquals(2, SqlUtil.countPlaceholders("SELECT * FROM foo WHERE bar = ? AND baz = ?"));
		
		// With quoted placeholders
		Assert.assertEquals(0, SqlUtil.countPlaceholders("SELECT * FROM foo WHERE bar = '?'"));
		Assert.assertEquals(1, SqlUtil.countPlaceholders("SELECT * FROM foo WHERE bar = '?' AND bar = ?"));
	}
	
	@Test
	public void testReplacePlaceholder() {
		
		// No placeholders
		Assert.assertEquals("SELECT * FROM foo",
				SqlUtil.replacePlaceholder("SELECT * FROM foo", null));
		Assert.assertEquals("SELECT * FROM foo",
				SqlUtil.replacePlaceholder("SELECT * FROM foo", Collections.singletonList("bar")));
		
		// Not enough arguments
		Assert.assertEquals("SELECT * FROM foo WHERE bar = ?",
				SqlUtil.replacePlaceholder("SELECT * FROM foo WHERE bar = ?", null));
		Assert.assertEquals("SELECT * FROM foo WHERE bar = ?",
				SqlUtil.replacePlaceholder("SELECT * FROM foo WHERE bar = ?", Collections.emptyList()));

		// One placeholder
		Assert.assertEquals("SELECT * FROM foo WHERE bar = 'baz'",
				SqlUtil.replacePlaceholder("SELECT * FROM foo WHERE bar = ?", Collections.singletonList("baz")));

		// With quoted placeholders
		Assert.assertEquals("SELECT * FROM foo WHERE bar = '?' AND bar = 'baz'",
				SqlUtil.replacePlaceholder("SELECT * FROM foo WHERE bar = '?' AND bar = ?", Collections.singletonList("baz")));
	}
	
	@Test
	public void testArgumentToString() {
		
		// null
		Assert.assertEquals("null", SqlUtil.parameterToString(null));
		
		// Arrays and collections
		Assert.assertEquals("()",
				SqlUtil.parameterToString(new Object[0]));
		Assert.assertEquals("('foo', 'bar', 'baz')",
				SqlUtil.parameterToString(new Object[]{ "foo", "bar", "baz" }));
		Assert.assertEquals("()",
				SqlUtil.parameterToString(Collections.emptyList()));
		Assert.assertEquals("('foo', 'bar', 'baz')",
				SqlUtil.parameterToString(Arrays.asList(new Object[]{ "foo", "bar", "baz" })));
		
		// CharSequence
		Assert.assertEquals("'foo'", SqlUtil.parameterToString("foo"));
		Assert.assertEquals("'foo''bar'", SqlUtil.parameterToString("foo'bar"));
		
		// Char
		Assert.assertEquals("'f'", SqlUtil.parameterToString('f'));
		Assert.assertEquals("''''", SqlUtil.parameterToString('\''));

		// Date
		final long testDateInMillis = 1459188000000L;
		final String formattedTestDate = String.format("'%s'",
				FastDateFormat.getInstance("yyyy-MM-dd HH:mm:SS.sss").format(testDateInMillis));
		final Calendar testCalendar = Calendar.getInstance();
		testCalendar.setTimeInMillis(testDateInMillis);
		Assert.assertEquals(formattedTestDate, SqlUtil.parameterToString(new Date(testDateInMillis)));
		Assert.assertEquals(formattedTestDate, SqlUtil.parameterToString(new java.sql.Date(testDateInMillis)));
		Assert.assertEquals(formattedTestDate, SqlUtil.parameterToString(new java.sql.Time(testDateInMillis)));
		Assert.assertEquals(formattedTestDate, SqlUtil.parameterToString(new java.sql.Timestamp(testDateInMillis)));
		Assert.assertEquals(formattedTestDate, SqlUtil.parameterToString(testCalendar));
		
		// Primitives
		Assert.assertEquals(Boolean.toString(true), SqlUtil.parameterToString(true));
		Assert.assertEquals(Boolean.toString(true), SqlUtil.parameterToString(Boolean.TRUE));
		Assert.assertEquals("'a'", SqlUtil.parameterToString('a'));
		Assert.assertEquals("'a'", SqlUtil.parameterToString(Character.valueOf('a')));
		Assert.assertEquals(Byte.toString((byte) 1), SqlUtil.parameterToString((byte) 1));
		Assert.assertEquals(Byte.toString((byte) 1), SqlUtil.parameterToString(Byte.valueOf((byte) 1)));
		Assert.assertEquals(Short.toString((short) 1), SqlUtil.parameterToString((short) 1));
		Assert.assertEquals(Short.toString((short) 1), SqlUtil.parameterToString(Short.valueOf((short) 1)));
		Assert.assertEquals(Integer.toString(1), SqlUtil.parameterToString(1));
		Assert.assertEquals(Integer.toString(1), SqlUtil.parameterToString(Integer.valueOf(1)));
		Assert.assertEquals(Long.toString(1L), SqlUtil.parameterToString(1L));
		Assert.assertEquals(Long.toString(1L), SqlUtil.parameterToString(Long.valueOf(1L)));
		Assert.assertEquals(Float.toString(1.1f), SqlUtil.parameterToString(1.1f));
		Assert.assertEquals(Float.toString(1.1f), SqlUtil.parameterToString(Float.valueOf(1.1f)));
		Assert.assertEquals(Double.toString(1.1d), SqlUtil.parameterToString(1.1d));
		Assert.assertEquals(Double.toString(1.1d), SqlUtil.parameterToString(Double.valueOf(1.1d)));
		
		// Unknown classes
		Assert.assertEquals("<class java.lang.Object>", SqlUtil.parameterToString(new Object()));
	}
}

