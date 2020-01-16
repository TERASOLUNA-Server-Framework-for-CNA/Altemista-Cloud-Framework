package org.altemista.cloudfwk.reporting.jasper.model;

import org.altemista.cloudfwk.core.reporting.model.ReportBean;

import net.sf.jasperreports.engine.JRDataSource;

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

/**
 * POJO with a report information in the JasperReports-based implementation
 * @author NTT DATA
 */
public class JasperReportBean extends ReportBean {

	/**
	 * Optional JRDataSource. Will be read by JRDataSourceJasperFillerImpl
	 * @see org.altemista.cloudfwk.reporting.jasper.filler.impl.JRDataSourceJasperFillerImpl
	 */
	private JRDataSource jrDataSource;

	/**
	 * @return the jrDataSource
	 */
	public JRDataSource getJrDataSource() {
		return jrDataSource;
	}

	/**
	 * @param jrDataSource the jrDataSource to set
	 */
	public void setJrDataSource(JRDataSource jrDataSource) {
		this.jrDataSource = jrDataSource;
	}
}
