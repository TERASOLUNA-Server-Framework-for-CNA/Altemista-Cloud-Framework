/**
 * 
 */
package cloud.altemista.fwk.plugin.core;

import java.util.List;

import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.InstallFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.core.model.NewApplicationDto;
import cloud.altemista.fwk.plugin.core.model.NewBusinessModuleDto;
import cloud.altemista.fwk.plugin.maven.model.MavenSettingsDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;

/**
 * Plug-in service for application related tasks
 * @author NTT DATA
 */
public interface PluginService {

	/**
	 * Sets the Maven settings options to use
	 * @param mavenSettings the mavenSettings to set
	 */
	void setMavenSettings(MavenSettingsDto mavenSettings);

	/**
	 * Creates a new application from the proper archetype, based on the aggregator type and the TSF+ version
	 * @param location Location where the new application will be created
	 * @param application the new application
	 * @return ModelAndLocation of the new project
	 */
	ModelAndLocation newApplication(String location, NewApplicationDto application);

	/**
	 * Creates a new business module in an existing TSF+ application
	 * @param application the ApplicationDto
	 * @param businessModule the new business module 
	 * @return ModelAndLocation of the business module projects
	 */
	List<ModelAndLocation> newBusinessModule(ApplicationDto application, NewBusinessModuleDto businessModule);
	
	/**
	 * Installs a feature in a module project
	 * @param project the ModuleProjectDto
	 * @param feature the InstallFeatureDto to install
	 */
	void install(ModuleProjectDto project, InstallFeatureDto feature);
}
