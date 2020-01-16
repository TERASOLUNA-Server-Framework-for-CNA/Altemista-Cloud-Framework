/**
 * 
 */
package org.altemista.cloudfwk.core.rules.ruleset;

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


import org.altemista.cloudfwk.core.rules.RulesService;
import org.altemista.cloudfwk.core.rules.StatelessRulesExecutor;
import org.altemista.cloudfwk.core.rules.session.StatefulRuleSession;
import org.altemista.cloudfwk.core.rules.session.StatelessRuleSession;

/**
 * Represents a rule set in the rules engine (i.e.: a.<br>
 * The {@link RulesExecutor} methods, when invoked on instances of {@link RulesService},
 * are equivalent to be run through the {@link #defaultSession()}.
 * @author NTT DATA
 */
public interface Ruleset extends StatelessRulesExecutor {
	
	/**
	 * Returns an stateless rule session for executing the rules of this rule set.
	 * @return StatelessRuleSession
	 */
	StatelessRuleSession defaultSession();
	
	/**
	 * Creates an stateful rule session for executing the rules of this rule set.<br>
	 * {@link StatefulRuleSession#release()} <b>must</b> always be called after finishing using the session
	 * @return StatefulRuleSession
	 */
	StatefulRuleSession createStatefulSession();
}
