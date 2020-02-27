/**
 * 
 */
package cloud.altemista.fwk.common.eclipse.util;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 * Utility class for Dialogs (UI)
 * @author NTT DATA
 */
public class DialogUtil {
	
	/**
	 * Opens a FileDialog
	 * @param parent WizardPage
	 * @param extensions to filter
	 * @return String with the selection of the dialog or null
	 */
	public static String fileDialog(WizardPage parent, String[] extensions) {
		
		return fileDialog(parent, null, extensions);
	}
	
	/**
	 * Opens a FileDialog
	 * @param parent WizardPage
	 * @param filename The initial path to show on the dialog and file to show selected
	 * @param extensions to filter
	 * @return String with the selection of the dialog or null
	 */
	public static String fileDialog(WizardPage parent, String filename, String[] extensions) {
		
		FileDialog dialog = new FileDialog(parent.getShell());
		
		String normalizedFilename = FilenameUtils.normalize(filename);
		if (normalizedFilename != null) {
			String path = FilenameUtils.getPath(normalizedFilename);
			if (path != null) {
				dialog.setFilterPath(path);
			}
			String file = FilenameUtils.getName(normalizedFilename);
			if (file != null) {
				dialog.setFileName(file);
			}
		}
		
		if (extensions != null) {
			dialog.setFilterExtensions(extensions);
		}
		
		return dialog.open();
	}
	
	/**
	 * Opens a DirectoryDialog with no initial path
	 * @param parent WizardPage
	 * @return String with the selection of the dialog or null
	 */
	public static String folderDialog(WizardPage parent) {
		
		return folderDialog(parent, null);
	}
	
	/**
	 * Opens a DirectoryDialog
	 * @param parent WizardPage
	 * @param filename The initial path to show on the dialog
	 * @return String with the selection of the dialog or null
	 */
	public static String folderDialog(WizardPage parent, String filename) {
		
		DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
		
		String path = FilenameUtils.getPath(filename);
		if (path != null) {
			dialog.setFilterPath(path);
		}
		
		return dialog.open();
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private DialogUtil() {
		super();
	}

	/**
	 * SelectionListener that links a Text box and a FileDialog
	 * @author NTT DATA
	 */
	public static class FileDialogSelectionListener extends SelectionAdapter implements SelectionListener {

		/** The parent */
		private final WizardPage parent;
		
		/** The text box */
		private final Text text;
		
		/** The extension to filter */
		private final String[] extensions;
		
		/**
		 * Constructor
		 * @param parent WizardPage
		 * @param text Text
		 * @param extensions String[]
		 */
		public FileDialogSelectionListener(WizardPage parent, Text text, String[] extensions) {
			super();
			
			this.parent = parent;
			this.text = text;
			this.extensions = extensions;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			
			// Initial path from the text box
			String filename = this.text.getText();
			if ((filename == null) || filename.isEmpty()) {
				filename = null;
			}
			
			// New value from the FileDialog
			filename = fileDialog(this.parent, this.extensions);
			if (filename != null) {
				TextUtil.setIfNot(this.text, filename);
			}
		}
	}

	/**
	 * SelectionListener that links a Text box and a DirectoryDialog
	 * @author NTT DATA
	 */
	public static class FolderDialogSelectionListener extends SelectionAdapter implements SelectionListener {

		/** The parent */
		private final WizardPage parent;
		
		/** The text box */
		private final Text text;
		
		/**
		 * Constructor
		 * @param parent WizardPage
		 * @param text Text
		 */
		public FolderDialogSelectionListener(WizardPage parent, Text text) {
			super();
			
			this.parent = parent;
			this.text = text;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			
			// Initial path from the text box
			String path = this.text.getText();
			if ((path == null) || path.isEmpty()) {
				path = null;
			}
			
			// New value from the DirectoryDialog
			path = folderDialog(this.parent, path);
			if (path != null) {
				TextUtil.setIfNot(this.text, path);
			}
		}
	}
	
}
