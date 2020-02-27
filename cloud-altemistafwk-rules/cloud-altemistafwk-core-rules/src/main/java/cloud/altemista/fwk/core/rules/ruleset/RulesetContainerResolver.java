package cloud.altemista.fwk.core.rules.ruleset;

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


/**
 * Defines a policy on how to resolve which RulesetContainer implementation should be used
 * depending on the logical name of the rule set.
 * @param <C> The actual type of the container (depends on the implementation provider)
 * @author NTT DATA
 */
public interface RulesetContainerResolver<C> {
	
	/**
	 * Retrieves the default RulesetContainer
	 * @return default RulesetContainer, or null if no default is configured in the resolver
	 * @see RulesetContainer#isDefaultContainer()
	 */
	RulesetContainer<C> getDefaultRulesetContainer();
	
	/**
	 * Retrieves the proper RulesetContainer based on the logical name of the rule set
	 * @param logicalName logical name of the rule set to be located
	 * @return RulesetContainer that should contain the rule set, or null if not found
	 */
	RulesetContainer<C> getRulesetContainer(String logicalName);
}
