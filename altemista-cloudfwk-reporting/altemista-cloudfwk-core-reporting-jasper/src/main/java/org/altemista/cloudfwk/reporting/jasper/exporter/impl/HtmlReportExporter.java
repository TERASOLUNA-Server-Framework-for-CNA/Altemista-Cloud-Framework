
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


import org.altemista.cloudfwk.common.model.ContentWritable;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporter;

import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;

/**
 * Creates a default Jasper Reports Exporter objects for "HyperText Markup Language (HTML)" format
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.model.ContentType#HTML
 */
public class HtmlReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.ReportExporter#getJasperExporter(org.altemista.cloudfwk.common.reporting.model.Report, org.altemista.cloudfwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		HtmlExporter exporter = new HtmlExporter();
		exporter.setConfiguration(new SimpleHtmlReportConfiguration());
		exporter.setConfiguration(new SimpleHtmlExporterConfiguration());
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
