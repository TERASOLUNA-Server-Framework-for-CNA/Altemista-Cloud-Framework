
package cloud.altemista.fwk.reporting.jasper.filler.impl;

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


import java.io.InputStream;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.reporting.exception.ReportingException;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFiller;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;

/**
 * 
 * This helper class can be used to generate reports with Hibernate
 * SessionFactory * to get connections to the database.
 * 
 * <p>
 * This helper only works if jasper reports is compatible with hibernate 4. With
 * actual version of jasper (4.6) doesn't work.
 * </p>
 * 
 * @author NTT DATA
 * @see cloud.altemista.fwk.reporting.jasper.filler.impl.JpaJasperFillerImpl 
 */
public class HibernateJasperFillerImpl implements JasperFiller, InitializingBean {

	/** The Hibernate session factory */
	private SessionFactory sessionFactory;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.sessionFactory, "The SessionFactory is required");
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.filler.JasperFiller#fillReport(cloud.altemista.fwk.common.reporting.model.ReportBean, java.io.InputStream)
	 */
	@Override
	public JasperPrint fillReport(ReportBean report, InputStream template) {
		
		try {
			Map<String, Object> parameters = report.getParameters();
			parameters.put(
					JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, this.sessionFactory.openSession());
			return JasperFillManager.fillReport(template, parameters);
			
		} catch (HibernateException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
			
		} catch (JRException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
		}
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
