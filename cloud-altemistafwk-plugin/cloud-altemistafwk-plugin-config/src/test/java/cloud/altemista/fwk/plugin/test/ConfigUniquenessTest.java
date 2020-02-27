/**
 * 
 */
package cloud.altemista.fwk.plugin.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.model.ConfigHolder;
import cloud.altemista.fwk.plugin.config.model.OptionDto;
import cloud.altemista.fwk.plugin.config.model.ValueDto;

/**
 * Tests there are no duplicates per version 
 * @author NTT DATA
 */
public class ConfigUniquenessTest {
	
	private List<OptionDto> versions = Config.VERSIONS.allValues();

	/**
	 * Ensures Config.VERSIONS is valid
	 */
	@Before
	public void testVersions() {
		
		Assume.assumeNotNull(this.versions);
		Assume.assumeFalse(this.versions.isEmpty());
	}

	/**
	 * Tests Config.BUSINESS_MODULES
	 */
	@Test
	public void testBusinessModules() {
		
		this.testUniqueness(Config.BUSINESS_MODULES, "Config.BUSINESS_MODULES");
	}

	/**
	 * Tests Config.MODULE_PROJECTS
	 */
	@Test
	public void testModuleProjects() {
		
		this.testUniqueness(Config.MODULE_PROJECTS, "Config.MODULE_PROJECTS");
	}

	/**
	 * Tests Config.MODULE_FEATURES
	 */
	@Test
	public void testFeatures() {
		
		this.testUniqueness(Config.FEATURES, "Config.FEATURES");
	}

	/**
	 * Tests Config.APPLICATION_ARCHETYPES
	 */
	@Test
	public void testApplicationArchetypes() {
		
		this.testUniqueness(Config.APPLICATION_ARCHETYPES, "Config.APPLICATION_ARCHETYPES");
	}
	
	/**
	 * Tests the list of values has no duplicates
	 * @param configHolder ConfigHolder
	 * @param pTrace String
	 */
	private <T extends ValueDto> void testUniqueness(ConfigHolder<T> configHolder, String pTrace) {
		
		// For each known version
		for (OptionDto versionDto : this.versions) {
			final String version = versionDto.getValue();
			final String trace = String.format("[%s:%s]", pTrace, version);
			
			// Test not empty per version
			final List<T> currentVersionValues = configHolder.values(version);
			Assert.assertNotNull(trace, currentVersionValues);
			Assert.assertFalse(trace, currentVersionValues.isEmpty());
			
			// Test no duplicates per version
			final Map<String, T> knownValues = new HashMap<String, T>();
			for (T valueDto : currentVersionValues) {
				final String value = valueDto.getValue();
				
				if (knownValues.containsKey(value)) {
					Assert.fail(String.format("%s duplicate value: %s:\n*\t%s\n*\t%s",
							trace, value, knownValues.get(value), valueDto));
				}
				
				knownValues.put(value, valueDto);
			}
		}
	}
}
