/**
 * 
 */
package cloud.altemista.fwk.plugin.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cloud.altemista.fwk.plugin.common.model.MavenCoordinatesDto;
import org.apache.commons.lang3.StringUtils;

/**
 * Utilities related to MavenCoordinatesDto class
 * @author NTT DATA
 */
public class MavenCoordinatesUtil {
	
	/**
	 * Given a list of coordinates, filters the coordinates whose groupId starts with an specific groupId
	 * @param list List of MavenCoordinatesDto
	 * @param groupId String
	 * @return List of filtered coordinates; never null
	 */
	public static List<MavenCoordinatesDto> filterDependenciesStartWith(
			List<MavenCoordinatesDto> list, String groupId, String goupIdAltemista) {
		
		// (sanity check)
		if ((list == null)
				|| list.isEmpty()
				|| StringUtils.isEmpty(groupId)) {
			return Collections.emptyList();
		}
		
		// Filters the list
		List<MavenCoordinatesDto> filteredList = new ArrayList<MavenCoordinatesDto>();
		for (MavenCoordinatesDto dto : list) {
			if (StringUtils.startsWith(dto.getGroupId(), groupId)) {
				filteredList.add(dto);
			}else if(StringUtils.startsWith(dto.getGroupId(), goupIdAltemista)) {
				filteredList.add(dto);
			}
		}
		return filteredList;
	}
	
	/**
	 * Checks if the list of MavenCoordinatesDto contains at least one of the values
	 * (comparing groupId and artifactId only)
	 * @param collection Collection of MavenCoordinatesDto
	 * @param values Collection of MavenCoordinatesDto
	 * @return true if the list contains at least one of the values
	 */
	public static boolean containsAny(
			Collection<MavenCoordinatesDto> collection, Collection<MavenCoordinatesDto> values) {
		
		// (sanity check)
		if ((values == null) || values.isEmpty()) {
			return false;
		}
		
		// Searchs for any value in the collection
		for (MavenCoordinatesDto value : values) {
			if (contains(collection, value)) {
				return true;
			}
		}
		
		// Not match
		return false;
	}
	
	/**
	 * Checks if the list of MavenCoordinatesDto contains all the values (comparing groupId and artifactId only)
	 * @param collection Collection of MavenCoordinatesDto
	 * @param values Collection of MavenCoordinatesDto
	 * @return true if the list contains all the values, or the values set was empty
	 */
	public static boolean containsAll(
			Collection<MavenCoordinatesDto> collection, Collection<MavenCoordinatesDto> values) {
		
		// (sanity check)
		if ((values == null) || values.isEmpty()) {
			return true;
		}
		
		// Searchs for every value in the collection
		for (MavenCoordinatesDto value : values) {
			if (!contains(collection, value)) {
				return false;
			}
		}
		
		// All matches
		return true;
	}
	
	/**
	 * Checks if the list of MavenCoordinatesDto contains the value (comparing groupId and artifactId only)
	 * @param collection Collection of MavenCoordinatesDto
	 * @param value MavenCoordinatesDto
	 * @return true if the list contains the value
	 */
	public static boolean contains(Collection<MavenCoordinatesDto> collection, MavenCoordinatesDto value) {
		
		// (sanity check)
		if ((collection == null) || collection.isEmpty() || (value == null)) {
			return false;
		}
		
		// Searchs in the list
		for (MavenCoordinatesDto i : collection) {
			if (StringUtils.equals(value.getGroupId(), i.getGroupId())
					&& StringUtils.equals(value.getArtifactId(), i.getArtifactId())) {
				return true;
			}
		}
		
		// Not contained
		return false;
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private MavenCoordinatesUtil() {
		super();
	}

}
