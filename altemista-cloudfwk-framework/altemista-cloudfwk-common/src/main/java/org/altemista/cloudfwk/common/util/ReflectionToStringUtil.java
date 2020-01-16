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


import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.ReflectionUtils;
import org.altemista.cloudfwk.common.annotation.HiddenValue;

/**
 * Utility methods for obtaining string representations of objects
 * mainly using reflection to introspect the objects (does not depend on the <code>toString()</code> implementations).
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.util.ToStringUtil
 */
public final class ReflectionToStringUtil {
	
	/** <code>toString()</code> implementation will be used for classes of these packages */
	private static final String[] OBJECT_TOSTRING_PACKAGES = {
			"java.", "javax.", "org.springframework", "org.apache.", "org.joda.time."
		};
	
	/** Matches fields that are neither static, transient nor volatile. */
	private static final ReflectionUtils.FieldFilter BEAN_FIELDS_FIELD_FILTER =
			new ReflectionUtil.WithoutAnyModifiersFieldFilter(Modifier.STATIC | Modifier.TRANSIENT | Modifier.VOLATILE);
	
	/** Maximum length of each nested object */
	private final int maxLength;
	
	/** Maximum depth for the introspection of the object */
	private final int maxDepth;
	
	/** Maximum length of the object after which the rest of the nested elements will be omitted */
	private final int stopPrintingAfter;
	
	/** The knownIds List<Pair<Long,Object>> */
	private List<Pair<Long, Object>> knownIds;
	
	/** The current knownId */
	private AtomicLong currentId;
	
