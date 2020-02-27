/**
 * 
 */
package cloud.altemista.fwk.plugin.common.exception;

/**
 * Plug-in configuration exception
 * @author NTT DATA
 */
public class ConfigException extends RuntimeException {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -8906277259272299304L;

	/**
	 * Constructor
	 * @param message the detail message
	 */
	public ConfigException(String message) {
		super(message);
	}
	
	/**
	 * Constructor
	 * @param cause the cause
	 * @param message the detail message
	 */
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
}
