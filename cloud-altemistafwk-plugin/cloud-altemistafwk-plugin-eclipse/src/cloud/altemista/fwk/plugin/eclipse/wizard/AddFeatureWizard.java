package cloud.altemista.fwk.plugin.eclipse.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.m2e.core.MavenPlugin;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.util.ValueUtil;
import cloud.altemista.fwk.plugin.core.PluginService;
import cloud.altemista.fwk.plugin.core.impl.PluginServiceImpl;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.InstallFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;
import cloud.altemista.fwk.common.eclipse.wizard.AbstractWizard;
import cloud.altemista.fwk.plugin.eclipse.util.EclipseMavenUtil;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.AddFeaturePage;
import cloud.altemista.fwk.plugin.eclipse.wizard.page.FeatureImplementationPage;

/**
 * Add features wizard
 * @author NTT DATA
 */
public class AddFeatureWizard extends AbstractWizard {
	
	/** Plug-in service */
	private final PluginService service;
	
	/** The module project where the feature is to be added */
	private final ModuleProjectDto moduleProject;
	
	/** The "Features to add" page */
	private AddFeaturePage addFeaturePage;
	
	/** The "Feature implementation" pages */
	private List<FeatureImplementationPage> featureImplementationPages;
	
	/**
	 * Constructor
	 * @param moduleProject The module project where the feature is to be added 
	 */
	public AddFeatureWizard(ModuleProjectDto moduleProject) {
		super();
		
		this.service = new PluginServiceImpl();
		this.service.setMavenSettings(EclipseMavenUtil.readSettings());
		
		this.moduleProject = moduleProject;
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
	@Override
	public void addPages() {
		
		// Initial AddFeaturePage 
		this.addFeaturePage = new AddFeaturePage(this.moduleProject);
		this.addPage(this.addFeaturePage);
		
		// Creates an implementation page for each possible feature
		this.featureImplementationPages = new ArrayList<FeatureImplementationPage>();
		for (CurrentFeatureDto feature : this.moduleProject.getCurrentFeatures()) {
			
			// Discard features that do not meet the requirements, are incompatible or area already installed
			if (!feature.isMeetsRequirements() || !feature.isCompatible() || feature.isInstalled()) {
				continue;
			}
			
			FeatureImplementationPage featureImplementationPage =
					new FeatureImplementationPage(this.moduleProject, feature);
			this.addPage(featureImplementationPage);
			this.featureImplementationPages.add(featureImplementationPage);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		
		// Gets the next page from the list of active pages
		final List<IWizardPage> activePages = this.getCurrentPages();
		final int i = activePages.indexOf(currentPage);
		final int n = activePages.size();
		return ((i + 1) < n) ? activePages.get(i + 1) : null; // (no more active pages)
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		
		// Implementation overwritten to check only the active pages
		for (IWizardPage page : this.getCurrentPages()) {
			if (!page.isPageComplete()) {
				return false;
			}
		}
		
		// All the active pages are complete
		return true;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard#prepareFinishRunnable()
	 */
	@Override
	protected IRunnableWithProgress prepareFinishRunnable() {
		
		// Reads the values from the wizard pages
		final ModuleProjectDto moduleProject = this.moduleProject;
		final List<InstallFeatureDto> installFeatures = new ArrayList<InstallFeatureDto>();
		for (FeatureImplementationPage implementationPage : this.getSelectedFeatureImplementationPages()) {
			final CurrentFeatureDto feature = implementationPage.feature;
			
			// Reads the InstallFeatureDto from the wizard page
			InstallFeatureDto installFeature = new InstallFeatureDto();
			installFeature.setFeature(feature);
			installFeature.setImplementation(implementationPage.getImplementation());
			installFeature.setAdditionalProperties(implementationPage.getAdditionalProperties());
			
			installFeatures.add(installFeature);
		}
		
		// Instantiates the proper IRunnableWithProgress object
		return new FinishRunnable() {
			
			/* (non-Javadoc)
			 * @see cloud.altemista.fwk.plugin.eclipse.wizard.AbstractWizard.FinishRunnable#doFinish(org.eclipse.core.runtime.SubMonitor)
			 */
			@Override
			protected void doFinish(SubMonitor progress) throws CoreException {
				
				// (initially, no project has been modified)
				List<ModuleProjectDto> allModules = moduleProject.getApplication().getModules();
				for (ModuleProjectDto module : allModules) {
					module.setTouched(false);
				}
				
				progress.beginTask("Installing features", 100);
				
				// Installs the features using the plug-in core
				{
					SubMonitor loopProgress = progress.split(70).setWorkRemaining(installFeatures.size());
					for (InstallFeatureDto installFeature : installFeatures) {
						loopProgress.subTask(installFeature.getFeature().getText());
						AddFeatureWizard.this.service.install(moduleProject, installFeature);
						loopProgress.worked(1);
					}
				}
				
				progress.subTask("Updating projects");
				
				// Builds the list of projects modified by the plug-in
				List<IProject> projectsToRefresh = new ArrayList<IProject>();
				{
					SubMonitor loopProgress = progress.split(10).setWorkRemaining(allModules.size());
					for (ModuleProjectDto module : allModules) {
						IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
						if (module.isTouched()) {
							projectsToRefresh.add(workspaceRoot.getProject(module.getArtifactId()));
						}
						loopProgress.worked(1);
					}
				}
				
				// Refreshes the project modified by the plug-in in eclipse
				try {
					SubMonitor loopProgress = progress.split(20).setWorkRemaining(2 * projectsToRefresh.size());
					for (IProject project : projectsToRefresh) {
						project.refreshLocal(IResource.DEPTH_INFINITE, loopProgress.split(1));
						MavenPlugin.getProjectConfigurationManager().updateProjectConfiguration(
								project, loopProgress.split(1));
					}
					
				} catch (CoreException e) {
					// Shows the update error but continues the execution
					EclipseExceptionHandler.handle(getShell(), e);
				}
			}
		};
	}
	
	/**
	 * Returns a view on the current pages on the wizard, based on user selections
	 * @return List of IWizardPage
	 */
	private List<IWizardPage> getCurrentPages() {
		
		List<IWizardPage> list = new ArrayList<IWizardPage>();
		list.add(this.addFeaturePage);
		list.addAll(this.getSelectedFeatureImplementationPages());
		return list;
	}
	
	/**
	 * Returns a view on the FeatureImplementationPage list with only the pages corresponding to selected features
	 * @return List of FeatureImplementationPage
	 */
	private List<FeatureImplementationPage> getSelectedFeatureImplementationPages() {
		
		// The implementation pages will be active if their corresponding feature is selected
		final List<FeatureDto> features = this.addFeaturePage.getFeatures();
		
		List<FeatureImplementationPage> list = new ArrayList<FeatureImplementationPage>();
		for (FeatureImplementationPage featureImplementationPage : this.featureImplementationPages) {
			
			// Is the feature of the page between the selected features?
			if (ValueUtil.find(features, featureImplementationPage.feature.getValue()) != null) {
				list.add(featureImplementationPage);
			}
		}
		return list;
	}
}