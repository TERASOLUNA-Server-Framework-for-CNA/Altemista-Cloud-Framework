/**
 * 
 */
package cloud.altemista.fwk.plugin.maven;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.cli.MavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.plugin.core.util.ConsoleHolder;
import cloud.altemista.fwk.plugin.core.util.FileUtil;
import cloud.altemista.fwk.plugin.core.util.StringUtil;
import cloud.altemista.fwk.plugin.maven.model.MavenSettingsDto;

/**
 * Maven command executor implemented using MavenCli
 * @author NTT DATA
 */
public class MavenCommand {

	/** Produce execution error messages */
	public static final String VERBOSITY_ERROR = "e"; //$NON-NLS-1$
	
	/** Produce execution debug output */
	public static final String VERBOSITY_DEBUG = "X"; //$NON-NLS-1$
	
	/** Quiet output - only show errors */
	public static final String VERBOSITY_QUIET = "q"; //$NON-NLS-1$
	
	//
	
	/**
	 * Name of the new system property "maven.multiModuleProjectDirectory", since Maven 3.3.1.
	 * This constant is declared here to avoid referencing <code>MavenCli.MULTIMODULE_PROJECT_DIRECTORY,</code>
	 * because the dependency on Maven 3.3.x (scope: provided) will force this artifact to be built with Java 7 
	 * @see org.apache.maven.cli.MavenCli#MULTIMODULE_PROJECT_DIRECTORY
	 */
    private static final String MULTIMODULE_PROJECT_DIRECTORY = "maven.multiModuleProjectDirectory";
	
	/** Define a system property */
	private static final String DEFINE = "D"; //$NON-NLS-1$
	
	/** Display version information without stopping build */
	private static final String SHOW_VERSION = "V"; //$NON-NLS-1$
	
	/** Run in non-interactive (batch) mode */
	private static final String BATCH_MODE = "B"; //$NON-NLS-1$
	
	/** Alternate path for the user settings file */
	private static final String USER_SETTINGS = "s"; //$NON-NLS-1$
	
	/** Alternate path for the global settings file */
	private static final String GLOBAL_SETTINGS = "gs"; //$NON-NLS-1$
	
	//
	
	/** The archetype:generate goal */
	private static final String ARCHETYPE_GENERATE_GOAL = "archetype:generate"; //$NON-NLS-1$

	/** The archetypeGroupId property */
	private static final String ARCHETYPE_GROUP_ID = "archetypeGroupId"; //$NON-NLS-1$

	/** The archetypeArtifactId property */
	private static final String ARCHETYPE_ARTIFACT_ID = "archetypeArtifactId"; //$NON-NLS-1$

	/** The archetypeVersion property */
	private static final String ARCHETYPE_VERSION = "archetypeVersion"; //$NON-NLS-1$

	/** The groupId property */
	private static final String GROUP_ID = "groupId"; //$NON-NLS-1$

	/** The artifactId property */
	private static final String ARTIFACT_ID = "artifactId"; //$NON-NLS-1$

	/** The version property */
	private static final String VERSION = "version"; //$NON-NLS-1$
	
	//
	
	/**
	 * Static ClassWorld instance to avoid error in sequential Maven executions:
	 * "o.c.p.c.r.e.ComponentLookupException: j.u.NoSuchElementException role: o.a.m.e.i.EventSpyDispatcher" 
	 * @see org.apache.maven.cli.MavenCli#container(org.apache.maven.cli.CliRequest)
	 */
	private static ClassWorld CLASSWORLD =
			new ClassWorld("plexus.core", Thread.currentThread().getContextClassLoader());
	
	//
	
	/** The Maven working directory */
	private final String workingDirectory;
	
	/** List of this Maven command arguments */
	private final List<String> arguments;

	/**
	 * Constructor
	 * @param workingDirectory The Maven working directory
	 */
	public MavenCommand(String workingDirectory) {
		this(workingDirectory, VERBOSITY_ERROR);
	}

	/**
	 * Constructor
	 * @param workingDirectory The Maven working directory
	 * @param verbosity one of VERBOSITY_ERROR, VERBOSITY_DEBUG or VERBOSITY_QUIET
	 */
	public MavenCommand(String workingDirectory, String verbosity) {
		super();
		
		if (StringUtils.isBlank(workingDirectory)) {
			throw new IllegalArgumentException("The working directory is required"); //$NON-NLS-1$
		}
		
		this.workingDirectory = workingDirectory;
		this.arguments = new ArrayList<String>();
		
		// (some convenience flags)
		this.option(SHOW_VERSION);
		this.option(BATCH_MODE);
		this.option(StringUtil.firstValid(verbosity, null, VERBOSITY_ERROR, VERBOSITY_DEBUG, VERBOSITY_QUIET));
	}
	
