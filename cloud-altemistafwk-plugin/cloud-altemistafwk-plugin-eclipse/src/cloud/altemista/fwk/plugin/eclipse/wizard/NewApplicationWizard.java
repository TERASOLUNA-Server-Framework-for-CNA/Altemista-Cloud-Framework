package cloud.altemista.fwk.plugin.eclipse.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.ui.INewWizard;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.core.PluginReaderService;
import cloud.altemista.fwk.plugin.core.PluginService;
import cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl;
import cloud.altemista.fwk.plugin.core.impl.PluginServiceImpl;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.NewApplicationDto;
import cloud.altemista.fwk.plugin.core.model.NewBusinessModuleDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;
import cloud.altemista.fwk.common.eclipse.wizard.AbstractWizard;
import cloud.altemista.fwk.plugin.eclipse.util.EclipseMavenUtil;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.AggregatorModulePage;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.InitialBusinessModulePage;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.NewApplicationPage;

/**
 * New application wizard
 * @author NTT DATA
 */
public class NewApplicationWizard extends AbstractWizard implements INewWizard {
	
	/** Plug-in service for read-only tasks related to TSF+ applications */
	protected final PluginReaderService pluginReaderService;

	/** Plug-in service */
	private final PluginService service;

	/** The "New application main attributes" page */
	private NewApplicationPage applicationPage;
	
	/** The "Aggregator project of the application" page */
	private AggregatorModulePage aggregatorModulePage;
	
	/** The "Initial business module" page */
	private InitialBusinessModulePage businessModulePage;
	
	/**
	 * Constructor
	 */
	public NewApplicationWizard() {
		super();
		
		this.pluginReaderService = new PluginReaderServiceImpl();
		
		this.service = new PluginServiceImpl();
		this.service.setMavenSettings(EclipseMavenUtil.readSettings());
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
		
		this.applicationPage = new NewApplicationPage();
		this.addPage(this.applicationPage);
		
		this.aggregatorModulePage = new AggregatorModulePage(this.applicationPage);
		this.addPage(this.aggregatorModulePage);
		
		this.businessModulePage = new InitialBusinessModulePage(this.applicationPage);
		this.addPage(this.businessModulePage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		
		// Re-fills the combo options depending on the TSF+ version 
		if ((currentPage == this.applicationPage) && currentPage.isPageComplete()) {
			
			String terasolunaPlusVersion = this.applicationPage.getTerasolunaPlusVersion();
			
			// Aggregator types 
			List<ModuleProjectTypeDto> aggregatorTypes = new ArrayList<ModuleProjectTypeDto>();
			for (ModuleProjectTypeDto projectType : Config.MODULE_PROJECTS.values(terasolunaPlusVersion)) {
				if (projectType.isAggregator()) {
					aggregatorTypes.add(projectType);
				}
				ComboUtil.setOptions(this.aggregatorModulePage.aggregatorType, aggregatorTypes, false);
			}

		}
		
		if(currentPage == aggregatorModulePage){
			String aggregatorSelected = aggregatorModulePage.getAggregatorType();
			String terasolunaPlusVersion = this.applicationPage.getTerasolunaPlusVersion();
			
	        // Check if is a TSF o Altemista version
	        List<String> listTSFVersions = Arrays.asList(Constant.TSF_VERSIONS);
			if(listTSFVersions.contains(terasolunaPlusVersion)){
				// Business module types
				ComboUtil.setOptions(this.businessModulePage.businessModuleType,
						Config.BUSINESS_MODULES.values(terasolunaPlusVersion,aggregatorSelected), false);
				}else{
				// Business module types
				ComboUtil.setOptions(this.businessModulePage.businessModuleType,
						Config.BUSINESS_MODULES_ALTEMISTA.values(terasolunaPlusVersion,aggregatorSelected), false);
				}
		}
		
		return super.getNextPage(currentPage);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard#prepareFinishRunnable()
	 */
	@Override
	protected IRunnableWithProgress prepareFinishRunnable() {
		
		// Reads the values from the wizard pages
		final String location = this.applicationPage.getLocation();
		final NewApplicationDto newApplication = this.applicationPage.getApplication();
		newApplication.setAggregatorType(this.aggregatorModulePage.getAggregatorType());
		final boolean initialBusinessModule = this.businessModulePage.isInitialBusinessModule();
		final NewBusinessModuleDto businessModule = !initialBusinessModule ? null
				: new NewBusinessModuleDto();
		final BusinessModuleTypeDto businessModuleType = (BusinessModuleTypeDto) (!initialBusinessModule ? null
				: ComboUtil.getOption(this.businessModulePage.businessModuleType));
		if (initialBusinessModule) {
			businessModule.setBusinessShortId(this.businessModulePage.getBusinessModuleName());
			businessModule.setType(businessModuleType.getValue());
		}
		
		
		// Instantiates the proper IRunnableWithProgress object
		return new FinishRunnable() {
			
			/* (non-Javadoc)
			 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard.FinishRunnable#doFinish(org.eclipse.core.runtime.SubMonitor)
			 */
			@Override
			protected void doFinish(SubMonitor progress) throws CoreException {
				
				List<MavenProjectInfo> projectsToImport = new ArrayList<MavenProjectInfo>();
				
				progress.beginTask(String.format("Creating application %s", newApplication.getArtifactId()), 100);
				
				// Creates the application using the plug-in core (i.e.: uses Maven archetype)
				progress.subTask("Multi-module project");
				ModelAndLocation applicationModel =
						NewApplicationWizard.this.service.newApplication(location, newApplication);
				progress.worked(35);
				
				// (the application and its projects should be imported afterwards)
				projectsToImport.addAll(EclipseMavenUtil.asMavenProjectInfo(applicationModel));
				progress.worked(5);
				
				// Creates the business module using the plug-in core (i.e.: uses Maven archetypes)
				if (initialBusinessModule) {
					progress.subTask(String.format("Initial business module %s", businessModule.getBusinessShortId()));
					
					// (prepares the application object to be usable by PluginService.newBusinessModule)
					ApplicationDto application = NewApplicationWizard.this.pluginReaderService.readApplication(
							new ModelAndLocation(applicationModel.getLocation()));
					application.setAggregatorProject(
							NewApplicationWizard.this.pluginReaderService.guessAggregatorProject(application));
					progress.worked(5);
					
					// Creates the business module using the plug-in core (i.e.: uses Maven archetypes)
					List<ModelAndLocation> models =
							NewApplicationWizard.this.service.newBusinessModule(application, businessModule);
					progress.worked(30);
					
					// (the projects of the business module should be imported afterwards)
					for (ModelAndLocation model : models) {
						projectsToImport.addAll(EclipseMavenUtil.asMavenProjectInfo(model));
					}
					progress.worked(5);
				}
				
				// Import the new projects into eclipse
				progress.setTaskName("Importing projects");
				try {
					progress.setWorkRemaining(20);
					MavenPlugin.getProjectConfigurationManager().importProjects(
							projectsToImport, new ProjectImportConfiguration(), progress.split(20));
					
				} catch (CoreException e) {
					// Shows the import error but continues the execution
					EclipseExceptionHandler.handle(getShell(), e);
				}
			}
		};
	}
	

}