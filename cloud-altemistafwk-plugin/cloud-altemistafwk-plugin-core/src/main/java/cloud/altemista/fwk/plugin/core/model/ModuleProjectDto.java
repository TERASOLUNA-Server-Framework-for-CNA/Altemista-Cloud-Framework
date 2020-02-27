/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.common.model.MavenProjectDto;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;

/**
 * Any TSF+ Maven project that forms part of an application:
 * the application project, aggregator project or module project
 * @author NTT DATA
 */
public class ModuleProjectDto extends MavenProjectDto implements ApplicationDtoProvider {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5666865362727385746L;
	
	/** The business shortId of the business module this module project forms part of */
	private String businessShortId;
	
	/** The TSF+ dependencies the project had when it was read */
	private List<MavenCoordinatesDto> terasolunaPlusDependencies;

	/** The parent application */
	private ApplicationDto application;
	
	/** The read current module project types of the project */
	private List<ModuleProjectTypeDto> currentTypes;
	
	/** The read current features of the project */
	private List<CurrentFeatureDto> currentFeatures;
	
	/** Flag for the plugin to mark which project have modifications after installing a feature */
	private transient boolean touched;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("businessShortId", this.businessShortId)
				.append("terasolunaPlusDependencies", this.terasolunaPlusDependencies)
				.append("application", this.application)
				.append("currentTypes", this.currentTypes)
				.append("currentFeatures", this.currentFeatures);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider#getApplication()
	 */
	@Override
	public ApplicationDto getApplication() {
		
		return this.application;
	}

	/**
	 * @return the businessShortId
	 */
	public String getBusinessShortId() {
		return businessShortId;
	}

	/**
	 * @param businessShortId the businessShortId to set
	 */
	public void setBusinessShortId(String businessShortId) {
		this.businessShortId = businessShortId;
	}

	/**
	 * @return the terasolunaPlusDependencies
	 */
	public List<MavenCoordinatesDto> getTerasolunaPlusDependencies() {
		return terasolunaPlusDependencies;
	}

	/**
	 * @param terasolunaPlusDependencies the terasolunaPlusDependencies to set
	 */
	public void setTerasolunaPlusDependencies(List<MavenCoordinatesDto> terasolunaPlusDependencies) {
		this.terasolunaPlusDependencies = terasolunaPlusDependencies;
	}

	/**
	 * @param application the application to set
	 */
	public void setApplication(ApplicationDto application) {
		this.application = application;
	}

	/**
	 * @return the currentTypes
	 */
	public List<ModuleProjectTypeDto> getCurrentTypes() {
		return currentTypes;
	}

	/**
	 * @param currentTypes the currentTypes to set
	 */
	public void setCurrentTypes(List<ModuleProjectTypeDto> currentTypes) {
		this.currentTypes = currentTypes;
	}

	/**
	 * @return the currentFeatures
	 */
	public List<CurrentFeatureDto> getCurrentFeatures() {
		return this.currentFeatures;
	}

	/**
	 * @param currentFeatures the currentFeatures to set
	 */
	public void setCurrentFeatures(List<CurrentFeatureDto> currentFeatures) {
		this.currentFeatures = currentFeatures;
	}

	/**
	 * @return the touched
	 */
	public boolean isTouched() {
	
		return this.touched;
	}

	/**
	 * @param touched the touched to set
	 */
	public void setTouched(boolean touched) {
	
		this.touched = touched;
	}
}
