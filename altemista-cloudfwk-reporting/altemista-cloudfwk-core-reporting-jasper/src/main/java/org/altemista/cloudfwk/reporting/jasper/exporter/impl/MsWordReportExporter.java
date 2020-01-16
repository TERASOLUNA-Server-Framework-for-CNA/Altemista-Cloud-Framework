
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

import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 * Creates a default Jasper Reports Exporter objects for "Microsoft Word" format
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.model.ContentType#MS_WORD
 */
public class MsWordReportExporter implements JasperExporter {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.ReportExporter#getJasperExporter(org.altemista.cloudfwk.common.reporting.model.Report, org.altemista.cloudfwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setConfiguration(new SimpleDocxReportConfiguration());
		exporter.setConfiguration(new SimpleDocxExporterConfiguration());
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
