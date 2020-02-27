/**
 * 
 */
package cloud.altemista.fwk.plugin.core;

import java.util.List;

import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.CurrentBusinessModuleDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;

/**
 * Plug-in service for read-only tasks related to TSF+ applications
 * @author NTT DATA
 */
public interface PluginReaderService {
	
	/**
	 * Reads one application
	 * @param application the application location (the application main project location)
	 * @return the read ApplicationDto
	 */
	ApplicationDto readApplication(ModelAndLocation application);
	
	/**
	 * Reads a module project and the application it belongs to
	 * @param moduleProject the module project location
	 * @return the read ModuleProjectDto with its ApplicationDto informed
	 */
	ModuleProjectDto readModuleProject(ModelAndLocation moduleProject);
	
	/**
	 * Based on the TSF+ version and the current dependencies,
	 * calculates the list of {@link org.terasoluna.plus.plugin.config.Config#MODULE_PROJECTS} of a ModuleProjectDto
	 * @param project the ModuleProjectDto
	 * @return List of ModuleProjectTypeDto
	 */
	List<ModuleProjectTypeDto> guessTypes(ModuleProjectDto project);
	
	/**
	 * Based on the TSF+ version and the current dependencies,
	 * calculates the list of CurrentFeatureDto of a ModuleProjectDto
	 * @param project the ModuleProjectDto
	 * @return List of CurrentFeatureDto
	 */
	List<CurrentFeatureDto> guessFeatures(ModuleProjectDto project);
	
	/**
	 * Based on the TSF+ version and the current dependencies and implementations,
	 * determines which implementation of a feature has the ModuleProjectDto
	 * @param project the ModuleProjectDto
	 * @param feature the FeatureDto to check
	 * @return FeatureImplementationDto in the module project,
	 * or null if the feature is self-implemented or the module does not have any implementation the feature 
	 */
	FeatureImplementationDto guessImplementation(ModuleProjectDto project, FeatureDto feature);
	
	/**
	 * Based on the TSF+ version and the current dependencies and implementations,
	 * determines which module of an application has an implementation of the feature
	 * @param application the ApplicationDto
	 * @param feature the FeatureDto to check
	 * @return the first ModuleProjectDto that has an implementation of the feature,
	 * or null if the feature is self-implemented or none of the modules of the application have any implementation 
	 */
	ModuleProjectDto guessProjectImplementing(ApplicationDto application, FeatureDto feature);
	
	/**
	 * Based on the types of the module projects of the application,
	 * calculates which project is the best candidate to be the actual aggregator project of the application
	 * @param application the ApplicationDto
	 * @return the best candidate
	 * @see #guessTypes(ModuleProjectDto)
	 */
	ModuleProjectDto guessAggregatorProject(ApplicationDto application);
	
	/**
	 * Based on the types of the module projects of the application,
	 * calculates which project is the best candidate to have the shared configurations for the feature implementations
	 * @param application the ApplicationDto
	 * @return the best candidate, or null if no candidate is found
	 * @see #guessTypes(ModuleProjectDto)
	 */
	ModuleProjectDto guessSharedEnvironmentProject(ApplicationDto application);
	
	/**
	 * Based on the types and names of the module projects of the application,
	 * calculates the list of CurrentBusinessModuleDto of the ApplicationDto
	 * @param application the ApplicationDto
	 * @return List of CurrentBusinessModuleDto
	 */
	List<CurrentBusinessModuleDto> guessBusinessModules(ApplicationDto application);
}
