
package cloud.altemista.fwk.reporting.jasper.exporter.impl;

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


import cloud.altemista.fwk.common.model.ContentWritable;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.exporter.JasperExporter;

import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;

/**
 * Creates a default Jasper Reports Exporter objects for "HyperText Markup Language (HTML)" format
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.ContentType#HTML
 */
public class HtmlReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.ReportExporter#getJasperExporter(cloud.altemista.fwk.common.reporting.model.Report, cloud.altemista.fwk.common.model.ContentWritable)
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
