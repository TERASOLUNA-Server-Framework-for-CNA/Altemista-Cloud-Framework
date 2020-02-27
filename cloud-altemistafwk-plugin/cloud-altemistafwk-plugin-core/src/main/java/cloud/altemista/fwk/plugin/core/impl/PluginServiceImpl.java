package cloud.altemista.fwk.plugin.core.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto;
import cloud.altemista.fwk.plugin.config.model.ProjectTemplateDto;
import cloud.altemista.fwk.plugin.core.PluginService;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.InstallFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.core.model.NewApplicationDto;
import cloud.altemista.fwk.plugin.core.model.NewBusinessModuleDto;
import cloud.altemista.fwk.plugin.core.util.FileUtil;
import cloud.altemista.fwk.plugin.core.util.StringUtil;
import cloud.altemista.fwk.plugin.core.util.WebXmlMerger;
import cloud.altemista.fwk.plugin.maven.MavenCommand;
import cloud.altemista.fwk.plugin.maven.model.MavenSettingsDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.plugin.maven.util.ModelUtil;

/**
 * Implementation of the Plug-in service over module projects
 * @author NTT DATA
 */
public class PluginServiceImpl implements PluginService {
	
	/** The maven settings this service will use */
	protected MavenSettingsDto mavenSettings;
	
	/**
	 * Default constructor
	 */
	public PluginServiceImpl() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.impl.AbstractMavenServiceImpl#setMavenSettings(org.terasoluna.plus.plugin.maven.model.MavenSettingsDto)
	 */
	@Override
	public void setMavenSettings(MavenSettingsDto mavenSettings) {
		
		this.mavenSettings = mavenSettings;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginService#newApplication(java.lang.String, org.terasoluna.plus.plugin.core.model.NewApplicationDto)
	 */
	@Override
	public ModelAndLocation newApplication(String location, NewApplicationDto application) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(location)) {
			throw new IllegalArgumentException("location is null or empty");
		}
		if (application == null) {
			throw new IllegalArgumentException("application is null");
		}
		
		// Ensures the location is a folder
		if (FileUtil.getDirectory(location) == null) {
			throw new PluginException(
					"Invalid location",
					"The selected location is not a directory",
					"%s: not a directory",
					location);
		}
		
		final String artifactId = application.getArtifactId();
		
		// Ensures the project does not already exist
		final String projectLocation = FilenameUtils.concat(location, application.getArtifactId());
		if (new File(projectLocation).exists()) {
			throw new PluginException(
					"Invalid location",
					"%s already exists at the selected location",
					"%s already exists at %s",
					artifactId, location);
		}
		
		// mvn archetype:generate
		final MavenCoordinatesDto archetype = this.newApplicationArchetype(application);
		new MavenCommand(location)
				.archetypeGenerate(archetype, application)
				.define(Constant.ARCHETYPE_PARAMETER_APP_SHORT_ID, application.getAppShortId())
				.define(Constant.ARCHETYPE_PARAMETER_TSFP_VERSION, application.getTerasolunaPlusVersion())
				.settings(this.mavenSettings)
				.execute();
		
