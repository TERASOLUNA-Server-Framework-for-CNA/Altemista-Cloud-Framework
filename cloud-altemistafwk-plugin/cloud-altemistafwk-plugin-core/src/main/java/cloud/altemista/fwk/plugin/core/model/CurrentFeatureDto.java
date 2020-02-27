/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Existing module project feature, including its status (available, installed, incompatible, etc.)
 * @author NTT DATA
 */
public class CurrentFeatureDto extends FeatureDto {

	/** The serialVersionUID */
	private static final long serialVersionUID = 2941513936822725201L;
	
	/** Does the module project meet the requirements to install this feature? */ 
	private boolean meetsRequirements;
	
	/** Is the feature currently installed in the module project? */
	private boolean installed;
	
	/** Is the feature compatible with all the module project dependencies? */
	private boolean compatible;
	
	/** The current implementation of this feature in the application */
	private transient FeatureImplementationDto currentImplementation;
	
	/** The module project that is currently implementing this feature */
	private transient ModuleProjectDto currentProjectImplementing;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("meetsRequirements", this.meetsRequirements)
				.append("installed", this.installed)
				.append("compatible", this.compatible);
	}
	
	/**
	 * @return the meetsRequirements
	 */
	public boolean isMeetsRequirements() {
		return meetsRequirements;
	}

	/**
	 * @param meetsRequirements the meetsRequirements to set
	 */
	public void setMeetsRequirements(boolean meetsRequirements) {
		this.meetsRequirements = meetsRequirements;
	}

	/**
	 * @return the installed
	 */
	public boolean isInstalled() {
		return this.installed;
	}

	/**
	 * @param installed the installed to set
	 */
	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	/**
	 * @return the compatible
	 */
	public boolean isCompatible() {
		return this.compatible;
	}

	/**
	 * @param compatible the compatible to set
	 */
	public void setCompatible(boolean compatible) {
		this.compatible = compatible;
	}

	/**
	 * @return the currentImplementation
	 */
	public FeatureImplementationDto getCurrentImplementation() {
		return currentImplementation;
	}

	/**
	 * @param currentImplementation the currentImplementation to set
	 */
	public void setCurrentImplementation(FeatureImplementationDto currentImplementation) {
		this.currentImplementation = currentImplementation;
	}

	/**
	 * @return the currentProjectImplementing
	 */
	public ModuleProjectDto getCurrentProjectImplementing() {
		return currentProjectImplementing;
	}

	/**
	 * @param currentProjectImplementing the currentProjectImplementing to set
	 */
	public void setCurrentProjectImplementing(ModuleProjectDto currentProjectImplementing) {
		this.currentProjectImplementing = currentProjectImplementing;
	}
}
