package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.plugin.core.model.CurrentBusinessModuleDto;
import cloud.altemista.fwk.plugin.core.model.CurrentFeatureDto;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;
import cloud.altemista.fwk.plugin.eclipse.Activator;

/**
 * The "Existing business modules" page
 * @author NTT DATA
 */
public class ExamineBusinessModulesPage extends AbstractWizardPage {

	/** The "application" icon */
	private static final String ICON_APPLICATION = "/icons/application.png";

	/** The "aggregator project" icon */
	private static final String ICON_AGGREGATOR_PROJECT = "/icons/aggregator-project.png";

	/** The "shared environment project" icon */
	private static final String ICON_ENV_PROJECT = "/icons/env-project.png";

	/** The "business module" icon */
	private static final String ICON_BUSINESS_MODULE = "/icons/business-module.png";
	
	/** The "module project" icon */
	private static final String ICON_MODULE_PROJECT = "/icons/module-project.png";
	
	/** The "feature" icon */
	private static final String ICON_FEATURE = "/icons/feature.png";

	//
	
	/** The applicationProvider */
	private final ApplicationDtoProvider applicationProvider;

	/** The read-only Tree */
	private Tree tree;

	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 */
	public ExamineBusinessModulesPage(ApplicationDtoProvider applicationProvider) {
		super("ExamineBusinessModulesPage", "Existing business modules");
		
		this.applicationProvider = applicationProvider;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {
		
		this.tree = builder.appendTree("Application:");
		((GridData) (this.tree.getLayoutData())).heightHint = 480; // 320 * 1.5
		builder.finishRow();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#initializePage()
	 */
	@Override
	protected void initializePage() {
		super.initializePage();
		
		// Page components initial values

		// Application
		final ApplicationDto application = this.applicationProvider.getApplication();
		
		// Application node
		TreeItem applicationTreeItem = new TreeItem(this.tree, SWT.NONE);
		applicationTreeItem.setImage(Activator.getImage(ICON_APPLICATION));
		applicationTreeItem.setText(application.getArtifactId());

		// Aggregator project
		ModuleProjectDto aggregatorProject = application.getAggregatorProject();
		if (aggregatorProject != null) {
			
			// Aggregator project node
			TreeItem aggregatorProjectTreeItem = new TreeItem(applicationTreeItem, SWT.NONE);
			aggregatorProjectTreeItem.setImage(Activator.getImage(ICON_AGGREGATOR_PROJECT));
			aggregatorProjectTreeItem.setText(aggregatorProject.getArtifactId());
			
			// Type(s)
			this.appendModuleProjectTypes(aggregatorProjectTreeItem, aggregatorProject.getCurrentTypes());
			
			// Feature(s)
			List<CurrentFeatureDto> installedFeatures = new ArrayList<CurrentFeatureDto>();
			for (CurrentFeatureDto currentFeature : aggregatorProject.getCurrentFeatures()) {
				if (currentFeature.isInstalled()) {
					installedFeatures.add(currentFeature);
				}
			}
			this.appendModuleProjectFeatures(aggregatorProjectTreeItem, installedFeatures);
			
			// (initially expanded)
			aggregatorProjectTreeItem.setExpanded(true);
		}

		// Business modules
		for (CurrentBusinessModuleDto businessModule :
				this.applicationProvider.getApplication().getCurrentBusinessModules()) {
			
			// Business module node
			TreeItem businessModuleTreeItem = new TreeItem(applicationTreeItem, SWT.NONE);
			businessModuleTreeItem.setImage(Activator.getImage(ICON_BUSINESS_MODULE));
			businessModuleTreeItem.setText(businessModule.getBusinessShortId());
			
			// Business module type node
			TreeItem businessModuleTypeTreeItem = new TreeItem(businessModuleTreeItem, SWT.NONE);
			businessModuleTypeTreeItem.setText(businessModule.getType().getText());
			
			for (ModuleProjectDto moduleProject : businessModule.getModules()) {
				this.appendModuleProject(businessModuleTreeItem, moduleProject);
			}
			
			// (initially expanded)
			businessModuleTreeItem.setExpanded(true);
		}
		
		// Environment project
		ModuleProjectDto envProject = application.getSharedEnvironmentProject();
		if (envProject != null) {
			
			TreeItem envProjectTreeItem = new TreeItem(applicationTreeItem, SWT.NONE);
			envProjectTreeItem.setImage(Activator.getImage(ICON_ENV_PROJECT));
			envProjectTreeItem.setText(envProject.getArtifactId());
			envProjectTreeItem.setExpanded(true);
			
//			// Type
//			this.appendModuleProjectTypes(envProjectItem, envProject.getCurrentTypes());
//			
//			// Features
//			List<CurrentFeatureDto> installedFeatures = new ArrayList<CurrentFeatureDto>();
//			for (CurrentFeatureDto currentFeature : envProject.getCurrentFeatures()) {
//				if (currentFeature.isInstalled()) {
//					installedFeatures.add(currentFeature);
//				}
//			}
//			this.appendModuleProjectFeatures(envProjectItem, installedFeatures);
			
			// (initially expanded)
			envProjectTreeItem.setExpanded(true);
		}
		
		// (initially expanded)
		applicationTreeItem.setExpanded(true);
	}

	/**
	 * Appends a module project (inside a business module)
	 * @param businessModuleTreeItem TreeItem
	 * @param moduleProject ModuleProjectDto
	 */
	private void appendModuleProject(TreeItem businessModuleTreeItem, ModuleProjectDto moduleProject) {
		
		// Module project node
		TreeItem moduleProjectItem = new TreeItem(businessModuleTreeItem, SWT.NONE);
		moduleProjectItem.setImage(Activator.getImage(ICON_MODULE_PROJECT));
		moduleProjectItem.setText(moduleProject.getArtifactId());
		
		// Type(s)
		this.appendModuleProjectTypes(moduleProjectItem, moduleProject.getCurrentTypes());
		
		// Feature(s)
		List<CurrentFeatureDto> installedFeatures = new ArrayList<CurrentFeatureDto>();
		for (CurrentFeatureDto currentFeature : moduleProject.getCurrentFeatures()) {
			if (currentFeature.isInstalled()) {
				installedFeatures.add(currentFeature);
			}
		}
		this.appendModuleProjectFeatures(moduleProjectItem, installedFeatures);
	}

	/**
	 * Appends the module project types, as a single child or nested if more than one type
	 * @param moduleProjectTreeItem TreeItem
	 * @param currentTypes List of ModuleProjectTypeDto
	 */
	private void appendModuleProjectTypes(
			TreeItem moduleProjectTreeItem, List<ModuleProjectTypeDto> currentTypes) {
		
		if (currentTypes.isEmpty()) {
			return;
		}
		
		// One single type
		if (currentTypes.size() == 1) {
			ModuleProjectTypeDto moduleProjectType = currentTypes.iterator().next();
			TreeItem typeTreeItem = new TreeItem(moduleProjectTreeItem, SWT.NONE);
			typeTreeItem.setText(moduleProjectType.getText());
			return;
		}
			
		// More than one type: nested
		TreeItem typesTreeItem = new TreeItem(moduleProjectTreeItem, SWT.NONE);
		typesTreeItem.setText("Types");
		for (ModuleProjectTypeDto moduleProjectType : currentTypes) {
			TreeItem typeTreeItem = new TreeItem(typesTreeItem, SWT.NONE);
			typeTreeItem.setText(moduleProjectType.getText());
		}
		// (initially expanded)
		typesTreeItem.setExpanded(true);
	}

	/**
	 * Appends the module project features, as a single child or nested if 
	 * @param moduleProjectTreeItem TreeItem
	 * @param installedFeatures List of CurrentFeatureDto
	 */
	private void appendModuleProjectFeatures(
			TreeItem moduleProjectTreeItem, List<CurrentFeatureDto> installedFeatures) {
		
		if (installedFeatures.isEmpty()) {
			return;
		}
		
		// One single feature
		for (CurrentFeatureDto installedFeature : installedFeatures) {
			TreeItem featureTreeItem = new TreeItem(moduleProjectTreeItem, SWT.NONE);
			featureTreeItem.setImage(Activator.getImage(ICON_FEATURE));
			featureTreeItem.setText(installedFeature.getText());
		}
	}
}
