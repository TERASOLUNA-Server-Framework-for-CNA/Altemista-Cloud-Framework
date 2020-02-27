/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.util.YamlUtil;

/**
 * Set of required dependencies
 * @author NTT DATA
 */
public class RequirementSetDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 267552622877317146L;

	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(RequirementSetDto.class, "requirements", MavenCoordinatesDto.class); //$NON-NLS-1$
	}
	
	/** Required dependencies */
	private List<MavenCoordinatesDto> requirements;
	
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
				.append("requirements", this.requirements); //$NON-NLS-1$
	}

	/**
	 * @return the requirements
	 */
	public List<MavenCoordinatesDto> getRequirements() {
		return this.requirements;
	}

	/**
	 * @param requirements the requirements to set
	 */
	public void setRequirements(List<MavenCoordinatesDto> requirements) {
		this.requirements = requirements;
	}
}
