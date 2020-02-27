package cloud.altemista.fwk.plugin.core.model;

import java.util.List;

import cloud.altemista.fwk.plugin.common.model.MavenProjectDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The TSF+ application
 * @author NTT DATA
 */
public class ApplicationDto extends MavenProjectDto implements ApplicationDtoProvider {

	/** The serialVersionUID */
	private static final long serialVersionUID = -5394281244656726508L;
	
	/** The appShortId of the whole application */
	private String appShortId;

	/** The TSF+ version of the whole application */
	private String terasolunaPlusVersion;

	/** The read module projects */
	private List<ModuleProjectDto> modules;
	
	/** Optionally, points to the module project that acts as the aggregator project of this application */
	private transient ModuleProjectDto aggregatorProject;
	
	/** Optionally, points to the module project that will contain configurations for shared implementations */
	private transient ModuleProjectDto sharedEnvironmentProject;

	/** Optionally, contains the business modules of this application */
	private transient List<CurrentBusinessModuleDto> currentBusinessModules;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider#getApplication()
	 */
	@Override
	public ApplicationDto getApplication() {
		
		return this;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("appShortId", this.appShortId)
				.append("terasolunaPlusVersion", this.terasolunaPlusVersion);
	}

	/**
	 * @return the appShortId
	 */
	public String getAppShortId() {
		return this.appShortId;
	}

	/**
	 * @param appShortId the appShortId to set
	 */
	public void setAppShortId(String appShortId) {
		this.appShortId = appShortId;
	}

	/**
	 * @return the terasolunaPlusVersion
	 */
	public String getTerasolunaPlusVersion() {
		return this.terasolunaPlusVersion;
	}

	/**
	 * @param terasolunaPlusVersion the terasolunaPlusVersion to set
	 */
	public void setTerasolunaPlusVersion(String terasolunaPlusVersion) {
		this.terasolunaPlusVersion = terasolunaPlusVersion;
	}

	/**
	 * @return the modules
	 */
	public List<ModuleProjectDto> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<ModuleProjectDto> modules) {
		this.modules = modules;
	}

	/**
	 * @return the aggregatorProject
	 */
	public ModuleProjectDto getAggregatorProject() {
		return aggregatorProject;
	}

	/**
	 * @param aggregatorProject the aggregatorProject to set
	 */
	public void setAggregatorProject(ModuleProjectDto aggregatorProject) {
		this.aggregatorProject = aggregatorProject;
	}

	/**
	 * @return the sharedEnvironmentProject
	 */
	public ModuleProjectDto getSharedEnvironmentProject() {
		return sharedEnvironmentProject;
	}

	/**
	 * @param sharedEnvironmentProject the sharedEnvironmentProject to set
	 */
	public void setSharedEnvironmentProject(ModuleProjectDto sharedEnvironmentProject) {
		this.sharedEnvironmentProject = sharedEnvironmentProject;
	}

	/**
	 * @return the currentBusinessModules
	 */
	public List<CurrentBusinessModuleDto> getCurrentBusinessModules() {
		return this.currentBusinessModules;
	}

	/**
	 * @param currentBusinessModules the currentBusinessModules to set
	 */
	public void setCurrentBusinessModules(List<CurrentBusinessModuleDto> currentBusinessModules) {
		this.currentBusinessModules = currentBusinessModules;
	}
}
