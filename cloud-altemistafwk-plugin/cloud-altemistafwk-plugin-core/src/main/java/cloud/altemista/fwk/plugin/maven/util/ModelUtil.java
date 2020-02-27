/**
 * 
 */
package cloud.altemista.fwk.plugin.maven.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.plugin.maven.model.ModelAndLocation;

/**
 * Utilities related to reading pom.xml files
 * @author NTT DATA
 */
public class ModelUtil {
	
	/** Pattern for the placeholder values: "\$\{(.*)\}" */
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.+)\\}");
	
	/**
	 * Reads a pom.xml file
	 * @param projectLocation String with the project location (can be the pom.xml file location as well)
	 * @return Model from the pom.xml; never null
	 */
	public static Model readModel(String projectLocation) {
		
		// Gets the pom.xml file
		final ModelAndLocation modelAndLocation = new ModelAndLocation(projectLocation);
		final File pomXmlFile = modelAndLocation.getPomXmlFile();
		
		// Reads the file
		InputStream is = null;
		try {
			is = new FileInputStream(pomXmlFile);
			Model model = new MavenXpp3Reader().read(is);
			return model;
			
		} catch (IOException e) {
			throw new PluginException(e,
					"Invalid pom.xml",
					"Failed reading pom.xml file",
					"Failed reading %s",
					modelAndLocation.getPomXmlLocation());
			
		} catch (XmlPullParserException e) {
			throw new PluginException(e,
					"Invalid pom.xml",
					"Failed parsing pom.xml file: %2$s",
					"Failed parsing %s: %s",
					modelAndLocation.getPomXmlLocation(), e.getMessage());
			
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * Writes a pom.xml file
	 * @param model Model of the pom.xml; never null
	 * @param projectLocation String with the project location (can be the pom.xml file location as well)
	 */
	public static void write(Model model, String projectLocation) {
		
		// (sanity check)
		if (model == null) {
			throw new IllegalArgumentException("model is null");
		}
		
		// Gets the pom.xml file
		final ModelAndLocation modelAndLocation = new ModelAndLocation(projectLocation);
		final File pomXmlFile = modelAndLocation.getPomXmlFile();
		
		// Writes the model to the file
		OutputStream os = null;
		try {
			os = new FileOutputStream(pomXmlFile);
			new MavenXpp3Writer().write(os, model);
			
		} catch (IOException e) {
			throw new PluginException(e,
					"Could not write pom.xml",
					"Failed writing pom.xml file",
					"Failed writing %s",
					modelAndLocation.getPomXmlLocation());
			
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
	
	/**
	 * Reads the groupId from the model, even if it inherited from parent
	 * @param model Model
	 * @return groupId, or null if the pom.xml has no groupId
	 */
	public static String getGroupId(Model model) {
	
		// (sanity check)
		if (model == null) {
			return null;
		}
		
		// groupId
		String groupId = model.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {
			return groupId;
		}
		
		// inherited groupId
		Parent parent = model.getParent();
		if (parent != null) {
			return parent.getGroupId();
		}
		
		return null;
	}
	
	/**
	 * Reads the artifactId from the model, even if it inherited from parent
	 * @param model Model
	 * @return artifactId, or null if the pom.xml has no artifactId
	 */
	public static String getArtifactId(Model model) {
	
		// (sanity check)
		if (model == null) {
			return null;
		}
	
		// artifactId
		String artifactId = model.getArtifactId();
		if (StringUtils.isNotBlank(artifactId)) {
			return artifactId;
		}
		
		// inherited artifactId
		Parent parent = model.getParent();
		if (parent != null) {
			return parent.getArtifactId();
		}
		
		return null;
	}
	
	/**
	 * Reads the version from the model, even if it inherited from parent
	 * @param model Model
	 * @return version, or null if the pom.xml has no version
	 */
	public static String getVersion(Model model) {
	
		// (sanity check)
		if (model == null) {
			return null;
		}
	
		// version
		String version = model.getVersion();
		if (StringUtils.isNotBlank(version)) {
			return version;
		}
		
		// inherited version
		Parent parent = model.getParent();
		if (parent != null) {
			return parent.getVersion();
		}
		
		return null;
	}
	
	/**
	 * Adds dependencies to the model
	 * @param model Model where the dependencies will be added
	 * @param dependencies List of MavenCoordinatesDto with the dependencies to add
	 */
	public static void addDependencies(Model model, List<MavenCoordinatesDto> dependencies) {
		
		for (MavenCoordinatesDto dependency : dependencies) {
			addDependency(model.getDependencies(), toMavenDependency(dependency));
		}
	}
	
	/**
	 * Adds a dependency to the model
	 * @param model Model where the dependency will be added
	 * @param dependency MavenCoordinatesDto to add
	 */
	public static void addDependency(Model model, MavenCoordinatesDto dependency) {
		
		addDependency(model.getDependencies(), toMavenDependency(dependency));
	}
	
	/**
	 * Removes dependencies from the model
	 * @param model Model whose dependencies will be removed
	 * @param dependencies List of MavenCoordinatesDto with the dependencies to remove
	 */
	public static void removeDependencies(Model model, List<MavenCoordinatesDto> dependencies) {
		
		for (MavenCoordinatesDto dependency : dependencies) {
			removeDependency(model.getDependencies(), toMavenDependency(dependency));
		}
	}
	
	/**
	 * Given a model, filters the dependencies whose groupId starts with an specific groupId
	 * @param model Model already read Model from the pom.xml
	 * @param groupId String
	 * @return List of filtered dependencies; never null
	 */
	public static List<MavenCoordinatesDto> filterDependenciesStartWith(Model model, String groupId) {
		
		// (sanity check)
		if (model == null) {
			return Collections.emptyList();
		}
		
		return filterStartWith(model.getDependencies(), model.getProperties(), groupId);
	}
	
	/**
	 * Adds managed dependencies to the model
	 * @param model Model where the managed dependencies will be added
	 * @param dependencies List of MavenCoordinatesDto with the managed dependencies to add
	 */
	public static void addManagedDependencies(Model model, List<MavenCoordinatesDto> dependencies) {
		
		if (model.getDependencyManagement() == null) {
			model.setDependencyManagement(new DependencyManagement());
		}
		
		for (MavenCoordinatesDto dependency : dependencies) {
			addDependency(model.getDependencyManagement().getDependencies(), toMavenDependency(dependency));
		}
	}
	
	/**
	 * Adds a managed dependency to the model
	 * @param model Model where the managed dependency will be added
	 * @param dependency managed depednency to add
	 */
	public static void addManagedDependency(Model model, MavenCoordinatesDto dependency) {
		
		if (model.getDependencyManagement() == null) {
			model.setDependencyManagement(new DependencyManagement());
		}
		
		addDependency(model.getDependencyManagement().getDependencies(), toMavenDependency(dependency));
	}
	
	/**
	 * Given a model, finds the managed dependency with an specific groupId and artifactId
	 * @param model Model already read Model from the pom.xml
	 * @param groupId String
	 * @param artifactId String
	 * @return found MavenCoordinatesDto or null
	 */
	public static MavenCoordinatesDto findManagedDependency(Model model, String groupId, String artifactId) {
		
		// (sanity check)
		if ((model == null) || (model.getDependencyManagement() == null)) {
			return null;
		}
		
		return find(model.getDependencyManagement().getDependencies(), model.getProperties(), groupId, artifactId);
	}
	
	/**
	 * Convenience method that adds a Dependency to a List if it is not already (considering only groupId and artifactId)
	 * @param list List of Dependency, not null
	 * @param dependency Dependency, not null
	 */
	private static void addDependency(List<Dependency> list, Dependency dependency) {
		
		for (Dependency it : list) {
			if (StringUtils.equals(it.getGroupId(), dependency.getGroupId())
					&& StringUtils.equals(it.getArtifactId(), dependency.getArtifactId())) {
				return;
			}
		}
		
		list.add(dependency);
	}
	
	/**
	 * Convenience method that removes a Dependency from a List (considering only groupId and artifactId)
	 * @param list List of Dependency, not null
	 * @param dependency Dependency, not null
	 */
	private static void removeDependency(List<Dependency> list, Dependency dependency) {
		
		for (Iterator<Dependency> iterator = list.iterator(); iterator.hasNext();) {
			Dependency it = iterator.next();
			
			if (StringUtils.equals(it.getGroupId(), dependency.getGroupId())
					&& StringUtils.equals(it.getArtifactId(), dependency.getArtifactId())) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Given a list of dependencies, finds the dependency with an specific groupId and artifactId
	 * @param dependencies List of Dependency
	 * @param properties Properties of the Model
	 * @param groupId String
	 * @param artifactId String
	 * @return found MavenCoordinatesDto or null
	 */
	private static MavenCoordinatesDto find(
			List<Dependency> dependencies, Properties properties, String groupId, String artifactId) {
		
		// (sanity check)
		if ((dependencies == null)
				|| dependencies.isEmpty()
				|| StringUtils.isEmpty(groupId)
				|| StringUtils.isEmpty(artifactId)) {
			return null;
		}
		
		// Searchs in the list of dependencies
		for (Dependency dependency : dependencies) {
			MavenCoordinatesDto dto = toMavenCoordinatesDto(dependency, properties);
			if (StringUtils.equals(dto.getGroupId(), groupId)
					&& StringUtils.equals(dto.getArtifactId(), artifactId)) {
				return dto;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Given a list of dependencies, filters the dependencies whose groupId starts with an specific groupId
	 * @param dependencies List of Dependency
	 * @param properties Properties of the Model
	 * @param groupId String
	 * @return List of filtered dependencies; never null
	 */
	private static List<MavenCoordinatesDto> filterStartWith(
			List<Dependency> dependencies, Properties properties, String groupId) {
		
		// (sanity check)
		if ((dependencies == null)
				|| dependencies.isEmpty()
				|| StringUtils.isEmpty(groupId)) {
			return Collections.emptyList();
		}
		
		List<MavenCoordinatesDto> list = new ArrayList<MavenCoordinatesDto>();
		for (Dependency dependency : dependencies) {
			MavenCoordinatesDto dto = toMavenCoordinatesDto(dependency, properties);
			if (StringUtils.startsWith(dto.getGroupId(), groupId)) {
				list.add(dto);
			}
		}
		return list;
	}
	
	/**
	 * Converts a Dependency into a MavenCoordinatesDto, properties-aware
	 * @param dependency Dependency, or null
	 * @param properties Properties
	 * @return MavenCoordinatesDto, or null
	 */
	private static MavenCoordinatesDto toMavenCoordinatesDto(Dependency dependency, Properties properties) {
		
		// (sanity check)
		if (dependency == null) {
			return null;
		}
		
		MavenCoordinatesDto dto = new MavenCoordinatesDto();
		dto.setGroupId(readValue(dependency.getGroupId(), properties));
		dto.setArtifactId(readValue(dependency.getArtifactId(), properties));
		dto.setVersion(readValue(dependency.getVersion(), properties));
		dto.setScope(readValue(dependency.getScope(), properties));
		return dto;
	}
	
	/**
	 * Converts a Dependency into a MavenCoordinatesDto, properties-aware
	 * @param dto MavenCoordinatesDto, or null
	 * @return Dependency, or null
	 */
	private static Dependency toMavenDependency(MavenCoordinatesDto dto) {
		
		// (sanity check)
		if (dto == null) {
			return null;
		}
		
		Dependency dependency = new Dependency();
		dependency.setGroupId(dto.getGroupId());
		dependency.setArtifactId(dto.getArtifactId());
		dependency.setVersion(dto.getVersion());
		dependency.setScope(dto.getScope());
		return dependency;
	}
	
	/**
	 * Reads a value, properties-aware
	 * @param value String with the value
	 * @param properties Properties
	 * @return the value, processed as a property if necessary
	 */
	private static String readValue(String value, Properties properties) {
		
		// (sanity checks)
		if (value == null) {
			return null;
		}
		if (StringUtils.isBlank(value)) {
			return value;
		}
		
		// If the value is not a placeholder, returns the value
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
		if (!matcher.find()) {
			return value;
		}
		
		// Returns the value of the property
		return properties.getProperty(matcher.group(1), value);
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private ModelUtil() {
		super();
	}
}
