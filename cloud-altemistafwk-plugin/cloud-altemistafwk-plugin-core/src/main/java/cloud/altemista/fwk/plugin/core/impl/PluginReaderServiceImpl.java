/**
 * 
 */
package cloud.altemista.fwk.plugin.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.config.model.OptionDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithDependenciesDto;
import cloud.altemista.fwk.plugin.config.model.ProjectTemplateDto;
import cloud.altemista.fwk.plugin.config.model.RequirementSetDto;
import cloud.altemista.fwk.plugin.config.util.ValueUtil;
import cloud.altemista.fwk.plugin.core.PluginReaderService;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.CurrentBusinessModuleDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.core.util.MavenCoordinatesUtil;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;
import cloud.altemista.fwk.plugin.maven.util.ModelUtil;

/**
 * Implementation of the `plug-in service for read-only tasks related to TSF+ applications
 * @author NTT DATA
 */
public class PluginReaderServiceImpl implements PluginReaderService {
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.ApplicationReaderService#readApplication(org.terasoluna.plus.plugin.maven.model.ModelAndLocation)
	 */
	@Override
	public ApplicationDto readApplication(ModelAndLocation applicationLocation) {
		
		// Reads the application project
		ApplicationDto application = this.readApplicationInternal(applicationLocation);
		
		// Reads all the module projects of the application
		application.setModules(new ArrayList<ModuleProjectDto>());
		for (ModelAndLocation ml : applicationLocation.gotoModules()) {
			
			// Read the module and adds it to the application
			ModuleProjectDto module = this.readModuleProjectInternal(ml, true);
			module.setApplication(application);
			application.getModules().add(module);
		}
		
		return application;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.ApplicationReaderService#readModuleProject(org.terasoluna.plus.plugin.maven.model.ModelAndLocation)
	 */
	@Override
	public ModuleProjectDto readModuleProject(ModelAndLocation moduleProjectLocation) {
		
		// Reads the project as a TSF+ module project
		ModuleProjectDto moduleProject = this.readModuleProjectInternal(moduleProjectLocation, false);
		
		// Locates the application project
		ModelAndLocation applicationLocation = moduleProjectLocation.gotoParent();
		if (applicationLocation == null) {
			throw new PluginException(
					"This TSF+ project is not inside a TSF+ application",
					"No TSF+ parent application found in TSF+ project %s",
					"Maven project at %2$s has no parent",
					moduleProject.getArtifactId(), moduleProjectLocation.getLocation());
		}
		
		// Reads the application project
		ApplicationDto application = this.readApplicationInternal(applicationLocation);
		
		// Reads all the module projects of the application
		application.setModules(new ArrayList<ModuleProjectDto>());
		for (ModelAndLocation ml : applicationLocation.gotoModules()) {
			
			// (avoids reading the module project again)
			ModuleProjectDto module = StringUtils.equals(moduleProjectLocation.getLocation(), ml.getLocation())
					? moduleProject
					: this.readModuleProjectInternal(ml, true);
			
			// Adds this module to the application
			module.setApplication(application);
			application.getModules().add(module);
		}
		
		// Checks the initially read module project is part of the returned application
		if (moduleProject.getApplication() == null) {
			throw new PluginException(
					"This TSF+ project is not a module of the TSF+ application",
					"The TSF+ project %s is not a module of the TSF+ application %s",
					"%s is not a module of the Maven project at %3$s",
					moduleProject.getArtifactId(), application.getArtifactId(), applicationLocation.getLocation());
		}
		
		return moduleProject;
	}
	
	/**
	 * Reads a project, ensuring it is a TSF+ application project
	 * @param applicationLocation the application project location
	 * @return the read ApplicationDto
	 */
	private ApplicationDto readApplicationInternal(ModelAndLocation applicationLocation) {
		
		// Loads the model
		Model model = null;
		try {
			model = applicationLocation.getModel();
			
		} catch (PluginException e) {
			throw new PluginException(e,
					"This project is not a ACF or TSF+ application",
					"Could not read project as a Maven project",
					"Project at %s could not be read as a Maven project: %s",
					applicationLocation.getLocation(), e.getReason());
		}
		
		// Locates TSF+ bill of materials
		MavenCoordinatesDto bom = ModelUtil.findManagedDependency(model,
				Constant.TSFP_BOM_GROUP_ID, Constant.TSFP_BOM_ARTIFACT_ID);
		if (bom == null) {
			 bom = ModelUtil.findManagedDependency(model,
						Constant.ALTEMISTA_BOM_GROUP_ID, Constant.ALTEMISTA_BOM_ARTIFACT_ID);
			 if (bom == null) {
				throw new PluginException(
						"This project is not a ACF or TSF+ application",
						"Missing TSF+ or Altemista bill of materials in Maven project %s",
						"Missing TSF+ or Altemista bill of materials in Maven project at %2$s",
						model.getArtifactId(), applicationLocation.getLocation());
			 }
		}
		
		// Locates the version in the configuration
		OptionDto version = Config.VERSIONS.value(bom.getVersion());
		if (version == null) {
			throw new PluginException(
					"Invalid TSF+ application",
					"TSF+ application %s uses an unrecognized TSF+ version",
					"Unrecognized TSF+ version %2$s in Maven project at %3$s",
					model.getArtifactId(), bom.getVersion(), applicationLocation.getLocation());
		}
		
		// Locates the appShortId
		final String appShortId = model.getProperties().getProperty(Constant.MAVEN_PROPERTY_APP_SHORT_ID);
		
		// Creates the ApplicationDto object
		ApplicationDto dto = new ApplicationDto();
		dto.setGroupId(ModelUtil.getGroupId(model));
		dto.setArtifactId(ModelUtil.getArtifactId(model));
		dto.setVersion(ModelUtil.getVersion(model));
		dto.setLocation(applicationLocation.getLocation());
		dto.setAppShortId(appShortId);
		dto.setTerasolunaPlusVersion(version.getValue());
		return dto;
	}
	
	/**
	 * Reads a project, ensuring it is a TSF+ module project
	 * @param moduleProjectLocation the module project location
	 * @param ignoreMissingDependencies true to ignore missing TSF+ dependencies (e.g.: to read the *-env module)
	 * @return the read ApplicationProjectDto
	 */
	private ModuleProjectDto readModuleProjectInternal(
			ModelAndLocation moduleProjectLocation, boolean ignoreMissingDependencies) {
		
		// Loads the model
		Model model = null;
		try {
			model = moduleProjectLocation.getModel();
			
		} catch (PluginException e) {
			throw new PluginException(e,
					"This project is not a TSF+ project",
					"Could not read project as a Maven project",
					"Project at %s could not be read as a Maven project: %s",
					moduleProjectLocation.getLocation(), e.getReason());
		}
		
		// The project must have at least one TSF+ dependency
		List<MavenCoordinatesDto> dependencies = ModelUtil.filterDependenciesStartWith(
				model, Constant.TSFP_GROUP_ID_PREFIX);
		// If the project is Altemista
		if(dependencies.isEmpty()){
			dependencies = ModelUtil.filterDependenciesStartWith(
					model, Constant.ALTEMISTA_GROUP_ID_PREFIX);
		}
		
		if (dependencies.isEmpty() && (!ignoreMissingDependencies)) {
			throw new PluginException(
					"This project is not a TSF+ or Altemista project",
					"No TSF+ dependencies found in Maven project %s",
					"No TSF+ dependencies found in Maven project at %2$s",
					model.getArtifactId(), moduleProjectLocation.getLocation());
		}
		
		// Creates the ModuleProjectDto
		ModuleProjectDto dto = new ModuleProjectDto();
		dto.setGroupId(ModelUtil.getGroupId(model));
		dto.setArtifactId(ModelUtil.getArtifactId(model));
		dto.setVersion(ModelUtil.getVersion(model));
		dto.setLocation(moduleProjectLocation.getLocation());
		dto.setTerasolunaPlusDependencies(dependencies);
		return dto;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginReaderService#guessTypes(org.terasoluna.plus.plugin.core.model.ModuleProjectDto)
	 */
	@Override
	public List<ModuleProjectTypeDto> guessTypes(ModuleProjectDto project) {
		
		final String terasolunaPlusVersion = project.getApplication().getTerasolunaPlusVersion();
		
		List<ModuleProjectTypeDto> typesFound = new ArrayList<ModuleProjectTypeDto>();
		List<ModuleProjectTypeDto> emptyTypes = new ArrayList<ModuleProjectTypeDto>();
		
		for (ModuleProjectTypeDto type : Config.MODULE_PROJECTS.values(terasolunaPlusVersion)) {
			
			// Save "empty types" (module project types without artifact) for later
			if (type.getArtifact() == null) {
				emptyTypes.add(type);
				
			// Checks if the project has the artifact of this module project type
			} else if (this.dependsOn(project, type.getArtifact())) {
				typesFound.add(type);
			}
		}
		
		// If at least one type has been found, return the types found
		// otherwise, consider that the project has the "empty types"
		return (!typesFound.isEmpty()) ? typesFound : emptyTypes;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.ModuleProjectService#currentFeatures(org.terasoluna.plus.plugin.core.model.ModuleProjectDto)
	 */
	@Override
	public List<CurrentFeatureDto> guessFeatures(ModuleProjectDto project) {
		
		final String terasolunaPlusVersion = project.getApplication().getTerasolunaPlusVersion();
		
		List<CurrentFeatureDto> list = new ArrayList<CurrentFeatureDto>();
		
		// For each feature for the concrete TSF+ version
		for (FeatureDto feature : Config.FEATURES.values(terasolunaPlusVersion)) {
			
			// (implementations filtered for the concrete TSF+ version)
			final List<FeatureImplementationDto> implementations =
					ValueUtil.filter(feature.getImplementations(), terasolunaPlusVersion);
			
			CurrentFeatureDto currentFeature = new CurrentFeatureDto();
			
			// Copies the FeatureDto
			currentFeature.setValue(feature.getValue());
			currentFeature.setText(feature.getText());
			currentFeature.setDescription(feature.getDescription());
			currentFeature.setRequirementSets(feature.getRequirementSets());
			currentFeature.setIncompatibilities(feature.getIncompatibilities());
			currentFeature.setDependencies(feature.getDependencies());
			currentFeature.setSelfImplemented(feature.isSelfImplemented());
			currentFeature.setArchetype(feature.getArchetype());
			currentFeature.setAdditionalProperties(feature.getAdditionalProperties());
			currentFeature.setImplementations(implementations);
			
			// Sets the flags
			boolean meetsRequirements = this.doesFeatureMeetRequirements(project, feature);
			currentFeature.setMeetsRequirements(meetsRequirements);
			currentFeature.setInstalled(this.isFeatureInstalled(project, feature, implementations, meetsRequirements));
			currentFeature.setCompatible(!this.dependsOnAny(project, feature.getIncompatibilities()));
			
			list.add(currentFeature);
		}
		
		return list;
	}
	
	/**
	 * Checks if the module project meets the requirements to install a feature
	 * @param project ModuleProjectDto
	 * @param feature FeatureDto
	 * @return true if the module project meets the requirements to install the feature
	 */
	private boolean doesFeatureMeetRequirements(ModuleProjectDto project, FeatureDto feature) {
		
		// If there are no sets of requirements, the feature can be installed
		if ((feature.getRequirementSets() == null) || (feature.getRequirementSets().isEmpty())) {
			return true;
		}
		
		// Checks all the requirements (AND) of all the sets of requirements (OR) 
		for (RequirementSetDto requirementSet : feature.getRequirementSets()) {
			if (this.dependsOnAll(project, requirementSet.getRequirements(), true)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if a feature is installed in a module project.
	 * A feature is installed if the module has its dependencies or the dependencies of one of its implementations 
	 * @param project ModuleProjectDto
	 * @param feature FeatureDto
	 * @param implementations List of already filtered FeatureImplementationDto
	 * @param meetsRequirements if the module project meets the requirements to install the feature
	 * @return true if the module project has the feature installed
	 */
	private boolean isFeatureInstalled(ModuleProjectDto project,
			FeatureDto feature, List<FeatureImplementationDto> implementations, boolean meetsRequirements) {
		
		// If the module project does not meet the requirements, then it is not installed
		// (this avoids modules with the same dependencies being shown twice; e.g.: app-security)
		if (!meetsRequirements) {
			return false;
		}
		
		// If there are required dependencies, check the required dependencies
		// (check only the TSF+ dependencies)
		List<MavenCoordinatesDto> featureDependencies = MavenCoordinatesUtil.filterDependenciesStartWith(
				feature.getDependencies(), Constant.TSFP_GROUP_ID_PREFIX, Constant.ALTEMISTA_GROUP_ID_PREFIX);
		if (this.dependsOnAll(project, featureDependencies, true)) {
			return true;
		}
		
		// If this feature is self-implemented (or has no implementations), then it is not installed
		if (feature.isSelfImplemented() || implementations.isEmpty()) {
			return false;
		}
		
		// Else (e.g.: persistence), check if the module has any of the implementations
		for (OptionWithDependenciesDto implementation : implementations) {
			// (check only the TSF+ dependencies)
			List<MavenCoordinatesDto> implementationDependencies = MavenCoordinatesUtil.filterDependenciesStartWith(
					implementation.getDependencies(), Constant.TSFP_GROUP_ID_PREFIX, Constant.ALTEMISTA_GROUP_ID_PREFIX);
			if (this.dependsOnAll(project, implementationDependencies, true)) {
				return true;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.ModuleProjectService#currentImplementation(org.terasoluna.plus.plugin.core.model.ModuleProjectDto, org.terasoluna.plus.plugin.config.model.FeatureDto)
	 */
	@Override
	public FeatureImplementationDto guessImplementation(ModuleProjectDto project, FeatureDto feature) {
		
		// If the feature is self-implemented, there is no implementation
		if (feature.isSelfImplemented()) {
			return null;
		}
		
		// (implementations filtered for the concrete TSF+ version)
		final String terasolunaPlusVersion = project.getApplication().getTerasolunaPlusVersion();
		final List<FeatureImplementationDto> implementations =
				ValueUtil.filter(feature.getImplementations(), terasolunaPlusVersion);
		
		// No known implementations (this should never happen)
		if (implementations.isEmpty()) {
			return null;
		}
		
		// Search for implementation in this module project
		for (FeatureImplementationDto implementation : implementations) {
			// (check only the TSF+ dependencies)
			List<MavenCoordinatesDto> implementationDependencies = MavenCoordinatesUtil.filterDependenciesStartWith(
					implementation.getDependencies(), Constant.TSFP_GROUP_ID_PREFIX, Constant.ALTEMISTA_GROUP_ID_PREFIX);
			if (this.dependsOnAll(project, implementationDependencies, true)) {
				return implementation;
			}
		}
		
		// No implementation found
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.ModuleProjectService#currentProjectImplementing(org.terasoluna.plus.plugin.core.model.ApplicationDto, org.terasoluna.plus.plugin.config.model.FeatureDto)
	 */
	@Override
	public ModuleProjectDto guessProjectImplementing(ApplicationDto application, FeatureDto feature) {
		
		// If the feature is self-implemented, there is no implementation
		if (feature.isSelfImplemented()) {
			return null;
		}
		
		// (implementations filtered for the concrete TSF+ version)
		final String terasolunaPlusVersion = application.getTerasolunaPlusVersion();
		final List<FeatureImplementationDto> implementations =
				ValueUtil.filter(feature.getImplementations(), terasolunaPlusVersion);
		
		// No known implementations (this should never happen)
		if (implementations.isEmpty()) {
			return null;
		}
		
		// Search for implementations in each module project
		for (ModuleProjectDto module : application.getModules()) {
			
			for (FeatureImplementationDto implementation : implementations) {
				// (check only the TSF+ dependencies)
				List<MavenCoordinatesDto> implementationDependencies = MavenCoordinatesUtil.filterDependenciesStartWith(
						implementation.getDependencies(), Constant.TSFP_GROUP_ID_PREFIX, Constant.ALTEMISTA_GROUP_ID_PREFIX);
				if (this.dependsOnAll(module, implementationDependencies, true)) {
					return module;
				}
			}
		}
		
		// No implementation found in any module
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginReaderService#guessAggregatorProject(org.terasoluna.plus.plugin.core.model.ApplicationDto)
	 */
	@Override
	public ModuleProjectDto guessAggregatorProject(ApplicationDto application) {
		
		// (sanity check)
		if ((application == null)
				|| (application.getModules() == null)
				|| (application.getModules().isEmpty())) {
			return null;
		}
		
		// Looks for the aggregator project
		for (ModuleProjectDto candidate : application.getModules()) {
			
			// Guesses the current types if necessary 
			if (candidate.getCurrentTypes() == null) {
				candidate.setCurrentTypes(this.guessTypes(candidate));
			}
			
			for (ModuleProjectTypeDto type : candidate.getCurrentTypes()) {
				if (type.isAggregator()) {
					return candidate;
				}
			}
		}
		
		// No aggregator found
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginReaderService#guessSharedEnvironmentProject(org.terasoluna.plus.plugin.core.model.ApplicationDto)
	 */
	@Override
	public ModuleProjectDto guessSharedEnvironmentProject(ApplicationDto application) {
		
		// (sanity check)
		if ((application == null)
				|| (application.getModules() == null)
				|| (application.getModules().isEmpty())) {
			return null;
		}
		
		// Looks for the module project that is given the most priority
		ModuleProjectDto bestCandidate = null;
		int bestCandidatePriority = Integer.MAX_VALUE;
		for (ModuleProjectDto candidate : application.getModules()) {
			
			// Guesses the current types if necessary 
			if (candidate.getCurrentTypes() == null) {
				candidate.setCurrentTypes(this.guessTypes(candidate));
			}
			
			// (the lowest the value, the better)
			Integer candidatePriority = this.computeSharedImplementationPriority(candidate);
			if ((candidatePriority != null) && (candidatePriority.intValue() < bestCandidatePriority)) {
				bestCandidate = candidate;
				bestCandidatePriority = candidatePriority;
			}
		}
		
		return bestCandidate;
	}
	
	/**
	 * Computes the priority to consider a module project a candidate to have the shared configurations
	 * @param project ModuleProjectDto
	 * @return priority to consider the module project a candidate to have the shared configurations,
	 * or {@code null} if the module project should not be considered as a candidate
	 */
	private Integer computeSharedImplementationPriority(ModuleProjectDto project) {
		
		final ApplicationDto application = project.getApplication();
		final List<ModuleProjectTypeDto> types = Config.MODULE_PROJECTS.values(application.getTerasolunaPlusVersion());
		
		// The priority of the project will be the greater priority of its project types
		Integer priority = null;
		for (ModuleProjectTypeDto type : types) {
			
			// (only non-empty types are taken into account)
			if (type.getArtifact() == null) {
				continue;
			}
			
			// (ignores module project types that should not be considered candidates)
			if (type.getSharedImplementationPriority() == null) {
				continue;
			}
			
			// (saves the greater priority of its project types)
			if (!this.hasType(project, type)) {
				continue;
			}
			if ((priority == null) || (type.getSharedImplementationPriority() < priority)) {
				priority = type.getSharedImplementationPriority();
			}
		}
		
		// Returns the priority specified by project type 
		if (priority != null) {
			return priority;
		}
		
		// If no project types are found, determine the priority by the suffixes of its artifactId
		final String artifactId = project.getArtifactId();
		for (Entry<String, Integer> entry : Constant.SHARED_IMPLEMENTATION_PRIORITY_BY_SUFFIX.entrySet()) {
			if (StringUtils.endsWith(artifactId, entry.getKey())) {
				return entry.getValue();
			}
		}
		
		// No type, no known suffix: this project is not a candidate
		return null;
	}
	
	/**
	 * Checks if the ModuleProjectDto has the type
	 * @param project ModuleProjectDto
	 * @param type ModuleProjectTypeDto
	 * @return true if the ModuleProjectDto has the type
	 */
	private boolean hasType(ModuleProjectDto project, ModuleProjectTypeDto type) {
		
		if (project.getCurrentTypes().isEmpty()) {
			return false;
		}
		
		for (OptionDto currentType : project.getCurrentTypes()) {
			if (StringUtils.equals(type.getValue(), currentType.getValue())) {
				return true;
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.PluginReaderService#guessBusinessModules(org.terasoluna.plus.plugin.core.model.ApplicationDto)
	 */
	@Override
	public List<CurrentBusinessModuleDto> guessBusinessModules(ApplicationDto application) {
		
		// (sanity check)
		if ((application == null)
				|| (application.getModules() == null)
				|| (application.getModules().isEmpty())) {
			return null;
		}
		
		// Guesses both the aggregator project and the shared environment project
		if (application.getAggregatorProject() == null) {
			application.setAggregatorProject(this.guessAggregatorProject(application));
		}
		if (application.getSharedEnvironmentProject() == null) {
			application.setSharedEnvironmentProject(this.guessSharedEnvironmentProject(application));
		}
		final ModuleProjectDto aggregatorProject = application.getAggregatorProject();
		final ModuleProjectDto sharedEnvironmentProject = application.getSharedEnvironmentProject();
		
		// Builds a set with the artifactId of the business module projects
		// (i.e.: excluding the aggregator and the shared environment projects)
		Set<String> artifactIds = new HashSet<String>();
		for (ModuleProjectDto moduleProject : application.getModules()) {
			artifactIds.add(moduleProject.getArtifactId());
		}
		if (aggregatorProject != null) {
			artifactIds.remove(aggregatorProject.getArtifactId());
		}
		if (sharedEnvironmentProject != null) {
			artifactIds.remove(sharedEnvironmentProject.getArtifactId());
		}
		
		// (sanity check)
		if (artifactIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		// (convenience constants)
		final String applicationName = application.getArtifactId();
		final String appShortId = application.getAppShortId();
		final String terasolunaPlusVersion = application.getTerasolunaPlusVersion();
		
        // Check if is a TSF o Altemista version
        List<String> listTSFVersions = Arrays.asList(Constant.TSF_VERSIONS);
        boolean terasolunaProject = false;
		if(listTSFVersions.contains(terasolunaPlusVersion)){
			terasolunaProject = true;
		}
		// Computes each business module type at a time in definition order
		List<CurrentBusinessModuleDto> currentBusinessModules = new ArrayList<CurrentBusinessModuleDto>();
		
		if(terasolunaProject){
		
			for (BusinessModuleTypeDto type : Config.BUSINESS_MODULES.values(terasolunaPlusVersion)) {
				
				// For each business short id of the current business module type
				for (String businessShortId :
						this.guesstBusinessShortIds(artifactIds, type, applicationName, appShortId)) {
					
					// Builds the CurrentBusinessModuleDto object
					CurrentBusinessModuleDto currentBusinessModule =
							this.buildCurrentBusinessModuleDto(application, businessShortId, type);
					currentBusinessModules.add(currentBusinessModule);
					
					// Removes the already used artifactIds from the set 
					for (ModuleProjectDto moduleProject : currentBusinessModule.getModules()) {
						artifactIds.remove(moduleProject.getArtifactId());
					}
				}
			}
		// If it is altemista Project	
		}else{
			for (BusinessModuleTypeDto type : Config.BUSINESS_MODULES_ALTEMISTA.values(terasolunaPlusVersion)) {
				
				// For each business short id of the current business module type
				for (String businessShortId :
					this.guesstBusinessShortIds(artifactIds, type, applicationName, appShortId)) {
					
					// Builds the CurrentBusinessModuleDto object
					CurrentBusinessModuleDto currentBusinessModule =
							this.buildCurrentBusinessModuleDto(application, businessShortId, type);
					currentBusinessModules.add(currentBusinessModule);
					
					// Removes the already used artifactIds from the set 
					for (ModuleProjectDto moduleProject : currentBusinessModule.getModules()) {
						artifactIds.remove(moduleProject.getArtifactId());
					}
				}
			}
		}
		
		return currentBusinessModules;
	}
	
	/**
	 * Given a set of artifact ids, a business module type and the names of the application,
	 * guesses the set of business short ids from the artifact ids that matches the business module type
	 * @param artifactIds set of artifact ids
	 * @param type BusinessModuleTypeDto
	 * @param applicationName the application name
	 * @param appShortId the application short id
	 * @return set of matching business short ids, or empty set if no matches
	 */
	private Set<String> guesstBusinessShortIds(
			Set<String> artifactIds, BusinessModuleTypeDto type, String applicationName, String appShortId) {
		
		Iterator<ProjectTemplateDto> projectTemplateIt = type.getProjectTemplates().iterator();
		
		// Initial set of business short ids from the first artifact id template
		final String firstArtifactIdTemplate = String.format(
				projectTemplateIt.next().getArtifactIdFormatter(), applicationName, appShortId, "?");
		Set<String> businessShortIds = this.extractBusinessShortIds(artifactIds, firstArtifactIdTemplate);
		
		// Keeps only the business short ids that matches the other artifact id templates 
		while (projectTemplateIt.hasNext()) {
			final String artifactIdTemplate = String.format(projectTemplateIt.next().getArtifactIdFormatter(),
					applicationName, appShortId, "?");
			businessShortIds.retainAll(this.extractBusinessShortIds(artifactIds, artifactIdTemplate));
		}
		
		// TODO improvements required here: common-util and core-util are indistinguishable from each other
		
		return businessShortIds;
	}
	
	/**
	 * Given an artifact id template and a set of artifact ids,
	 * extracts the set of business short ids from the artifact ids that matches the artifact id template
	 * @param artifactIds set of artifact ids
	 * @param artifactIdTemplate artifact id template
	 * @return set of matching business short ids, or empty set if no matches
	 */
	private Set<String> extractBusinessShortIds(Set<String> artifactIds, String artifactIdTemplate) {
		
		// TODO translate the original artifactIdFormatter to a regex and use InputValidator.APPSHORTID
		
		final String prefix = StringUtils.substringBefore(artifactIdTemplate, "?");
		final String suffix = StringUtils.substringAfter(artifactIdTemplate, "?");
		
		Set<String> businessShortIds = new HashSet<String>();
		for (String artifactId : artifactIds) {
			if (StringUtils.startsWith(artifactId, prefix) && StringUtils.endsWith(artifactId, suffix)) {
				
				// (match)
				String businessShortId = StringUtils.removeStart(StringUtils.removeEnd(artifactId, suffix), prefix);
				businessShortIds.add(businessShortId);
			}
		}
		
		return businessShortIds;
	}
	
	/**
	 * Builds the CurrentBusinessModuleDto object for the given application and business module name and type 
	 * @param application ApplicationDto
	 * @param businessShortId the business module short id
	 * @param type the business module type
	 * @return CurrentBusinessModuleDto
	 */
	private CurrentBusinessModuleDto buildCurrentBusinessModuleDto(
			ApplicationDto application, String businessShortId, BusinessModuleTypeDto type) {
		
		// (convenience constants)
		final String applicationName = application.getArtifactId();
		final String appShortId = application.getAppShortId();
		
		// Builds the list of ModuleProjectDto of the application that belong to this business module
		List<ModuleProjectDto> modules = new ArrayList<ModuleProjectDto>();
		for (ProjectTemplateDto projectTemplate : type.getProjectTemplates()) {
			
			// Computes the expected artifactId
			final String artifactIdFormatter = projectTemplate.getArtifactIdFormatter();
			final String artifactId = String.format(artifactIdFormatter, applicationName, appShortId, businessShortId);
			
			// Locates the ModuleProjectDto
			for (ModuleProjectDto moduleProject : application.getModules()) {
				if (!StringUtils.equals(artifactId, moduleProject.getArtifactId())) {
					continue;
				}
				
				// (saves the correct businessShortId)
				moduleProject.setBusinessShortId(businessShortId);
				modules.add(moduleProject);
			}
		}
		
		// Builds the CurrentBusinessModuleDto object
		CurrentBusinessModuleDto businessModule = new CurrentBusinessModuleDto();
		businessModule.setBusinessShortId(businessShortId);
		businessModule.setType(type);
		businessModule.setModules(modules);
		return businessModule;
	}

	/**
	 * Checks if the ModuleProjectDto contains at least one of the dependencies
	 * @param project ModuleProjectDto
	 * @param dependencies Collection of MavenCoordinatesDto
	 * @return true if the ModuleProjectDto contains all the dependencies
	 */
	private boolean dependsOnAny(ModuleProjectDto project, Collection<MavenCoordinatesDto> dependencies) {
		
		// (sanity checks)
		if ((dependencies == null) || dependencies.isEmpty()) {
			return false;
		}
		
		for (MavenCoordinatesDto dependency : dependencies) {
			if (this.dependsOn(project, dependency)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Checks if the ModuleProjectDto contains all the dependencies
	 * @param project ModuleProjectDto
	 * @param dependencies Collection of MavenCoordinatesDto
	 * @param requireAtLeastOne true to return false if there are no dependencies, false otherwise
	 * @return true if the ModuleProjectDto contains all the dependencies,
	 * or the dependency set was empty and it was not required to have at least one
	 */
	private boolean dependsOnAll(ModuleProjectDto project,
			Collection<MavenCoordinatesDto> dependencies, boolean requireAtLeastOne) {
		
		// (sanity checks)
		if ((dependencies == null) || dependencies.isEmpty()) {
			return !requireAtLeastOne;
		}
		
		for (MavenCoordinatesDto dependency : dependencies) {
			if (!this.dependsOn(project, dependency)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the ModuleProjectDto contains the dependency
	 * @param project ModuleProjectDto
	 * @param dependency MavenCoordinatesDto
	 * @return true if the ModuleProjectDto contains the dependency
	 */
	private boolean dependsOn(ModuleProjectDto project, MavenCoordinatesDto dependency) {
		
		// (sanity check)
		if (project == null) {
			return false;
		}
		
		return MavenCoordinatesUtil.contains(project.getTerasolunaPlusDependencies(), dependency);
	}
}
