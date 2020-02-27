package cloud.altemista.fwk.plugin.eclipse.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.core.PluginService;
import cloud.altemista.fwk.plugin.core.impl.PluginServiceImpl;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.NewBusinessModuleDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;
import cloud.altemista.fwk.common.eclipse.wizard.AbstractWizard;
import cloud.altemista.fwk.plugin.eclipse.util.EclipseMavenUtil;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.BusinessModulePage;

/**
 * New business module wizard
 * @author NTT DATA
 */
public class NewBusinessModuleWizard extends AbstractWizard {
	
	/** Plug-in service */
	private final PluginService service;
	
	/** The application where the business module is to be added */
	private final ApplicationDto application;
	
	/** The "Business module" page */
	private BusinessModulePage businessModulePage;
	
	/**
	 * Constructor for NewTerasolunaProjectWizard.
	 * @param application The application where the business module is to be added 
	 */
	public NewBusinessModuleWizard(ApplicationDto application) {
		super();
		
		this.service = new PluginServiceImpl();
		this.service.setMavenSettings(EclipseMavenUtil.readSettings());
		
		this.application = application;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle() {
		
		return "New ALTEMISTA Cloud Framework (ACF) application";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		
		this.businessModulePage = new BusinessModulePage(this.application);
		this.addPage(this.businessModulePage);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard#prepareFinishRunnable()
	 */
	@Override
	protected IRunnableWithProgress prepareFinishRunnable() {
		
		// Reads the values from the wizard pages
		final NewBusinessModuleDto businessModule = new NewBusinessModuleDto();
		businessModule.setBusinessShortId(this.businessModulePage.getBusinessModuleName());
		final BusinessModuleTypeDto businessModuleType =
				ComboUtil.getOption(this.businessModulePage.businessModuleType);
		businessModule.setType(businessModuleType.getValue());
		
		// Instantiates the proper IRunnableWithProgress object
		return new FinishRunnable() {
			
			/* (non-Javadoc)
			 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard.FinishRunnable#doFinish(org.eclipse.core.runtime.SubMonitor)
			 */
			@Override
			protected void doFinish(SubMonitor progress) throws CoreException {
				
				List<MavenProjectInfo> projectsToImport = new ArrayList<MavenProjectInfo>();
				
				progress.beginTask(
						String.format("Creating business module %s", businessModule.getBusinessShortId()), 100);
				
				// Creates the application using the plug-in core (i.e.: uses Maven archetypes)
				progress.subTask("Module projects");
				List<ModelAndLocation> models = NewBusinessModuleWizard.this.service.newBusinessModule(
						NewBusinessModuleWizard.this.application, businessModule);
				SubMonitor loopProgress = progress.split(80).setWorkRemaining(models.size());
				for (ModelAndLocation model : models) {
					projectsToImport.addAll(EclipseMavenUtil.asMavenProjectInfo(model));
					loopProgress.worked(1);
				}
				
				// Import the new projects into eclipse
				progress.subTask("Importing projects");
				try {
					MavenPlugin.getProjectConfigurationManager().importProjects(projectsToImport,
							new ProjectImportConfiguration(), progress.split(20));
					
				} catch (CoreException e) {
					// Shows the import error but continues the execution
					EclipseExceptionHandler.handle(getShell(), e);
				}
			}
		};
	}
}