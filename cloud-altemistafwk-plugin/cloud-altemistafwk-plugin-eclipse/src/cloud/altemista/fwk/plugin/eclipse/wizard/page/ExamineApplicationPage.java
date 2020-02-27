package cloud.altemista.fwk.plugin.eclipse.wizard.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Text;
import cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDto;
import cloud.altemista.fwk.plugin.core.model.ApplicationDtoProvider;
import cloud.altemista.fwk.plugin.core.model.ModuleProjectDto;
import cloud.altemista.fwk.common.eclipse.util.ComponentBuilder;
import cloud.altemista.fwk.common.eclipse.wizard.page.AbstractWizardPage;

/**
 * The "Existing application" page
 * @author NTT DATA
 */
public class ExamineApplicationPage extends AbstractWizardPage {
	
	/** The applicationProvider */
	private final ApplicationDtoProvider applicationProvider;

	/** The read-only application name */
	private Text applicationName;
	
	/** The read-only appShortId of the application */
	private Text appShortId;

	/** The read-only TSF+ version of the application */
	private Text terasolunaPlusVersion;
	
	/** The read-only type(s) of the aggregator project */
	private Text aggregatorTypes;

	/** The read-only artifactId of the aggregator project */
	private Text aggregatorArtifactId;

	/** The read-only artifactId of the shared environment/configuration project */
	private Text envArtifactId;

	/** The read-only groupId for the application */
	private Text groupId;

	/** The read-only artifactId for the application */
	private Text artifactId;

	/** The read-only version of the appliction */
	private Text version;
	
	/**
	 * Constructor
	 * @param applicationProvider the ApplicationDtoProvider to read the values of the application
	 */
	public ExamineApplicationPage(ApplicationDtoProvider applicationProvider) {
		super("ExamineApplicationPage", "Existing application");
		
		this.applicationProvider = applicationProvider;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#buildControls(org.terasoluna.plus.eclipse.util.ComponentBuilder)
	 */
	@Override
	public void buildControls(ComponentBuilder builder) {

		final ApplicationDto application = this.applicationProvider.getApplication();
		
		// Project name, TSF+ version and AppShortId
		this.applicationName = builder.appendReadOnlyText("Application name:", false);
		builder.finishRow();
		
		this.terasolunaPlusVersion = builder.appendReadOnlyText("TSF+ version:", false);
		builder.finishRow();
		
		this.appShortId = builder.appendReadOnlyText("App. short ID:", false);
		builder.finishRow();
		
		// Creates the "Maven attributes" section
		{
			ComponentBuilder mavenAttributesBuilder = builder.appendGroup("Maven attributes:");
			
			this.groupId = mavenAttributesBuilder.appendReadOnlyText("groupId:", false);
			mavenAttributesBuilder.finishRow();
			
			this.artifactId = mavenAttributesBuilder.appendReadOnlyText("artifactId:", false);
			mavenAttributesBuilder.finishRow();
			
			this.version = mavenAttributesBuilder.appendReadOnlyText("version:", false);
			mavenAttributesBuilder.finishRow();
		}
		
		// Creates the "Aggregator project" section
		if (application.getAggregatorProject() != null) {
			this.aggregatorTypes = builder.appendReadOnlyText("Aggregator type:", false);
			builder.finishRow();
			
			this.aggregatorArtifactId = builder.appendReadOnlyText("Aggregator project:", false);
			builder.finishRow();
		}
		
		// Creates the "Shared environment project" section
		if (application.getSharedEnvironmentProject() != null) {
			this.envArtifactId = builder.appendReadOnlyText("Shared environment/configuration project:", false);
			builder.finishRow();
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.eclipse.util.AbstractWizardPage#initializePage()
	 */
	@Override
	protected void initializePage() {
		super.initializePage();
		
		// Page components initial values
		
		final ApplicationDto application = this.applicationProvider.getApplication();
		this.applicationName.setText(application.getArtifactId());
		this.appShortId.setText(application.getAppShortId());
		this.terasolunaPlusVersion.setText(application.getTerasolunaPlusVersion());

		final ModuleProjectDto aggregatorProject = application.getAggregatorProject();
		if (aggregatorProject != null) {
			List<String> types = new ArrayList<String>();
			for (ModuleProjectTypeDto moduleProjectType : aggregatorProject.getCurrentTypes()) {
				if (moduleProjectType.isAggregator()) {
					types.add(moduleProjectType.getText());
				}
			}
			this.aggregatorTypes.setText(StringUtils.join(types, ", "));
			
			this.aggregatorArtifactId.setText(aggregatorProject.getArtifactId());
		}
		
		final ModuleProjectDto envProject = application.getSharedEnvironmentProject();
		if (envProject != null) {
			this.envArtifactId.setText(envProject.getArtifactId());
		}

		this.groupId.setText(application.getGroupId());
		this.artifactId.setText(application.getArtifactId());
		this.version.setText(application.getVersion());
	}
}
