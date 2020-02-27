/**
 * 
 */
package cloud.altemista.fwk.plugin.maven.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import cloud.altemista.fwk.plugin.config.Constant;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.plugin.core.util.FileUtil;
import cloud.altemista.fwk.plugin.maven.util.ModelUtil;

/**
 * Represents a Maven project location and eases access to its Model and other related Maven project locations
 * @author NTT DATA
 */
public class ModelAndLocation implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 8517354650350719825L;

	/** The default filename of the pom.xml file */
	public static final String POM_XML_FILE = "pom.xml";
	
	/** The default relative path of the parent */
	private static final String DEFAULT_PARENT_RELATIVE_PATH = "..";
	
	/** The Maven project location */
	private final String location;

	/** The Model for the pom.xml */
	private transient Model model;
	
	/**
	 * Constructor
	 * @param location The maven project location
	 */
	public ModelAndLocation(String location) {
		super();
		
		if (StringUtils.isBlank(location)) {
			throw new IllegalArgumentException("location null or empty");
		}
		
		this.location = FilenameUtils.normalize(StringUtils.removeEnd(location, POM_XML_FILE));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return new ToStringBuilder(this, Constant.TO_STRING_STYLE)
				.append("location", this.location)
				.toString();
	}

	/**
	 * Returns this Maven project location
	 * @return this Maven project location
	 */
	public String getLocation() {
		
		return this.location;
	}
	
	/**
	 * Convenience method to calculate this Maven project pom.xml file location
	 * @return the pom.xml file location
	 */
	public String getPomXmlLocation() {
		
		return FilenameUtils.concat(this.location, POM_XML_FILE);
	}
	
	/**
	 * Convenience method to get this Maven project pom.xml file location if it is a readable file
	 * @return the pom.xml file
	 */
	public File getPomXmlFile() {
		
		// Calucaltes the pom.xml file location
		final String pomXmlLocation = this.getPomXmlLocation();
		
		// Gets the file
		final File pomXmlFile = FileUtil.getFile(pomXmlLocation);
		if (pomXmlFile == null) {
			throw new PluginException(
					"Unreadable pom.xml",
					"pom.xml does not exists or is not readable",
					"No readable pom.xml file found at %s",
					pomXmlLocation);
		}
		
		return pomXmlFile;
	}

	/**
	 * Returns this Model, reading it if necessary from the pom.xml file at {@link #location}
	 * @return the model
	 */
	public Model getModel() {
		
		if (this.model == null) {
			this.refresh();
		}
		
		return this.model;
	}
	
	/**
	 * Discards any change done in the model returned by {@link #getModel()}
	 * and reads the model again from pom.xml file at {@link #location}
	 */
	public void refresh() {
		
		this.model = ModelUtil.readModel(this.location);
	}
	
	/**
	 * Obtains the location of the parent of this Maven project
	 * @return ModelAndLocation parent Maven project location or null if there is no parent
	 */
	public ModelAndLocation gotoParent() {
		
		// Locates <parent> section
		Parent parent = this.getModel().getParent();
		if (parent == null) {
			return null;
		}
		
		// Calculates the parent location
		final String relativePath = StringUtils.defaultIfBlank(parent.getRelativePath(), DEFAULT_PARENT_RELATIVE_PATH);
		final String parentLocation = FilenameUtils.concat(this.getLocation(), relativePath);
		return new ModelAndLocation(parentLocation);
	}
	
	/**
	 * Obtains the location of the modules of this multi-module Maven project
	 * @return List of Maven modules locations or empty list if there is no modules
	 */
	public List<ModelAndLocation> gotoModules() {
		
		// Locates <modules> section
		List<String> modules = this.getModel().getModules();
		if (modules.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Calculates the parent location
		List<ModelAndLocation> moduleLocations = new ArrayList<ModelAndLocation>(modules.size());
		for (String module : modules) {
			moduleLocations.add(new ModelAndLocation(FilenameUtils.concat(this.getLocation(), module)));
		}
		return moduleLocations;
	}
}
