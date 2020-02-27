/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Concrete implementation of a module project feature (e.g.: core-workflow-activiti)
 * @author NTT DATA
 */
public class FeatureImplementationDto extends OptionWithDependenciesDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5267571444103822755L;

	/** The Maven artifact coordinates of the partial archetype for this implementation */
	private MavenCoordinatesDto archetype;
	
	/**
	 * Convenience method to check if the feature implementation to be installed uses an archetype
	 * @return if this feature implementation uses an archetype
	 */
	public boolean hasArchetype() {
		
		return this.archetype != null; 
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("archetype", this.archetype); //$NON-NLS-1$
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
}
