/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.util.List;

import cloud.altemista.fwk.plugin.config.util.YamlUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Business module type (e.g.: common, common-core-web)
 * @author NTT DATA
 */
public class BusinessModuleTypeDto extends OptionDto {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 6933976580005510639L;

	static {
		// Configures this class in the YamlUtil reader 
		YamlUtil.CONFIG.setPropertyElementType(
				BusinessModuleTypeDto.class, "projectTemplates", ProjectTemplateDto.class); //$NON-NLS-1$
	}
	

	/** The projects that compose this type of business module */
	private List<ProjectTemplateDto> projectTemplates;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto#toStringBuilder()
	 */
	@Override
	protected ToStringBuilder toStringBuilder() {
		
		return super.toStringBuilder()
				.append("projectTemplates", this.projectTemplates); //$NON-NLS-1$
	}

	/**
	 * @return the projectTemplates
	 */
	public List<ProjectTemplateDto> getProjectTemplates() {
		return projectTemplates;
	}

	/**
	 * @param projectTemplates the projectTemplates to set
	 */
	public void setProjectTemplates(List<ProjectTemplateDto> projectTemplates) {
		this.projectTemplates = projectTemplates;
	}

}
