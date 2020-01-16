
package org.altemista.cloudfwk.reporting.jasper.impl;

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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.model.ContentWritable;
import org.altemista.cloudfwk.core.reporting.exception.ReportingException;
import org.altemista.cloudfwk.core.reporting.impl.AbstractReportBuilderImpl;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporter;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporterPolicy;
import org.altemista.cloudfwk.reporting.jasper.filler.JasperFiller;
import org.altemista.cloudfwk.reporting.jasper.filler.JasperFillerPolicy;
import org.altemista.cloudfwk.reporting.jasper.model.JasperReportBean;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;

/**
 * Jasper Reports implementation of the report builder.
 * Relies on a JasperFiller to fill the report with the data using the proper source (database, etc.)
 * and in a JasperExporter to create the Jasper Reports Exporter that actually will generate the report
 * @author NTT DATA
 */
public class JasperReportBuilderImpl extends AbstractReportBuilderImpl implements InitializingBean {
	
	/** Policy that will be used to retrieve the proper JasperFiller based on the report information */
	private JasperFillerPolicy fillerPolicy;
	
	/** Policy that will be used to retrieve the proper JasperExporter based on the report information */
	private JasperExporterPolicy exporterPolicy;
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.reporting.impl.AbstractReportBuilderImpl#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		Assert.notNull(this.fillerPolicy, "The JasperFillerPolicy is required");
		Assert.notNull(this.exporterPolicy, "The JasperExporterPolicy is required");
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.reporting.impl.AbstractReportBuilderImpl#newReport()
	 */
	@Override
	public JasperReportBean newReport() {
		
		// Overwritten to return a more specific JasperReportBean
		return new JasperReportBean();
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.reporting.impl.AbstractReportBuilderImpl#internalBuild(org.altemista.cloudfwk.common.reporting.model.Report, java.io.InputStream, org.altemista.cloudfwk.common.model.ContentWritable)
	 */
	@Override
	protected void internalBuild(ReportBean report, InputStream template, ContentWritable destination) {
		
		// Locates the exporter
		JasperExporter reportExporter = this.exporterPolicy.getReportExporter(report);
		if (reportExporter == null) {
			throw new ReportingException("no_exporter_available");
		}
		
		// Fetches the source of the data that will populate the report
		JasperFiller reportFiller = this.fillerPolicy.getJasperFiller(report);
		if (reportFiller == null) {
			throw new ReportingException("no_filler_available");
		}
		
		// Prepares the JasperPrint object that will fed the data to the report
		JasperPrint jasperPrint = reportFiller.fillReport(report, template);

		// Prepares the Jasper exporter
		Exporter<ExporterInput, ?, ?, ?> exporter = reportExporter.getJasperExporter(report, destination);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		
		try {
			// Performs the export
			exporter.exportReport();
			
		} catch (JRException e) {
			throw new ReportingException("export_failed", new Object[]{ report.getTemplate() }, e);
			
		} catch (JRRuntimeException e) {
			throw new ReportingException("export_failed", new Object[]{ report.getTemplate() }, e);
		}
	}

	/**
	 * @param fillerPolicy the fillerPolicy to set
	 */
	public void setFillerPolicy(JasperFillerPolicy fillerPolicy) {
		this.fillerPolicy = fillerPolicy;
	}

	/**
	 * @param exporterPolicy the exporterPolicy to set
	 */
	public void setExporterPolicy(JasperExporterPolicy exporterPolicy) {
		this.exporterPolicy = exporterPolicy;
	}
}
