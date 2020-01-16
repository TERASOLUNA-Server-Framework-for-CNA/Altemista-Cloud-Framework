
package org.altemista.cloudfwk.reporting.jasper.filler.impl;

import java.io.InputStream;

/*
 * #%L
 * altemista-cloud reporting: JasperReports implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.reporting.exception.ReportingException;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.reporting.jasper.filler.JasperFiller;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;

/**
 * 
 * This helper class can be used to generate reports with JPA EntityManager to
 * get connections to the database
 * 
 * @author NTT DATA
 * 
 */
public class JpaJasperFillerImpl implements JasperFiller, InitializingBean {

	/** The JPA Entity manager */
	private EntityManager entityManager;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.entityManager, "The EntityManager is required");
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.filler.JasperFiller#fillReport(org.altemista.cloudfwk.common.reporting.model.ReportBean, java.io.InputStream)
	 */
	@Override
	public JasperPrint fillReport(ReportBean report, InputStream template) {
		
		try {
			Map<String, Object> parameters = report.getParameters();
			parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, this.entityManager);
			return JasperFillManager.fillReport(template, parameters);
			
		} catch (JRException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
		}
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
