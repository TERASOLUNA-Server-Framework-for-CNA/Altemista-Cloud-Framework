/**
 * 
 */
package org.altemista.cloudfwk.core.rules.ruleset.impl;

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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver;

/**
 * Simple RulesetContainerResolver implementation that always returns the same RulesetContainer
 * (i.e.: this implementation ignores the logical name of the rule set).
 * @author NTT DATA
 */
public class SimpleRulesetContainerResolverImpl<C> implements RulesetContainerResolver<C> {
	
	/** The single RulesetContainer */
	private RulesetContainer<C> rulesetResolver;
	
	/**
	 * Constructor
	 * @param rulesetResolver The single RulesetContainer
	 */
	@Autowired
	public SimpleRulesetContainerResolverImpl(RulesetContainer<C> rulesetResolver) {
		super();
		
		Assert.notNull(rulesetResolver);
		
		this.rulesetResolver = rulesetResolver;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver#getDefaultContainer()
	 */
	@Override
	public RulesetContainer<C> getDefaultRulesetContainer() {
		
		// (this implementation ignores the default container flag)
		return this.rulesetResolver;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver#getRulesetContainer(java.lang.String)
	 */
	@Override
	public RulesetContainer<C> getRulesetContainer(String ignored) {
		
		// (this implementation ignores the logical name of the rule set)
		return this.rulesetResolver;
	}
}
