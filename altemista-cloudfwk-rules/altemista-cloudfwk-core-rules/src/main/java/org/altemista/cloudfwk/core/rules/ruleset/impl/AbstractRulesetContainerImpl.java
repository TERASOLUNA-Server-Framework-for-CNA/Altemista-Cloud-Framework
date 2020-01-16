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


import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer;

/**
 * Convenience base class for classes that implement RulesetContainer
 * @param <C> The actual type of the container
 * @author NTT DATA
 */
public abstract class AbstractRulesetContainerImpl<C> implements RulesetContainer<C> {
	
	/** Flag to mark this container as the default container */
	private boolean defaultContainer;
	
	/** The actual container */
	private C container;
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer#isDefaultContainer()
	 */
	@Override
	public boolean isDefaultContainer() {
		
		return this.defaultContainer;
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer#getContainer()
	 */
	@Override
	public C getContainer() {
		
		if (this.container == null) {
			this.container = initContainer();
		}
		
		return this.container;
	}

	/**
	 * Initializes the actual container 
	 * @return C, never null
	 */
	protected abstract C initContainer();

	/**
	 * @param defaultContainer the defaultContainer to set
	 */
	public void setDefaultContainer(boolean defaultContainer) {
		
		this.defaultContainer = defaultContainer;
	}
}
