/**
 * 
 */
package org.altemista.cloudfwk.core.rules;

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


import org.altemista.cloudfwk.core.rules.ruleset.Ruleset;

/**
 * Rules executor service; the main interface of the rules module.<br>
 * The {@link RulesExecutor} methods, when invoked on instances of {@link RulesService},
 * are equivalent to be run through the {@link #defaultRuleset()}.
 * @author NTT DATA
 */
public interface RulesService extends StatelessRulesExecutor {
	
	/**
	 * Retrieves the default rule set configured in the rules service
	 * @return default Ruleset
	 */
	Ruleset defaultRuleset();
	
	/**
	 * Retrieves an specific rule set from the rules service
	 * @param logicalName String with the logical name of the rule set.
	 * If null or empty, the default rule set will be retrieved
	 * @return Ruleset
	 */
	Ruleset getRuleset(String logicalName);
}
