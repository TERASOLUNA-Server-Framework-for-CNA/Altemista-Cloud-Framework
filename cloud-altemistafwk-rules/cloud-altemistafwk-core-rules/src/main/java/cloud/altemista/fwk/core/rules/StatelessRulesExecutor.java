/**
 * 
 */
package cloud.altemista.fwk.core.rules;

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

/**
 * Defines a common interface for objects that can execute rules in a stateless
 * manner.
 * 
 * @author NTT DATA
 */
public interface StatelessRulesExecutor {

	/**
	 * Executes the rules on a single bean
	 * 
	 * @param <T>
	 *            the type of the bean
	 * @param input
	 *            T the input bean
	 * @return the output bean (i.e.: the input bean with the rules applied)
	 */
	<T> T executeRules(T input);

	/**
	 * Executes the rules on a list of beans
	 * 
	 * @param <T>
	 *            the type of the bean
	 * @param inputs
	 *            List of input beans
	 * @return the list with the output beans, in the same order as the
	 *         parameter list
	 */
	<T> List<T> executeRules(List<T> inputs);
}
