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


import java.lang.annotation.Annotation;

import org.apache.commons.lang3.StringUtils;
import cloud.altemista.fwk.common.annotation.HiddenValue;

/**
 * Utility methods for obtaining string representations of objects
 * based on the <code>toString()</code> implementation of that objects.
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.util.ReflectionToStringUtil
 */
public final class ToStringUtil {
	
	/**
	 * Default private constructor (utility class).
	 */
	private ToStringUtil() {
		super();
	}
	
	/**
	 * Null-safe toString().
	 * Projects using Java 7 or newer versiones should use java.util.Objects.toString(Object) instead.
	 * This method does not honor the <code>HiddenValue</code> annotation.
	 * @param object Object
	 * @return toString of the object or "null"
	 * @see java.util.Objects#toString(Object)
	 */
	public static String toString(Object object) {
		
		return (object == null) ? "null" : object.toString();
	}
	
	/**
	 * Null-safe toString() with maximum length.
	 * This method does not honor the <code>HiddenValue</code> annotation.
	 * @param object Object
	 * @param maxLength maximum length of result String, should be at least 4 (otherwise, 4 will be used)
	 * @return toString of the object, no longer that maxLength characters, or "null" 
	 */
	public static String toString(Object object, int maxLength) {
		
		return abbreviate(toString(object), maxLength);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString()
	 * @param object Object
	 * @return toString of the object, the placeholder literal, or "null"
	 */
	public static String hiddenAwareToString(Object object) {
		
		if (object == null) {
			return "null";
		}
		
		final HiddenValue hiddenValue = object.getClass().getAnnotation(HiddenValue.class);
		return (hiddenValue != null) ? hiddenValue.value() : object.toString();
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() with maximum length
	 * @param object Object
	 * @param maxLength maximum length of result String, should be at least 4 (otherwise, 4 will be used)
	 * @return toString of the object, the placeholder literal, no longer that maxLength characters or "null"
	 */
	public static String hiddenAwareToString(Object object, int maxLength) {
		
		return abbreviate(hiddenAwareToString(object), maxLength);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() 
	 * @param object Object
	 * @param annotations Additional array of Annotations to look for the <code>@HiddenValue</code> annotation
	 * (e.g.: annotations of the method for the return value, or argument annotations)
	 * @return toString of the object, the placeholder literal, or "null"
	 */
	public static String hiddenAwareToString(Object object, Annotation[] annotations) {
		
		// Checks the additional array of Annotations for @HiddenValue first
		if ((annotations != null) && (annotations.length != 0)) {
			for (Annotation annotation : annotations) {
				if (HiddenValue.class.equals(annotation.annotationType())) {
					return ((HiddenValue) annotation).value();
				}
			}
		}
		
		// Delegates in the method without additional annotations
		return hiddenAwareToString(object);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() with maximum length
	 * @param object Object
	 * @param annotations Additional array of Annotations to look for the <code>@HiddenValue</code> annotation
	 * (e.g.: annotations of the method for the return value, or argument annotations)
	 * @param maxLength maximum length of result String, should be at least 4 (otherwise, 4 will be used)
	 * @return toString of the object, the placeholder literal, or "null"
	 */
	public static String hiddenAwareToString(Object object, Annotation[] annotations, int maxLength) {
		
		return abbreviate(hiddenAwareToString(object, annotations), maxLength);
	}
	
	/**
	 * Convenience delegated method to <code>StringUtils.abbreviate()</code>
	 * that does not throws IllegalArgumentException if maxLength is too small
	 * @param string String to abbreviate, can be null
	 * @param maxLength maximum length of result String, should be at least 4 (otherwise, 4 will be used)
	 * @return abbreviated String or null
	 */
	private static String abbreviate(String string, int maxLength) {
		
		return StringUtils.abbreviate(string, Math.max(4, maxLength)); // NOSONAR
	}
}
