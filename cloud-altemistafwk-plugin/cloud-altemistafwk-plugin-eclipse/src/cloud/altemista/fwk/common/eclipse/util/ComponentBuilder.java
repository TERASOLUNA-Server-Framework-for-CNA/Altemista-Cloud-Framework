package cloud.altemista.fwk.common.eclipse.util;

import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

/**
 * Convenience class to build visually-pleasant and homogeneous containers (i.e.: page)
 * @author NTT DATA
 */
public class ComponentBuilder {

	/** Default number of columns */
	private static final int DEFAULT_NUM_COLUMNS = 3;

	/** The container */
	private Composite container;
	
	/** The GridLayour used in the container */
	private GridLayout layout;
	
	/** The current row of controls */
	private java.util.List<Control> currentRow = new ArrayList<Control>();
	
	/**
	 * Initial constructor with the default number of columns
	 * @param container Composite
	 */
	public ComponentBuilder(Composite container) {
		this(container, DEFAULT_NUM_COLUMNS);
	}
	
	/**
	 * Initial constructor
	 * @param container Composite
	 * @param numColumns the number of columns
	 */
	public ComponentBuilder(Composite container, int numColumns) {
		super();
		
		this.container = container;
		
		this.layout = new GridLayout(numColumns, false);
		this.container.setLayout(this.layout);
	}
	
	/**
	 * Nested container constructor, for groups
	 * @param group nested Group
	 * @param parentBuilder the parent ComponentBuilder
	 */
	private ComponentBuilder(Composite group, ComponentBuilder parentBuilder) {
		super();
		
		this.container = group;
		this.layout = parentBuilder.layout;
		this.container.setLayout(this.layout);
	}
	
	/**
	 * Process the current row of Controls to justify them
	 */
	public void finishRow() {
		
		// No elements; do nothing
		if (this.currentRow.isEmpty()) {
			return;
		}
		
		// Looks for the best candidate to span horizontally
		Control candidate = null;
		for (int i = this.currentRow.size() - 1; i >= 0; i--) {
			Control control = this.currentRow.get(i);
			
			// (if there is a combo or a text, then is preferred to other control)
			if ((control instanceof Combo) || (control instanceof Text)) {
				candidate = control;
				break;
			}
			
			// (the rightmost control is the initial candidate)
			if (candidate == null) {
				candidate = control;
			}
		}
		
		if (candidate != null) {
			
			// Calculates the gap to fill (reuses the gridData if possible)
			GridData gridData = ObjectUtils.defaultIfNull(
					(GridData) candidate.getLayoutData(), new GridData(GridData.FILL_HORIZONTAL));
			int difference = this.currentRow.size() % this.layout.numColumns;
			gridData.horizontalSpan = (difference != 0)
					? this.layout.numColumns - difference + 1
					: 1; // (no horizontalSpan, but FILL_HORIZONTAL anyway)
			
			// Spans horizontally the best candidate found
			candidate.setLayoutData(gridData);
		}
		
		this.currentRow.clear(); // (for the next row)
	}
	
	/**
	 * Finishes the current row and returns the container
	 * @return Composite
	 */
	public Composite getContainer() {
		
		this.finishRow();
		return this.container;
	}
	
	/**
	 * Appends a Button to the container
	 * @param label String with the text of the button (e.g.: "Browse...")
	 * @return Button of type SWT.PUSH
	 */
	public Button appendButton(String label) {
		
		Button button = new Button(this.container, SWT.PUSH);
		button.setText(label);
		this.currentRow.add(button);
		return button;
		
	}
	
	/**
	 * Appends a check box to the container
	 * @param label String with the text of the check box
	 * @return Button of type SWT.CHECK
	 */
	public Button appendCheckbox(String label) {
		
		// (empty label to align the checkbox with the rest of the fields)
		this.currentRow.add(new Label(this.container, SWT.NULL));
		
		Button checkbox = new Button(this.container, SWT.CHECK);
		checkbox.setText(label);
		this.currentRow.add(checkbox);
		return checkbox;
	}
	
	/**
	 * Appends a Label and a combo box to the container
	 * @param label String with the text of the label (e.g.: "&Type:")
	 * @return Combo
	 */
	public Combo appendCombo(String label) {
		
		this.appendLabel(label);
		return this.appendCombo();
	}
	
	/**
	 * Appends a combo box to the container
	 * @return Combo
	 */
	public Combo appendCombo() {
		
		Combo combo = new Combo(this.container, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		this.currentRow.add(combo);
		return combo;
	}
	
	/**
	 * Decorates an existing control
	 * @param control The control to decorate
	 * @param decorationId String, one of FieldDecorationRegistry.DEC_*
	 * @param description The description
	 */
	public void decorateControl(Control control, String decorationId, String description) {
		
		// Center-left for buttons (namely, check boxes); top-left for combo boxes, texts, etc. 
		final int position = (control instanceof Button)
				? SWT.CENTER | SWT.LEFT
				: SWT.TOP | SWT.LEFT;
		
		ControlDecoration decoration = new ControlDecoration(control, position);
		
		decoration.setDescriptionText(description);
		decoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(decorationId).getImage());
		decoration.setShowOnlyOnFocus(false);
	}
	
