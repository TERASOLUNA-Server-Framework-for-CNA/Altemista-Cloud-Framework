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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import cloud.altemista.fwk.core.rules.session.StatelessRuleSession;

/**
 * Drools-based implementation of an stateless rule session
 * 
 * @author NTT DATA
 */
public class DroolsStatelessRuleSessionImpl implements StatelessRuleSession {

	/** Format for the input identifiers */
	private static final String INPUT_ID_FORMAT = "input#%d";

	/**
	 * The CommandExecutor (a KieSession or StatelessKieSession) wrapped by this
	 * rule session
	 */
	private StatelessKieSession statelessKieSession;

	/**
	 * Constructor
	 * 
	 * @param statelessKieSession
	 *            The StatelessKieSession wrapped by this rule session
	 */
	public DroolsStatelessRuleSessionImpl(StatelessKieSession statelessKieSession) {
		super();
		this.statelessKieSession = statelessKieSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cloud.altemista.fwk.core.rules.RulesExecutor#executeRules(java.lang.
	 * Object)
	 */
	@Override
	public <T> T executeRules(T input) {

		// (sanity checks)
		if (input == null) {
			return null;
		}

		// Executes Drools rules
		this.statelessKieSession.execute((Command<?>) CommandFactory.newInsert(input));

		// Returns the input bean (updated by Drools rules)
		return input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cloud.altemista.fwk.core.rules.RulesExecutor#executeRules(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> executeRules(List<T> inputs) {

		// (sanity checks)
		if (inputs == null) {
			// Desired behavior
			return null; // NOSONAR
		}
		
		// (optimize edge case)
		if (inputs.isEmpty()) {
			return Collections.emptyList();
		}

		// Builds the batch command
		List<String> identifiers = new ArrayList<String>();
		List<Command<?>> inputCommands = new ArrayList<Command<?>>();
		int index = 0;
		for (T input : inputs) {
			final String identifier = String.format(INPUT_ID_FORMAT, index++);
			identifiers.add(identifier);
			inputCommands.add(CommandFactory.newInsert(input, identifier));
		}
		BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(inputCommands);

		// Executes Drools rules
		ExecutionResults executionResults = this.statelessKieSession.execute(batchExecutionCommand);

		// Prepares the output list, preserving the input order
		List<T> outputs = new ArrayList<T>();
		for (String identifier : identifiers) {
			outputs.add((T) executionResults.getValue(identifier));
		}
		return outputs;
	}
}
