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


import org.apache.commons.lang3.RandomUtils;
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
 * Tests for the rules module, Drools implementation, using a simple configuration
 */
@ContextConfiguration(locations = { "classpath:spring/test-app-context-rules-simple.xml" })
public class SimpleClasspathTest extends AbstractSpringContextTest {

	/** The name of the default ruleset (that should be no longer available) */
	private static final String DEFAULT_RULES = "defaultKieBase";

	/** The name of the ruleset */
	private static final String MY_RULESET = "myKieBase";
	
	/** The message that identifies the proper ruleset is being applied */
	private static final String MY_RULES_MESSAGE = "my rules";
	
	/** The default configuration Drools RulesResolver */
	@Autowired
	private RulesService service;
	
	/**
	 * Test for the execute method
	 */
	@Test(expected = RulesEngineException.class)
	public void testHasDefaultRuleset() {
		
		// (there is no default ruleset in the simple configuration example)
		this.service.defaultRuleset();
	}
	
	/**
	 * Test for the executeRules method
	 */
	@Test(expected = RulesEngineException.class)
	public void testExecuteRulesInDefaultRuleset() {
		
		// (there is no default ruleset in the simple configuration example)
		this.testSingleBean(this.service);
	}

	/**
	 * Test for the executeRules method
	 */
	@Test(expected = RulesEngineException.class)
	public void testExecuteRulesINDefaultRulesetExplicitly() {
		
		// (there is no ruleset with the internal default name in the simple configuration example)
		Ruleset ruleset = this.service.getRuleset(DEFAULT_RULES);
		Assume.assumeNotNull(ruleset);
		
		this.testSingleBean(ruleset);
	}

	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testExecuteRules() {
		
		// Gets the default rule set
		Ruleset ruleset = this.service.getRuleset(MY_RULESET);
		Assume.assumeNotNull(ruleset);
		
		this.testSingleBean(ruleset);
	}

	/**
	 * Convenience method for testing the executeRules over a single bean
	 * @param executor
	 */
	private void testSingleBean(StatelessRulesExecutor executor) {
		
		// Creates the single input
		boolean randomFlag = RandomUtils.nextInt(0, 1) == 1;
		DummyBean input = new DummyBean();
		input.setFlag(randomFlag);
		Assume.assumeTrue(input.getMessages().isEmpty());
		
		// Executes the rule engine
		DummyBean output = executor.executeRules(input);
		
		// Tests the output
		Assert.assertNotNull(output);
		Assert.assertNotNull(output.getMessages());
		Assert.assertTrue(output.getMessages().contains(MY_RULES_MESSAGE));
	}
}
