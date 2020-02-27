/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Combo;
import cloud.altemista.fwk.plugin.config.model.OptionDto;

/**
 * Utility class for Combo (UI)
 * @author NTT DATA
 */
public class ComboUtil {
	
	/** Convenience constant for no items */
	private static final String[] NO_ITEMS = new String[0];

	/**
	 * Sets the options for the Combo
	 * @param combo Combo
	 * @param options Collection of Options
	 * @param defaultSelection true if the first option should be automatically selected
	 */
	public static void setOptions(Combo combo, Collection<? extends OptionDto> options, boolean defaultSelection) {
		
		// (sanity check)
		if (combo == null) {
			return;
		}
		
		// (fail fast)
		if ((options == null) || (options.isEmpty())) {
			combo.setItems(NO_ITEMS);
			combo.deselectAll();
			return;
		}
		
		String currentValue = getItem(combo);
		Integer keepSelectionIndex = null; 
		int i = 0;
		String[] items = new String[options.size()];
		for (OptionDto option : options) {
			String value = StringUtils.defaultIfEmpty(option.getText(), option.getValue());
			if (StringUtils.equals(currentValue, value)) {
				keepSelectionIndex = i;
			}
			items[i++] = value;
			combo.setData(value, option);
		}
		combo.setItems(items);
		if (keepSelectionIndex != null) {
			combo.select(keepSelectionIndex);
		} else if ((items.length != 0) && defaultSelection) {
			combo.select(0);
		} else {
			combo.deselectAll();
		}
	}
	
	/**
	 * Changes the selected value of the combo if it is not the selected value
	 * @param combo Combo
	 * @param pValue the value to select
	 * @return the previous value of the Combo if changed; null otherwise
	 */
	public static String setItemIfNot(Combo combo, String pValue) {
		
		// (sanity check)
		if (combo == null) {
			return null;
		}
		
		// Checks the original value
		String originalValue = getItem(combo);
		String value = StringUtils.defaultString(pValue);
		if (StringUtils.equals(originalValue, value)) {
			return null;
		}
		
		// Sets the new value
		String[] items = combo.getItems();
		for (int i = 0, n = items.length; i < n; i++) {
			String item = items[i];
			if (StringUtils.equals(item, value)) {
				combo.select(i);
				return originalValue;
			}
		}
		
		// The chosen value is not between the options of the combo 
		combo.deselectAll();
		return originalValue;
	}
	
	/**
	 * Gets the selected value
	 * @param combo Combo
	 * @return String with the selected value or the empty string
	 */
	public static String getItem(Combo combo) {
		
		int index = combo.getSelectionIndex();
		if (index == -1) {
			return "";
		}
		return combo.getItem(index);
	}
	
	/**
	 * Gets the selected option
	 * @param combo Combo
	 * @return OptionDto with the selected option or null
	 */
	@SuppressWarnings("unchecked")
	public static <T extends OptionDto> T getOption(Combo combo) {
		
		String item = getItem(combo);
		if (StringUtils.isEmpty(item)) {
			return null;
		}
		
		return (T) combo.getData(item);
	}
	
	/**
	 * Gets the selected value, validated against a collection of Options
	 * @param combo Combo
	 * @return String with the validated selected value
	 */
	public static String getOptionValue(Combo combo) {
		
		OptionDto option = getOption(combo);
		if (option == null) {
			return null;
		}
		
		return option.getValue();
	}
	
	/**
	 * Private default constructor (utilty class)
	 */
	private ComboUtil() {
		super();
	}
}
