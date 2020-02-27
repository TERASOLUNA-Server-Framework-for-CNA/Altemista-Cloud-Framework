/**
 * 
 */
package cloud.altemista.fwk.plugin.common.model;

import java.io.Serializable;

import cloud.altemista.fwk.plugin.config.Constant;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Maven artifacts coordinates; represents a logical Maven project
 * @author NTT DATA
 */
public class MavenCoordinatesDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 4625874123347694608L;

	/** The groupId */
	private String groupId;
	
	/** The artifactId */
	private String artifactId;
	
	/** The version */
	private String version;
	
	/** The scope (if present or necessary) */
	private String scope;
	
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
				.append("groupId", this.groupId) //$NON-NLS-1$
				.append("artifactId", this.artifactId) //$NON-NLS-1$
				.append("version", this.version) //$NON-NLS-1$
				.append("scope", this.scope); //$NON-NLS-1$
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return this.scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

}
