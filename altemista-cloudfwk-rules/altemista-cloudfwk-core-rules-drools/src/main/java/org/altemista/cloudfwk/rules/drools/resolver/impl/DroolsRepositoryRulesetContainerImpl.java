/**
 * 
 */
package org.altemista.cloudfwk.rules.drools.resolver.impl;

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
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer;
import org.altemista.cloudfwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl;

/**
 * RulesetContainer implementation that builds a KieModule from a Maven dependency
 * @author NTT DATA
 */
public class DroolsRepositoryRulesetContainerImpl extends AbstractRulesetContainerImpl<KieContainer>
		implements RulesetContainer<KieContainer>, InitializingBean {
	
	/** The groupId */
	private String groupId;
	
	/** The artifactId */
	private String artifactId;
	
	/** The version */
	private String version = "LATEST";
	
	/** Enable Scanner */
	private boolean enableScanner;
	
	/** Scanning Interval in milliseconds */
	private long scannerInterval;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(this.groupId);
		Assert.notNull(this.artifactId);
		Assert.notNull(this.artifactId);
		
		if (this.enableScanner) {
			Assert.isTrue(this.scannerInterval > 0L, "scannerInterval must be positive");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl#initContainer()
	 */
	@Override
	protected KieContainer initContainer() {
		
		final KieServices kieServices = KieServices.Factory.get();
		
		ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);
		KieContainer kieContainer = kieServices.newKieContainer(releaseId);
		
		if (this.enableScanner) {
			KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
			kieScanner.start(this.scannerInterval);
		}
		
		return kieContainer;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param enableScanner the enableScanner to set
	 */
	public void setEnableScanner(boolean enableScanner) {
		this.enableScanner = enableScanner;
	}

	/**
	 * @param scannerInterval the scannerInterval to set
	 */
	public void setScannerInterval(long scannerInterval) {
		this.scannerInterval = scannerInterval;
	}
}
