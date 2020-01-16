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


import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.util.DefensiveUtil;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver;

/**
 * RulesetContainerResolver implementation that maps rule set identifiers to RulesetContainer implementations,
 * and allows a fallback RulesetContainer when no matches found.
 * @author NTT DATA
 */
public class NameBasedRulesetContainerResolverImpl<C> implements RulesetContainerResolver<C> {

	/** Maps logical names of rule sets to RulesetContainer implementations */
	private Map<String, RulesetContainer<C>> containerMap;

	/** An optional fallback container in case the rule set name is not found in the map */
	private RulesetContainer<C> fallbackContainer;
	
	/**
	 * Constructor
	 * @param containerMap Maps logical names of rule sets to RulesetContainer implementations
	 */
	public NameBasedRulesetContainerResolverImpl(Map<String, RulesetContainer<C>> containerMap) {
		super();
		
		Assert.notNull(containerMap);
		
		this.containerMap = ObjectUtils.defaultIfNull(
				DefensiveUtil.unmodifiableMap(containerMap),
				Collections.<String, RulesetContainer<C>> emptyMap());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver#getDefaultContainer()
	 */
	@Override
	public RulesetContainer<C> getDefaultRulesetContainer() {
		
		// Looks for the first container marked as default in the map
		for (RulesetContainer<C> container : this.containerMap.values()) {
			if (container.isDefaultContainer()) {
				return container;
			}
		}
		
		// If no container in the map is default, checks the fallback container 
		return this.fallbackContainer.isDefaultContainer() ? this.fallbackContainer : null;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainerResolver#getRulesetContainer(java.lang.String)
	 */
	@Override
	public RulesetContainer<C> getRulesetContainer(String logicalName) {
		
		// Looks for the RulesResolver using the map or uses the fallback resolver
		return ObjectUtils.defaultIfNull(this.containerMap.get(logicalName), this.fallbackContainer);
	}

	/**
	 * @param fallbackResolver the fallbackResolver to set
	 */
	public void setFallbackRulesetResolver(RulesetContainer<C> fallbackResolver) {
		
		this.fallbackContainer = fallbackResolver;
	}
}
