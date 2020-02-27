/**
 * 
 */
package cloud.altemista.fwk.plugin.config;

import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.ConfigHolder;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.config.model.OptionDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto;

/**
 * Convenience class to access the configuration values.
 * @author NTT DATA
 */
public class Config {
	
	/** Versions supported by cloud-altemistafwk-plugin */
	public static final ConfigHolder<OptionDto> VERSIONS = 
			new ConfigHolder<OptionDto>(OptionDto.class,
					"versions");
	
	/** Business module types (e.g.: common, common-core-web) */
	public static final ConfigHolder<BusinessModuleTypeDto> BUSINESS_MODULES =
			new ConfigHolder<BusinessModuleTypeDto>(BusinessModuleTypeDto.class,
					"businessModules");
	
	/** Business module types (e.g.: common, common-core-web) */
	public static final ConfigHolder<BusinessModuleTypeDto> BUSINESS_MODULES_ALTEMISTA =
			new ConfigHolder<BusinessModuleTypeDto>(BusinessModuleTypeDto.class,
					"businessModulesAltemista");
	
	/** Module project types (e.g.: common, core, web, webapp) */
	public static final ConfigHolder<ModuleProjectTypeDto> MODULE_PROJECTS =
			new ConfigHolder<ModuleProjectTypeDto>(ModuleProjectTypeDto.class,
					"moduleProjects");
	
	/** Maven archetype coordinates for new applications, depending on the aggregator project types (e.g.: webapp) */
	public static final ConfigHolder<OptionWithArtifactDto> APPLICATION_ARCHETYPES =
			new ConfigHolder<OptionWithArtifactDto>(OptionWithArtifactDto.class,
					"applicationArchetypes");
	
	/** Module project features (e.g.: core-mail, core-workflow) */
	public static final ConfigHolder<FeatureDto> FEATURES =
			new ConfigHolder<FeatureDto>(FeatureDto.class,
					"features");
	
	/**
	 * Default private constructor (utility class)
	 */
	private Config() {
		super();
	}
}
