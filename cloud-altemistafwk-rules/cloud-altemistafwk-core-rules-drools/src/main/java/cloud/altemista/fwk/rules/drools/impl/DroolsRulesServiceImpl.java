/**
 * 
 */
package cloud.altemista.fwk.rules.drools.impl;

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

import org.kie.api.KieBase;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloud.altemista.fwk.core.rules.RulesService;
import cloud.altemista.fwk.core.rules.exception.RulesEngineException;
import cloud.altemista.fwk.core.rules.impl.AbstractRulesServiceImpl;
import cloud.altemista.fwk.core.rules.ruleset.Ruleset;
import cloud.altemista.fwk.core.rules.ruleset.RulesetContainerResolver;

/**
 * Drool-based implementation of the rules executor service
 * 
 * @author NTT DATA
 */
public class DroolsRulesServiceImpl extends AbstractRulesServiceImpl<KieContainer> implements RulesService {

	/** The SLF4J Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DroolsRulesServiceImpl.class);

	/**
	 * Constructor
	 * 
	 * @param rulesetContainerResolver
	 *            The policy to retrieve the RulesetContainers
	 */
	public DroolsRulesServiceImpl(RulesetContainerResolver<KieContainer> rulesetContainerResolver) {
		super(rulesetContainerResolver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cloud.altemista.fwk.core.rules.RulesService#defaultRuleset()
	 */
	@Override
	public Ruleset defaultRuleset() {

		KieContainer kieContainer = this.getDefaultContainer();
		try {
			KieBase defaultKieBase = kieContainer.getKieBase();
			return new DroolsRulesetImpl(defaultKieBase);

		} catch (RuntimeException e) {
			throw new RulesEngineException("no_default_ruleset", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cloud.altemista.fwk.core.rules.RulesService#getRuleset(java.lang.String)
	 */
	@Override
	public Ruleset getRuleset(String logicalName) {

		KieContainer kieContainer = this.getContainer(logicalName);
		try {
			KieBase kieBase = kieContainer.getKieBase(logicalName);
			return new DroolsRulesetImpl(kieBase);

		} catch (RuntimeException e) {
			throw new RulesEngineException("invalid_ruleset", new Object[] { logicalName }, e);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.impl.AbstractRulesServiceImpl#getContainer(java.lang.String)
	 */
	@Override
	protected KieContainer getContainer(String logicalName) {

		KieContainer container = super.getContainer(logicalName);
		
		// Added control over errors in rules
		Results rulesVerification = container.verify();
		String format = "Loading rule set {}: {}";
		for (Message msg : rulesVerification.getMessages()) {			
			switch (msg.getLevel()) { // NOSONAR
			case ERROR:
				LOGGER.error(format, logicalName, msg.getText());
				break;
				
			case WARNING:
				LOGGER.warn(format, logicalName, msg.getText());
				break;
				
			case INFO:
				LOGGER.info(format, logicalName, msg.getText());
				break;
			}
		}
		
		if (rulesVerification.hasMessages(Level.ERROR)) {
			throw new RulesEngineException("malformed_ruleset", new Object[] { logicalName });
		}

		return container;
	}
}
