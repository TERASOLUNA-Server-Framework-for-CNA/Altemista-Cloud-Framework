package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.plugin.core.util.StringUtil;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.TextUtil;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;
import cloud.altemista.fwk.plugin.eclipse.util.DescriptionListener;

/**
 * The "Aggregator project of the application" page
 * @author NTT DATA
 */
public class AggregatorModulePage extends AbstractWizardPage {
	
	/** The applicationProvider */
	private final ApplicationDtoProvider applicationProvider;
	
	/** The selectable aggregator type */
	public Combo aggregatorType;
	
	/** The description listener on the selectable aggregator type */
	public DescriptionListener aggregatorTypeDescriptionListener;

	/** The read-only groupId of the aggregator project */
	private Text groupId;

	/** The read-only artifactId of the aggregator project */
	private Text artifactId;

	/** The read-only version of the aggregator project */
	private Text version;
	
	/** The read-only package of the aggregator project */
	private Text package_;

	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 */
	public AggregatorModulePage(ApplicationDtoProvider applicationProvider) {
		super("AggregatorModulePage", "Aggregator project of the application");
		
		this.applicationProvider = applicationProvider;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {
		
		// Main aggregator module type
		this.aggregatorType = builder.appendCombo("&Aggregator project type:");
		builder.finishRow();
		
		this.aggregatorTypeDescriptionListener = DescriptionListener.on(
				this.aggregatorType, builder.appendReadOnlyTextArea("", 5));
		builder.finishRow();
		
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
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible) {
			this.aggregatorType.setFocus();
			
			// (if the user went back and changed the version, the description may have changed for the same value) 
			this.aggregatorTypeDescriptionListener.refreshDescription();
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#updatePage()
	 */
	@Override
	protected void updatePage() {
		super.updatePage();
		
		final ApplicationDto application = this.applicationProvider.getApplication();
		final String aggregatorType = this.getAggregatorType();
		
		TextUtil.setIfNot(this.groupId, application.getGroupId());
		TextUtil.setIfNot(this.artifactId, StringUtil.formatNullSafe(
				"%s-%s", application.getArtifactId(), aggregatorType));
		TextUtil.setIfNot(this.version, application.getVersion());
		TextUtil.setIfNot(this.package_, StringUtil.formatNullSafe(
				"%s.%s.%s", application.getGroupId(), application.getAppShortId(), aggregatorType));
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#validatePage()
	 */
	@Override
	protected void validatePage() {
		super.validatePage();
		
		if (StringUtils.isBlank(this.getAggregatorType())) {
			this.setError("The aggregator type is required");
			return;
		}
	}

	/**
	 * @return the aggregatorType
	 */
	public String getAggregatorType() {
		return ComboUtil.getOptionValue(aggregatorType);
	}
}