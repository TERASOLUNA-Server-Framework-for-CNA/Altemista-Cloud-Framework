
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

import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * Creates a default Jasper Reports Exporter objects for "Microsoft Office - OOXML - Spreadsheet" format
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.model.ContentType#OOXML_EXCEL
 */
public class OoxmlExcelReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.ReportExporter#getJasperExporter(org.altemista.cloudfwk.common.reporting.model.Report, org.altemista.cloudfwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setConfiguration(new SimpleXlsxReportConfiguration());
		exporter.setConfiguration(new SimpleXlsxExporterConfiguration());
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
