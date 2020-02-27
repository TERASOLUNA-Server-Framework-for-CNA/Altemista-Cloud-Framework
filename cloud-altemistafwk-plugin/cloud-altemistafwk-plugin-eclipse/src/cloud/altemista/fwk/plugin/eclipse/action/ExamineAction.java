package cloud.altemista.fwk.plugin.eclipse.action;

import org.eclipse.ui.IWorkbenchWizard;
import cloud.altemista.fwk.plugin.core.PluginReaderService;
import cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.common.eclipse.action.AbstractAction;
import cloud.altemista.fwk.plugin.eclipse.wizard.ExamineWizard;

/**
 * Examine TSF+ project action
 * @author NTT DATA
 */
public class ExamineAction extends AbstractAction {
	
	/** Plug-in service for read-only tasks related to TSF+ applications */
	protected final PluginReaderService pluginReaderService;
	
	/**
	 * Constructor
	 */
	public ExamineAction() {
		super();
		
		this.pluginReaderService = new PluginReaderServiceImpl();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractAction#createWizard()
	 */
	@Override
	protected IWorkbenchWizard createWizard() {
		
		// Reads the selected project as an ApplicationDto
		ApplicationDto application = this.pluginReaderService.readApplication(
				new ModelAndLocation(this.getProject().getLocation().toString()));
		
		// Completes the information of the module projects
		for (ModuleProjectDto moduleProject : application.getModules()) {
			/*
			 * No longer necessary; guessBusinessModules() will take care of this
			moduleProject.setBusinessShortId(this.pluginReaderService.guessBusinessShortId(moduleProject));
			 */
			moduleProject.setCurrentTypes(this.pluginReaderService.guessTypes(moduleProject));
			moduleProject.setCurrentFeatures(this.pluginReaderService.guessFeatures(moduleProject));
		}
		
		// Guesses the actual structure of the projeects of the application
		application.setAggregatorProject(this.pluginReaderService.guessAggregatorProject(application));
		application.setSharedEnvironmentProject(this.pluginReaderService.guessSharedEnvironmentProject(application));
		application.setCurrentBusinessModules(this.pluginReaderService.guessBusinessModules(application));
		
		// Creates the wizard
		return new ExamineWizard(application);
	}
}
