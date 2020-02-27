/**
 * 
 */
package cloud.altemista.fwk.plugin.core.util;

import cloud.altemista.fwk.plugin.config.Constant;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for input field validations
 * @author NTT DATA
 */
public class InputValidator {
	
	/** Convenience instance to validate a Maven groupId (or a more restricted Java package) */
	public static final InputValidator GROUP_ID = new InputValidator(
			Constant.GROUP_ID_INVALID_CHARS_REGEX, Constant.GROUP_ID_REGEX, Constant.GROUP_ID_MAX_LENGTH);
	
	/** Convenience instance to validate a Maven artifactId */
	public static final InputValidator ARTIFACT_ID = new InputValidator(
			Constant.ARTIFACT_ID_INVALID_CHARS_REGEX, Constant.ARTIFACT_ID_REGEX, Constant.ARTIFACT_ID_MAX_LENGTH);
	
	/** Convenience instance to validate a Maven version */
	public static final InputValidator VERSION = new InputValidator(
			Constant.VERSION_INVALID_CHARS_REGEX, Constant.VERSION_REGEX, Constant.VERSION_MAX_LENGTH);
	
	/** Convenience instance to validate an application short ID (or a more restricted Java identifier) */
	public static final InputValidator APPSHORTID = new InputValidator(
			Constant.APPSHORTID_INVALID_CHARS_REGEX, Constant.APPSHORTID_REGEX, Constant.APPSHORTID_MAX_LENGTH);

	/** Regex that matches any invalid character */
	private final String invalidRegex;
	
	/** Regex that matches any valid final value */
	private final String finalValidRegex;
	
	/** The maximum length */
	private final int maxLength;

	/**
	 * Constructor
	 * @param invalidRegex Regex that matches any invalid character
	 * @param finalValidRegex Regex that matches any valid final value
	 * @param maxLength The maximum length
	 */
	public InputValidator(String invalidRegex, String finalValidRegex, int maxLength) {
		super();
		
		this.invalidRegex = invalidRegex;
		this.finalValidRegex = finalValidRegex;
		this.maxLength = maxLength;
	}
	
	/**
	 * Cleans the value, while it is still being written (i.e.: may return an invalid values)
	 * @param pValue the input value
	 * @return the clean input value
	 */
	public String clean(String pValue) {
		
		// (sanity check)
		String value = StringUtils.trimToNull(pValue);
		if (value == null) {
			return StringUtils.EMPTY;
		}
		
		return StringUtils.left(value.replaceAll(this.invalidRegex, StringUtils.EMPTY), this.maxLength);
	}
	
	/**
	 * Validates the value
	 * @param pValue the input value
	 * @param mandatory boolean if the field is mandatory
	 * @return true if the value is valid
	 */
	public boolean isValid(String pValue, boolean mandatory) {
		
		String value = StringUtils.trimToNull(pValue);
		if (value == null) {
			return !mandatory;
		}
		
		return value.matches(finalValidRegex) && (value.length() <= this.maxLength);
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}
}
