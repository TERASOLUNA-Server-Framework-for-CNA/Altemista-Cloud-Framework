/**
 * 
 */
package cloud.altemista.fwk.plugin.test;

import java.util.List;

import cloud.altemista.fwk.plugin.config.Config;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the configuration is readable from the YAML files 
 * @author NTT DATA
 */
public class ConfigReadabilityTest {
	
	/**
	 * Tests Config.VERSIONS
	 */
	@Test
	public void testVersions() {
		
		this.testNotNullNotEmpty(Config.VERSIONS.values());
	}

	/**
	 * Tests Config.BUSINESS_MODULES
	 */
	@Test
	public void testBusinessModules() {
		
		this.testNotNullNotEmpty(Config.BUSINESS_MODULES.values());
	}

	/**
	 * Tests Config.MODULE_PROJECTS
	 */
	@Test
	public void testModuleProjects() {
		
		this.testNotNullNotEmpty(Config.MODULE_PROJECTS.values());
	}

	/**
	 * Tests Config.MODULE_FEATURES
	 */
	@Test
	public void testFeatures() {
		
		this.testNotNullNotEmpty(Config.FEATURES.values());
	}

	/**
	 * Tests Config.APPLICATION_ARCHETYPES
	 */
	@Test
	public void testApplicationArchetypes() {
		
		this.testNotNullNotEmpty(Config.APPLICATION_ARCHETYPES.values());
	}
	
	/**
	 * Tests the list of values is not null, not empty and print the values in the console
	 * @param values List
	 */
	private <T> void testNotNullNotEmpty(List<T> values) {
		
		Assert.assertNotNull(values);
		Assert.assertFalse(values.isEmpty());
		for (T value : values) {
			System.out.println(value);
		}
		System.out.println();
	}
}
