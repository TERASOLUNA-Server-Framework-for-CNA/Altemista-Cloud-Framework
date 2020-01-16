/**
 * 
 */
package org.altemista.cloudfwk.rules.drools.impl;

/*
 * #%L
 * altemista-cloud rules engine: Drools implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import org.kie.api.KieBase;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.rules.ruleset.Ruleset;
import org.altemista.cloudfwk.core.rules.session.StatefulRuleSession;
import org.altemista.cloudfwk.core.rules.session.StatelessRuleSession;

/**
 * Drool-based implementation of a rule set.
 * Basically, a wrapper for {@link KieBase} objects. 
 * @author NTT DATA
 */
public class DroolsRulesetImpl implements Ruleset {
	
	/** The KieBase wrapped by this instance */
	private KieBase kieBase;

	/** Convenience holder to avoid creating a new stateless session every time */
	private DroolsStatelessRuleSessionImpl statelessSession;
	
	/**
	 * Constructor
	 * @param kieBase The KieBase wrapped by this instance
	 */
	public DroolsRulesetImpl(KieBase kieBase) {
		super();
		
		Assert.notNull(kieBase);
		
		this.kieBase = kieBase;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.RulesExecutor#executeRules(java.lang.Object)
	 */
	@Override
	public <T> T executeRules(T input) {
		
		// (uses the defaultSession() per Ruleset contract) 
		return this.defaultSession().executeRules(input);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.RulesExecutor#executeRules(java.util.List)
	 */
	@Override
	public <T> List<T> executeRules(List<T> input) {
		
		// (uses the defaultSession() per Ruleset contract) 
		return this.defaultSession().executeRules(input);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.Ruleset#defaultSession()
	 */
	@Override
	public StatelessRuleSession defaultSession() {
		
		if (this.statelessSession == null) {
			this.statelessSession = new DroolsStatelessRuleSessionImpl(this.kieBase.newStatelessKieSession());
		}
		
		return this.statelessSession;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.Ruleset#createStatefulSession()
	 */
	@Override
	public StatefulRuleSession createStatefulSession() {
		
		return new DroolsStatefulRuleSessionImpl(this.kieBase.newKieSession());
	}
}
