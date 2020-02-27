/**
 * 
 */
package cloud.altemista.fwk.plugin.common.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A physical Maven project
 * @author NTT DATA
 */
public class MavenProjectDto extends MavenCoordinatesDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -4236907854432529105L;

	/** The project location */
	private String location;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("location", this.location); //$NON-NLS-1$
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
