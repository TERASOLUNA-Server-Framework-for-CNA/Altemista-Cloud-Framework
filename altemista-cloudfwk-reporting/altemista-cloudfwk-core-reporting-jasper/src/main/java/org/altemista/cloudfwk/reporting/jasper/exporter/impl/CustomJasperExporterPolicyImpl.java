package org.altemista.cloudfwk.reporting.jasper.exporter.impl;

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


import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporter;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporterPolicy;

/**
 * JasperExporterPolicy implementation that allows to return custom JasperExporter implementations
 * with an optional fallback policy to simplify the implementation of default exporters.
 * @author NTT DATA
 */
public abstract class CustomJasperExporterPolicyImpl implements JasperExporterPolicy {
	
	/** An optional fallback policy */
	private JasperExporterPolicy fallbackPolicy;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.JasperExporterPolicy#getReportExporter(org.altemista.cloudfwk.common.reporting.model.Report)
	 */
	@Override
	public JasperExporter getReportExporter(ReportBean report) {
		
		// Tries to retrieve the custom JasperExporter
		JasperExporter exporter = this.getCustomReportExpoter(report);
		if (exporter != null) {
			return exporter;
		}
		
		// If not found, uses the fallback policy
		if (this.fallbackPolicy != null) {
			return this.fallbackPolicy.getReportExporter(report);
		}
		
		// No JasperExporter found
		return null;
	}

	/**
	 * Retrieves the proper JasperExporter based on the report information
	 * @param report Report that is to be generated
	 * @return JasperExporter to be used, or null if the fallback policy should be used instead 
	 */
	protected abstract JasperExporter getCustomReportExpoter(ReportBean report);

	/**
	 * @param fallbackPolicy the fallbackPolicy to set
	 */
	public void setFallbackPolicy(JasperExporterPolicy fallbackPolicy) {
		this.fallbackPolicy = fallbackPolicy;
	}
}
