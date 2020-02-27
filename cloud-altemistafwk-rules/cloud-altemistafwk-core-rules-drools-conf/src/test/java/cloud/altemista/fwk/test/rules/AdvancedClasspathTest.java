package cloud.altemista.fwk.test.rules;

/*
 * #%L
 * altemista-cloud rules engine: Drools implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import cloud.altemista.fwk.core.rules.RulesService;
import cloud.altemista.fwk.core.rules.StatelessRulesExecutor;
import cloud.altemista.fwk.core.rules.exception.RulesEngineException;
import cloud.altemista.fwk.core.rules.ruleset.Ruleset;
import cloud.altemista.fwk.test.AbstractSpringContextTest;
import cloud.altemista.fwk.test.rules.model.DummyBean;

/**
 * Tests for the rules module, Drools implementation, using advanced configuration
 */
@ContextConfiguration(locations = { "classpath:spring/test-app-context-rules-advanced.xml" })
public class AdvancedClasspathTest extends AbstractSpringContextTest {

	/** The name of the ruleset */
	private static final String MY_RULES = "myKieBase";

	/** The message that identifies the ruleset is being applied */
	private static final String MY_RULES_MESSAGE = "my rules";

	/** The name of the ruleset #1 */
	private static final String RULES_1 = "ruleset_1";
	
	/** The message that identifies the ruleset #1 is being applied */
	private static final String RULES_1_MESSAGE = "rules1 rules";
	
	/** The name of the ruleset #2 */
	private static final String RULES_2 = "ruleset_2";
	
	/** The message that identifies the ruleset #2 being applied */
	private static final String RULES_2_MESSAGE = "rules2 rules";
	
	/** The name of the ruleset #3 */
	private static final String RULES_3 = "ruleset_3";

	/** The default configuration Drools RulesResolver */
	@Autowired
	private RulesService service;
	
	/**
	 * Test the default rule set is available
	 */
	@Test
	public void testHasDefaultRuleset() {
		
		Ruleset ruleset = this.service.defaultRuleset();
		Assert.assertNotNull(ruleset);
	}
	
	/**
	 * Test that uses the default rule set implicitly
	 */
	@Test
	public void testImplicitDefaultRuleset() {
		
		// (In this example, the default container is the alternative one
		// and the default ruleset is the ruleset #1)
		this.testExecutor("implicit default ruleset", this.service, RULES_1_MESSAGE);
	}

	/**
	 * Test that uses the default rule set explicitly
	 */
	@Test
	public void testExplicitDefaultRuleset() {
		
		// (In this example, the default container is the alternative one
		// and the default ruleset is the ruleset #1)
		Ruleset defaultRuleset = this.service.defaultRuleset();
		this.testExecutor("default ruleset", defaultRuleset, RULES_1_MESSAGE);
	}

	/**
	 * Test that uses a rule set contained in an alternative container (not the default container)
	 */
	@Test
	public void testAltcontainer1() {
		
		Ruleset ruleset1 = this.service.getRuleset(RULES_1);
		this.testExecutor("ruleset1", ruleset1, RULES_1_MESSAGE);
	}

	/**
	 * Test that uses a rule set contained in an alternative container (not the default container)
	 */
	@Test
	public void testAltcontainer2() {
		
		Ruleset ruleset2 = this.service.getRuleset(RULES_2);
		this.testExecutor("ruleset2", ruleset2, RULES_2_MESSAGE);
	}

	/**
	 * Test that a rule set contained in the wrong container (due name-based resolver configuration) is not available
	 */
	@Test(expected = RulesEngineException.class)
	public void testWrongContainer() {
		
		// (The ruleset #3 is available in the alternative container,
		// but the name-based mapper is not using that container for the ruleset #3) 
		this.service.getRuleset(RULES_3);
	}	

	/**
	 * Test that uses a rule set contained in an fallback container
	 */
	@Test
	public void testFallbackContainer() {
		
		Ruleset defaultRuleset = this.service.getRuleset(MY_RULES);
		this.testExecutor("default ruleset by name", defaultRuleset, MY_RULES_MESSAGE);
	}
	
	/**
	 * Convenience method
	 * @param description String to show a different description for each execution
	 * @param executor RulesExecutor to test
	 * @param message String with the output message to look for
	 */
	private void testExecutor(String description, StatelessRulesExecutor executor, String message) {
		
		// Creates the single input
		DummyBean input = new DummyBean();
		Assume.assumeTrue(input.getMessages().isEmpty());
		
		// Executes the rule engine
		Assert.assertNotNull(description, executor);
		DummyBean output = executor.executeRules(input);
		
		// Tests the output
		Assert.assertNotNull(description, output);
		Assert.assertNotNull(description, output.getMessages());
		Assert.assertTrue(String.format("%s: expected [%s] but found [%s]",
				description, message, StringUtils.join(output.getMessages(), ", ")),
				output.getMessages().contains(message));
	}
}
