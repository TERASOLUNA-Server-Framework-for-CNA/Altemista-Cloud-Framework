package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.ProjectTemplateDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.plugin.core.util.InputValidator;
import cloud.altemista.fwk.plugin.core.util.StringUtil;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.TextUtil;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;
import cloud.altemista.fwk.plugin.eclipse.util.DescriptionListener;

/**
 * The "New business module" page
 * @author NTT DATA
 */
public class BusinessModulePage extends AbstractWizardPage {
	
	/** The tooltip for the editable business module name */
	private static final String BUSINESSMODULENAME_TOOLTIP = "An alphanumeric short identifier of the business module";

	/** The applicationProvider */
	private final ApplicationDtoProvider applicationProvider;
	
	/** The editable business module name */
	protected Text businessModuleName;

	/** The selectable business module type */
	public Combo businessModuleType;

	/** The read-only business module type description */
	protected Text businessModuleDescription;

	/** The read-only groupId of the business module project(s) */
	protected Text groupId;

	/** The read-only artifactId of the business module project(s) */
	protected Text artifactId;

	/** The read-only version of the business module project(s) */
	protected Text version;
	
	/** The read-only package of the business module project(s) */
	protected Text package_;

	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 */
	public BusinessModulePage(ApplicationDtoProvider applicationProvider) {
		this("New business module", applicationProvider);
	}

	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 * @param title the title for this wizard page
	 */
	protected BusinessModulePage(String title, ApplicationDtoProvider applicationProvider) {
		super("BusinessModulePage", title);
		
		this.applicationProvider = applicationProvider;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	public void buildControls(ComponentBuilder builder) {
		
		// Business module name
		this.businessModuleName = builder.appendText("Business module &name:");
		this.businessModuleName.setTextLimit(InputValidator.APPSHORTID.getMaxLength());
		builder.decorateControl(
				this.businessModuleName, FieldDecorationRegistry.DEC_CONTENT_PROPOSAL, BUSINESSMODULENAME_TOOLTIP);
		this.businessModuleName.setToolTipText(BUSINESSMODULENAME_TOOLTIP);
		builder.finishRow();
		
		// Business module type
		this.businessModuleType = builder.appendCombo("Business module &type");
		
        // Check if is a TSF o Altemista version
        List<String> listTSFVersions = Arrays.asList(Constant.TSF_VERSIONS);
        boolean terasolunaProject = false;
		if(listTSFVersions.contains(this.applicationProvider.getApplication().getTerasolunaPlusVersion())){
			terasolunaProject = true;
		}
		
		if(terasolunaProject){
		
			if (this.applicationProvider.getApplication().getAggregatorProject() != null){
				ComboUtil.setOptions(this.businessModuleType, Config.BUSINESS_MODULES.values(
					this.applicationProvider.getApplication().getTerasolunaPlusVersion(), this.applicationProvider.getApplication().getAggregatorProject().getCurrentTypes().get(0)),false);
				builder.finishRow();
			}else{
				ComboUtil.setOptions(this.businessModuleType, Config.BUSINESS_MODULES.values(
					this.applicationProvider.getApplication().getTerasolunaPlusVersion()), false);
				builder.finishRow();
			}
			
		}else{
			
			if (this.applicationProvider.getApplication().getAggregatorProject() != null){
				ComboUtil.setOptions(this.businessModuleType, Config.BUSINESS_MODULES_ALTEMISTA.values(
					this.applicationProvider.getApplication().getTerasolunaPlusVersion(), this.applicationProvider.getApplication().getAggregatorProject().getCurrentTypes().get(0)),false);
				builder.finishRow();
			}else{
				ComboUtil.setOptions(this.businessModuleType, Config.BUSINESS_MODULES_ALTEMISTA.values(
					this.applicationProvider.getApplication().getTerasolunaPlusVersion()), false);
				builder.finishRow();
			}
		}
		
		
		
		this.businessModuleDescription = builder.appendReadOnlyTextArea("", 4);
		builder.finishRow();

		DescriptionListener.on(this.businessModuleType, this.businessModuleDescription);
		
		// Creates the "Maven attributes" section
		{
			ComponentBuilder mavenAttributesBuilder = builder.appendGroup("Maven attributes:");
			
			this.groupId = mavenAttributesBuilder.appendReadOnlyText("groupId:");
			mavenAttributesBuilder.finishRow();
			
			this.artifactId = mavenAttributesBuilder.appendReadOnlyText("artifactId:");
			mavenAttributesBuilder.finishRow();
	
			this.version = mavenAttributesBuilder.appendReadOnlyText("version:");
			mavenAttributesBuilder.finishRow();
		}

		// Module package
		this.package_ = builder.appendReadOnlyText("Module package:");
		builder.finishRow();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#initializePage()
	 */
	protected void initializePage() {
		super.initializePage();

		this.businessModuleName.setText(Constant.DEFAULT_BUSINESS_SHORT_ID);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible) {
			this.businessModuleName.setFocus();
			this.businessModuleName.setSelection(0, this.businessModuleName.getText().length());
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#updatePage()
	 */
	@Override
	protected void updatePage() {
		super.updatePage();
		
		final ApplicationDto application = this.applicationProvider.getApplication();
		
		TextUtil.cleanUsing(this.businessModuleName, InputValidator.APPSHORTID);
		
		TextUtil.setIfNot(this.groupId, application.getGroupId());
		TextUtil.setIfNot(this.artifactId, this.guessHumanReadableArtfactId());
		TextUtil.setIfNot(this.version, application.getVersion());
		
		TextUtil.setIfNot(this.package_, StringUtil.formatNullSafe("%s.%s.%s",
				application.getGroupId(), application.getAppShortId(), this.getBusinessModuleName()));
	}
	
	private String guessHumanReadableArtfactId() {
		
		final BusinessModuleTypeDto businessModuleTypeDto = ComboUtil.getOption(this.businessModuleType);
		if (businessModuleTypeDto == null) {
			return null;
		}
		
		final ApplicationDto application = this.applicationProvider.getApplication();
		
		// (honors the business module artifactIdFormatter)
		List<String> artifactIds = new ArrayList<String>();
		for (ProjectTemplateDto dto : businessModuleTypeDto.getProjectTemplates()) {
			artifactIds.add(StringUtil.formatNullSafe(dto.getArtifactIdFormatter(),
					application.getArtifactId(), application.getAppShortId(), this.getBusinessModuleName()));
		}
		
		// Combines all the artifactIds in an human readable form
		return this.combineCommonPrefix(artifactIds, "%s (%s)", "*", ", ");
	}
	
	/**
	 * Combines the common prefix of the Strings (word-boundary-wise boundary)
	 * @param list of Strings
	 * @param format pattern to format the combined String (e.g.: "%s (%s)")
	 * @param pReplacement replacement for the common prefix and the split suffixes (e.g.: "*")
	 * @param separator Separator for the parts (e.g.: ", ")
	 * @return String with the common prefix combined
	 */
	private String combineCommonPrefix(List<String> list, String format, String pReplacement, String separator) {
		
		// (sanity checks)
		if ((list == null) || list.isEmpty()) {
			return StringUtils.EMPTY;
		}
		if (list.size() == 1) {
			return StringUtils.defaultString(list.iterator().next());
		}
		
		final String[] array = list.toArray(new String[list.size()]);
		
		// Extracts the common prefix
		final String commonPrefix = StringUtil.getCommonPrefix(array);
		if (StringUtils.isEmpty(commonPrefix)) {
			// (no common prefix)
			return StringUtils.join(array, separator);
		}
		
		// Creates the combined string
		final String replacement = StringUtils.defaultString(pReplacement);
		List<String> ret = new ArrayList<String>();
		for (String value : list) {
			ret.add(replacement + StringUtils.removeStart(value, commonPrefix));
		}
		return String.format(format, commonPrefix + replacement, StringUtils.join(ret, separator));
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractWizardPage#validatePage()
	 */
	@Override
	protected void validatePage() {
		super.validatePage();
		
		if (StringUtils.isBlank(this.getBusinessModuleName())) {
			this.setError("The business module name is required");
			return;
		}
		if (!InputValidator.APPSHORTID.isValid(this.getBusinessModuleName(), true)) {
			this.setError("The business module name is invalid");
			return;
		}
		
		if (StringUtils.isBlank(this.getBusinessModuleType())) {
			this.setError("The business module type is required");
			return;
		}
	}

	/**
	 * @return the businessModuleName
	 */
	public String getBusinessModuleName() {
		return businessModuleName.getText();
	}

	/**
	 * @return the businessModuleType
	 */
	public String getBusinessModuleType() {
		return ComboUtil.getOptionValue(businessModuleType);
	}
}