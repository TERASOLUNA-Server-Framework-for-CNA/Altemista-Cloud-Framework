
package org.altemista.cloudfwk.core.monitoring.impl.indicator;

/*
 * #%L
 * altemista-cloud monitoring
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.LdapTemplate;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;

/**
 * Indicator to check the status of an LDAP status.
 * @author NTT DATA
 */
public class LdapIndicator extends AbstractIndicator implements InitializingBean {
	
	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapIndicator.class);

	/** Core LDAP functionality. */
	private LdapTemplate ldapTemplate;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.impl.indicator.AbstractIndicator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (this.ldapTemplate == null) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "ldapTemplate", this.getId() });
		}
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return String.format("ldap %s", this.getId());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {

		MonitoringInfo info = new MonitoringInfo(this.getId(), this.getDescription());
		
		// Executes a query in the LDAP server
		try {
			this.ldapTemplate.list("");
			return info.update(Status.OK);

		} catch (NamingException e) {
			LOGGER.error(this.getDescription() + ": naming exception", e);
			return info.update(Status.FAILED, this.getDescription() + ": naming exception");

		}
	}

	/**
	 * @param ldapTemplate the ldapTemplate to set
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
	
		this.ldapTemplate = ldapTemplate;
	}
}
