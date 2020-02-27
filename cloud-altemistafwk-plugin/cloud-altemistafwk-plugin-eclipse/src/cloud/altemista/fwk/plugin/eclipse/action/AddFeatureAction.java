package cloud.altemista.fwk.plugin.eclipse.action;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.ui.IWorkbenchWizard;
import cloud.altemista.fwk.plugin.core.PluginReaderService;
import cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.common.eclipse.action.AbstractAction;
import cloud.altemista.fwk.plugin.eclipse.wizard.AddFeatureWizard;

/**
 * Add features action
 * @author NTT DATA
 */
public class AddFeatureAction extends AbstractAction {
	
	/** Plug-in service for read-only tasks related to TSF+ applications */
	protected final PluginReaderService pluginReaderService;
	
	/**
	 * Constructor
	 */
	public AddFeatureAction() {
		super();
		
		this.pluginReaderService = new PluginReaderServiceImpl();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractAction#createWizard()
	 */
	@Override
	protected IWorkbenchWizard createWizard() {
		
		// Reads the selected project as an ModuleProjectDto (and the application as ApplicationDto)
		ModuleProjectDto moduleProject = this.pluginReaderService.readModuleProject(
				new ModelAndLocation(this.getProject().getLocation().toString()));
		ApplicationDto application = moduleProject.getApplication();
		
		// Guesses the current types, business short ID and features of the module project
		this.pluginReaderService.guessBusinessModules(application);
		moduleProject.setCurrentTypes(this.pluginReaderService.guessTypes(moduleProject));
		moduleProject.setBusinessShortId(StringUtils.replace(moduleProject.getBusinessShortId(),"-",""));
		moduleProject.setCurrentFeatures(this.pluginReaderService.guessFeatures(moduleProject));
		
		// Guesses the current implementation of the features (include other application module projects)
		for (CurrentFeatureDto feature : moduleProject.getCurrentFeatures()) {
			ModuleProjectDto currentProjectImplementing = this.pluginReaderService.guessProjectImplementing(
					application, feature);
			if (currentProjectImplementing != null) {
				feature.setCurrentProjectImplementing(currentProjectImplementing);
				feature.setCurrentImplementation(this.pluginReaderService.guessImplementation(
						currentProjectImplementing, feature));
			}
		}
		
		// Guesses which module project contains the configurations for shared implementations
		application.setSharedEnvironmentProject(this.pluginReaderService.guessSharedEnvironmentProject(application));
		
		// Creates the wizard
		return new AddFeatureWizard(moduleProject);
	}
}
