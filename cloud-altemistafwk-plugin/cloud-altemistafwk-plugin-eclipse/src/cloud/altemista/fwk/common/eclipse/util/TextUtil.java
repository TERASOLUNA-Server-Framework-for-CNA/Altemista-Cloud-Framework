/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.core.util.InputValidator;

/**
 * Utility class for Text (UI)
 * @author NTT DATA
 */
public class TextUtil {
	
	/**
	 * Sets the value of the text field, triggering its listeners
	 * @param text Text
	 * @param value the value to set
	 */
	public static void set(Text text, String value) {
		
		// (sanity check)
		if (text == null) {
			return;
		}
		
		text.setText(StringUtils.defaultString(value));
	}
	
	/**
	 * Sets the value of the text field if it is not the current value
	 * @param text Text
	 * @param value the value to set
	 * @return the previous value of the Text if changed; null otherwise
	 */
	public static String setIfNot(Text text, String value) {
		
		return setIfNot(text, value, false);
	}
	
	/**
	 * Sets the value of the text field if it is not the current value,
	 * allowing to keep the cursor position (to modify the value while the user is typing)
	 * @param text Text
	 * @param pValue the value to set
	 * @param keepCursor flag 
	 * @return the previous value of the Text if changed; null otherwise
	 */
	public static String setIfNot(Text text, String pValue, boolean keepCursor) {
		
		// (sanity check)
		if (text == null) {
			return null;
		}
		
		String originalValue = StringUtils.defaultString(text.getText());
		String value = StringUtils.defaultString(pValue);
		if (originalValue.equals(value)) {
			return null;
		}
		
		text.setText(value);
		if (keepCursor) {
			text.setSelection(StringUtils.indexOfDifference(value, originalValue));
		}
		return originalValue;
	}
	
	/**
	 * If the value of the text field is the replaceable value, sets the new value
	 * @param text Text
	 * @param pReplaceableValue the value to replace
	 * @param pValue the value to set
	 */
	public static void replaceValue(Text text, String pReplaceableValue, String pValue) {
		
		// (sanity check)
		if (text == null) {
			return;
		}

		String originalValue = StringUtils.defaultString(text.getText());
		String replaceableValue = (pReplaceableValue == null) ? "" : pReplaceableValue;
		if (!originalValue.equals(replaceableValue)) {
			return;
		}
		
		setIfNot(text, pValue);
	}
	
	/**
	 * Cleans the value, while it is still being written, using an InputValidator
	 * @param text Text
	 * @param validator the InputValidator to use
	 */
	public static void cleanUsing(Text text, InputValidator validator) {
		
		// (sanity check)
		if ((text == null) || (validator == null)) {
			return;
		}
		
		String originalValue = StringUtils.defaultString(text.getText());
		String value = validator.clean(originalValue);
		if (StringUtils.equals(value, originalValue)) {
			return;
		}
		
		text.setText(value);
		text.setSelection(StringUtils.indexOfDifference(value, originalValue));
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private TextUtil() {
		super();
	}

}
