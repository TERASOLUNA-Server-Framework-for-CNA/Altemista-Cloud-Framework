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


import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.common.annotation.HiddenValue;

/**
 * Tests for the uility methods for obtaining string representations of objects.
 * @author NTT DATA
 */
public class ToStringUtilTest {
	
	/**
	 * Checks the utility class is handling extreme values properly (e.g.: null) 
	 */
	@Test
	public void sanityChecks() {
		
		Assert.assertEquals("null", ToStringUtil.toString(null));
		Assert.assertEquals("null", ToStringUtil.toString(null, 10));
		Assert.assertEquals("null", ToStringUtil.toString(null, 0));
		Assert.assertEquals("null", ToStringUtil.toString(null, -1));
	}
	
	/**
	 * Checks the null-safe toString() methods
	 */
	@Test
	public void testToString() {
		
		Assert.assertEquals("Hidden Class toString()", ToStringUtil.toString(new HiddenClass()));
		Assert.assertEquals("Hidden ...", ToStringUtil.toString(new HiddenClass(), 10));
		Assert.assertEquals("H...", ToStringUtil.toString(new HiddenClass(), 0));
		Assert.assertEquals("H...", ToStringUtil.toString(new HiddenClass(), -1));
	}
	
	/**
	 * Checks the null-safe, <code>@HiddenValue</code>-aware toString()
	 */
	@Test
	public void testHiddenAwareToString() {
		
		Assert.assertEquals("<hidden class>", ToStringUtil.hiddenAwareToString(new HiddenClass()));
		Assert.assertEquals("<hidden...", ToStringUtil.hiddenAwareToString(new HiddenClass(), 10));
		Assert.assertEquals("<...", ToStringUtil.hiddenAwareToString(new HiddenClass(), 0));
		Assert.assertEquals("<...", ToStringUtil.hiddenAwareToString(new HiddenClass(), -1));
	}
	
	/**
	 * Example class annotated with <code>@HiddenValue</code>
	 */
	@HiddenValue("<hidden class>")
	private class HiddenClass {
		
		@Override
		public String toString() {
			
			return "Hidden Class toString()";
		}
	}
}
