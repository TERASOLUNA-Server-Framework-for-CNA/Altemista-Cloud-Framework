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
 * Template for creating the projects that compose a business module
 * @author NTT DATA
 */
public class ProjectTemplateDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 6933976580005510639L;

	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(
				ProjectTemplateDto.class, "dependencyArtifactIdFormatter", String.class); //$NON-NLS-1$
	}

	/**
	 * Format string for the artifactId (in the style of <code>String.format()</code>).
	 * The arguments will be: the applicationName, the appShortId and the businessShortId
	 */
	private String artifactIdFormatter;
	
	/** If not empty, this project will be a dependency of the aggregator with the scope specified */
	private String aggregatorScope;

	/** The Maven artifact coordinates of the partial archetype for this feature */
	private MavenCoordinatesDto archetype;

	/**
	 * Format string for relationships of this project with other projects of the same business module,
	 * in the same format as {@link #artifactIdFormatter}
	 */
	private List<String> dependencyArtifactIdFormatter;
	
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
				.append("artifactIdFormatter", this.artifactIdFormatter) //$NON-NLS-1$
				.append("archetype", this.archetype) //$NON-NLS-1$
				.append("dependencyArtifactIdFormatter", this.dependencyArtifactIdFormatter); //$NON-NLS-1$
	}


	/**
	 * @return the artifactIdFormatter
	 */
	public String getArtifactIdFormatter() {
		return artifactIdFormatter;
	}

	/**
	 * @param artifactIdFormatter the artifactIdFormatter to set
	 */
	public void setArtifactIdFormatter(String artifactIdFormatter) {
		this.artifactIdFormatter = artifactIdFormatter;
	}

	/**
	 * @return the aggregatorScope
	 */
	public String getAggregatorScope() {
		return aggregatorScope;
	}

	/**
	 * @param aggregatorScope the aggregatorScope to set
	 */
	public void setAggregatorScope(String aggregatorScope) {
		this.aggregatorScope = aggregatorScope;
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
	 * @return the dependencyArtifactIdFormatter
	 */
	public List<String> getDependencyArtifactIdFormatter() {
		return dependencyArtifactIdFormatter;
	}

	/**
	 * @param dependencyArtifactIdFormatter the dependencyArtifactIdFormatter to set
	 */
	public void setDependencyArtifactIdFormatter(List<String> dependencyArtifactIdFormatter) {
		this.dependencyArtifactIdFormatter = dependencyArtifactIdFormatter;
	}
}
