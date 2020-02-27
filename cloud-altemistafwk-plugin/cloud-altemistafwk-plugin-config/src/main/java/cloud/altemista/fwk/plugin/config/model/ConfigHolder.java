/**
 * 
 */
package cloud.altemista.fwk.plugin.config.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cloud.altemista.fwk.plugin.config.util.ValueUtil;
import cloud.altemista.fwk.plugin.config.util.YamlUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Configuration values container, supporting caching and version filtering
 * @param <T> Type of the values
 * @author NTT DATA
 */
public class ConfigHolder<T extends ValueDto> implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -4309148280069671656L;

	/** The pattern to resolve the resource locations to avoid repetition */
	private static final String RESOURCE_PATTERN = "/cloud/altemista/fwk/plugin/config/%s.yaml"; //$NON-NLS-1$

	/** The class of the configuration value */
	private final Class<T> class_;
	
	/** The location of the resource */
	private final String[] resourceNames;
	
	/** Cached version of the read values */
	private transient List<T> cachedValues;
	
	/**
	 * Convenience constructor
	 * @param class_ The class of the configuration value
	 * @param fileNames The name of configuration files without neither path nor extension
	 */
	public ConfigHolder(Class<T> class_, String... fileNames) {
		this(class_, false, fileNames);
	}
	
	/**
	 * Constructor
	 * @param class_ The class of the configuration value
	 * @param absolute true if the location of the resource is absolute
	 * @param resourceNames The location of the configuration files as resources
	 */
	public ConfigHolder(Class<T> class_, boolean absolute, String... resourceNames) {
		super();
		
		this.class_ = class_;
		if (ArrayUtils.isEmpty(resourceNames)) {
			this.resourceNames = new String[0];
			
		}else if (absolute) {
			this.resourceNames = resourceNames;
			
		} else {
			final int n = resourceNames.length;
			this.resourceNames = new String[n];
			for (int i = 0; i < n; i++) {
				this.resourceNames[i] = String.format(RESOURCE_PATTERN, resourceNames[i]);
			}
		}
	}
	
	/**
	 * Return the first value of this collection (including deprecated values) that matches an internal value
	 * @param value the internal value
	 * @return T or null if no match
	 */
	public T value(String value) {
		
		// (sanity check)
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		
		return ValueUtil.find(this.allValues(), value, true, null);
	}
	
	/**
	 * Return the first non-deprecated value of this collection
	 * that matches an internal value and is valid for a specific version
	 * @param value the internal value
	 * @param version String with the version; if null, the collection will not be filtered by version
	 * @return T or null if no match
	 */
	public T value(String value, String version) {
		
		// (sanity check)
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		
		return ValueUtil.find(this.allValues(), value, false, version);
	}
	
	/**
	 * Returns all the non-deprecated values from a collection of ValueDto
	 * @return the non-deprecated values
	 */
	public List<T> values() {
		
		return this.values(null);
	}
	
	public List<T> values(String version, ModuleProjectTypeDto aggregator) {
		 
		List<T> ret = ValueUtil.filter(this.allValues(), version);
		ret=ValueUtil.filterAggreator(ret, aggregator.getValue());
		return ret;
	}
	

	public List<T> values(String version,String aggregator) {
		 
		List<T> ret = ValueUtil.filter(this.allValues(), version);
		ret=ValueUtil.filterAggreator(ret, aggregator);
		return ret;
	}

	
	/**
	 * Returns all the non-deprecated values from a collection of ValueDto, filtered for a specific version
	 * @param version String with the version; if null, the collection will not be filtered by version
	 * @return the non-deprecated, filtered by version, values
	 */
	public List<T> values(String version) {
		
		return ValueUtil.filter(this.allValues(), version);
	}
	
	/**
	 * Return all the values (including deprecated values) of this collection, using cache
	 * @return the values
	 */
	public List<T> allValues() {
		
		if (this.cachedValues == null) {
			
			// Read the values from the YAML configuration file
			List<T> list = new ArrayList<T>();
			for (String resourceName : this.resourceNames) {
				list.addAll(YamlUtil.readArray(this.class_, resourceName));
			}
			
			// Stores the values in the cache
			this.cachedValues = Collections.unmodifiableList(list);
		}
		
		// Returns the cached values
		return this.cachedValues;
	}
}
