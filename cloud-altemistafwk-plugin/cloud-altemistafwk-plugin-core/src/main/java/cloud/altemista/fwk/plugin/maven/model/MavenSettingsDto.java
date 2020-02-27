/**
 * 
 */
package cloud.altemista.fwk.plugin.maven.model;

import java.io.Serializable;

import cloud.altemista.fwk.plugin.config.Constant;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Maven global and user settings location container
 * @author NTT DATA
 */
public class MavenSettingsDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 4453611211905284389L;

	/** The location of the Maven global settings */
	private String mavenGlobalSettingsLocation;
	
	/** The location of the Maven user settings */
	private String mavenUserSettingsLocation;
	
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
				.append("mavenGlobalSettingsLocation", this.mavenGlobalSettingsLocation)
				.append("mavenUserSettingsLocation", this.mavenUserSettingsLocation);
	}

	/**
	 * @return the mavenGlobalSettingsLocation
	 */
	public String getMavenGlobalSettingsLocation() {
		return this.mavenGlobalSettingsLocation;
	}

	/**
	 * @param mavenGlobalSettingsLocation the mavenGlobalSettingsLocation to set
	 */
	public void setMavenGlobalSettingsLocation(String mavenGlobalSettingsLocation) {
		this.mavenGlobalSettingsLocation = mavenGlobalSettingsLocation;
	}

	/**
	 * @return the mavenUserSettingsLocation
	 */
	public String getMavenUserSettingsLocation() {
		return this.mavenUserSettingsLocation;
	}

	/**
	 * @param mavenUserSettingsLocation the mavenUserSettingsLocation to set
	 */
	public void setMavenUserSettingsLocation(String mavenUserSettingsLocation) {
		this.mavenUserSettingsLocation = mavenUserSettingsLocation;
	}

}
