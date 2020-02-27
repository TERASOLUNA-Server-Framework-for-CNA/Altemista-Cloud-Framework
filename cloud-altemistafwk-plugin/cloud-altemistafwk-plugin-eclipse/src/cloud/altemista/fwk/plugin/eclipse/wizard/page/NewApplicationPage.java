package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.plugin.core.model.NewApplicationDto;
import cloud.altemista.fwk.plugin.core.util.InputValidator;
import cloud.altemista.fwk.common.eclipse.util.ButtonUtil;
import cloud.altemista.fwk.common.eclipse.util.ComboUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.DialogUtil;
import cloud.altemista.fwk.common.eclipse.util.EnableDisableListener;
import cloud.altemista.fwk.common.eclipse.util.TextUtil;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;

/**
 * The "New application main attributes" page
 * @author NTT DATA
 */
public class NewApplicationPage extends AbstractWizardPage implements ApplicationDtoProvider {

	/** The tooltip for the editable appShortId */
	private static final String APPSHORTID_TOOLTIP = "An alphanumeric short identifier of the application.\n"
			+ "This identifier is used to calculate\n"
			+ "most of the default names used in the application by the plug-in,\n"
			+ "such as bean names, configuration filenames and packages.";
	
	/** The editable application name */
	private Text applicationName;

	/** The editable groupId for the application */
	private Text groupId;

	/** The read-only artifactId of the application */
	private Text artifactId;

	/** The editable version of the appliction */
	private Text version;
	
	/** Check to use the default location */
	private Button useDefaultLocation;
	
	/** The editable location of the application */
	private Text location;
	
	/** The back-up value for the default location when useDefaultLocation gets unchecked */
	private String defaultLocation;
	
	/** The back-up value for the user location when useDefaultLocation gets checked */
	private String userLocation;

	/** The selectable TSF+ version of the application */
	private Combo terasolunaPlusVersion;
	
	/** The editable appShortId of the application */
	private Text appShortId;
	
