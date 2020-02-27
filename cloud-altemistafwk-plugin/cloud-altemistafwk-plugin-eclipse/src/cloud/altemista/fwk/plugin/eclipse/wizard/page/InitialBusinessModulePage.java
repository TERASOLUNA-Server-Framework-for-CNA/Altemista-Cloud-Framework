package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.common.eclipse.util.ButtonUtil;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.util.EnableDisableListener;

/**
 * The "Initial business module" page
 * @author NTT DATA
 */
public class InitialBusinessModulePage extends BusinessModulePage {

	/** The initial business module check-box*/
	private Button initialBusinessModule;

	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 */
	public InitialBusinessModulePage(ApplicationDtoProvider applicationProvider) {
		super("Initial business module", applicationProvider);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	public void buildControls(ComponentBuilder builder) {
		
		// Add initial business module checkbox
		this.initialBusinessModule = builder.appendCheckbox("&Initial business module");
		builder.finishRow();
		
		super.buildControls(builder);
		
		// Disables the elements when checking the checkbox
		EnableDisableListener.on(this.initialBusinessModule)
				.enable(this.businessModuleName)
				.enable(this.businessModuleType)
				.enable(this.businessModuleDescription)
				.enable(this.groupId)
				.enable(this.artifactId)
				.enable(this.version)
				.enable(this.package_);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		// Sets the initial focus
		if (visible) {
			this.initialBusinessModule.setFocus();
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#initializePage()
	 */
	protected void initializePage() {
		super.initializePage();

		ButtonUtil.setSelection(this.initialBusinessModule, false);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.AbstractWizardPage#validatePage()
	 */
	@Override
	protected void validatePage() {
		
		// If the check box is not marked, overrides the validation 
		if (this.isInitialBusinessModule()) {
			super.validatePage();
			
		} else {
			setOk();
		}
	}

	/**
	 * @return the includeModule
	 */
	public boolean isInitialBusinessModule() {
		
		return this.initialBusinessModule.getSelection();
	}

	public void updateCombo(String aggratorTypeSelected, String version ) {
		
		
	}

}