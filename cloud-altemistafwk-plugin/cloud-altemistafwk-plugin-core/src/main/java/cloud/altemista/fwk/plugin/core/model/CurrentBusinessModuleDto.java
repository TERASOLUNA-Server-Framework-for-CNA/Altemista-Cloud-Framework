package cloud.altemista.fwk.plugin.core.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;

/**
 * Existing business module
 * @author NTT DATA
 */
public class CurrentBusinessModuleDto implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 4919428803860707403L;

	/** The business shortId of the business module */
	private String businessShortId;
	
	/** The matching type of the business module (e.g.: common, common-core, web-common-core) */
	private BusinessModuleTypeDto type;
	
	/** The module projects composing this business module */
	private List<ModuleProjectDto> modules;

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
		return this.businessShortId;
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
	public BusinessModuleTypeDto getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(BusinessModuleTypeDto type) {
		this.type = type;
	}

	/**
	 * @return the modules
	 */
	public List<ModuleProjectDto> getModules() {
		return this.modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<ModuleProjectDto> modules) {
		this.modules = modules;
	}

}
