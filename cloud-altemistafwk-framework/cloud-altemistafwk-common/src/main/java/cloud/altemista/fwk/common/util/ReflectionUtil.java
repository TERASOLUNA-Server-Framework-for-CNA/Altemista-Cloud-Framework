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


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * Reflection utility class for specific needs not covered by ReflectionUtils (Spring).
 * @author NTT DATA
 */
public final class ReflectionUtil {

	/** Cache for <code>getMethod()</code> */
	private static final Map<String, Method> METHOD_CACHE = new ConcurrentReferenceHashMap<String, Method>(256);
	
	/**
	 * Private default constructor (utility class)
	 */
	private ReflectionUtil() {
		super();
	}
	
	/**
	 * This variant uses Apache Commons' {@link ClassUtils#getClass(java.lang.String)}
	 * and retrieves {@link Class#getMethod(String, Class...)} from a local cache and throws no exceptions
	 * @param pClassName the name of the class to introspect
	 * @param pName the name of the method
	 * @param parameterTypes the list of parameters
	 * @return the Method object that matches the specified name and parameterTypes,
	 * or null if no matching Method could be found
	 */
	public static Method getMethod(String pClassName, String pName, Class<?>... parameterTypes) {
		
		// (sanity checks)
		if (StringUtils.isBlank(pClassName) || StringUtils.isBlank(pName)) {
			return null;
		}
		
		// (sanitize arguments)
		final String className = StringUtils.trim(pClassName);
		final String name = StringUtils.trim(pName);
		
		// Checks the cache first
		final String key = String.format("%s.%S", className, name);
		if (METHOD_CACHE.containsKey(key)) {
			return METHOD_CACHE.get(key);
		}
		
		// Loads the class and gets the method
		try {
			Class<?> lClass = ClassUtils.getClass(className, false);
			Method method = lClass.getMethod(name, parameterTypes);
			METHOD_CACHE.put(key, method);
			return method;
			
		} catch (ClassNotFoundException e) { // NOSONAR
			// (ignored)
			return null;
			
		} catch (NoSuchMethodException e) { // NOSONAR
			// (ignored)
			return null;
			
		} catch (SecurityException e) { // NOSONAR
			// (ignored)
			return null;
		}
	}
	
	/**
	 * This variant retrieves {@link Class#getMethod(String, Class...)} from a local cache and throws no exceptions
	 * @param pClass the class to introspect
	 * @param pName the name of the method
	 * @param parameterTypes the list of parameters
	 * @return the Method object that matches the specified name and parameterTypes,
	 * or null if no matching Method could be found
	 */
	public static Method getMethod(Class<?> pClass, String pName, Class<?>... parameterTypes) {
		
		// (sanity checks)
		if ((pClass == null) || StringUtils.isBlank(pName)) {
			return null;
		}
		
		// (sanitize arguments)
		final String name = StringUtils.trim(pName);
		
		// Checks the cache first
		final String key = String.format("%s.%S", pClass.getName(), name);
		if (METHOD_CACHE.containsKey(key)) {
			return METHOD_CACHE.get(key);
		}
		
		// Gets the method
		try {
			Method method = pClass.getMethod(name, parameterTypes);
			METHOD_CACHE.put(key, method);
			return method;
			
		} catch (NoSuchMethodException e) { // NOSONAR
			// (ignored)
			return null;
			
		} catch (SecurityException e) { // NOSONAR
			// (ignored)
			return null;
		}
	}

	/**
	 * Clear the internal method cache.
	 */
	public static void clearCache() {
		
		METHOD_CACHE.clear();
	}
	
	/**
	 * FieldFilter that matches methods with all the specified modifiers
	 * @author NTT DATA
	 */
	public static class WithAllModifiersFieldFilter implements ReflectionUtils.FieldFilter {
		
		/** The modifiers */
		private int modifiers;
		
		/**
		 * Constructor
		 * @param modifiers the modifiers
		 */
		public WithAllModifiersFieldFilter(int modifiers) {
			super();
			this.modifiers = modifiers;
		}
		
		/* (non-Javadoc)
		 * @see org.springframework.util.ReflectionUtils.FieldFilter#matches(java.lang.reflect.Field)
		 */
		@Override
		public boolean matches(Field field) {
			return (field.getModifiers() & this.modifiers) == this.modifiers;
		}
	}
	
	/**
	 * FieldFilter that matches methods with at least one of the specified modifiers
	 * @author NTT DATA
	 */
	public static class WithAnyModifiersFieldFilter implements ReflectionUtils.FieldFilter {
		
		/** The modifiers */
		private int modifiers;
		
		/**
		 * Constructor
		 * @param modifiers the modifiers
		 */
		public WithAnyModifiersFieldFilter(int modifiers) {
			super();
			this.modifiers = modifiers;
		}
		
		/* (non-Javadoc)
		 * @see org.springframework.util.ReflectionUtils.FieldFilter#matches(java.lang.reflect.Field)
		 */
		@Override
		public boolean matches(Field field) {
			return (field.getModifiers() & this.modifiers) != 0;
		}
	}
	
	/**
	 * FieldFilter that matches methods without any of the specified modifiers
	 * @author NTT DATA
	 */
	public static class WithoutAnyModifiersFieldFilter implements ReflectionUtils.FieldFilter {
		
		/** The modifiers */
		private int modifiers;
		
		/**
		 * Constructor
		 * @param modifiers the modifiers
		 */
		public WithoutAnyModifiersFieldFilter(int modifiers) {
			super();
			this.modifiers = modifiers;
		}
		
		/* (non-Javadoc)
		 * @see org.springframework.util.ReflectionUtils.FieldFilter#matches(java.lang.reflect.Field)
		 */
		@Override
		public boolean matches(Field field) {
			return (field.getModifiers() & this.modifiers) == 0;
		}
	}

}
