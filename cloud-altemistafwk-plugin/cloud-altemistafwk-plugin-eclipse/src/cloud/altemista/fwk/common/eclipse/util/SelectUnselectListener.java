/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

/**
 * SelectionListener implementation to select/unselect buttons (usually, check boxes)
 * depending on the selection of a button (usually, a check box)
 * @author NTT DATA
 */
public class SelectUnselectListener extends SelectionAdapter implements SelectionListener {
	
	/**
	 * Adds a new SelectUnselectListener as SelectionListener on a button
	 * @param button the Button
	 * @return the added SelectUnselectListener
	 */
	public static SelectUnselectListener on(Button button) {
		
		SelectUnselectListener listener = new SelectUnselectListener(button);
		button.addSelectionListener(listener);
		return listener;
	}
	
	/** The button this listener is listening */
	private final Button button;
	
	/** The buttons to select when this button is selected */
	private final List<Button> buttonsToSelect = new ArrayList<Button>();
	
	/** The buttons to unselect when this button is selected */
	private final List<Button> buttonsToUnselect = new ArrayList<Button>();
	
	/**
	 * Constructor
	 * @param button the button this listener is listening
	 * @see #on(Button)
	 */
	private SelectUnselectListener(Button button) {
		super();
		
		this.button = button;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		super.widgetSelected(e);
		
		this.selectUnselectControls();
	}
	
	/**
	 * Enables and disables the dependant controls
	 */
	private void selectUnselectControls() {
		
		final boolean selected = this.button.getSelection();
		
		if (selected) {
			// The controls to be selected if the button is selected
			for (Button button : this.buttonsToSelect) {
				button.setSelection(true);
			}
			// The controls to be unselected if the button is selected
			for (Button button : this.buttonsToUnselect) {
				button.setSelection(false);
			}
		}
	}
	
	/**
	 * Adds a button to be selected when this button is selected
	 * @param button the Button
	 * @return this SelectUnselectListener
	 */
	public SelectUnselectListener select(Button button) {
		
		if (button != null) {
			this.buttonsToSelect.add(button);
		}
		
		return this;
	}
	
	/**
	 * Adds a button to be unselected when this button is selected
	 * @param button the Button
	 * @return this SelectUnselectListener
	 */
	public SelectUnselectListener unselect(Button button) {
		
		if (button != null) {
			this.buttonsToUnselect.add(button);
		}
		
		return this;
	}
}