		// Creates the new ModelAndLocation
		return new ModelAndLocation(projectLocation);
	}
	
	/**
	 * Retrieves the Maven coordinates of the archetype for the new application
	 * @param dto NewApplicationDto
	 * @return MavenCoordinatesDto
	 */
	private MavenCoordinatesDto newApplicationArchetype(NewApplicationDto dto) {
		
		final String aggregatorType = dto.getAggregatorType();
		final String terasolunaPlusVersion = dto.getTerasolunaPlusVersion();
		final OptionWithArtifactDto archetype =
				Config.APPLICATION_ARCHETYPES.value(aggregatorType, terasolunaPlusVersion);
		
		if (archetype == null) {
			throw new PluginException(
					"Illegal new TSF+ application",
					"The requested aggregator type %s is not valid for TSF+ version %s",
					"Unrecognized aggregator type %s for TSF+ version %s",
					aggregatorType, terasolunaPlusVersion);
		}
		
		return archetype.getArtifact();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginService#newBusinessModule(java.lang.String, org.terasoluna.plus.plugin.core.model.ApplicationDto, org.terasoluna.plus.plugin.core.model.NewBusinessModuleDto)
	 */
	@Override
	public List<ModelAndLocation> newBusinessModule(ApplicationDto applicationDto, NewBusinessModuleDto businessModule) {
		
		// (sanity checks)
		if (applicationDto == null) {
			throw new IllegalArgumentException("application is null");
		}
		if (businessModule == null) {
			throw new IllegalArgumentException("business module is null");
		}
		
		// Convenience constants
		final String location = applicationDto.getLocation();
		final String applicationName = applicationDto.getArtifactId();
		final String appShortId = applicationDto.getAppShortId();
		final String terasolunaPlusVersion = applicationDto.getTerasolunaPlusVersion();
		final String businessShortId = businessModule.getBusinessShortId();
		
        // Check if is a TSF o Altemista version
        List<String> listTSFVersions = Arrays.asList(Constant.TSF_VERSIONS);
        boolean terasolunaProject = false;
		if(listTSFVersions.contains(terasolunaPlusVersion)){
			terasolunaProject = true;
		}
		
		BusinessModuleTypeDto businessModuleType = null;
		
		if(terasolunaProject){
			// Retrieves the set of projects for this new business module
			businessModuleType =
					Config.BUSINESS_MODULES.value(businessModule.getType(), terasolunaPlusVersion);
			if (businessModuleType == null) {
				throw new PluginException(
						"Illegal new TSF+ business module",
						"The requested business module type %s is not valid for TSF+ version %s",
						"Unrecognized business module type %s for TSF+ version %s",
						businessModule.getType(), terasolunaPlusVersion);
			}
		}else{
			businessModuleType =
						Config.BUSINESS_MODULES_ALTEMISTA.value(businessModule.getType(), terasolunaPlusVersion);
			if (businessModuleType == null) {
				throw new PluginException(
						"Illegal new TSF+ business module",
						"The requested business module type %s is not valid for TSF+ version %s",
						"Unrecognized business module type %s for TSF+ version %s",
						businessModule.getType(), terasolunaPlusVersion);
			}
		}
		
		// Previous validations
		boolean aggregatorRequired = false;
		for (ProjectTemplateDto projectTemplate : businessModuleType.getProjectTemplates()) {
			
			final String artifactId = String.format(
					projectTemplate.getArtifactIdFormatter(), applicationName, appShortId, businessShortId);
			final String projectLocation = FilenameUtils.concat(location, artifactId);
			
			// Ensures that any of the new projects does already exist
			if (new File(projectLocation).exists()) {
				throw new PluginException(
						"Invalid location",
						"%s already exists at the selected location",
						"%s already exists at %s",
						artifactId, location);
			}
			
			// Should this project be included as dependency of the aggregator project?
			aggregatorRequired |= StringUtils.isNotEmpty(projectTemplate.getAggregatorScope());
		}
		
		// (some application types do not have any aggregator project (e.g.: empty application for shared utility projects);
		// avoid any additional work related with the aggregator project)
		aggregatorRequired &= (applicationDto.getAggregatorProject() != null);
		
		// Ensures the plug-in will be able to modify the application
		final ModelAndLocation application = new ModelAndLocation(location);
		application.getModel(); // (this actually reads pom.xml)
		final List<MavenCoordinatesDto> applicationManagedDependencies = new ArrayList<MavenCoordinatesDto>();
		
		// Ensures the plug-in will be able to modify the aggregator project pom.xml files
		ModelAndLocation aggregator = null;
		List<MavenCoordinatesDto> aggregatorDependencies = new ArrayList<MavenCoordinatesDto>();
		if (aggregatorRequired) {
			aggregator = new ModelAndLocation(applicationDto.getAggregatorProject().getLocation());
			aggregator.getModel(); // (this actually reads pom.xml)
		}
		
		// (for returning the ModelAndLocation of the new projects)
		List<ModelAndLocation> returnList = new ArrayList<ModelAndLocation>();
		
		// (for relating the business module projects)
		List<ModelAndLocation> relateList = new ArrayList<ModelAndLocation>();
		
		// For each archetype
		for (ProjectTemplateDto projectTemplate : businessModuleType.getProjectTemplates()) {
			
			final String artifactId = String.format(
					projectTemplate.getArtifactIdFormatter(), applicationName, appShortId, businessShortId);
			
			final MavenCoordinatesDto newProjectCoordinates = new MavenCoordinatesDto();
			newProjectCoordinates.setGroupId(applicationDto.getGroupId());
			newProjectCoordinates.setArtifactId(artifactId);
			newProjectCoordinates.setVersion(applicationDto.getVersion());
			
			// mvn archetype:generate
			new MavenCommand(location)
					.archetypeGenerate(projectTemplate.getArchetype(), newProjectCoordinates)
					.define(Constant.ARCHETYPE_PARAMETER_APP_NAME, applicationName)
					.define(Constant.ARCHETYPE_PARAMETER_APP_SHORT_ID, appShortId)
					.define(Constant.ARCHETYPE_PARAMETER_BUSINESS_SHORT_ID, businessShortId)
					.settings(this.mavenSettings)
					.execute();
			
			// Creates the new ModelAndLocation
			final String projectLocation = FilenameUtils.concat(location, artifactId);
			final ModelAndLocation project = new ModelAndLocation(projectLocation);
			returnList.add(project);
			
			// Will include the project as a managed dependency of the application
			applicationManagedDependencies.add(newProjectCoordinates);
			
			// Will relate this project with other projects of the same business module
			if ((projectTemplate.getDependencyArtifactIdFormatter() != null)
					&& (!projectTemplate.getDependencyArtifactIdFormatter().isEmpty())) {
				
				List<MavenCoordinatesDto> projectDependencies = new ArrayList<MavenCoordinatesDto>();
				for (String dependencyArtifactIdFormatter : projectTemplate.getDependencyArtifactIdFormatter()) {
					
					final String dependencyArtifactId = String.format(
							dependencyArtifactIdFormatter, applicationName, appShortId, businessShortId);
					
					MavenCoordinatesDto projectDependency = new MavenCoordinatesDto();
					projectDependency.setGroupId(applicationDto.getGroupId());
					projectDependency.setArtifactId(dependencyArtifactId);
					projectDependencies.add(projectDependency);
				}
				ModelUtil.addDependencies(project.getModel(), projectDependencies);
				
				// The actual writing of the updated model is deferred because it may cause further archetypes to fail
				// (as applying the archetype parses the application pom.xml, where the module list
				// has been already updated but its managed dependencies section has been not yet)
				relateList.add(project);
			}
			
			// Will include this project as a dependency of the aggregator project
			if (StringUtils.isNotEmpty(projectTemplate.getAggregatorScope())) {
				
				MavenCoordinatesDto aggregatorDependency = new MavenCoordinatesDto();
				aggregatorDependency.setGroupId(applicationDto.getGroupId());
				aggregatorDependency.setArtifactId(artifactId);
				aggregatorDependency.setScope(
						StringUtil.defaultIf(projectTemplate.getAggregatorScope(), null, "compile"));
				
				aggregatorDependencies.add(aggregatorDependency);
			}
		}
		
		// Includes the projects as managed dependencies of the application
		application.refresh(); // (because archetype:generate already added the projects as modules)
		for (MavenCoordinatesDto managedDependency : applicationManagedDependencies) {
			ModelUtil.addManagedDependency(application.getModel(), managedDependency);
		}
		ModelUtil.write(application.getModel(), application.getLocation());
	
		// Effectively relates the business module projects
		for (ModelAndLocation project : relateList) {
			ModelUtil.write(project.getModel(), project.getLocation());
		}
		
		// Includes the projects as dependencies of the aggregator project
		if ((aggregator != null) && (!aggregatorDependencies.isEmpty())) {
			for (MavenCoordinatesDto aggregatorDependency : aggregatorDependencies) {
				ModelUtil.addDependency(aggregator.getModel(), aggregatorDependency);
			}
			ModelUtil.write(aggregator.getModel(), aggregator.getLocation());
		}
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginService#install(org.terasoluna.plus.plugin.core.model.ModuleProjectDto, org.terasoluna.plus.plugin.core.model.InstallFeatureDto)
	 */
	@Override
	public void install(ModuleProjectDto project, InstallFeatureDto installFeature) {
		
		// (sanity checks)
		if (project == null) {
			throw new IllegalArgumentException("project is null");
		}
		if (StringUtils.isEmpty(project.getLocation())) {
			throw new IllegalArgumentException("project.location is null or empty");
		}
		if (project.getApplication() == null) {
			throw new IllegalArgumentException("project.application is null");
		}
		if (installFeature == null) {
			throw new IllegalArgumentException("installFeature is null");
		}
		
		// Ensures the location is a folder
		if (FileUtil.getDirectory(project.getLocation()) == null) {
			throw new PluginException(
					"Invalid location",
					"The selected location is not a directory",
					"%s: not a directory",
					project.getLocation());
		}
		
		// Installs the implementation first;
		// so if the same dependency with different scopes is in both feature and implementation,
		// the correct scope will prevail after the modifications (partial archetypes do not overwrite dependencies)
		boolean implementationShared = false;
		if (!installFeature.getFeature().isSelfImplemented()) {
			implementationShared = this.installImplementation(project, installFeature);
		}
		
		// Installs the feature
		this.installFeature(project, installFeature, implementationShared);
	}

	/**
	 * Installs the implementation feature
	 * @param moduleProject the module project
	 * @param installFeature the InstallFeatureDto to install
	 * @return implementationShared if the implementation has been installed in the shared environment project
	 */
	private boolean installImplementation(ModuleProjectDto moduleProject, InstallFeatureDto installFeature) {
		
		final FeatureImplementationDto implementation = installFeature.getImplementation();
		
		// Install the feature/implementation using a partial archetype 
		if (implementation.getArchetype() != null) {
			return this.installImplementationUsingArchetype(moduleProject, installFeature);
		}

		// Install the feature/implementation by managing the dependencies
		if (implementation.hasDependencies()) {
			return this.installImplementationUsingDependencies(moduleProject, installFeature);
		}
		
		// (should not happen)
		throw new PluginException(
				"Illegal feature implementation",
				"The requested implementation %s is not valid for TSF+ version %s",
				"%s has neither archetype nor dependencies for TSF+ version %s",
				implementation.getText(), moduleProject.getApplication().getTerasolunaPlusVersion());
	}

	/**
	 * Installs the implementation feature using an archetype
	 * @param moduleProject the module project
	 * @param installFeature the InstallFeatureDto to install
	 * @return implementationShared if the implementation has been installed in the shared environment project
	 */
	private boolean installImplementationUsingArchetype(
			ModuleProjectDto moduleProject, InstallFeatureDto installFeature) {
		
		final FeatureImplementationDto implementation = installFeature.getImplementation();
		final MavenCoordinatesDto archetype = implementation.getArchetype();
		
		// Install the implementation using a partial archetype 
		this.installArchetype(moduleProject, archetype, installFeature.getAdditionalProperties());
		moduleProject.setTouched(true);
		
		// (checks the project is not the shared environment project already; this should never happen)
		final ModuleProjectDto targetProject = ObjectUtils.defaultIfNull(
				moduleProject.getApplication().getSharedEnvironmentProject(), moduleProject);
		if (StringUtils.equals(moduleProject.getLocation(), targetProject.getLocation())) {
			return false;
		}
		
		// (convenience constants)
		final boolean shareConfiguration = installFeature.getFeature().getCurrentImplementation() == null;
		final String sourceLocation = moduleProject.getLocation();
		final ModelAndLocation source = new ModelAndLocation(sourceLocation);
		final String targetLocation = targetProject.getLocation();
		
		// Removes the implementation dependencies (they will eventually go to the shared environment project)
		if (implementation.hasDependencies()) {
			ModelUtil.removeDependencies(source.getModel(), implementation.getDependencies());
			ModelUtil.write(source.getModel(), sourceLocation);
			
			// Adds the implementation dependencies in the shared environment project
			if (shareConfiguration) {
				ModelAndLocation target = new ModelAndLocation(targetLocation);
				
				ModelUtil.addDependencies(target.getModel(), implementation.getDependencies());
				ModelUtil.write(target.getModel(), targetLocation);
				
				targetProject.setTouched(true);
			}
		}
		
		// Moves/deletes the shared part of the configuration
		this.postProcessArchetypeInstallation(moduleProject, targetProject, shareConfiguration,
				"feature implementation " + implementation.getText());
		return shareConfiguration;
	}

	/**
	 * Installs the implementation feature using dependencies only
	 * @param moduleProject the module project
	 * @param installFeature the InstallFeatureDto to install
	 * @return implementationShared if the implementation has been installed in the shared environment project
	 */
	private boolean installImplementationUsingDependencies(
			ModuleProjectDto moduleProject, InstallFeatureDto installFeature) {

		// If the feature is already shared, there is nothing to install for the implementation
		if (installFeature.getFeature().getCurrentImplementation() != null) {
			return false;
		}

		final FeatureImplementationDto implementation = installFeature.getImplementation();

		// The configuration is to be shared; determines the shared implementation project
		final String sourceLocation = moduleProject.getLocation();
		final ModelAndLocation project = new ModelAndLocation(sourceLocation);
		ModelAndLocation target = null;
		final ModuleProjectDto targetProject = ObjectUtils.defaultIfNull(
				moduleProject.getApplication().getSharedEnvironmentProject(), moduleProject);
		
		// (checks the project is not the shared environment project already; this should never happen)
		if (!StringUtils.equals(sourceLocation, targetProject.getLocation())) {
			
			// (will be installed on the shared environment project)
			target = new ModelAndLocation(targetProject.getLocation());
		}
		
		// Manages the dependencies on the correct project
		this.installDependencies(ObjectUtils.firstNonNull(target, project), implementation.getDependencies());
		targetProject.setTouched(true);
		
		return true;
	}
	
	/**
	 * Installs the feature
	 * @param moduleProject the module project
	 * @param installFeature the InstallFeatureDto to install
	 * @param implementationShared true if the implementation has been installed in the shared environment project
	 */
	private void installFeature(ModuleProjectDto moduleProject,
			InstallFeatureDto installFeature, boolean implementationShared) {
		
		final ModelAndLocation modelAndLocation = new ModelAndLocation(moduleProject.getLocation());
		final FeatureDto feature = installFeature.getFeature();
		
		final List<MavenCoordinatesDto> dependencies = new ArrayList<MavenCoordinatesDto>();
		
		// Install the feature using a partial archetype 
		final MavenCoordinatesDto archetype = feature.getArchetype();
		if (archetype != null) {
			this.installFeatureUsingArchetype(moduleProject, installFeature);
			
		// Will install the feature by managing the dependencies
		} else {
			dependencies.addAll(ObjectUtils.defaultIfNull(
					feature.getDependencies(), Collections.<MavenCoordinatesDto> emptyList()));
		}
		
		// If the implementation has been installed in the shared environment project,
		// includes a test-scope dependency to the shared environment project
		// in order for the business module tests to have the implementation available
		if (implementationShared) {
			final ModuleProjectDto sharedProject = moduleProject.getApplication().getSharedEnvironmentProject();
			
			if (sharedProject != null) {
				MavenCoordinatesDto sharedCoordinates = new MavenCoordinatesDto();
				sharedCoordinates.setGroupId(sharedProject.getGroupId());
				sharedCoordinates.setArtifactId(sharedProject.getArtifactId());
				sharedCoordinates.setScope("test");
				dependencies.add(sharedCoordinates);
			}
		}

		// Install the feature/implementation by managing the dependencies
		if (!dependencies.isEmpty()) {
			this.installDependencies(modelAndLocation, dependencies);
			moduleProject.setTouched(true);
		}
	}
	
	private void installFeatureUsingArchetype(ModuleProjectDto moduleProject, InstallFeatureDto installFeature) {
		
		// Install the feature using a partial archetype 
		this.installArchetype(moduleProject,
				installFeature.getFeature().getArchetype(), installFeature.getAdditionalProperties());
		moduleProject.setTouched(true);
		
		// (checks the project is not the shared environment project already; this should never happen)
		final ModuleProjectDto targetProject = ObjectUtils.defaultIfNull(
				moduleProject.getApplication().getSharedEnvironmentProject(), moduleProject);
		if (StringUtils.equals(moduleProject.getLocation(), targetProject.getLocation())) {
			return;
		}
		
		// (convenience constants)
		final CurrentFeatureDto feature = installFeature.getFeature();
		final boolean shareConfiguration = feature.getCurrentImplementation() == null;
		
		// Moves/deletes the shared part of the configuration
		this.postProcessArchetypeInstallation(moduleProject, targetProject, shareConfiguration,
				"feature " + feature.getText());
	}

	/**
	 * Convenience method to execute the archetype:generate goal on a module project with a partial archetype
	 * @param target the module project
	 * @param archetype MavenCoordinatesDto with the archetype coordinates
	 * @param additionalProperties the possible additional properties for the archetype
	 */
	private void installArchetype(ModuleProjectDto target, MavenCoordinatesDto archetype, Properties additionalProperties) {

		final ApplicationDto application = target.getApplication();
		
		// Installs by partial archetype
		MavenCommand mavenCommand = new MavenCommand(target.getLocation())
				.archetypeGenerate(archetype, target)
				.define(Constant.ARCHETYPE_PARAMETER_APP_NAME, application.getArtifactId())
				.define(Constant.ARCHETYPE_PARAMETER_APP_SHORT_ID, application.getAppShortId())
				.define(Constant.ARCHETYPE_PARAMETER_BUSINESS_SHORT_ID, target.getBusinessShortId());
		
		// Additional properties
		if (additionalProperties != null) {
			for (Entry<Object, Object> additionalProperty : additionalProperties.entrySet()) {
				mavenCommand.define((String) additionalProperty.getKey(), (String) additionalProperty.getValue());
			}
		}
		
		// Executes the maven command
		mavenCommand
				.settings(this.mavenSettings)
				.execute();
	}
	
	private void postProcessArchetypeInstallation(
			final ModuleProjectDto sourceProject, final ModuleProjectDto targetProject,
			final boolean shareConfiguration, final String text) {
		
		final String sourceLocation = sourceProject.getLocation();
		
		// Moves/deletes the shared part of the configuration
		final File sourceFolder = FileUtils.getFile(sourceLocation, Constant.SHARED_CONFIGURATION_FOLDER);
		final FileFilter shareFilter = FileUtil.makeWildcardRecursive(Constant.SHARED_CONFIGURATION_WILDCARDS);
		if (shareConfiguration) {
			final String targetLocation = targetProject.getLocation();
			
			// Moves the shared part of the configuration to the shared implementation project
			final File targetFolder = FileUtils.getFile(targetLocation, Constant.SHARED_CONFIGURATION_FOLDER);
			try {
				if (FileUtil.moveFiles(sourceFolder, targetFolder, shareFilter, true)) {
					targetProject.setTouched(shareConfiguration);
				}
				
			} catch (IOException e) {
				throw new PluginException(e,
						"Failed configuring shared environment project",
						"Configuration of %s in shared environment project %s failed",
						"Error moving configuration files of %1$s from %$3s to %4$s: %5$s",
						text, targetProject.getArtifactId(),
						sourceLocation, targetLocation, e.getMessage());
			}
			
		} else {
			
			// Deletes the shared part of the configuration (already in the shared implementation project)
			try {
				FileUtil.deleteFiles(sourceFolder, shareFilter);
				
			} catch (IOException e) {
				throw new PluginException(e,
						"Failed removing already shared configuration",
						"Removal of already shared configuration of %s in project %s failed",
						"Error deleting configuration files of %1$s from %$3s: %4$s",
						text, sourceProject.getArtifactId(), sourceLocation, e.getMessage());
			}
		}
		
		// Merges web-fragment.xml into web.xml
		final File webXmlFile = FileUtil.getFile(sourceLocation, Constant.WEB_APP_DEPLOYMENT_DESCRIPTOR_FILENAME);
		final File webFragmentXmlFile = FileUtil.getFile(sourceLocation, Constant.WEB_FRAGMENT_FILENAME);
		if ((webXmlFile == null) || (webFragmentXmlFile == null)) {
			return;
		}
		
		ByteArrayOutputStream baos = null;
		try {
			// Merges web.xml and web-fragment.xml into memory
			baos = new ByteArrayOutputStream();
			WebXmlMerger.merge(webXmlFile, webFragmentXmlFile, baos);
			
			// Deletes (actually, backs up) the original existing files 
			FileUtil.backupFile(webXmlFile, true);
			FileUtil.backupFile(webFragmentXmlFile, false);
			
			// Writes the merged web.xml into the actual file
			FileUtils.writeByteArrayToFile(webXmlFile, baos.toByteArray());
			
		} catch (Exception e) {
			throw new PluginException(e,
					"Failed merging web.xml and web-fragment.xml",
					"Merging the web-fragment.xml contents from %s into existing web.xml of project %s failed",
					"Error trying to merge %$4s into %$3s: %5$s",
					text, sourceProject.getArtifactId(),
					webXmlFile.getAbsolutePath(), webFragmentXmlFile.getAbsolutePath(), e.getMessage());
			
		} finally {
			IOUtils.closeQuietly(baos);
		}
	}

	/**
	 * Convenience method to append a set of dependencies on a module project 
	 * @param target the ModelAndLocation of the module project
	 * @param dependencies List of MavenCoordinatesDto with the dependencies
	 */
	private void installDependencies(ModelAndLocation target, List<MavenCoordinatesDto> dependencies) {
		
		// Install by managing the dependencies
		Model model = target.getModel();
		ModelUtil.addDependencies(model, dependencies);
		ModelUtil.write(model, target.getLocation());
	}
}
