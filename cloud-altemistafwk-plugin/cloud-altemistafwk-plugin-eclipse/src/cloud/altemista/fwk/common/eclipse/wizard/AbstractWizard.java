package cloud.altemista.fwk.common.eclipse.wizard;

import java.lang.reflect.InvocationTargetException;

import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Convenient base class for building Wizards
 * @author NTT DATA
 */
public abstract class AbstractWizard extends Wizard implements IWorkbenchWizard {
	
	/** Workbench */
	protected IWorkbench workbench;
	
	/** Workbench selection when the wizard was started */
	protected IStructuredSelection selection;
	
	/**
	 * Constructor for AbstractWizard.
	 */
	public AbstractWizard() {
		super();
		
		this.setNeedsProgressMonitor(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		this.workbench = workbench;
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		IRunnableWithProgress readValues = this.prepareFinishRunnable();
		
		// Executes the runnable with progress
		try {
			this.getContainer().run(true, false, readValues);
			return true;
			
		} catch (InterruptedException e) {
			EclipseExceptionHandler.handle(this.getShell(), e);
			return false;
			
		} catch (InvocationTargetException e) {
			EclipseExceptionHandler.handle(this.getShell(), e);
			return false;
		}
	}
	
	/**
	 * Reads the value(s) from the wizard pages and instantiates the proper IRunnableWithProgress object
	 * @return IRunnableWithProgress
	 * @see FinishRunnable
	 */
	protected abstract IRunnableWithProgress prepareFinishRunnable();
	
	/**
	 * Returns the selected project
	 * @return IProject
	 */
	protected IProject getProject() {
		
		return (IProject) this.selection.getFirstElement();
	}
	
	/**
	 * Convenience base class for runnable with progress that will actually perform the finish operation
	 * @author NTT DATA
	 */
	protected abstract class FinishRunnable implements IRunnableWithProgress {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			// Convert the given monitor into a progress instance
			SubMonitor progress = SubMonitor.convert(monitor, 100);
			try {
				this.doFinish(progress);

			} catch (Exception e) {
				throw new InvocationTargetException(e);

			} finally {
				SubMonitor.done(monitor);
			}
		}
		
		/**
		 * The method that actually performs the finish operation.
		 * This method can not read values from the wizard pages to avoid "SWTException: Invalid thread access"
		 * @param progress the SubMonitor
		 * @throws CoreException in case of error
		 * @see #prepareFinishRunnable()
		 */
		protected abstract void doFinish(SubMonitor progress) throws CoreException;
	};
}