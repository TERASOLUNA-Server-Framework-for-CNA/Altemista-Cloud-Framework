
package cloud.altemista.fwk.reporting.jasper.filler.impl;

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


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.reporting.exception.ReportingException;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFiller;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * 
 * This helper class can be used to generate reports with JDBC DataSource to get
 * connections to the database
 * 
 * @author NTT DATA
 * 
 */
public class DataSourceJasperFillerImpl implements JasperFiller, InitializingBean {

	/** The DataSource */
	private DataSource datasource;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.datasource, "The DataSource is required");
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.filler.JasperFiller#fillReport(cloud.altemista.fwk.common.reporting.model.ReportBean, java.io.InputStream)
	 */
	@Override
	public JasperPrint fillReport(ReportBean report, InputStream template) {
		
		Connection connection = null;
		try {
			connection = this.datasource.getConnection();
			
			return JasperFillManager.fillReport(template, report.getParameters(), connection);
			
		} catch (SQLException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
			
		} catch (JRException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
			
		} finally {
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * @param datasource the datasource to set
	 */
	public void setDataSource(DataSource datasource) {
		this.datasource = datasource;
	}
}
