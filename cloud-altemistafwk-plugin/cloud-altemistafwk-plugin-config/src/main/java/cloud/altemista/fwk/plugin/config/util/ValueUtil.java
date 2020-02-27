/**
 * 
 */
package cloud.altemista.fwk.plugin.config.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cloud.altemista.fwk.plugin.config.model.ValueDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * Configuration values container, supporting caching and version filtering
 * @author NTT DATA
 */
public class ValueUtil {
	
	/**
	 * Finds a value in a collection of ValueDto, including deprecated values and any version
	 * @param <T> the type of the ValueDto
	 * @param collection the collection of ValueDto
	 * @param value the internal value
	 * @return the first ValueDto of the collection that matches the internal value, or null if no value was found
	 */
	public static <T extends ValueDto> T find(Collection<T> collection, String value) {
		
		return find(collection, value, null);
	}
	
	/**
	 * Finds a value in a collection of ValueDto, including deprecated values
	 * @param <T> the type of the ValueDto
	 * @param collection the collection of ValueDto
	 * @param value the internal value
	 * @param version String with the version; if null, the collection will not be filtered by version
	 * @return the first ValueDto of the collection that matches the internal value, or null if no value was found
	 */
	public static <T extends ValueDto> T find(Collection<T> collection, String value, String version) {
		
		return find(collection, value, true, version);
	}
	
	/**
	 * Finds a value in a collection of ValueDto
	 * @param <T> the type of the ValueDto
	 * @param collection the collection of ValueDto
	 * @param value the internal value
	 * @param allowDeprecated true to allow deprecated values
	 * @param pVersion String with the version; if null, the collection will not be filtered by version
	 * @return the first ValueDto of the collection that matches the internal value, or null if no value was found
	 */
	public static <T extends ValueDto> T find(Collection<T> collection,
			String value, boolean allowDeprecated, String pVersion) {
		
		// (sanity check)
		if ((collection == null) || collection.isEmpty()) {
			return null;
		}
		
		// Instantiates the ArtifactVersion object only once
		ArtifactVersion version = null;
		if (StringUtils.isNotEmpty(pVersion)) {
			version = new DefaultArtifactVersion(pVersion);
		}
		
		// Looks for the internal value
		// (does not use filter() to get the filtered collection for peformance reasons)
		for (T t : collection) {
			if (StringUtils.equals(t.getValue(), value)
					&& ((version == null) || t.isValidFor(version))
					&& (allowDeprecated || (!t.isDeprecated()))) {
				return t;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Filters non-deprecated values from a collection of ValueDto
	 * @param <T> the type of the ValueDto
	 * @param collection the collection of ValueDto
	 * @param version String with the version; if null, the collection will not be filtered by version
	 * @return List of ValueDto, never null
	 */
	public static <T extends ValueDto> List<T> filter(Collection<T> collection, String version) {
		
		return filter(collection, false, version);
	}
	
	/**
	 * Filters a collection of ValueDto
	 * @param <T> the type of the ValueDto
	 * @param collection the collection of ValueDto
	 * @param allowDeprecated true to allow deprecated values
	 * @param pVersion String with the version; if null, the collection will not be filtered by version
	 * @return List of ValueDto, never null
	 */
	public static <T extends ValueDto> List<T> filter(Collection<T> collection,
			boolean allowDeprecated, String pVersion) {
		
		// (sanity check)
		if ((collection == null) || collection.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Instantiates the ArtifactVersion object only once
		ArtifactVersion version = null;
		if (StringUtils.isNotEmpty(pVersion)) {
			version = new DefaultArtifactVersion(pVersion);
		}
		
		// Discards by version and by deprecated
		List<T> ret = new ArrayList<T>();
		for (T t : collection) {
			if (((version == null) || t.isValidFor(version))
					&& (allowDeprecated || (!t.isDeprecated()))) {
				ret.add(t);
			}
		}
		return ret;
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private ValueUtil() {
		super();
	}
	
	public static <T extends ValueDto> List<T> filterAggreator(List<T> ret, String value) {
		List<T> newRet = new ArrayList<T>();
		for(T t: ret ){
			if(t.isValidFor(value)){
				newRet.add(t);
			}
		}
		return newRet;
	}
}