	/**
	 * Private constructor.
	 * @param maxLength Maximum length of each nested object; should be at least 4 (otherwise, 4 will be used)
	 * @param maxDepth maximum depth for the introspection of the object
	 * @param stopPrintingAfter Maximum length of the object after which the rest of the nested elements will be omitted
	 */
	private ReflectionToStringUtil(int maxLength, int maxDepth, int stopPrintingAfter) {
		super();
		
		this.maxLength = Math.max(4, maxLength); // NOSONAR
		this.maxDepth = Math.max(1, maxDepth);
		this.stopPrintingAfter = Math.max(4, stopPrintingAfter); // NOSONAR
		
		this.knownIds = new ArrayList<Pair<Long, Object>>();
		this.currentId = new AtomicLong(0L);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() using reflection
	 * @param object Object
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	public static String toString(Object object) {
		
		return toString(object, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() using reflection
	 * @param object Object
	 * @param annotations Additional array of Annotations to look for the <code>@HiddenValue</code> annotation
	 * (e.g.: annotations of the method for the return value, or argument annotations)
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	public static String toString(Object object, Annotation[] annotations) {
		
		// Checks the additional array of Annotations for @HiddenValue first
		if ((annotations != null) && (annotations.length != 0)) {
			for (Annotation annotation : annotations) {
				if (HiddenValue.class.equals(annotation.annotationType())) {
					return ((HiddenValue) annotation).value();
				}
			}
		}
		
		// Delegates in the method without additional annotations
		return toString(object);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() using reflection
	 * @param object Object
	 * @param maxLength Maximum length of each nested object; should be at least 4 (otherwise, 4 will be used)
	 * @param maxDepth maximum depth for the introspection of the object
	 * @param stopPrintingAfter Maximum length of the object after which the rest of the nested elements will be omitted
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	public static String toString(Object object, int maxLength, int maxDepth, int stopPrintingAfter) {
		
		// If the object is null
		if (object == null) {
			return "null";
		}
		
		// Uses the placeholder if the object is hidden
		final HiddenValue hiddenValue = object.getClass().getAnnotation(HiddenValue.class);
		if (hiddenValue != null) {
			return StringUtils.abbreviate(hiddenValue.value(), Math.max(4, maxLength)); // NOSONAR
		}
		
		// Starts the introspection
		return new ReflectionToStringUtil(maxLength, maxDepth, stopPrintingAfter).toStringObject(object);
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() using reflection
	 * @param object Object
	 * @param annotations Additional array of Annotations to look for the <code>@HiddenValue</code> annotation
	 * (e.g.: annotations of the method for the return value, or argument annotations)
	 * @param maxLength Maximum length of each nested object; should be at least 4 (otherwise, 4 will be used)
	 * @param maxDepth maximum depth for the introspection of the object
	 * @param stopPrintingAfter Maximum length of the object after which the rest of the nested elements will be omitted
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	public static String toString(
			Object object, Annotation[] annotations, int maxLength, int maxDepth, int stopPrintingAfter) {
		
		// Checks the additional array of Annotations for @HiddenValue first
		if ((annotations != null) && (annotations.length != 0)) {
			for (Annotation annotation : annotations) {
				if (HiddenValue.class.equals(annotation.annotationType())) {
					return ((HiddenValue) annotation).value();
				}
			}
		}
		
		// Delegates in the method without additional annotations
		return toString(object, maxLength, maxDepth, stopPrintingAfter);
	}
	
	/**
	 * Entry point for generating the String representation of an object
	 * @param object Object
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	private String toStringObject(Object object) {
		
		return this.toStringObject(object, 0);
	}
	
	/**
	 * Entry point for generating the String representation of an object at a specific depth 
	 * @param object Object
	 * @param depth current depth of introspection
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	@SuppressWarnings("unchecked")
	private String toStringObject(Object object, int depth) { // NOSONAR "Cyclomatic Complexity"
		
		// If the maxDepth has been exhausted, does nothing 
		if (depth == this.maxDepth) {
			return "";
		}
		
		// If the object is null
		if (object == null) {
			return "null";
		}
		
		// Uses the placeholder if the object is hidden
		final HiddenValue hiddenValue = object.getClass().getAnnotation(HiddenValue.class);
		if (hiddenValue != null) {
			return this.abbreviateElement(hiddenValue.value());
		}
		
		// Uses a reference if the object was already known
		for (Pair<Long, Object> knownId : this.knownIds) {
			if (knownId.getRight() == object) {
				return this.useReference(object.getClass(), knownId.getLeft());
			}
		}
		
		// Primitives or Enum: uses native toString()
		Class<?> lClass = object.getClass();
		if (ClassUtils.isPrimitiveOrWrapper(lClass) || (object instanceof Enum)) {
			return this.abbreviateElement(object.toString());
		}
		
		// Uninitialized hibernate proxies: use a placeholder
		if (BooleanUtils.isNotFalse(isUninitializedHibernateProxy(object,
				"org.hibernate.collection.PersistentCollection"))) {
			return "<hibernate.PersistentCollection>";
		}
		if (BooleanUtils.isNotFalse(isUninitializedHibernateProxy(object,
				"org.hibernate.proxy.HibernateProxy"))) {
			return "<hibernate.HibernateProxy>";
		}
		
		// Arrays, collections and maps
		if (lClass.isArray()) {
			return this.toStringArray(object, depth);
		}
		if (object instanceof Collection) {
			return this.toStringCollection((Collection<?>) object, depth);
		}
		if (object instanceof Map) {
			return this.toStringMap((Map<Object, Object>) object, depth);
		}
		
		// CharSequences (e.g.: String): abbreviate to the maximum length of each element
		if (object instanceof CharSequence) {
			return String.format("\"%s\"", this.abbreviateElement(object.toString())); 
		}
		
		// JDK and other known utility classes: uses native toString()
		Package lPackage = lClass.getPackage();
		if ((lPackage != null) && StringUtils.startsWithAny(lPackage.getName(), OBJECT_TOSTRING_PACKAGES)) {
			return this.abbreviateElement(object.toString());
		}
		
		// Actual reflection
		return this.toStringBean(object, depth);
	}
	
	/**
	 * Generates the String representation of an object that is known to be an array 
	 * @param array Object
	 * @param depth current depth of introspection
	 * @return string representation of the array, the placeholder literal, or "null"
	 */
	private String toStringArray(Object array, int depth) {
		
		// Special cases: empty array, byte array, char array or maximum depth reached
		int length = Array.getLength(array);
		if (length == 0) {
			return "[]";
		}
		if (array instanceof byte[]) {
			return "<[" + length + " bytes]>";
		}
		if (array instanceof char[]) {
			return "<[" + length + " chars]>";
		}
		if (depth + 1 == this.maxDepth) {
			return String.format("<%s:[%d]>", ClassUtils.getShortClassName(array.getClass()), length);
		}
		
		// Goes inside the array
		StringBuilder sb = new StringBuilder();
		sb.append(this.createReference(array));
		sb.append("[");
		for (int i = 0; i < length; i++) {
			Object element = Array.get(array, i);
			sb.append(this.toStringObject(element));
			if (i < length - 1) {
				if (sb.length() <= this.stopPrintingAfter) {
					sb.append(", ");
				} else {
					sb.append("... <").append(length).append(">");
					break;
				}
			}
		}
		return sb.append("]>").toString();
	}

	/**
	 * Generates the String representation of a collection
	 * @param collection Collection
	 * @param depth current depth of introspection
	 * @return string representation of the collection, the placeholder literal, or "null"
	 */
	private String toStringCollection(Collection<?> collection, int depth) {
		
		// Special cases: empty collection or maximum depth reached
		int size = collection.size();
		if (size == 0) {
			return String.format("<%s:[]>", ClassUtils.getShortClassName(collection.getClass()));
		}
		if (depth + 1 == this.maxDepth) {
			return String.format("<%s:[%d]>", ClassUtils.getShortClassName(collection.getClass()), size);
		}
		
		// Goes inside the collection
		StringBuilder sb = new StringBuilder();
		sb.append(this.createReference(collection));
		sb.append("[");
		for (Iterator<?> it = collection.iterator(); it.hasNext(); ) {
			Object element = it.next();
			sb.append(this.toStringObject(element));
			if (it.hasNext()) {
				if (sb.length() <= this.stopPrintingAfter) {
					sb.append(", ");
				} else {
					sb.append("... <").append(size).append(">");
					break;
				}
			}
		}
		return sb.append("]>").toString();
	}

	/**
	 * Generates the String representation of a map
	 * @param map Map
	 * @param depth current depth of introspection
	 * @return string representation of the map, the placeholder literal, or "null"
	 */
	private String toStringMap(Map<Object, Object> map, int depth) {
		
		// Special cases: empty map or maximum depth reached
		int size = map.size();
		if (size == 0) {
			return String.format("<%s:{}>", ClassUtils.getShortClassName(map.getClass()));
		}
		if (depth + 1 == this.maxDepth) {
			return String.format("<%s:{<%d>}>", ClassUtils.getShortClassName(map.getClass()), size);
		}
		
		// Goes inside the array
		StringBuilder sb = new StringBuilder();
		sb.append(this.createReference(map));
		sb.append("{");
		for (Iterator<Map.Entry<Object, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<Object, Object> entry = it.next();
			sb.append(toStringObject(entry.getKey(), depth + 1));
			sb.append("=");
			sb.append(toStringObject(entry.getValue(), depth + 1));
			if (it.hasNext()) {
				if (sb.length() <= this.stopPrintingAfter) {
					sb.append(", ");
				} else {
					sb.append("... <").append(size).append(">");
					break;
				}
			}
		}
		return sb.append("}>").toString();
	}

	/**
	 * Generates the String representation of a class with introspection
	 * @param object Object
	 * @param depth current depth of introspection
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	private String toStringBean(Object object, int depth) {
		
		// Special cases: maximum depth reached
		if (depth + 1 == this.maxDepth) {
			return this.createClosedReference(object);
		}
		
		// Creates a map representation of the class
		Map<String, Object> map;
		try {
			BeanToMapCallback callback = new BeanToMapCallback(object);
			ReflectionUtils.doWithFields(object.getClass(), callback, BEAN_FIELDS_FIELD_FILTER);
			map = callback.getMap();
			
		} catch (IllegalStateException e) { // NOSONAR
			// (in case of exception, fallback to the toString() implementation)
			return this.abbreviateElement(object.toString());
		}
		
		// Goes inside the class
		StringBuilder sb = new StringBuilder();
		sb.append(this.createReference(object));
		sb.append("{");
		for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Object> entry = it.next();
			sb.append(entry.getKey()).append("=").append(this.toStringObject(entry.getValue(), depth + 1));
			if (it.hasNext()) {
				if (sb.length() <= this.stopPrintingAfter) {
					sb.append(", ");
				} else {
					sb.append("... <").append(map.size()).append(">");
					break;
				}
			}
		}
		return sb.append("}>").toString();
	}
	
	/**
	 * Field callback to put the fields of a bean into a map
	 * @author NTT DATA
	 */
	private static class BeanToMapCallback implements ReflectionUtils.FieldCallback {
		
		/** The object being introspected */
		private Object object;
		
		/** The result map */
		private Map<String, Object> map = new TreeMap<String, Object>();
		
		/**
		 * Constructor
		 * @param object The object being introspected
		 */
		public BeanToMapCallback(Object object) {
			super();
			
			this.object = object;
		}
		
		/* (non-Javadoc)
		 * @see org.springframework.util.ReflectionUtils.FieldCallback#doWith(java.lang.reflect.Field)
		 */
		@Override
		public void doWith(Field field) throws IllegalAccessException {
			
			// For fields with the @HiddenValue annotation, uses the placeholder value
			final HiddenValue hiddenValue = field.getAnnotation(HiddenValue.class);
			if (hiddenValue != null) {
				this.map.put(field.getName(), hiddenValue.value());
				return;
			}
			
			// Sets the field as accessible and stores its value in the map
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			Object value = field.get(this.object);
			this.map.put(field.getName(), value);
			field.setAccessible(accessible);
		}

		/**
		 * Returns the result map
		 * @return the result map
		 */
		public Map<String, Object> getMap() {
			return map;
		}
	}

	/**
	 * Convenience delegated method to <code>StringUtils.abbreviate()</code> for elements
	 * @param string String to abbreviate, can be null
	 * @return abbreviated String or null
	 */
	private String abbreviateElement(String string) {
		
		return StringUtils.abbreviate(string, this.maxLength);
	}
	
	/**
	 * Creates a reference to an object when no more information is to be added to the object
	 * @param object Object
	 * @return String representation of the reference in the format <code>&lt;%s:%d></code>
	 */
	private String createClosedReference(Object object) {
		
		long id = this.currentId.incrementAndGet();
		this.knownIds.add(new ImmutablePair<Long, Object>(id, object));
		return String.format("<%s:%d>", ClassUtils.getShortClassName(object.getClass()), id);
	}
	
	/**
	 * Creates a reference to an object when introspection is to be done in the object
	 * @param object Object
	 * @return String representation of the reference in the format <code>&lt;%s:%d:</code>
	 */
	private String createReference(Object object) {
		
		long id = this.currentId.incrementAndGet();
		this.knownIds.add(new ImmutablePair<Long, Object>(id, object));
		return String.format("<%s:%d:", ClassUtils.getShortClassName(object.getClass()), id);
	}
	
	/**
	 * Includes a reference to a known object
	 * @param pClass The Class of the object
	 * @param id The referenced ID
	 * @return String representation of the reference in the format <code>&lt;%s:%d&gt;</code>
	 */
	private String useReference(Class<?> pClass, long id) {
		
		return String.format("<%s:%d>", ClassUtils.getShortClassName(pClass), id);
	}
	
	/**
	 * Checks if the object is an uninitialized Hibernate proxy
	 * @param object Object
	 * @param interfaceNames name of the interfaces to check
	 * @return <code>true</code> if the object is an uninitialized Hibernate proxy.
	 * <code>false</code> if the object is not an Hibernate proxy or it is initialized.
	 * <code>null</code> if the object is an Hibernate proxy but can not be determined if initialized.
	 */
	private Boolean isUninitializedHibernateProxy(Object object, String... interfaceNames) {
		
		// The object can not be an Hibernate proxy (its class is not an Hibernate proxy)
		if (!isHibernateProxy(object, interfaceNames)) {
			return false;
		}
		
		// Checks if the object is an uninitialized Hibernate proxy
		try {
			Method method = ReflectionUtil.getMethod("org.hibernate.Hibernate", "isInitialized", Object.class);
			if (method == null) {
				// (cannot determine if initialized)
				return null; // NOSONAR
			}
			
			// Is the Hibernate proxy uninitialized?
			Boolean isInitialized = (Boolean) method.invoke(null, object);
			return BooleanUtils.isNotTrue(isInitialized);
			
		} catch (Exception e) { // NOSONAR
			// (cannot determine if initialized)
			return null; // NOSONAR
		}
	}
	
	/**
	 * Checks if the class of the object can be an Hibernate proxy
	 * @param object Object
	 * @param interfaceNames name of the interfaces to check
	 * @return <code>true</code> if the object is an Hibernate proxy
	 */
	private boolean isHibernateProxy(Object object, String... interfaceNames) {
		
		Class<?> lClass = object.getClass();
		Class<?>[] interfaces = lClass.getInterfaces();
		for (String interfaceName : interfaceNames) {
			// Is the object class of one of the specified interfaces?
			if (lClass.getName().equals(interfaceName)) { // NOSONAR
				return true;
			}
			
			// Is any interface of the object class of one of the specified interfaces?
			for (Class<?> lInterface : interfaces) {
				if (lInterface.getName().equals(interfaceName)) { // NOSONAR
					return true;
				}
			}
		}
		
		// The object can not be an Hibernate proxy (its class is not an Hibernate proxy)
		return false;
	}
}
