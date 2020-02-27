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

import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Creates a default Jasper Reports Exporter objects for "Comma-Separated Values" format
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.ContentType#CSV
 */
public class CsvReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.ReportExporter#getJasperExporter(cloud.altemista.fwk.common.reporting.model.Report, cloud.altemista.fwk.common.model.ContentWritable)
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
