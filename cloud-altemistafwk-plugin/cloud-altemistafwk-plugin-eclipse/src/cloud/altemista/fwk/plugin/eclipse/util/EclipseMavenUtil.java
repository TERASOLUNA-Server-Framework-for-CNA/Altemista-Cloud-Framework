/**
 * 
 */
package cloud.altemista.fwk.plugin.eclipse.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMavenConfiguration;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import cloud.altemista.fwk.plugin.maven.model.MavenSettingsDto;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;

/**
 * Convenience class to simplify the uasge of the Eclipse Maven plug-in
 * @author NTT DATA
 */
public class EclipseMavenUtil {
	
	/**
	 * Reads the Eclipse Maven plug-in settings
	 * @return MavenSettingsDto or null if the Maven plug-in can not be found or has no configuration
	 */
	public static MavenSettingsDto readSettings() {
		
		// Access the Eclipse Maven plug-in
		IMavenConfiguration mavenConfiguration = MavenPlugin.getMavenConfiguration();
		if (mavenConfiguration == null) {
			return null;
		}
		
		// Reads the configuration
		final String globalSettingsFile = StringUtils.trimToNull(mavenConfiguration.getGlobalSettingsFile());
		final String userSettingsFile = StringUtils.trimToNull(mavenConfiguration.getUserSettingsFile());
		
		// No configuration?
		if ((globalSettingsFile == null) && (userSettingsFile == null)) {
			return null;
		}
		
		// Creates the MavenSettingsDto object
		MavenSettingsDto dto = new MavenSettingsDto();
		dto.setMavenGlobalSettingsLocation(globalSettingsFile);
		dto.setMavenUserSettingsLocation(userSettingsFile);
		return dto;
	}
	
	/**
	 * Converts a ModelAndLocation (multi-module supported) into a MavenProjectInfo 
	 * @param mavenProject ModelAndLocation to convert
	 * @return Collection of MavenProjectInfo with the converted project and all its modules (recursive)
	 */
	public static Collection<MavenProjectInfo> asMavenProjectInfo(ModelAndLocation mavenProject) {
		
		Collection<MavenProjectInfo> collection = new ArrayList<MavenProjectInfo>();
		asMavenProjectInfo(collection, mavenProject, null);
		return collection;
	}
	
	/**
	 * Converts a ModelAndLocation (and its modules) into a MavenProjectInfo (and its children)
	 * @param collection Collection of MavenProjectInfo where the MavenProjectInfo will be added, never
	 * @param mavenProject ModelAndLocation to convert
	 * @param parentProjectInfo parent MavenProjectInfo or null
	 * @return the converted MavenProjectInfo
	 */
	private static MavenProjectInfo asMavenProjectInfo(Collection<MavenProjectInfo> collection,
			ModelAndLocation mavenProject, MavenProjectInfo parentProjectInfo) {
		
		// (sanity check)
		if (mavenProject == null) {
			return null;
		}
		
		// This project
		final Model model = mavenProject.getModel();
		MavenProjectInfo projectInfo = new MavenProjectInfo(
				model.getArtifactId(), mavenProject.getPomXmlFile(), model, parentProjectInfo);
		collection.add(projectInfo);
		
		// Recursively, add the modules as child projects
		for (ModelAndLocation moduleProject : mavenProject.gotoModules()) {
			projectInfo.add(asMavenProjectInfo(collection, moduleProject, projectInfo));
		}
		
		return projectInfo;
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	public EclipseMavenUtil() {
		super();
	}
}
