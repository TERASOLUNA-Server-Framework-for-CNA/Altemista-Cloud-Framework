
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

import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * Creates a default Jasper Reports Exporter objects for "Microsoft Excel" format
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.ContentType#MS_EXCEL
 */
public class MsExcelReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.ReportExporter#getJasperExporter(cloud.altemista.fwk.common.reporting.model.Report, cloud.altemista.fwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setConfiguration(new SimpleXlsReportConfiguration());
		exporter.setConfiguration(new SimpleXlsExporterConfiguration());
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
