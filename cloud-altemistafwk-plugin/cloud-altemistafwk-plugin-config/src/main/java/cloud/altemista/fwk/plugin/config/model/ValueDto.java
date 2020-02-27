/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.util.YamlUtil;

/**
 * Base class for configuration values: can be mapped to a String value and can be version-dependent
 * @author NTT DATA
 */
public class ValueDto implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -4931564406427561902L;

	/** The internal value */
	private String value;
	
	/** The string representation of the versions this object depends on */
	private String versions;
	
	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(
				ValueDto.class, "avaiableOn", String.class); //$NON-NLS-1$
	}
	
	/**
	 * Marks a value as deprecated: this allows compatibility with previous versions
	 * while hiding values that should not be used for new projects
	 * (e.g.: version X.Y.0 should be disabled if version X.Y.1 exists )
	 */
	private boolean deprecated;
	
	/** Cached version range */
	private transient VersionRange versionRange;
	
	private List<String> avaiableOn;
	
	/**
	 * Checks if this version-dependent object is valid for an specific version
	 * @param version ArtifactVersion with the version
	 * @return true if the object is enabled and the version range contains the version, false otherwise.
	 * If this object has no information about the version range,
	 * or the version is null or empty, this method returns true
	 */
	public boolean isValidFor(ArtifactVersion version) {
		
		if ((version == null) || (this.versionRange == null)) {
			return true;
		}
		
		return this.versionRange.containsVersion(version);
	}
	
	public boolean isValidFor(String archValue){
		if(avaiableOn==null || avaiableOn.isEmpty())
			return false;
		return avaiableOn.contains(archValue);
	}

	/**
	 * @param versions the versions to set
	 * @throws InvalidVersionSpecificationException if VersionRange.createFromVersionSpec fails
	 */
	public void setVersions(String versions) throws InvalidVersionSpecificationException {
		
		this.versions = versions;
		
		// Parses the version range
		if (StringUtils.isEmpty(this.versions)) {
			this.versionRange = null;
		} else {
			this.versionRange = VersionRange.createFromVersionSpec(this.versions);
		}
	}

	/**
	 * Duplicated getter (with get* prefix instead of is*) to avoid
	 * <code>YamlReaderException: Unable to find property 'deprecated' on class: o.t.p.p.c.m.OptionDto</code>
	 * @return the deprecated
	 * @deprecated For YamlReader only. Use {@link #isDeprecated()} instead
	 */
	@Deprecated
	public boolean getDeprecated() {
		return this.isDeprecated();
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
				.append("value", this.value) //$NON-NLS-1$
				.append("versions", this.versions)
				.append("avaiableOn", this.avaiableOn); //$NON-NLS-1$
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the versions
	 */
	public String getVersions() {
		return this.versions;
	}

	/**
	 * @return the deprecated
	 */
	public boolean isDeprecated() {
		return this.deprecated;
	}

	/**
	 * @param deprecated the deprecated to set
	 */
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public List<String> getAvaiableOn() {
		return avaiableOn;
	}

	public void setAvaiableOn(List<String> avaiableOn) {
		this.avaiableOn = avaiableOn;
	}
	
	
	
}
