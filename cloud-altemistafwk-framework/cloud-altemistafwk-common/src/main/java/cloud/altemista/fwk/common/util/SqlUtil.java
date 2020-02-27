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


import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Utility class to format SQL queries
 * @author NTT DATA
 */
public final class SqlUtil {

	/** The placeholder character */
	private static final char PLACEHOLDER = '?';
	
	/** The quoted sections delimiter character */
	private static final char QUOTE = '\'';
	
	/** The quoted sections delimiter character (as string) */
	private static final String QUOTE_STRING = Character.toString(QUOTE);
	
	/** The escaped version of the quoted sections delimiter character (as string) */
	private static final String QUOTED_QUOTE_STRING = "''";

	/** Controls how the String will be printed in the formatted query */
	private static final String STRING_FORMAT = "'%s'";
	
	/** Controls how the arrays and collections will be printed in the formatted query */
	private static final String LIST_FORMAT = "(%s)";
	
	/** Controls how the unknown classes will be printed in the formatted query */
	private static final String UNKNOWN_FORMAT = "<%s>";
	
	/** Separator string between parameters */
	private static final String PARAMETERS_SEPARATOR = ", ";
	
	/** Formatter for the dates in the formatted query */
	private static final Format DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
	
	/**
	 * Private default constructor (utility class)
	 */
	private SqlUtil() {
		super();
	}

	/**
	 * Finds a placeholder in a query string
	 * @param query String
	 * @param index the start position
	 * @return the index of the next placeholder, or -1 if no match, null or empty input string or invalid initial index
	 */
	public static int findPlaceholder(String query, int index) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(query)) {
			return -1;
		}
		if ((index < 0) || (index >= query.length())) {
			return -1;
		}
		
		// Scans up to the start position to know if it is inside or outside quotes
		boolean isQuoted = false;
		int i = 0;
		for (; i < index; i++) {
			isQuoted ^= (query.charAt(i) == QUOTE);
		}
		// Scans the rest of the string looking for unquoted placeholders
		for (int n = query.length(); i < n; i++) {
			char c = query.charAt(i);
			if ((c == PLACEHOLDER) && (!isQuoted)) {
				return i;
			}
			isQuoted ^= (c == QUOTE);
		}
		
		// No placeholder found
		return -1;
	}
	
	/**
	 * Counts the number of placeholders in a query string
	 * @param query String
	 * @return the number of placeholders in the query string
	 */
	public static int countPlaceholders(String query) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(query)) {
			return 0;
		}
		
		// Scans the string looking for unquoted placeholders
		int count = 0;
		boolean isQuoted = false;
		for (int i = 0, n = query.length(); i < n; i++) {
			char c = query.charAt(i);
			if ((c == PLACEHOLDER) && (!isQuoted)) {
				count++;
			}
			isQuoted ^= (c == QUOTE);
		}
		return count;
	}
	
	/**
	 * Replaces the placeholder of a query string with the printable value of an array of parameters 
	 * @param query String
	 * @param parameters the array of parameters
	 * @return the query String with the placeholders replaced by the printable version of the parameters
	 * @see #parameterToString(Object)
	 */
	public static String replacePlaceholder(String query, List<?> parameters) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(query)) {
			return StringUtils.EMPTY;
		}
		if (CollectionUtils.isEmpty(parameters)) {
			return StringUtils.trimToEmpty(query);
		}
		
		// For each parameter
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int index;
		for (Object parameter : parameters) {
			
			// Looks for the next placeholder to replace
			index = findPlaceholder(query, begin);
			if (index < 0) {
				// No placeholder to replace
				break;
			}
			
			// Appends the query string up to the placeholder and the value of the parameter
			sb.append(query.substring(begin, index)).append(parameterToString(parameter));
			begin = index + 1;
		}
		
		// Appends the tailing part of the query string
		sb.append(query.substring(begin));
		return sb.toString();
	}
	
	/**
	 * Gets the printable version of a collection of parameters (or an parameter that is an array)
	 * @param parameters array
	 * @return String with the printable version of the collection
	 */
	public static String parametersToString(Object[] parameters) {
		
		// (sanity checks)
		if (ArrayUtils.isEmpty(parameters)) {
			return StringUtils.EMPTY;
		}
		
		// Gets the printable version of each element and joins them using the proper separator
		List<String> formattedParameters = new ArrayList<String>();
		for (Object parameter : parameters) {
			formattedParameters.add(parameterToString(parameter));
		}
		return StringUtils.join(formattedParameters, PARAMETERS_SEPARATOR);
	}
	
	/**
	 * Gets the printable version of a collection of parameters (or an parameter that is a collection)
	 * @param parameters Collection
	 * @return String with the printable version of the collection
	 */
	public static String parametersToString(Collection<?> parameters) {
		
		// (sanity checks)
		if (CollectionUtils.isEmpty(parameters)) {
			return StringUtils.EMPTY;
		}
		
		// Gets the printable version of each element and joins them using the proper separator
		List<String> formattedParameters = new ArrayList<String>();
		for (Object parameter : parameters) {
			formattedParameters.add(parameterToString(parameter));
		}
		return StringUtils.join(formattedParameters, PARAMETERS_SEPARATOR);
	}
	
	/**
	 * Gets the printable version of an parameter
	 * @param parameter Object with the parameter
	 * @return String with the printable version of the parameter
	 */
	public static String parameterToString(Object parameter) {
		
		// null values get printed as null
		if (parameter == null) {
			return "null";
		}
		
		// Arrays and collections are printed between parenthesis: ( ... )
		if (parameter.getClass().isArray()) {
			return String.format(LIST_FORMAT, parametersToString(ArrayUtil.toObjectArray(parameter)));
		}
		if (parameter instanceof Collection<?>) {
			return String.format(LIST_FORMAT, parametersToString((Collection<?>) parameter));
		}
		
		// String values are printed with single quotes: 'value'
		if ((parameter instanceof CharSequence) || (parameter instanceof Character)) {
			return String.format(STRING_FORMAT,
					StringUtils.replace(parameter.toString(), QUOTE_STRING, QUOTED_QUOTE_STRING));
		}
		
		// Date values are printed as quoted textual versions: 'yyyy-MM-dd HH:mm:ss.SSS'
		if ((parameter instanceof Date) || (parameter instanceof Calendar)) {
			return String.format(STRING_FORMAT, DATE_FORMAT.format(parameter));
		}
		
		// Primitive or primitive wrapper values are printed as is
		Class<? extends Object> parameterClass = parameter.getClass();
		if (ClassUtils.isPrimitiveOrWrapper(parameterClass)) {
			return parameter.toString();
		}
		
		// In other cases, just print the name of the class
		return String.format(UNKNOWN_FORMAT, parameterClass);
	}

}
