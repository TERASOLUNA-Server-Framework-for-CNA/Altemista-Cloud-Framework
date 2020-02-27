/**
 * 
 */
package cloud.altemista.fwk.plugin.config.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cloud.altemista.fwk.plugin.common.exception.ConfigException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * Utility class for reading the plug-in YAML configuration files.
 * @author NTT DATA
 */
public class YamlUtil {
	
	/** The static YAML reader configuration */
	public static final YamlConfig CONFIG = new YamlConfig();
	
	/**
	 * Reads an array of entities
	 * @param <T> the type of the array to read
	 * @param class_ the type of array to read
	 * @param resourceName the name of the resource
	 * @return Read array
	 */
	public static <T> List<T> readArray(Class<T> class_, String resourceName) {
		
		// (sanity check)
		if (class_ == null) {
			throw new ConfigException("class is null");
		}
		if (StringUtils.isEmpty(resourceName)) {
			throw new ConfigException("resourceName is null or empty");
		}
		
		// Opens the input stream to the resource
		InputStream inputStream = null;
		Reader inputStreamReader = null;
		Reader bufferedReader = null;
		YamlReader yamlReader = null;
		try {
			inputStream = class_.getResourceAsStream(resourceName);
			if (inputStream == null) {
				throw new ConfigException(String.format("getResourceAsStream(\"%s\") returned null", resourceName));
			}
			
			// Opens the YamlReader to the input stream
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			yamlReader = new YamlReader(inputStreamReader, CONFIG);
			
			List<T> list = new ArrayList<T>();
			for (T entity; (entity = yamlReader.read(class_)) != null;) {
				list.add(entity);
			}
			return list;
			
		} catch (YamlException e) {
			throw new ConfigException(String.format("Could not read configuration file \"%s\"", resourceName), e);
			
		} finally {
			closeQuietly(yamlReader);
			IOUtils.closeQuietly(bufferedReader);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * Convenience method to close a YamlReader ignoring the exception on closing
	 * @param yamlReader YamlReader; can be null
	 */
	private static void closeQuietly(YamlReader yamlReader) {
		
		if (yamlReader == null) {
			return;
		}
		
		try {
			yamlReader.close();
		} catch (IOException ignored) { // NOSONAR
			// (ignored)
		}
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private YamlUtil() {
		super();
	}

}
