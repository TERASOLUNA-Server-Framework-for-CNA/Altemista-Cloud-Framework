/**
 * 
 */
package cloud.altemista.fwk.plugin.core.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for string manipulation within the plug-in 
 * @author NTT DATA
 */
public class StringUtil {
	
	/**
	 * Convenience method to invoke String.format() only if all the args are not null and not empty 
	 * @param format a format String
	 * @param args arguments referenced by the format specifiers in the format String
	 * @return the formatted string or the empty string
	 */
	public static String formatNullSafe(String format, String... args) {
		
		if (args == null) {
			return String.format(format);
		}
		
		for (String fragment : args) {
			if (StringUtils.isEmpty(fragment)) {
				return StringUtils.EMPTY;
			}
		}
		
		return String.format(format, (Object[]) args);
	}
	
	/**
	 * Returns the passed value if it is between the valid values, otherwise returns the defaultValue
	 * @param value the value to check against the valid values
	 * @param defaultValue the default value if the value is not valid
	 * @param validValues the valid values
	 * @return the valid value or the default value
	 */
	public static String firstValid(String value, String defaultValue, String... validValues) {
		
		if (validValues == null) {
			return defaultValue;
		}
		
		for (String validValue : validValues) {
			if (StringUtils.equals(value, validValue)) {
				return value;
			}
		}
		
		return defaultValue;
	}
	
	/**
	 * Returns the passed value if it is not between the invalid values; returns the defaultValue otherwise
	 * @param value the value to check against the invalid values
	 * @param defaultValue the default value if the value is not valid
	 * @param invalidValues the invalid values
	 * @return the not invalid value or the default value
	 */
	public static String defaultIf(String value, String defaultValue, String... invalidValues) {
		
		if (invalidValues == null) {
			return value;
		}
		
		for (String invalidValue : invalidValues) {
			if (StringUtils.equals(value, invalidValue)) {
				return defaultValue;
			}
		}
		
		return value;
	}
	
	/**
	 * Returns the common prefix to the Strings, word-boundary-wise
	 * (i.e.: stripping the last alphanumeric characters so the common prefix does not include a split words)
	 * @param strings array of Strings
	 * @return the common prefix or the empty String
	 */
	public static String getCommonPrefix(String... strings) {

		// Common prefix
		final String commonPrefix = StringUtils.getCommonPrefix(strings);
		
		// Strips the last alphanumeric characters
		for (int length = commonPrefix.length(); length > 0; length--) {
			if (!CharUtils.isAsciiAlphanumeric(commonPrefix.charAt(length -1))) {
				return StringUtils.left(commonPrefix, length);
			}
		}
		
		return StringUtils.EMPTY;
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private StringUtil() {
		super();
	}
}
