/**
 * 
 */
package cloud.altemista.fwk.plugin.eclipse.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.model.OptionDto;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.TextUtil;

/**
 * SelectionListener implementation for OptionDto-based combo boxes
 * to show the description of the selected option in a text (usually, a read-only text area) 
 * @author NTT DATA
 */
public class DescriptionListener extends SelectionAdapter implements SelectionListener {

	/**
	 * Adds a new DescriptionListener as SelectionListener on a combo box
	 * @param combo the OptionDto-based combo box
	 * @param description the text that will show the description
	 * @return the added DescriptionListener
	 */
	public static DescriptionListener on(Combo combo, Text description) {
		
		DescriptionListener listener = new DescriptionListener(combo, description);
		combo.addSelectionListener(listener);
		return listener;
	}
	
	/** The combo box this listener is listening */
	private final Combo combo;
	
	/** The text that will show the description */
	private final Text description;
	
	/**
	 * Constructor
	 * @param combo the OptionDto-based combo box
	 * @param description the text that will show the description
	 */
	private DescriptionListener(Combo combo, Text description) {
		super();
		
		this.combo = combo;
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		super.widgetSelected(e);
		
		this.refreshDescription();
	}

	/**
	 * Shows the description of the selected option
	 */
	public void refreshDescription() {
		
		String descriptionValue = StringUtils.EMPTY;
		
		Object o = ComboUtil.getOption(combo);
		if (o instanceof OptionDto) {
			OptionDto option = (OptionDto) o;
			descriptionValue = StringUtils.defaultString(option.getDescription(), option.getText());
		}
		
		TextUtil.setIfNot(this.description, descriptionValue);
	}
}
