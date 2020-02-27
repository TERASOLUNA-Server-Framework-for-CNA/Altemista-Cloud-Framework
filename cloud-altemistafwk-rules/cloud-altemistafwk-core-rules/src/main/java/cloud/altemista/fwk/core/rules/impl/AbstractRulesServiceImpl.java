/**
 * 
 */
package cloud.altemista.fwk.core.rules.impl;

/*
 * #%L
 * altemista-cloud rules engine
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.rules.RulesService;
import cloud.altemista.fwk.core.rules.exception.RulesEngineException;
import cloud.altemista.fwk.core.rules.ruleset.Ruleset;
import cloud.altemista.fwk.core.rules.ruleset.RulesetContainer;
import cloud.altemista.fwk.core.rules.ruleset.RulesetContainerResolver;

/**
 * Base class for rules executor service implementations that uses a resolver to obtain the rule set containers.
 * @param <C> The actual type of the container (depends on the implementation provider)
 * @author NTT DATA
 */
public abstract class AbstractRulesServiceImpl<C> implements RulesService {

	/** The policy to retrieve the RulesetContainers */
	private RulesetContainerResolver<C> rulesetContainerResolver;
	
	/**
	 * Constructor
	 * @param rulesetContainerResolver The policy to retrieve the RulesetContainers
	 */
	public AbstractRulesServiceImpl(RulesetContainerResolver<C> rulesetContainerResolver) {
		super();
		
		Assert.notNull(rulesetContainerResolver);
		
		this.rulesetContainerResolver = rulesetContainerResolver;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.RulesExecutor#executeRules(java.lang.Object)
	 */
	@Override
	public <T> T executeRules(T input) {
		
		// (uses the defaultRuleset() per RulesService contract) 
		Ruleset defaultRuleset = this.defaultRuleset();
		if (defaultRuleset == null) {
			throw new RulesEngineException("no_default_ruleset");
		}
		
		return defaultRuleset.executeRules(input);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.RulesExecutor#executeRules(java.util.List)
	 */
	@Override
	public <T> List<T> executeRules(List<T> input) {
		
		// (uses the defaultRuleset() per RulesService contract) 
		Ruleset defaultRuleset = this.defaultRuleset();
		if (defaultRuleset == null) {
			throw new RulesEngineException("no_default_ruleset");
		}
		
		return defaultRuleset.executeRules(input);
	}
	
	/**
	 * Helper method to retrieve the default rules container
	 * @return C, never null
	 */
	protected C getDefaultContainer() {
		
		// (uses the default RulesetContainer provided by the resolver)
		RulesetContainer<C> defaultRulesetContainer = this.rulesetContainerResolver.getDefaultRulesetContainer();
		if (defaultRulesetContainer == null) {
			throw new RulesEngineException("no_default_ruleset");
		}
		
		return defaultRulesetContainer.getContainer();
	}
	
	/**
	 * Helper method to retrieve the proper rules container based on the logical name of the rule set
	 * @param logicalName String with the logical name of the rule set
	 * @return C, never null
	 */
	protected C getContainer(String logicalName) {
		
		// (sanity checks)
		if (StringUtils.isEmpty(logicalName)) {
			return this.getDefaultContainer();
		}
		
		// (uses the proper RulesetContainer provided by the resolver)
		RulesetContainer<C> rulesetContainer = this.rulesetContainerResolver.getRulesetContainer(logicalName);
		if (rulesetContainer == null) {
			throw new RulesEngineException("invalid_ruleset", new Object[]{ logicalName });
		}
		
		return rulesetContainer.getContainer();
	}

}
