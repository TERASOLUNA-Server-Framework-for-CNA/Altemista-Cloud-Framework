/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

import java.io.Serializable;

import cloud.altemista.fwk.plugin.config.Constant;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A business module that is to be added to an existing TSF+ application
 * @author NTT DATA
 */
public class NewBusinessModuleDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 1953983359887982366L;

	/** The business shortId of the business module this module project forms part of */
	private String businessShortId;
	
	/** The type of the business module (e.g.: common, common-core, web-common-core) */
	private String type;

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
				.append("businessShortId", this.businessShortId)
				.append("type", this.type);
	}

	/**
	 * @return the businessShortId
	 */
	public String getBusinessShortId() {
		return businessShortId;
	}

	/**
	 * @param businessShortId the businessShortId to set
	 */
	public void setBusinessShortId(String businessShortId) {
		this.businessShortId = businessShortId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
