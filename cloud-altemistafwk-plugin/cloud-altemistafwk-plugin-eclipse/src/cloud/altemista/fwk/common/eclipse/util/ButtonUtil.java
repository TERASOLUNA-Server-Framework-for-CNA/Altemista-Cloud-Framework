/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

/**
 * Utility class for Button (UI)
 * @author NTT DATA
 */
public class ButtonUtil {
	
	/**
	 * Convenience method to <code>setSelection</code> of a Button and trigger the appropriate listeners
	 * @param button Button
	 * @param selected boolean
	 */
	public static void setSelection(Button button, boolean selected) {
		
		// (sanity check)
		if (button == null) {
			return;
		}
		
		// Change the button state and trigger the appropriate listeners
		button.setSelection(selected);
		button.notifyListeners(SWT.Selection, new Event());
	}
	
	/**
	 * Convenience method to <code>setSelection</code> of a Button and trigger the appropriate listeners
	 * @param button Button
	 * @param selected boolean
	 */
	public static void setSelectionIfNot(Button button, boolean selected) {
		
		// (sanity check)
		if (button == null) {
			return;
		}
		
		// The button is already in the desired state: do nothing
		if (button.getSelection() == selected) {
			return;
		}
		
		// Change the button state and trigger the appropriate listeners
		button.setSelection(selected);
		button.notifyListeners(SWT.Selection, new Event());
	}
	
	/**
	 * Private default constructor (utilty class)
	 */
	private ButtonUtil() {
		super();
	}

}
