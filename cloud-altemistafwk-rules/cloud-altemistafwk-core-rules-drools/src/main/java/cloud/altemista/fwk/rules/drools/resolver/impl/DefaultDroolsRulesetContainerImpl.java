/**
 * 
 */
package cloud.altemista.fwk.rules.drools.resolver.impl;

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


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import cloud.altemista.fwk.core.rules.ruleset.RulesetContainer;
import cloud.altemista.fwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl;

/**
 * RulesetContainer implementation that uses {@link KieServices#getKieClasspathContainer()};
 * i.e.: identifies the rules declared in the "META-INF/kmodule.xml" file in the root of the classpath.
 * @author NTT DATA
 */
public class DefaultDroolsRulesetContainerImpl extends AbstractRulesetContainerImpl<KieContainer>
		implements RulesetContainer<KieContainer> {

	/**
	 * Default constructor
	 */
	public DefaultDroolsRulesetContainerImpl() {
		super();
		
		// This container will be a default container by default
		this.setDefaultContainer(true);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl#initContainer()
	 */
	@Override
	protected KieContainer initContainer() {
		
		return KieServices.Factory.get().getKieClasspathContainer();
	}
}
