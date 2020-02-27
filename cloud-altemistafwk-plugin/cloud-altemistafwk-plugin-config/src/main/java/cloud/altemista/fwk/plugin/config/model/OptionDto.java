/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a selectable option, typically in a combo box.
 * @author NTT DATA
 */
public class OptionDto extends ValueDto implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = -7417456822205052238L;
	
	/** The human-readable value (i.e.: the text to be displayed in a combo box) */
	private String text;
	
	/** Optional description */
	private String description;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.VersionDependentDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("text", this.text) //$NON-NLS-1$
				.append("description", this.description); //$NON-NLS-1$
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