	/**
	 * Constructor
	 */
	public NewApplicationPage() {
		super("NewApplicationPage", "Main attributes of the new TSF+ application");
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {

		// Project name
		this.applicationName = builder.appendText("Application &name:");
		this.applicationName.setTextLimit(InputValidator.ARTIFACT_ID.getMaxLength());
		this.applicationName.setToolTipText("The name of the application");
		builder.finishRow();
		
		// TSF+ version
		this.terasolunaPlusVersion = builder.appendCombo("TSF+ &version:");
		this.terasolunaPlusVersion.setToolTipText(
				"The version of Terasoluna Server Framework Plus the application will use");
		builder.finishRow();
		
		// (sets the values -and the selected value- after binding the DescriptionListener)
		ComboUtil.setOptions(this.terasolunaPlusVersion, Config.VERSIONS.values(), true);
		
		// AppShortId
		this.appShortId = builder.appendText("App. &short ID:");
		this.appShortId.setTextLimit(InputValidator.APPSHORTID.getMaxLength());
		builder.decorateControl(this.appShortId, FieldDecorationRegistry.DEC_CONTENT_PROPOSAL, APPSHORTID_TOOLTIP);
		this.appShortId.setToolTipText(APPSHORTID_TOOLTIP);
		builder.finishRow();
		
		// Creates the "General" section
		{
			ComponentBuilder generalBuilder = builder.appendGroup("General:");
			
			// Use default location checkbox
			this.useDefaultLocation = generalBuilder.appendCheckbox("Use &default location");
			generalBuilder.finishRow();
			
			// Location selector
			this.location = generalBuilder.appendText("Location:");
			Button customPathButton = generalBuilder.appendButton("Browse...");
			customPathButton.addSelectionListener(new DialogUtil.FolderDialogSelectionListener(this, this.location));
			generalBuilder.finishRow();
			
			// Disables the elements when checking the checkbox
			EnableDisableListener.on(this.useDefaultLocation)
					.disable(this.location)
					.disable(customPathButton);
		}
		
		// Creates the "Maven attributes" section
		{
			ComponentBuilder mavenAttributesBuilder = builder.appendGroup("Maven attributes:");
			
			this.groupId = mavenAttributesBuilder.appendText("&groupId:");
			this.groupId.setTextLimit(InputValidator.GROUP_ID.getMaxLength());
			this.groupId.setToolTipText("The groupId that will be used for the artifacts of the project");
			mavenAttributesBuilder.finishRow();
			
			this.artifactId = mavenAttributesBuilder.appendReadOnlyText("&artifactId:");
			mavenAttributesBuilder.finishRow();
			
			this.version = mavenAttributesBuilder.appendText("v&ersion:");
			this.version.setTextLimit(InputValidator.VERSION.getMaxLength());
			this.version.setToolTipText("The artifact version");
			mavenAttributesBuilder.finishRow();
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#initializePage()
	 */
	@Override
	protected void initializePage() {
		super.initializePage();
		
		// Page components initial values
		this.applicationName.setText(Constant.DEFAULT_ARTIFACT_ID);
		this.appShortId.setText(Constant.DEFAULT_APP_SHORT_ID);
		ButtonUtil.setSelection(this.useDefaultLocation, true);
		this.groupId.setText(Constant.DEFAULT_GROUP_ID);
		this.version.setText(Constant.DEFAULT_VERSION);
		
		// Back-up initial values
		this.defaultLocation = FilenameUtils.normalize(
				ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
		this.userLocation = this.defaultLocation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible) {
			this.applicationName.setFocus();
			this.applicationName.setSelection(0, this.applicationName.getText().length());
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#updatePage()
	 */
	@Override
	public void updatePage() {
		super.updatePage();
		
		TextUtil.cleanUsing(this.applicationName, InputValidator.ARTIFACT_ID);
		TextUtil.cleanUsing(this.appShortId, InputValidator.APPSHORTID);
		
		// Default location / location
		if (this.useDefaultLocation()) {
			// If default path is checked, sets the default value, preserving the user value
			String originalValue = TextUtil.setIfNot(this.location, this.defaultLocation);
			if (originalValue != null) {
				this.userLocation = originalValue;
			}
			
		} else {
			// If not checked, sets the preserved user value
			TextUtil.replaceValue(this.location, this.defaultLocation, this.userLocation);
		}
		
		// "Maven attributes" section
		TextUtil.cleanUsing(this.groupId, InputValidator.GROUP_ID);
		TextUtil.setIfNot(this.artifactId, this.getApplicationName());
		TextUtil.cleanUsing(this.version, InputValidator.VERSION);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#validatePage()
	 */
	@Override
	public void validatePage() {
		super.validatePage();
		
		if (StringUtils.isBlank(this.getApplicationName())) {
			this.setError("The application name is required");
			return;
		}
		if (!InputValidator.ARTIFACT_ID.isValid(this.getApplicationName(), true)) {
			this.setError("The application name is invalid");
			return;
		}
		
		if (StringUtils.isBlank(this.getTerasolunaPlusVersion())) {
			this.setError("The TSF+ version is required");
			return;
		}
		
		if (StringUtils.isBlank(this.getAppShortId())) {
			this.setError("The application short ID is required");
			return;
		}
		if (!InputValidator.APPSHORTID.isValid(this.getAppShortId(), true)) {
			this.setError("The application short ID is invalid");
			return;
		}
		
		if (StringUtils.isBlank(this.getLocation())) {
			this.setError("The location is required");
			return;
		}
		
		if (StringUtils.isBlank(this.getGroupId())) {
			this.setError("The artifact groupId is required");
			return;
		}
		if (!InputValidator.GROUP_ID.isValid(this.getGroupId(), true)) {
			this.setError("The artifact groupId is invalid");
			return;
		}
		
		if (StringUtils.isBlank(this.getVersion())) {
			this.setError("The artifact version is required");
			return;
		}
		if (!InputValidator.VERSION.isValid(this.getVersion(), true)) {
			this.setError("The artifact version is invalid");
			return;
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider#getApplication()
	 */
	@Override
	public NewApplicationDto getApplication() {
		
		NewApplicationDto dto = new NewApplicationDto();
		dto.setGroupId(this.getGroupId());
		dto.setArtifactId(this.getArtifactId());
		dto.setVersion(this.getVersion());
		dto.setTerasolunaPlusVersion(this.getTerasolunaPlusVersion());
		dto.setAppShortId(this.getAppShortId());
		return dto;
	}
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName.getText();
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId.getText();
	}

	/**
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId.getText();
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version.getText();
	}

	/**
	 * @return the useDefaultLocation
	 */
	public boolean useDefaultLocation() {
		return useDefaultLocation.getSelection();
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location.getText();
	}

	/**
	 * @return the terasolunaPlusVersion
	 */
	public String getTerasolunaPlusVersion() {
		return ComboUtil.getOptionValue(terasolunaPlusVersion);
	}

	/**
	 * @return the appShortId
	 */
	public String getAppShortId() {
		return appShortId.getText();
	}
}