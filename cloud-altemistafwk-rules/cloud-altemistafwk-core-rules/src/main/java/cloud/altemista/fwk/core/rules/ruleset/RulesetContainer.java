/**
 * 
 */
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
 * Represents a generic container of rules and rule sets.
 * @param <C> The actual type of the container (depends on the implementation provider)
 * @author NTT DATA
 */
public interface RulesetContainer<C> {
	
	/**
	 * Flag to mark this container as the default container
	 * @return true if this container is to be considered the default container
	 * @see RulesetContainerResolver#getDefaultRulesetContainer()
	 */
	boolean isDefaultContainer();
	
	/**
	 * Builds or obtains the actual rules container.
	 * The actual container type will depend on the concrete implementation provider
	 * @return C, never null
	 */
	C getContainer();
}
