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

import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Creates a default Jasper Reports Exporter objects for "Comma-Separated Values" format
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.model.ContentType#CSV
 */
public class CsvReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.ReportExporter#getJasperExporter(org.altemista.cloudfwk.common.reporting.model.Report, org.altemista.cloudfwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setConfiguration(new SimpleCsvReportConfiguration());
		exporter.setConfiguration(new SimpleCsvExporterConfiguration());
		exporter.setExporterOutput(new SimpleWriterExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
