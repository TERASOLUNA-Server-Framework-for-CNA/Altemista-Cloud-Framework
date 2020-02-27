/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.config.util.YamlUtil;

/**
 * Selectable option that is related to a set of dependencies (e.g.: features, feature implementations, etc.)
 * @author NTT DATA
 */
public class OptionWithDependenciesDto extends OptionDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5267571444103822755L;

	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(
				OptionWithDependenciesDto.class, "dependencies", MavenCoordinatesDto.class); //$NON-NLS-1$
	}
	
	/** The set of dependencies */
	private List<MavenCoordinatesDto> dependencies;
	
	/**
	 * Convenience method to check if the set of dependencies is empty
	 * @return true if the set of dependencies has at least one dependency
	 */
	public boolean hasDependencies() {
		
		return (this.dependencies != null) && (!this.dependencies.isEmpty()); 
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("dependencies", this.dependencies); //$NON-NLS-1$
	}

	/**
	 * @return the dependencies
	 */
	public List<MavenCoordinatesDto> getDependencies() {
		return this.dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<MavenCoordinatesDto> dependencies) {
		this.dependencies = dependencies;
	}
}
