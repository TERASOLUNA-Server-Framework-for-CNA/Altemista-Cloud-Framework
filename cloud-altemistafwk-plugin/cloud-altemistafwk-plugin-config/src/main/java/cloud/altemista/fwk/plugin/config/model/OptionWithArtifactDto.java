/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;

import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Selectable option that is related to a Maven artifact (e.g.: archetypes, module project types, etc.)
 * @author NTT DATA
 */
public class OptionWithArtifactDto extends OptionDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5267571444103822755L;

	/** The Maven artifact coordinates */
	private MavenCoordinatesDto artifact;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("artifact", this.artifact); //$NON-NLS-1$
	}

	/**
	 * @return the artifact
	 */
	public MavenCoordinatesDto getArtifact() {
		return artifact;
	}

	/**
	 * @param artifact the artifact to set
	 */
	public void setArtifact(MavenCoordinatesDto artifact) {
		this.artifact = artifact;
	}

}
