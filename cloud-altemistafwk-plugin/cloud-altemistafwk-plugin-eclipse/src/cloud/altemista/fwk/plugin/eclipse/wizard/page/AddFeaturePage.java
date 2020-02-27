package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.plugin.core.util.MavenCoordinatesUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.EnableDisableListener;
import cloud.altemista.fwk.common.eclipse.util.SelectUnselectListener;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;

/**
 * The "Features to add" page
 * @author NTT DATA
 */
public class AddFeaturePage extends AbstractWizardPage {
	
	/** The module project */
	private final ModuleProjectDto moduleProject;
	
	/** The read-only business module short ID */
	private Text businessShortId;
	
	/** The read-only module project type(s) */
	private Text moduleProjectType;
	
	/** The read-only TSF+ version of the application */
	private Text terasolunaPlusVersion;
	
	/** The list of check-box with the features candidates to be installed */
	private List<Button> checkboxes;

	/**
	 * Constructor
	 * @param moduleProject The module project
	 */
	public AddFeaturePage(ModuleProjectDto moduleProject) {
		super("AddFeaturePage", "Features to add");
		
		this.moduleProject = moduleProject;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {

		// shortId of the business module this module project forms part of
		if (StringUtils.isNotBlank(this.moduleProject.getBusinessShortId())) {
			this.businessShortId = builder.appendReadOnlyText("Business module name:", false);
			builder.finishRow();
		}
		
		// Module project type(s) and TSF+ version
		this.moduleProjectType = builder.appendReadOnlyText("Module project type:", false);
		builder.finishRow();
		
		this.terasolunaPlusVersion = builder.appendReadOnlyText("TSF+ version:", false);
		builder.finishRow();

		{
			ComponentBuilder featuresBuilder = builder.appendGroup("Available features:");
		
			this.checkboxes = new ArrayList<Button>();
			for (CurrentFeatureDto feature : this.moduleProject.getCurrentFeatures()) {
				
				// If the feature does not meet requirements, ignore it
				// (also keep the already installed features so they get shown on the page)
				if (!feature.isMeetsRequirements() && !feature.isInstalled()) {
					continue;
				}
				
				// Creates the check box
				Button checkbox = featuresBuilder.appendCheckbox(feature.getText());
				checkbox.setToolTipText(feature.getDescription());
				checkbox.setData(feature);
				featuresBuilder.finishRow();
				
				// If the feature is already installed: disabled checked check box
				if (feature.isInstalled()) {
					checkbox.setSelection(true);
					checkbox.setEnabled(false);
					featuresBuilder.decorateControl(checkbox, FieldDecorationRegistry.DEC_INFORMATION,
							"This feature is already installed in the module project");
					
				// If the feature is not compatible: disabled check box
				} else if (!feature.isCompatible()) {
					checkbox.setEnabled(false);
					featuresBuilder.decorateControl(checkbox, FieldDecorationRegistry.DEC_WARNING,
							"This feature is not compatible with other features of this module project");
				
				// Otherwise, put the check box in the list of candidates
				} else {
					this.checkboxes.add(checkbox);
				}
			}
		}
		
		for (Button checkbox : this.checkboxes) {
			
			// "On the fly" incompatibilities and transitive inclusions
			final Collection<Button> incompatibleCheckboxes = this.getIncompatibleCheckboxesFor(checkbox);
			final Collection<Button> transitiveCheckboxes = this.getTransitiveCheckboxesFor(checkbox);
			
			// Disables incompatible and transitive checkboxes
			if ((!incompatibleCheckboxes.isEmpty()) || (!transitiveCheckboxes.isEmpty())) {
				final EnableDisableListener listener = EnableDisableListener.on(checkbox);
				for (Button incompatibleCheckbox : incompatibleCheckboxes) {
					listener.disable(incompatibleCheckbox);
				}
				for (Button transitiveCheckbox : transitiveCheckboxes) {
					listener.disable(transitiveCheckbox);
				}
			}
			
			// Unselects transitive checkboxes
			if (!transitiveCheckboxes.isEmpty()) {
				final SelectUnselectListener listener = SelectUnselectListener.on(checkbox);
				for (Button transitiveCheckbox : transitiveCheckboxes) {
					listener.unselect(transitiveCheckbox);
				}
			}
		}
	}

	/**
	 * Returns which check boxes are incompatible with a specific check box
	 * @param checkbox the original check box
	 * @return collection of incompatible check boxes with the check box
	 */
	private Collection<Button> getIncompatibleCheckboxesFor(Button checkbox) {
		
		// If the feature of the checkbox has no dependencies, will make no check box incompatible
		final FeatureDto feature = (FeatureDto) checkbox.getData();
		if (!feature.hasDependencies()) {
			return Collections.emptyList();
		}
		
		Collection<Button> ret = new ArrayList<Button>();
		for (Button it : checkboxes) {
			// (skips checks with itself)
			if (it == checkbox) {
				continue;
			}
			
			// The second check box will be incompatible if its incompatibilities has any of the dependencies
			FeatureDto itFeature = (FeatureDto) it.getData();
			if (MavenCoordinatesUtil.containsAny(itFeature.getIncompatibilities(), feature.getDependencies())) {
				ret.add(it);
			}
		}
		return ret;
	}

	private Collection<Button> getTransitiveCheckboxesFor(Button checkbox) {
		
		// If the feature of the checkbox has no dependencies, skip
		final FeatureDto feature = (FeatureDto) checkbox.getData();
		if (!feature.hasDependencies()) {
			return Collections.emptyList();
		}
		
		Collection<Button> ret = new ArrayList<Button>();
		for (Button it : checkboxes) {
			// (skips checks with itself)
			if (it == checkbox) {
				continue;
			}
			
			// The second check box is transitive if its dependencies are a subset of the dependencies of the feature
			FeatureDto itFeature = (FeatureDto) it.getData();
			if (MavenCoordinatesUtil.containsAll(feature.getDependencies(), itFeature.getDependencies())) {
				ret.add(it);
			}
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractWizardPage#initializePage()
	 */
	@Override
	protected void initializePage() {
		super.initializePage();
		
		List<String> moduleProjectTypes = new ArrayList<String>();
		for (OptionWithArtifactDto currentType : this.moduleProject.getCurrentTypes()) {
			moduleProjectTypes.add(currentType.getText());
		}
		
		if (this.businessShortId != null) {
			this.businessShortId.setText(this.moduleProject.getBusinessShortId());
		}
		this.moduleProjectType.setText(StringUtils.defaultIfEmpty(
				StringUtils.join(moduleProjectTypes, ", "), "(none)"));
		this.terasolunaPlusVersion.setText(this.moduleProject.getApplication().getTerasolunaPlusVersion());
		
		for (Button checkbox : this.checkboxes) {
			checkbox.setSelection(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible && (!this.checkboxes.isEmpty())) {
			this.checkboxes.iterator().next().setFocus();
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#validatePage()
	 */
	@Override
	public void validatePage() {
		super.validatePage();
		
		if (this.getFeatures().isEmpty()) {
			this.setError("At least one feature is required");
			return;
		}
	}

	/**
	 * Returns a view on the list of the features with the selected ones
	 * @return the selected features
	 */
	public List<FeatureDto> getFeatures() {
		
		List<FeatureDto> values = new ArrayList<FeatureDto>();
		for (Button checkbox : checkboxes) {
			if (checkbox.getSelection()) {
				values.add((FeatureDto) checkbox.getData());
			}
		}
		return values;
	}
}