	/**
	 * Appends a nested group to the container with the default number of columns
	 * @param label String with the label of the group (e.g.: "General:")
	 * @return ComponentBuilder for the nested group
	 */
	public ComponentBuilder appendGroup(String label) {
		
		return this.appendGroup(label, DEFAULT_NUM_COLUMNS);
	}
	
	/**
	 * Appends a nested group to the container
	 * @param label String with the label of the group (e.g.: "General:")
	 * @param numColumns the number of columns
	 * @return ComponentBuilder for the nested group
	 */
	public ComponentBuilder appendGroup(String label, int numColumns) {
		
		// Ensures the group is placed at the beginning of a new row
		this.finishRow();
		
		// Ensures the group will expand horizontally
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = this.layout.numColumns;
		
		// Creates the nested group
		Group group = new Group(this.container, SWT.V_SCROLL);
		group.setText(label);
		group.setLayoutData(gridData);
		
		// Returns the ComponentBuilder for the nested group
		return new ComponentBuilder(group, this);
	}
	
	/**
	 * Appends a Label to the container
	 * @param string String with the text of the label (e.g.: "&Container:")
	 * @return Label
	 */
	public Label appendLabel(String string) {
		
		Label label = new Label(this.container, SWT.NULL);
		label.setText(string);
		this.currentRow.add(label);
		return label;
	}
	
	/**
	 * Appends a Label and a read-only Text to the container that looks like a field
	 * @param label String with the text of the label (e.g.: "Detected TSF+ version:")
	 * @return read-only Text
	 */
	public Text appendReadOnlyText(String label) {
		
		return this.appendReadOnlyText(label, true);
	}
	
	/**
	 * Appends a Label and a read-only Text to the container
	 * @param label String with the text of the label (e.g.: "Detected TSF+ version:")
	 * @param fieldLike true if the Text should look like a field; false if the Text should look like a label
	 * @return read-only Text
	 */
	public Text appendReadOnlyText(String label, boolean fieldLike) {
		
		this.appendLabel(label);
		
		int style = SWT.READ_ONLY | SWT.SINGLE;
		if (fieldLike) {
			style |= SWT.BORDER;
		}
		Text text = new Text(this.container, style);
		this.currentRow.add(text);
		return text;
	}
	
	/**
	 * Appends a Label and a read-only text area to the container
	 * @param label String with the text of the label
	 * @param expectedRowCount the expected row count (for esthetic purposes) 
	 * @return read-only text area
	 */
	public Text appendReadOnlyTextArea(String label, int expectedRowCount) {
		
		this.appendLabel(label);
		
		final int maxRowCount = 4;
		final int expectedColumnCount = 96;
		
		// Will have scroll if the expected row count equals or exceeds 4 
		int style = SWT.READ_ONLY | SWT.MULTI | SWT.WRAP;
		if (expectedRowCount >= maxRowCount) {
			style |= SWT.V_SCROLL;
		}
		Text text = new Text(this.container, style);
		
		// Calculates the height of the text area
		GC gc = new GC(text);
		try {
			gc.setFont(text.getFont());
			FontMetrics fontMetrics = gc.getFontMetrics();
			int fontHeight = fontMetrics.getHeight();
			int fontWidth = fontMetrics.getAverageCharWidth();
			
			// At most 5 rows
			GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.heightHint = Math.min(expectedRowCount, maxRowCount) * fontHeight;
			gridData.widthHint = expectedColumnCount * fontWidth;
			text.setLayoutData(gridData);
			
		} finally {
			gc.dispose();
		}
		
		this.currentRow.add(text);
		return text;
	}
	
	/**
	 * Appends a Label and a Text to the container
	 * @param label String with the text of the label (e.g.: "&Container:")
	 * @return Text
	 */
	public Text appendText(String label) {
		
		this.appendLabel(label);
		return this.appendText();
	}
	
	/**
	 * Appends a Text to the container
	 * @return Text
	 */
	public Text appendText() {
		
		Text text = new Text(this.container, SWT.BORDER | SWT.SINGLE);
		this.currentRow.add(text);
		return text;
	}
	
	/**
	 * Appends a Label and a Tree to the container
	 * @param label String with the text of the label (e.g.: "&Container:")
	 * @return Tree
	 */
	public Tree appendTree(String label) {
		
		this.appendLabel(label);
		return this.appendTree();
	}
	
	/**
	 * Appends a Tree to the container
	 * @return Tree
	 */
	public Tree appendTree() {
		
		Tree tree = new Tree(this.container, SWT.BORDER | SWT.V_SCROLL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 320;
		tree.setLayoutData(gridData);
		this.currentRow.add(tree);
		return tree;
	}
}
