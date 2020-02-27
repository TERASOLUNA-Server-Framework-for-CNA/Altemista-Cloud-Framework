/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

/**
 * Convenience interface for objects that can return an ApplicationDto
 * @author NTT DATA
 */
public interface ApplicationDtoProvider {

	/**
	 * @return the ApplicationDto
	 */
	ApplicationDto getApplication();
}
