package cloud.altemista.fwk.plugin.eclipse.action;

import org.eclipse.ui.IWorkbenchWizard;
import cloud.altemista.fwk.plugin.core.PluginReaderService;
import cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.common.eclipse.action.AbstractAction;
import cloud.altemista.fwk.plugin.eclipse.wizard.NewBusinessModuleWizard;

/**
 * Add business module action
 * @author NTT DATA
 */
public class NewBusinessModuleAction extends AbstractAction {
	
	/** Plug-in service for read-only tasks related to TSF+ applications */
	protected final PluginReaderService pluginReaderService;
	
	/**
	 * Constructor
	 */
	public NewBusinessModuleAction() {
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
		
		// Guesses which module project is the actual aggregator project of the application
		application.setAggregatorProject(this.pluginReaderService.guessAggregatorProject(application));
		
		// Creates the wizard
		return new NewBusinessModuleWizard(application);
	}
}
