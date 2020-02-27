
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

import org.apache.commons.lang3.ObjectUtils;
import cloud.altemista.fwk.core.reporting.exception.ReportingException;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFiller;
import cloud.altemista.fwk.reporting.jasper.model.JasperReportBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * This helper class can be used to generate reports with Jasper DataSource to get data
 * @author NTT DATA
 * @see cloud.altemista.fwk.reporting.jasper.model.JasperReportBean
 */
public class JRDataSourceJasperFillerImpl implements JasperFiller {

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.filler.JasperFiller#fillReport(cloud.altemista.fwk.common.reporting.model.ReportBean, java.io.InputStream)
	 */
	@Override
	public JasperPrint fillReport(ReportBean report, InputStream template) {

		try {
			JRDataSource jrDataSource = (report instanceof JasperReportBean)
					? ((JasperReportBean) report).getJrDataSource() : null;
			return JasperFillManager.fillReport(template, report.getParameters(),
					ObjectUtils.defaultIfNull(jrDataSource, new JREmptyDataSource()));
			
		} catch (JRException e) {
			throw new ReportingException("fill_failed", new Object[]{ report.getTemplate() }, e);
		}
	}
}
