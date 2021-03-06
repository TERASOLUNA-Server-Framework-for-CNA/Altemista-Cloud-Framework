
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

import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;

/**
 * Creates a default Jasper Reports Exporter objects for "Extensible Markup Language (XML)" format
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.ContentType#XML
 */
public class XmlReportExporter implements JasperExporter {

	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setConfiguration(new SimpleTextReportConfiguration());
		exporter.setConfiguration(new SimpleTextExporterConfiguration());
		exporter.setExporterOutput(new SimpleXmlExporterOutput(destination.getOutputStream()));
		return exporter;
	}
}
