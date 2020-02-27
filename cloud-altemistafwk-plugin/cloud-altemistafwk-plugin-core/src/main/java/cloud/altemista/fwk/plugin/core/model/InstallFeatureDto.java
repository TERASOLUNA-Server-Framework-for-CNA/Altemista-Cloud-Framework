/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;

/**
 * The TSF+ feature that is being installed
 * @author NTT DATA
 */
public class InstallFeatureDto implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = 2307644379587456557L;
	
	/** The feature that will be installed */
	private CurrentFeatureDto feature;
	
	/** The implementation that will be installed for this feature */
	private FeatureImplementationDto implementation;
	
	/** The possible additional properties for the feature (usually, if it is based on archetypes) */
	private Properties additionalProperties;
	
	/**
	 * Convenience method to check if the feature to be installed uses an archetype or just a set of dependencies
	 * @return if this feature uses an archetype
	 */
	public boolean hasArchetype() {
		
		return (this.feature != null) && this.feature.hasArchetype(); 
	}
	
	/**
	 * Convenience method to check if the feature implementation to be installed uses an archetype
	 * @return if this feature implementation uses an archetype
	 */
	public boolean hasImplementationArchetype() {
		
		return (this.implementation != null) && this.implementation.hasArchetype(); 
	}
	
	/**
	 * Convenience method to check if the feature to be installed uses an archetype with additional properties
	 * @return if this feature uses an archetype with additional properties
	 */
	public boolean hasArchetypeAdditionalProperties() {
		
		return this.feature.hasArchetypeAdditionalProperties();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		
		return this.toStringBuilder().toString();
	}

	/**
	 * Extensible ToStringBuilder
	 * @return ToStringBuilder
	 */
	protected ToStringBuilder toStringBuilder() {
		
		return new ToStringBuilder(this, Constant.TO_STRING_STYLE)
				.append("feature", this.feature)
				.append("implementation", this.implementation)
				.append("additionalProperties", this.additionalProperties);
	}

	/**
	 * @return the feature
	 */
	public CurrentFeatureDto getFeature() {
		return feature;
	}

	/**
	 * @param feature the feature to set
	 */
	public void setFeature(CurrentFeatureDto feature) {
		this.feature = feature;
	}

	/**
	 * @return the implementation
	 */
	public FeatureImplementationDto getImplementation() {
		return implementation;
	}

	/**
	 * @param implementation the implementation to set
	 */
	public void setImplementation(FeatureImplementationDto implementation) {
		this.implementation = implementation;
	}

	/**
	 * @return the additionalProperties
	 */
	public Properties getAdditionalProperties() {
		return additionalProperties;
	}

	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	public void setAdditionalProperties(Properties additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
