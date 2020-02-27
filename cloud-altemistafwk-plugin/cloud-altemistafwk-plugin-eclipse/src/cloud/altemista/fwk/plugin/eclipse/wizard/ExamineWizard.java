package cloud.altemista.fwk.plugin.eclipse.wizard;

import org.eclipse.jface.operation.IRunnableWithProgress;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.common.eclipse.wizard.AbstractWizard;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.ExamineApplicationPage;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.ExamineBusinessModulesPage;

/**
 * Examine application wizard
 * @author NTT DATA
 */
public class ExamineWizard extends AbstractWizard {
	
	/** The application whose information is to be displayed */
	private final ApplicationDto application;
	
	/**
	 * Constructor for ExamineWizard.
	 * @param application The application whose information is to be displayed
	 */
	public ExamineWizard(ApplicationDto application) {
		super();
		
		this.application = application;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		
		return "Add ALTEMISTA Cloud Framework (ACF) feature";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		
		this.addPage(new ExamineApplicationPage(this.application));
		this.addPage(new ExamineBusinessModulesPage(this.application));
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		// (this wizard does nothing)
		return true;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard#prepareFinishRunnable()
	 */
	@Override
	protected IRunnableWithProgress prepareFinishRunnable() {
		
		// (unnecessary; @see #performFinish())
		return null;
	}
}