/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.wizard.page;

import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * Convenient base class for building wizard pages using ComponentBuilder (and other improvements)
 * @author NTT DATA
 */
public abstract class AbstractWizardPage extends WizardPage {
	
	/** Simple enumeration to know in which state the page is to avoid double updates */
	private static enum Status {
		
		/** Constructor has been invoked */ 
		CONSTRUCTED,
		
		/** buildControls() has been invoked */
		CONTROL_CREATED,
		
		/** initializePage() has been invoked */
		INITIALIZED,
		
		/**
		 * After updatePage() has been invoked for the first,
		 * or when subsequent invocations of updatePage() have finished
		 */ 
		READY,
		
		/** Around the updatePage() invocation from triggerUpdatePage() */
		UPDATING
	};
	
	/** The updatePageListener */
	private UpdatePageListener updatePageListener = new UpdatePageListener();
	
	/** State of the page, to avoid double updates */
	private Status status;
	
	/**
	 * Constructor
	 * @param pageName the name of the page
	 */
	public AbstractWizardPage(String pageName) {
		super(pageName);
		
		this.status = Status.CONSTRUCTED;
	}
	
	/**
	 * Constructor
	 * @param pageName the name of the page
	 * @param title the title for this wizard page
	 */
	public AbstractWizardPage(String pageName, String title) {
		this(pageName);
		
		this.setTitle(title);
	}
	
	/**
	 * Constructor
	 * @param pageName the name of the page
	 * @param title the title for this wizard page
	 * @param description the description text for this dialog page
	 */
	public AbstractWizardPage(String pageName, String title, String description) {
		this(pageName, title);
		
		this.setDescription(description);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		// Scrollable container, so the contents of the wizard can be larger than the window itself
		ScrolledComposite scrollableContainer = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrollableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrollableContainer.setExpandVertical(false);
		scrollableContainer.setExpandHorizontal(true);
		
		// Actual container
		Composite container = new Composite(scrollableContainer, SWT.NONE);
		
		// Builds the page
		ComponentBuilder builder = new ComponentBuilder(container);
		this.buildControls(builder);
		
		// Makes the page aware of the changes of its controls
		this.setUpdatePageListener(container);
		
		// Sets the initial value
		this.initializePage();
		this.status = Status.INITIALIZED;
		
		// Updates the page according the initial values
		this.updatePage();
		this.validatePage();
		this.status = Status.READY;
		
		// (avoids initial error message, but keeps the pageComplete set by updatePage)
		if (!this.isPageComplete()) {
			this.setError(null);
		}

		// Joins the scrollable container and the actual container 
		scrollableContainer.setContent(container);
		Point point = container.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		container.setSize(point);
		scrollableContainer.setMinSize(point);
		
		// Sets the scrollable container as the contents of the page
		this.setControl(scrollableContainer);
	}
	
	/**
	 * Convenience method to create the page using <code>ComponentBuilder</code>,
	 * invoked by <code>createControl()</code>
	 * @param builder ComponentBuilder
	 */
	protected abstract void buildControls(ComponentBuilder builder);
	
	/**
	 * Makes the page aware of the changes of its controls
	 * @param composite Composite whose children will invoke updatePageStatus on their listeners
	 */
	private void setUpdatePageListener(Composite composite) {
		
		// (sanity check)
		if (composite == null) {
			return;
		}
		
		// For each child control of the composite
		this.setUpdatePageListener(composite.getChildren());
	}
	
	
	/**
	 * Makes the page aware of the changes of its controls
	 * @param controls array of controls that will invoke updatePageStatus on their listeners
	 */
	private void setUpdatePageListener(Control[] controls) {
	
		// For each control
		for (Control control : controls) {
			
			// Texts
			if (control instanceof Text) {
				Text text = (Text) control;
				text.addModifyListener(this.updatePageListener);
			
			// Combo boxes
			} else if (control instanceof Combo) {
				Combo combo = (Combo) control;
				combo.addModifyListener(this.updatePageListener);
				combo.addSelectionListener(this.updatePageListener);
			
			// Check boxes
			} else if (control instanceof Button) {
				Button button = (Button) control;
				if ((button.getStyle() & SWT.CHECK) != 0) {
					button.addSelectionListener(this.updatePageListener);
				}
			
			// Tables
			} else if (control instanceof Table) {
				Table table = (Table) control;
				table.addSelectionListener(this.updatePageListener);
			
			// Groups
			} else if (control instanceof Composite) {
				Composite nestedComposite = (Composite) control;
				this.setUpdatePageListener(nestedComposite);
			}
		}
	}
	
	/**
	 * Convenience method to implement the initialization of the page values
	 */
	protected void initializePage() {
		
		// (default empty implementation)
	}
	
	/**
	 * Convenience method to implement the update of the page values
	 */
	protected void updatePage() {
		
		// (default empty implementation)
	}
	
	/**
	 * Convenience method to implement the validation of the page values, once they have been updated
	 */
	protected void validatePage() {
		
		// (the default implementation simply invokes setOk())
		this.setOk();
	}
	
	/**
	 * Convenience method to set the error message
	 * @param message String with the error message
	 */
	protected void setError(String message) {
		
		this.setErrorMessage(message);
		this.setPageComplete(false);
	}
	
	/**
	 * Convenience method to remove any error message and mark the page as completed
	 */
	protected void setOk() {
		
		this.setErrorMessage(null);
		this.setPageComplete(true);
	}
	
	/**
	 * Convenience class that implements ModifyListener and SelectionListener and that triggers updatePageStatus()
	 * @author NTT DATA
	 */
	private class UpdatePageListener extends SelectionAdapter implements Listener, ModifyListener, SelectionListener {

		/* (non-Javadoc)
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		@Override
		public void handleEvent(Event e) {
			
			// (Tables of check boxes)
			if (e.detail == SWT.CHECK) {
				this.updateAndValidatePageOnce();
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		@Override
		public void modifyText(ModifyEvent e) {
			
			// (Text and Combo boxes)
			this.updateAndValidatePageOnce();
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			
			// (Combo and check boxes)
			this.updateAndValidatePageOnce();
		}
		
		/**
		 * Conditionally updates the page if it is not already getting updating
		 */
		private void updateAndValidatePageOnce() {
			
			if (AbstractWizardPage.this.status == Status.READY) {
				AbstractWizardPage.this.status = Status.UPDATING;
				try {
					AbstractWizardPage.this.updatePage();
					AbstractWizardPage.this.validatePage();
					
				} finally {
					AbstractWizardPage.this.status = Status.READY;
				}
			}
		}
	}
}
