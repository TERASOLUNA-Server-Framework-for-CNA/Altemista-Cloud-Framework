/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

// TODO: Auto-generated Javadoc
/**
 * Input (namely, additional feature option) the user will be prompted to fill.
 *
 * @author NTT DATA
 */
public class OptionWithDefaultDto extends OptionDto implements Serializable {
	
	/**  The serialVersionUID. */
	private static final long serialVersionUID = 5267571444103822755L;

	/**  The default value of the option. */
	private String defaultValue;
	
	/**  The activated control names of the option. */
	private String activatedControls;
	
	/**  The deactivated control names of the option. */
	private String deactivatedControls;
	
	/** The optional. */
	private Boolean optional = false;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("defaultValue", this.defaultValue)
				.append("activatedControls", this.activatedControls)
				.append("deactivatedControls", this.deactivatedControls)
				.append("optional", this.optional);
	}

	/**
	 * Gets the default value.
	 *
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the activated controls.
	 *
	 * @return the activated controls
	 */
	public String getActivatedControls() {
		return activatedControls;
	}

	/**
	 * Sets the activated controls.
	 *
	 * @param activatedControls the new activated controls
	 */
	public void setActivatedControls(String activatedControls) {
		this.activatedControls = activatedControls;
	}

	/**
	 * Gets the deactivated controls.
	 *
	 * @return the deactivated controls
	 */
	public String getDeactivatedControls() {
		return deactivatedControls;
	}

	/**
	 * Sets the deactivated controls.
	 *
	 * @param deactivatedControls the new deactivated controls
	 */
	public void setDeactivatedControls(String deactivatedControls) {
		this.deactivatedControls = deactivatedControls;
	}

	/**
	 * Gets the optional.
	 *
	 * @return the optional
	 */
	public Boolean getOptional() {
		return optional;
	}

	/**
	 * Sets the optional.
	 *
	 * @param optional the new optional
	 */
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	
	

}
