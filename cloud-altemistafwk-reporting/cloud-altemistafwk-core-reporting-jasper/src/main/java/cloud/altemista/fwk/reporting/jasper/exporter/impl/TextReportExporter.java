
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

import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * Creates a default Jasper Reports Exporter objects for "Text file" format
 * @author NTT DATA
 * @see cloud.altemista.fwk.common.model.ContentType#TEXT
 */
public class TextReportExporter implements JasperExporter {
	
	/*
	 * The default values makes characters 7.238px wide and 13.948px high.
	 * Equivalent A4 paper dimensions in pixels at 72 DPI (screen resolution) are 595 pixels x 842 pixels
	 * (or 2480 pixels x 3508 pixels at 300 DPI, print resolution).
	 * It follows that, at said screen resolution, the pages in the generated text document
	 * will count ~80 characters per line and ~60 lines per page.
	 */
	
	/** Makes characters 7.238px wide by default */
	private static final float DEFAULT_CHAR_WIDTH = 7.238f;

	/** Makes characters 13.948px high by default */
	private static final float DEFAULT_CHAR_HEIGHT = 13.948f;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.ReportExporter#getJasperExporter(cloud.altemista.fwk.common.reporting.model.Report, cloud.altemista.fwk.common.model.ContentWritable)
	 */
	@Override
	public Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination) {
		
		JRTextExporter exporter = new JRTextExporter();
		
		// charWidth and charHeight are set to avoid: "JRRuntimeException:
		// Character width/height in pixels (...) must be specified and must be greater than zero."
		SimpleTextReportConfiguration textReportConfiguration = new SimpleTextReportConfiguration();
		textReportConfiguration.setCharWidth(DEFAULT_CHAR_WIDTH);
		textReportConfiguration.setCharHeight(DEFAULT_CHAR_HEIGHT);
		exporter.setConfiguration(textReportConfiguration);
		
		exporter.setConfiguration(new SimpleTextExporterConfiguration());
		exporter.setExporterOutput(new SimpleWriterExporterOutput(destination.getOutputStream()));
		
		return exporter;
	}
}
