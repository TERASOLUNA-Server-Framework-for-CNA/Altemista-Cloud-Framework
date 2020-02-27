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


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cloud.altemista.fwk.core.rules.RulesService;
import cloud.altemista.fwk.core.rules.StatelessRulesExecutor;
import cloud.altemista.fwk.core.rules.ruleset.Ruleset;
import cloud.altemista.fwk.test.AbstractSpringContextTest;
import cloud.altemista.fwk.test.rules.model.DummyBean;

/**
 * Tests for the rules module, Drools implementation, with the default configuration
 */
public class DefaultTest extends AbstractSpringContextTest {

	/** The name of the default ruleset */
	private static final String DEFAULT_RULES = "defaultKieBase";
	
	/** The message that identifies the proper ruleset is being applied */
	private static final String DEFAULT_RULES_MESSAGE = "default rules";
	
	/** The message that identifies the conditional rule */
	private static final String FLAG_TRUE_MESSAGE = "flag was true";

	/** The default configuration Drools RulesResolver */
	//tag::autowire[]
	@Autowired
	private RulesService service;
	//end::autowire[]
	
	/**
	 * Test for the execute method
	 */
	@Test
	public void testHasDefaultRuleset() {
		
		// Gets the default rule set
		Ruleset ruleset = this.service.defaultRuleset();
		Assert.assertNotNull(ruleset);
	}
	
	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testDefaultExecuteRules() {
		
		// This uses the default rule set
		this.testSingleBean(this.service);
	}

	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testExecuteRulesUsingDefaultRuleset() {
		
		// Gets the default rule set
		Ruleset ruleset = this.service.defaultRuleset();
		Assume.assumeNotNull(ruleset);
		
		this.testSingleBean(ruleset);
	}

	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testExecuteRulesUsingDefaultRulesetByName() {
		
		// Gets the default rule set
		Ruleset ruleset = this.service.getRuleset(DEFAULT_RULES);
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
		//tag::example[]
		DummyBean input = new DummyBean();
		// (...)
		
		//end::example[]
		input.setFlag(randomFlag);
		Assume.assumeTrue(input.getMessages().isEmpty());
		
		// Executes the rule engine
		// (local alias for better naming in the documentation)
		final StatelessRulesExecutor service = executor;
		//tag::example[]
		DummyBean output = service.executeRules(input);
		//end::example[]
		
		// Tests the output
		Assert.assertNotNull(output);
		Assert.assertNotNull(output.getMessages());
		Assert.assertTrue(output.getMessages().contains(DEFAULT_RULES_MESSAGE));
		Assert.assertEquals(randomFlag, output.getMessages().contains(FLAG_TRUE_MESSAGE));
	}
	
	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testDefaultExecuteRulesList() {
		
		// This uses the default rule set
		this.testMultipleBeans(this.service); 
	}
	
	/**
	 * Test for the executeRules method
	 */
	@Test
	public void testDefaultExecuteRulesListUsingDefaultRuleset() {
		
		// Gets the default rule set
		Ruleset ruleset = this.service.defaultRuleset();
		Assume.assumeNotNull(ruleset);
		
		this.testMultipleBeans(ruleset); 
	}

	/**
	 * Convenience method for testing the executeRules over a collection
	 * @param executor
	 */
	private void testMultipleBeans(StatelessRulesExecutor executor) {
		
		// Creates the inputs
		List<DummyBean> inputs = new ArrayList<DummyBean>();
		for (int i = 0, n = 10; i < n; i++) {
			DummyBean input = new DummyBean();
			input.setNumber(i);
			input.setFlag((i % 2) == 0);
			inputs.add(input);
		}
		
		// Executes the rule engine
		List<DummyBean> outputs = executor.executeRules(inputs);
		
		// Tests the output
		Assert.assertNotNull(outputs);
		Assert.assertEquals(inputs.size(), outputs.size());
		for (int i = 0, n = 10; i < n; i++) {
			final String message = "#" + i;
			DummyBean output = outputs.get(i);
			Assert.assertNotNull(message, output);
			
			// (ensures order)
			Assert.assertEquals(message, i, output.getNumber());
			
			// (ensures messages)
			Assert.assertNotNull(message, output.getMessages());
			Assert.assertTrue(String.format("%s: expected [%s] but found [%s]",
					message, DEFAULT_RULES_MESSAGE, StringUtils.join(output.getMessages(), ", ")),
					output.getMessages().contains(DEFAULT_RULES_MESSAGE));
			Assert.assertEquals(message, (i % 2) == 0, output.getMessages().contains(FLAG_TRUE_MESSAGE));
		}
	}
}
