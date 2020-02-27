/**
 * 
 */
package cloud.altemista.fwk.plugin.test;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.plugin.config.Config;
import cloud.altemista.fwk.plugin.config.model.BusinessModuleTypeDto;
import cloud.altemista.fwk.plugin.config.model.FeatureDto;
import cloud.altemista.fwk.plugin.config.model.FeatureImplementationDto;
import cloud.altemista.fwk.plugin.config.model.OptionDto;
import cloud.altemista.fwk.plugin.config.model.OptionWithArtifactDto;
import cloud.altemista.fwk.plugin.config.model.ProjectTemplateDto;

/**
 * Minimal tests the consistency of the configuration 
 * @author NTT DATA
 */
public class ConfigConsistencyTest {
	
	/**
	 * Tests configuration consistency
	 */
	@Test
	public void testConsistency() {
		
		final List<OptionDto> versions = Config.VERSIONS.allValues();
		Assert.assertNotNull(versions);
		
		// For each version
		for (OptionDto version : versions) {
			final String trace = String.format("[%s]", version.getValue());
			
			// Tests configuration consistency for this specific version
			this.testApplicationsArchetypesConsistency(version.getValue(), trace);
			this.testBusinessModuleTypesConsistentcy(version.getValue(), trace);
			this.testFeaturesConsistency(version.getValue(), trace);
			
			// This version is OK
			System.out.println(trace + "\tOk\n");
		}
	}

	/**
	 * Tests configuration consistency of the Maven archetype coordinates for new applications
	 * @param version String
	 * @param pTrace String
	 */
	private void testApplicationsArchetypesConsistency(String version, String pTrace) {
		
		final String trace = pTrace + " Config.APPLICATION_ARCHETYPES";
		final List<OptionWithArtifactDto> applicationArchetypes = Config.APPLICATION_ARCHETYPES.values(version);
		Assert.assertNotNull(trace, applicationArchetypes);
		Assert.assertFalse(trace, applicationArchetypes.isEmpty());
		
		// For each Maven archetype coordinates
		for (OptionWithArtifactDto applicationArchetype : applicationArchetypes) {
			final String subtrace = String.format("[%s:%s]", version, applicationArchetype.getValue());
			
			final boolean hasArchetype = (applicationArchetype.getArtifact() != null);
			Assert.assertTrue(subtrace + " has no archetype coordinates", hasArchetype);
			
			// This business module type is OK
			System.out.println(subtrace + "\tOk");
		}
		System.out.println(trace + "\tOk\n");
	}

	/**
	 * Tests configuration consistency of the business module types
	 * @param version String
	 * @param pTrace String
	 */
	private void testBusinessModuleTypesConsistentcy(String version, String pTrace) {
		
		final String trace = pTrace + " Config.BUSINESS_MODULES";
		final List<BusinessModuleTypeDto> types = Config.BUSINESS_MODULES.values(version);
		Assert.assertNotNull(trace, types);
		Assert.assertFalse(trace, types.isEmpty());
		
		// For each business module type
		for (BusinessModuleTypeDto type : types) {
			final String subtrace = String.format("[%s:%s]", version, type.getValue());
			
			final boolean hasProjectTemplates =
					(type.getProjectTemplates() != null) && (!type.getProjectTemplates().isEmpty());
			Assert.assertTrue(subtrace + " has no project templates", hasProjectTemplates);
			
			// For each project template
			for (ProjectTemplateDto template : type.getProjectTemplates()) {
				final String subsubtrace = String.format(
						"[%s:%s:%s]", version, type.getValue(), template.getArtifactIdFormatter());

				Assert.assertTrue(subsubtrace + " has no artifactId formatter",
						StringUtils.isNotEmpty(template.getArtifactIdFormatter()));
				
				final boolean hasArchetype = (template.getArchetype() != null);
				Assert.assertTrue(subsubtrace + " is not installable: has no archetype", hasArchetype);
				
				// This project template is OK
				System.out.println(subsubtrace + "\tOk");
			}
			
			// This business module type is OK
			System.out.println(subtrace + "\tOk");
		}
		System.out.println(trace + "\tOk\n");
	}

	/**
	 * Tests configuration consistency of the features
	 * @param version String
	 * @param pTrace String
	 */
	private void testFeaturesConsistency(String version, String pTrace) {
		
		final String trace = pTrace + " Config.FEATURES";
		final List<FeatureDto> features = Config.FEATURES.values(version);
		Assert.assertNotNull(trace, features);
		Assert.assertFalse(trace, features.isEmpty());
		
		// For each feature
		for (FeatureDto feature : features) {
			final String subtrace = String.format("[%s:%s]", version, feature.getValue());
			
			final boolean hasImplementations =
					(feature.getImplementations() != null) && (!feature.getImplementations().isEmpty());
			
			Assert.assertTrue(subtrace + " is not installable: "
					+ "it has neither archetype, dependencies nor implementations",
					feature.hasArchetype() || feature.hasDependencies() || hasImplementations);
			
			if (feature.isSelfImplemented()) {
				
				// Self-implemented
				Assert.assertTrue(
						subtrace + " claims to be self-implemented, but has neither archetype nor dependencies",
						feature.hasArchetype() || feature.hasDependencies());
				Assert.assertFalse(
						subtrace + " claims to be self-implemented, but has implementations",
						hasImplementations);
				
			} else {
				
				// Not self-implemented
				Assert.assertTrue(
						subtrace + " does not claim to be self-implemented, but has no implementations",
						hasImplementations);
				
				for (FeatureImplementationDto implementation : feature.getImplementations()) {
					final String subsubtrace = String.format("[%s:%s:%S]",
							version, feature.getValue(), implementation.getValue());
					
					Assert.assertTrue(subsubtrace + " is not installable: "
							+ "it has neither archetype nor dependencies",
							implementation.hasArchetype() || implementation.hasDependencies());
				}
			}
			
			// This feature is OK
			System.out.println(subtrace + "\tOk");
		}
		System.out.println(trace + "\tOk\n");
	}
}
