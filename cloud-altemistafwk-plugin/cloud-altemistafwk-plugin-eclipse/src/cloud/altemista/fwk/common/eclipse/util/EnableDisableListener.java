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
import org.eclipse.swt.widgets.Control;

/**
 * SelectionListener implementation to enable/disable controls
 * depending on the selection of a button (usually, a check box)
 * @author NTT DATA
 */
public class EnableDisableListener extends SelectionAdapter implements SelectionListener {
	
	/**
	 * Adds a new EnableDisableListener as SelectionListener on a button
	 * @param button the Button
	 * @return the added EnableDisableListener
	 */
	public static EnableDisableListener on(Button button) {
		
		EnableDisableListener listener = new EnableDisableListener(button);
		button.addSelectionListener(listener);
		return listener;
	}
	
	/** The button this listener is listening */
	private final Button button;
	
	/** The controls to enable when this button is selected */
	private final List<Control> controlsToEnable = new ArrayList<Control>();
	
	/** The controls to disable when this button is selected */
	private final List<Control> controlsToDisable = new ArrayList<Control>();
	
	/**
	 * Constructor
	 * @param button the button this listener is listening
	 * @see #on(Button)
	 */
	private EnableDisableListener(Button button) {
		super();
		
		this.button = button;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		super.widgetSelected(e);
		
		this.enableDisableControls();
	}
	
	/**
	 * Enables and disables the dependant controls
	 */
	private void enableDisableControls() {
		
		final boolean selected = this.button.getSelection();
		
		// The controls to be enabled are enabled if the button is selected
		for (Control control : this.controlsToEnable) {
			control.setEnabled(selected);
		}
		
		// The controls to be disabled are enabled if the button is not selected
		for (Control control : this.controlsToDisable) {
			control.setEnabled(!selected);
		}
	}
	
	/**
	 * Adds a control to be enabled when this button is selected and disabled otherwise
	 * @param control the Control
	 * @return this EnableDisableListener
	 */
	public EnableDisableListener enable(Control control) {
		
		if (control != null) {
			this.controlsToEnable.add(control);
		}
		
		return this;
	}
	
	/**
	 * Adds a control to be disabled when this button is selected and enabled otherwise
	 * @param control the Control
	 * @return this EnableDisableListener
	 */
	public EnableDisableListener disable(Control control) {
		
		if (control != null) {
			this.controlsToDisable.add(control);
		}
		
		return this;
	}

}
