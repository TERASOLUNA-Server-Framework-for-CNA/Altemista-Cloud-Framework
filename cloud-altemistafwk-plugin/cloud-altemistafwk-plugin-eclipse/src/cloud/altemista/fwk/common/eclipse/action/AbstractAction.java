package cloud.altemista.fwk.common.eclipse.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;

/**
 * Convenient base class for building Actions that invokes Wizards
 * @author NTT DATA
 */
public abstract class AbstractAction implements IObjectActionDelegate {

	/** The shell */
	private Shell shell;
	
	/** The window */
	private IWorkbenchWindow window;
	
	/** The selection */
	private IStructuredSelection selection;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		
		this.window = targetPart.getSite().getWorkbenchWindow();
		this.shell = this.window.getShell();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		
		try {
			IWorkbenchWizard wizard = this.createWizard();
			
			// Creates the wizard
			wizard.init(this.window.getWorkbench(), this.selection);
			
			WizardDialog dialog = new WizardDialog(this.shell, wizard);
			dialog.updateSize();
			dialog.open();
			
		} catch (PluginException e) {
			EclipseExceptionHandler.handle(this.shell, e);
			
		} catch (Exception e) {
			EclipseExceptionHandler.handle(this.shell, e);
		}
	}
	
	/**
	 * Creates the wizard this action will use
	 * @return IWorkbenchWizard
	 */
	protected abstract IWorkbenchWizard createWizard();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
		this.selection = (selection instanceof IStructuredSelection) ? (IStructuredSelection) selection : null;
	}
	
	/**
	 * Returns the selected project
	 * @return IProject
	 */
	protected IProject getProject() {
		
		Object object = this.selection.getFirstElement();
		
		// The object is an IProject from Project Explorer
		if (object instanceof IProject) {
			return (IProject) object;
		}
		
		// The object is a IJavaProject from Package Explorer (default view of Spring perspective) 
		if (object instanceof IJavaProject) {
			return ((IJavaProject) object).getProject();
		}
		
		throw new PluginException(
				"Unexpected item selected",
				"The type of the selected item is not recognized; use the Project Explorer or the Package Explorer",
				"The selected item is neither an IProject nor an IJavaProject");
	}
}