	/**
	 * Executes the built command
	 */
	public void execute() {
		
		/*
		 * Since Maven 3.3.1, there is a new system property called maven.multiModuleProjectDirectory.
		 * It is set by default to the root of the project (project base directory) by the mvn (or mvn.bat) script.
		 * Therefore, when running Maven through maven-embedder, you also need to set this system property.
		 * It needs to be set to the project root.
		 */
		String backupValue = System.getProperty(MULTIMODULE_PROJECT_DIRECTORY);
		try {
			System.setProperty(MULTIMODULE_PROJECT_DIRECTORY, workingDirectory);
			
			// Executes the command
			int ret = new MavenCli(CLASSWORLD).doMain(
					this.arguments.toArray(new String[this.arguments.size()]),
					this.workingDirectory, ConsoleHolder.getOut(), ConsoleHolder.getErr());
			
			// If the execution failed, throw an exception
			if (ret != 0) {
				throw new PluginException(
						"Maven failed",
						"Maven execution failed with error code %d",
						"exitCode: %d, mvn args: %s",
						ret, StringUtils.join(this.arguments, ' '));
			}
			
		} finally {
			// (restores the previous values)
			if (StringUtils.isEmpty(backupValue)) {
				System.clearProperty(MULTIMODULE_PROJECT_DIRECTORY);
			} else {
				System.setProperty(MULTIMODULE_PROJECT_DIRECTORY, backupValue);
			}
		}
	}
	
	/**
	 * Convenience method to execute the archetype:generate goal
	 * @param archetype MavenCoordinatesDto with the archetype coordinates
	 * @param project MavenCoordinatesDto for the resulting project
	 * @return this MavenCommand
	 */
	public MavenCommand archetypeGenerate(MavenCoordinatesDto archetype, MavenCoordinatesDto project) {
		
		return this.goal(ARCHETYPE_GENERATE_GOAL)
				.define(ARCHETYPE_GROUP_ID, archetype.getGroupId())
				.define(ARCHETYPE_ARTIFACT_ID, archetype.getArtifactId())
				.define(ARCHETYPE_VERSION, archetype.getVersion())
				.define(GROUP_ID, project.getGroupId())
				.define(ARTIFACT_ID, project.getArtifactId())
				.define(VERSION, project.getVersion());
	}
	
	/**
	 * Adds a goal to the current command
	 * @param goal String with the goal
	 * @return this MavenCommand
	 */
	public MavenCommand goal(String goal) {

		return this.add(goal);
	}
	
	/**
	 * Adds a non-parameterizable option to the argument list
	 * @param option The option
	 * @return this MavenCommand
	 */
	public MavenCommand option(String option) {
		
		return this.add(StringUtil.formatNullSafe("-%s", option)); //$NON-NLS-1$
	}
	
	/**
	 * Adds a parameterizable option to the argument list
	 * @param option The option
	 * @param value The parameter of the option
	 * @return this MavenCommand
	 */
	public MavenCommand option(String option, String value) {
		
		return this.add(StringUtil.formatNullSafe("-%s=%s", option, value)); //$NON-NLS-1$
	}
	
	/**
	 * Adds a define option to the argument list
	 * @param property The property to define
	 * @param value The value for the property
	 * @return this MavenCommand
	 */
	public MavenCommand define(String property, String value) {
		
		return this.option(StringUtil.formatNullSafe("%s%s", DEFINE, property), StringUtils.trimToNull(value)); //$NON-NLS-1$
	}
	
	/**
	 * Adds the settings options to the argument list
	 * @param settings MavenSettingsDto
	 * @return this MavenCommand
	 */
	public MavenCommand settings(MavenSettingsDto settings) {
		
		if (settings != null) {
			final String userSettings = StringUtils.trimToNull(settings.getMavenUserSettingsLocation());
			if ((userSettings != null) && (FileUtil.getFile(userSettings) != null)) {
				this.option(USER_SETTINGS, userSettings);
			}
			
			final String globalSettings = StringUtils.trimToNull(settings.getMavenGlobalSettingsLocation());
			if ((globalSettings != null) && (FileUtil.getFile(globalSettings) != null)) {
				this.option(GLOBAL_SETTINGS, globalSettings);
			}
		}
		
		return this;
	}
	
	/**
	 * Convenience method
	 * @param argument to be added to the argument list
	 * @return this MavenCommand
	 */
	private MavenCommand add(String argument) {
		
		if (StringUtils.isNotBlank(argument)) {
			this.arguments.add(StringUtils.trim(argument));
		}
		
		return this;
	}
}
