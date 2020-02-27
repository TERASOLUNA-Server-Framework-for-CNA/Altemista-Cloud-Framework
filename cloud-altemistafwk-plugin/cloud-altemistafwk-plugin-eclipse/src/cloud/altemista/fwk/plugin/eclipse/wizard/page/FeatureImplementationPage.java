package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithDefaultDto;
import cloud.altemista.fwk.plugin.config.util.ValueUtil;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.EnableDisableListener;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;
import cloud.altemista.fwk.plugin.eclipse.util.DescriptionListener;

/**
 * The "Features implementation" page
 * @author NTT DATA
 */
public class FeatureImplementationPage extends AbstractWizardPage {
	
	/** The module project */
	private final ModuleProjectDto moduleProject;

	/** The current feature */
	public final CurrentFeatureDto feature;

	/** The read-only feature name */
	private Text featureName;
	
	/** The read-only description for self-implemented features */
	private Text selfImplementedFeatureDescription;
	
	/** The selectable implementation of the feature */
	public Combo implementation;
	
	/** The possible additional properties for the feature */
	private List<Control> additionalProperties;

	/**
	 * Constructor
	 * @param moduleProject The module project
	 * @param feature The current feature
	 */
	public FeatureImplementationPage(ModuleProjectDto moduleProject, CurrentFeatureDto feature) {
		super("FeatureImplementationPage", "Choose the provider for the feature implementation");
		
		this.moduleProject = moduleProject;
		this.feature = feature;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {
		
		// Feature name and decription
		this.featureName = builder.appendReadOnlyText("Feature:", false);
		builder.finishRow();
		
		this.selfImplementedFeatureDescription = builder.appendReadOnlyTextArea("", 5);
		builder.finishRow();
		
		// If the feature is self-implemented without additional properties, there is nothing to do on this page
		if (this.feature.isSelfImplemented() && (!this.feature.hasArchetypeAdditionalProperties())) {
			return;
		}

		boolean showArchetypeAdditionalProperties = this.feature.hasArchetypeAdditionalProperties();
		boolean showSharedEnvironmentProjectName = !this.feature.isSelfImplemented();
		
		// Not a self-implemented feature?
		if (!this.feature.isSelfImplemented()) {
			
			// Already implemented feature?
			FeatureImplementationDto currentImplementation = this.feature.getCurrentImplementation();
			if (currentImplementation == null) {
				
				// Implementation combo-box
				this.implementation = builder.appendCombo("&Implementation:");
				builder.finishRow();
				
				DescriptionListener.on(this.implementation, builder.appendReadOnlyTextArea("", 5));
				builder.finishRow();
				
			} else {
				
				// Current implementation ("fake" implementation combo-box)
				Text readOnlyImplementation = builder.appendReadOnlyText("Implementation:");
				readOnlyImplementation.setText(currentImplementation.getText());
				builder.decorateControl(
						readOnlyImplementation, FieldDecorationRegistry.DEC_WARNING, String.format(
						"This feature already has an implementation provider and/or configuration\n"
						+ "in the application module project %s",
						this.feature.getCurrentProjectImplementing().getArtifactId()));
				builder.finishRow();
				
				builder.appendReadOnlyTextArea("", 5).setText(StringUtils.defaultString(
						currentImplementation.getDescription(), currentImplementation.getText()));
				builder.finishRow();
				
				// (is it not possible to configure an already configured feature)
				showArchetypeAdditionalProperties = false;
			}
		}
		
		// Additional properties for the feature
		if (showArchetypeAdditionalProperties) {
			
			this.additionalProperties = new ArrayList<Control>();
			for (OptionWithDefaultDto property : this.feature.getAdditionalProperties()) {
				
				if (property.getDefaultValue().equals("true") || property.getDefaultValue().equals("false")) {
					Button propertyCheckbox = builder.appendCheckbox(StringUtils.appendIfMissing(property.getText(), ":"));
					propertyCheckbox.setData(property);
					propertyCheckbox.setText(StringUtils.defaultString(property.getText()));
					propertyCheckbox.setToolTipText(property.getDescription());
					builder.decorateControl(
							propertyCheckbox, FieldDecorationRegistry.DEC_CONTENT_PROPOSAL, property.getDescription());
					builder.finishRow();
					
					this.additionalProperties.add(propertyCheckbox);
				} else {
					Text propertyText = builder.appendText(StringUtils.appendIfMissing(property.getText(), ":"));
					propertyText.setData(property);
					propertyText.setText(StringUtils.defaultString(property.getDefaultValue()));
					propertyText.setToolTipText(property.getDescription());
					builder.decorateControl(
							propertyText, FieldDecorationRegistry.DEC_CONTENT_PROPOSAL, property.getDescription());
					builder.finishRow();
					
					this.additionalProperties.add(propertyText);
				}
				
			}
			
			// Enable additional listeners for checks 
			for (OptionWithDefaultDto property : this.feature.getAdditionalProperties()) {
				if (property.getDefaultValue().equals("true") || property.getDefaultValue().equals("false")) {
					setEnableDisableListener(property);
				}
			}
			
		}
		
		// Read-only shared environment project name
		if (showSharedEnvironmentProjectName) {
			final ModuleProjectDto sharedEnvironmentProject =
					this.moduleProject.getApplication().getSharedEnvironmentProject();
			
			if (sharedEnvironmentProject != null) {
				Text environmentProjectName = builder.appendReadOnlyText("Shared environment/configuration:", false);
				environmentProjectName.setText(sharedEnvironmentProject.getArtifactId());
				builder.finishRow();
				
			} else {
				Text environmentProjectName = builder.appendReadOnlyText("Environment/configuration:", false);
				environmentProjectName.setText(this.moduleProject.getArtifactId());
				builder.finishRow();
			}
		}
	}

	private void setEnableDisableListener(OptionWithDefaultDto property) {
		
		for (Control control : this.additionalProperties) {
			if (control.getData().equals(property)) {
				Button button = (Button) control;
				EnableDisableListener controlListener = EnableDisableListener.on((Button) control);
				String[] activatedControls = null;
				String[] deactivatedControls = null;
				if (property.getActivatedControls() != null && !property.getActivatedControls().isEmpty()) {
					activatedControls = property.getActivatedControls().split(",");
				}
				if (property.getDeactivatedControls() != null && !property.getDeactivatedControls().isEmpty()) {
					deactivatedControls = property.getDeactivatedControls().split(",");
				}
				setActivatedDeactivatedControlsToCheck(controlListener, activatedControls, deactivatedControls, button.getSelection());
			}
		}
		
	}

	private void setActivatedDeactivatedControlsToCheck(EnableDisableListener controlListener,
			String[] activatedControls, String[] deactivatedControls, Boolean enabled) {
		
		if (activatedControls!=null || deactivatedControls!=null){
			for (Control control : this.additionalProperties) {
				OptionWithDefaultDto property = (OptionWithDefaultDto) control.getData();
				if (activatedControls!=null && ArrayUtils.contains(activatedControls, property.getValue())) {
					controlListener.enable(control);
					control.setEnabled(enabled);
				}
				if (deactivatedControls!=null && ArrayUtils.contains(deactivatedControls, property.getValue())) {
					controlListener.disable(control);
					control.setEnabled(!enabled);
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractWizardPage#initializePage()
	 */
	@Override
	protected void initializePage() {
		super.initializePage();
		
		this.featureName.setText(this.feature.getText());
		
		this.selfImplementedFeatureDescription.setText(
				StringUtils.defaultString(this.feature.getDescription(), this.feature.getText()));
		
		if (this.implementation != null) {
			final List<FeatureImplementationDto> implementations = ValueUtil.filter(
					this.feature.getImplementations(), this.moduleProject.getApplication().getTerasolunaPlusVersion());
			ComboUtil.setOptions(this.implementation, implementations, false);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible && (this.implementation != null)) {
			this.implementation.setFocus();
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractWizardPage#validatePage()
	 */
	@Override
	protected void validatePage() {
		super.validatePage();
		
		if ((this.implementation != null) && (this.getImplementation() == null)) {
			this.setError("The implementation for this feature is required");
			return;
		}
		
		if (this.additionalProperties != null) {
			for (Control additionalProperty : this.additionalProperties) {
				if (additionalProperty instanceof Text) {
					Text textAdditionalProperty = (Text) additionalProperty;
					OptionWithDefaultDto data = (OptionWithDefaultDto) textAdditionalProperty.getData();
					if (textAdditionalProperty.isEnabled() && !data.getOptional() && 
							StringUtils.isEmpty(textAdditionalProperty.getText())) {
						this.setError(String.format("%s is required", data.getText()));
						return;
					}
				}
			}
		}
	}

	/**
	 * Determines the implementation, taking into account if the feature
	 * is self-implemented, already installed elsewhere, and finally the inputs of the page
	 * @return the implementation
	 */
	public FeatureImplementationDto getImplementation() {
		
		// Is the feature self-implemented?
		if (this.feature.isSelfImplemented()) {
			return null;
		}
		
		// Has the feature an implementation already?
		final FeatureImplementationDto currentImplementation = this.feature.getCurrentImplementation();
		if (currentImplementation != null) {
			return currentImplementation;
		}
		
		// (should not happen)
		if (this.implementation == null) {
			return null;
		}
		
		return (FeatureImplementationDto) ComboUtil.getOption(this.implementation);
	}

	/**
	 * @return the additionalProperties
	 */
	public Properties getAdditionalProperties() {
		
		if ((this.additionalProperties == null) || this.additionalProperties.isEmpty()) {
			return null;
		}
		
		Properties properties = new Properties();
		for (Control additionalProperty : this.additionalProperties) {
			if (additionalProperty instanceof Text){
				properties.put(
						((OptionWithDefaultDto) additionalProperty.getData()).getValue(), ((Text)additionalProperty).getText());
			} else if (additionalProperty instanceof Button){
				properties.put(
						((OptionWithDefaultDto) additionalProperty.getData()).getValue(), ""+((Button)additionalProperty).getSelection());
			} 

		}
		return properties;
	}
}