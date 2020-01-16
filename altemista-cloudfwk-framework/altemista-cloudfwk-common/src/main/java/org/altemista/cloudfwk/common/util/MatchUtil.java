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


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * Utility class to match classes and class names.
 * The patterns are mainly Ant-style based but also support some expressions similar to the AspectJ expressions,
 * using the following rules:<ul>
 * <li>? matches one character</li>
 * <li>* matches zero or more characters</li>
 * <li>** matches zero or more packages in the path</li>
 * <li>implements(&lt;pattern&gt;) will use the interfaces implemented by the class to execute the nested pattern</li>
 * </ul>
 * Examples:<ul>
 * <li>**.Prefix* matches classes whose name starts with "Prefix"</li>
 * <li>**.*Name* matches classes whose name contains "Name"</li>
 * <li>**.*Suffix matches classes whose name ends with "Suffix"</li>
 * <li>com.sample.* matches classes in the package "com.sample"</li>
 * <li>com.sample.** matches classes in the package "com.sample" or subpackages</li>
 * <li>implements(&lt;pattern&gt;) matches classes implementing any interface that matches the nested pattern</li>
 * </ul>
 * @author NTT DATA
 * @see org.springframework.util.AntPathMatcher
 */
public final class MatchUtil {
	
	/**
	 * Almost default AntPathMatcher, but using dot (".") as the path separator,
	 * so it is suitable to match packages and classes instead of file paths
	 */
	private static final PathMatcher PATH_MATCHER = new AntPathMatcher(".");
	
	/** Pattern for matching "implements()" patterns */
	private static final Pattern PATTERN_IMPLEMENTS = Pattern.compile("^implements\\((.*)\\)$");
	
	/** Index of the capturing group with the nested pattern in "implements()" patterns */
	private static final int IMPLEMENTS_NESTED_PATTERN_GROUP = 1;
	
	/**
	 * Private default constructor (utility class)
	 */
	private MatchUtil() {
		super();
	}

	/**
	 * Match the name of the given {@code class} against the given {@code pattern}.
	 * @param pattern the pattern to match against
	 * @param clazz the class whose name will be test
	 * @return if the supplied {@code class} matched
	 */
	public static boolean match(String pattern, Class<?> clazz) {
		
		// (sanity checks)
		if (clazz == null) {
			return false;
		}
		if (StringUtils.isEmpty(pattern)) {
			return false;
		}
		
		// Is the pattern implements()?
		Matcher implementsMatcher = PATTERN_IMPLEMENTS.matcher(pattern);
		if (implementsMatcher.matches()) {
			// Matches the interfaces implemented by the class against the nested pattern
			String nestedPattern = implementsMatcher.group(IMPLEMENTS_NESTED_PATTERN_GROUP);
			return StringUtils.isNotEmpty(nestedPattern) && interfacesMatch(nestedPattern, clazz);
		}
		
		// Matches the class name against the pattern
		return classNameMatches(pattern, clazz.getName());
	}
	
	/**
	 * Match the given {@code className} against the given {@code pattern}.
	 * @param pattern the pattern to match against
	 * @param className the String to test; usually a qualified name of a class
	 * @return if the supplied {@code className} matched
	 */
	public static boolean match(String pattern, String className) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(className)) {
			return false;
		}
		if (StringUtils.isEmpty(pattern)) {
			return false;
		}
		
		// Is the pattern implements()?
		Matcher implementsMatcher = PATTERN_IMPLEMENTS.matcher(pattern);
		if (implementsMatcher.matches()) {
			// Extracts the nested pattern
			String nestedPattern = implementsMatcher.group(IMPLEMENTS_NESTED_PATTERN_GROUP);
			if (StringUtils.isEmpty(nestedPattern)) {
				return false;
			}
			
			// Tries to get the actual class
			Class<?> clazz = null;
			try {
				clazz = ClassUtils.getClass(className);
				
			} catch (ClassNotFoundException e) { // NOSONAR
				return false;
			}
			
			// Matches the interfaces implemented by the class against the nested pattern
			return interfacesMatch(nestedPattern, clazz);
		}
		
		// Matches the class name against the pattern
		return classNameMatches(pattern, className);
	}
	
	/**
	 * Looks for the first (i.e.: most recent) class in the stack trace that matches any of the given {@code patterns}.
	 * @param patterns the patterns to match against
	 * @return the most recent StackTraceElement that matches any of the patterns or null of there is no match
	 */
	public static StackTraceElement stackTraceElementMatching(String... patterns) {
		
		// (sanity checks)
		if (ArrayUtils.isEmpty(patterns)) {
			return null;
		}

		// (convenience constants)
		final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		final int n = stackTraceElements.length;
		int index = Integer.MAX_VALUE;

		// For each pattern
	patternsLoop: // NOSONAR
		for (String pattern : patterns) {
			
			// (sanity checks)
			if (StringUtils.isEmpty(pattern)) {
				continue;
			}
			
			// Is the pattern implements()?
			Matcher implementsMatcher = PATTERN_IMPLEMENTS.matcher(pattern);
			if (implementsMatcher.matches()) {
				
				// Extracts the nested pattern
				String nestedPattern = implementsMatcher.group(IMPLEMENTS_NESTED_PATTERN_GROUP);
				if (StringUtils.isEmpty(nestedPattern)) {
					// (no nested pattern)
					continue;
				}
				
				// Matches the interfaces implemented by the classes in the stack trace against the nested pattern
				for (int i = 0; i < n; i++) {
					StackTraceElement element = stackTraceElements[i];
					if (interfacesMatch(nestedPattern, element.getClass())) {
						// (match found for this pattern)
						index = Math.min(i, index);
						continue patternsLoop;
					}
				}
				
				// No match for this pattern
				continue patternsLoop;
			}
			
			// Matches the class names in the stack trace against the pattern
			for (int i = 0; i < n; i++) {
				StackTraceElement element = stackTraceElements[i];
				if (classNameMatches(pattern, element.getClassName())) {
					// (match found for this pattern)
					index = Math.min(i, index);
					continue patternsLoop;
				}
			}
		}
		
		// Match found
		if (index < n) {
			return stackTraceElements[index];
		}
		
		// No match
		return null;
	}
	
	/**
	 * Matches the given {@code className} against the given {@code pattern}, Ant style.
	 * @param pattern the pattern to match against, never null
	 * @param className the class name to test, never null
	 * @return if the supplied {@code className} matched
	 */
	private static boolean classNameMatches(String pattern, String className) {
		
		return PATH_MATCHER.match(pattern, className);
	}
	
	/**
	 * Matches the interfaces of the given {@code class} against the given nested pattern, Ant style. 
	 * @param nestedPattern the pattern to match against, never null
	 * @param clazz the class whose interfaces are to be tested, never null
	 * @return if the one of the interfaces of the supplied {@code class} matched
	 */
	private static boolean interfacesMatch(String nestedPattern, Class<?> clazz) {
		
		// For each interface implemented by the class,
		for (Class<?> interf : clazz.getInterfaces()) {
			
			// Matches its name against the nested pattern
			if (classNameMatches(nestedPattern, interf.getName())) {
				return true;
			}
		}
		
		// No match
		return false;
	}
	
}
