/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.config.util.YamlUtil;

/**
 * Module project features (e.g.: common-connection, core-workflow, webapp-jsf)
 * @author NTT DATA
 */
public class FeatureDto extends OptionWithDependenciesDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5267571444103822755L;

	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(FeatureDto.class, "requirementSets", RequirementSetDto.class); //$NON-NLS-1$
		YamlUtil.CONFIG.setPropertyElementType(FeatureDto.class, "incompatibilities", MavenCoordinatesDto.class); //$NON-NLS-1$
		YamlUtil.CONFIG.setPropertyElementType(FeatureDto.class, "implementations", FeatureImplementationDto.class); //$NON-NLS-1$
		YamlUtil.CONFIG.setPropertyElementType(FeatureDto.class, "additionalProperties", OptionWithDefaultDto.class); //$NON-NLS-1$
	}
	
	/** Sets of required dependencies for this feature to be installable */
	private List<RequirementSetDto> requirementSets;

	/** Incompatible dependencies that disallow this feature for being installable */
	private List<MavenCoordinatesDto> incompatibilities;
	
	/**
	 * This feature is installed by itself (e.g.: common-connection, webapp-jsf).
	 * A self-implemented feature will not show a list of implementations to choose
	 * but will be installed from a partial archetype (if {@link #archetype} is not null)
	 * or simply including its {@link OptionWithDependenciesDto#dependencies} (if {@link #archetype} is null)
	 */
	private boolean selfImplemented;

	/** The Maven artifact coordinates of the partial archetype for this feature */
	private MavenCoordinatesDto archetype;
	
	/** The list of possible implementations */
	private List<FeatureImplementationDto> implementations;
	
	/** The list of possible additional properties for the Maven archetype(s) */
	private List<OptionWithDefaultDto> additionalProperties;
	
	/**
	 * Duplicated getter (with get* prefix instead of is*) to avoid
	 * <code>YamlReaderException: Unable to find property 'selfImplemented' on class: o.t.p.p.c.m.FeatureDto</code>
	 * @return the deprecated
	 * @deprecated For YamlReader only. Use {@link #isSelfImplemented()} instead
	 */
	@Deprecated
	public boolean getSelfImplemented() {
		return this.isSelfImplemented();
	}
	
	/**
	 * Convenience method to check if this feature uses an archetype or just a set of dependencies
	 * @return if this feature uses an archetype
	 */
	public boolean hasArchetype() {
		
		return this.archetype != null; 
	}
	
	/**
	 * Convenience method to check if this feature uses an archetype with additional properties
	 * @return if this feature uses an archetype with additional properties
	 */
	public boolean hasArchetypeAdditionalProperties() {
		
		return (this.additionalProperties != null)
				&& (!this.additionalProperties.isEmpty());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("requirementSets", this.requirementSets) //$NON-NLS-1$
				.append("incompatibilities", this.incompatibilities) //$NON-NLS-1$
				.append("selfImplemented", this.selfImplemented) //$NON-NLS-1$
				.append("archetype", this.archetype) //$NON-NLS-1$
				.append("implementations", this.implementations) //$NON-NLS-1$
				.append("additionalProperties", this.additionalProperties); //$NON-NLS-1$
	}
	
	/**
	 * @return the requirementSets
	 */
	public List<RequirementSetDto> getRequirementSets() {
		return requirementSets;
	}

	/**
	 * @param requirementSets the requirementSets to set
	 */
	public void setRequirementSets(List<RequirementSetDto> requirementSets) {
		this.requirementSets = requirementSets;
	}

	/**
	 * @return the incompatibilities
	 */
	public List<MavenCoordinatesDto> getIncompatibilities() {
		return this.incompatibilities;
	}

	/**
	 * @param incompatibilities the incompatibilities to set
	 */
	public void setIncompatibilities(List<MavenCoordinatesDto> incompatibilities) {
		this.incompatibilities = incompatibilities;
	}

	/**
	 * @return the selfImplemented
	 */
	public boolean isSelfImplemented() {
		return selfImplemented;
	}

	/**
	 * @param selfImplemented the selfImplemented to set
	 */
	public void setSelfImplemented(boolean selfImplemented) {
		this.selfImplemented = selfImplemented;
	}
	
	/**
	 * @return the archetype
	 */
	public MavenCoordinatesDto getArchetype() {
		return archetype;
	}

	/**
	 * @param archetype the archetype to set
	 */
	public void setArchetype(MavenCoordinatesDto archetype) {
		this.archetype = archetype;
	}
	
	/**
	 * @return the implementations
	 */
	public List<FeatureImplementationDto> getImplementations() {
		return this.implementations;
	}

	/**
	 * @param implementations the implementations to set
	 */
	public void setImplementations(List<FeatureImplementationDto> implementations) {
		this.implementations = implementations;
	}

	/**
	 * @return the additionalProperties
	 */
	public List<OptionWithDefaultDto> getAdditionalProperties() {
		return additionalProperties;
	}

	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	public void setAdditionalProperties(List<OptionWithDefaultDto> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
