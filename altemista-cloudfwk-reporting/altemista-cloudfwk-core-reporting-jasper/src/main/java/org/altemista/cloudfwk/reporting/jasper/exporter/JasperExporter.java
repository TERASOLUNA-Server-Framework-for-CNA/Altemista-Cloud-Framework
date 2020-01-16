package org.altemista.cloudfwk.reporting.jasper.exporter;

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

import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;

/**
 * Defines a factory capable of creating Jasper Reports Exporter objects for an specific format
 * @author NTT DATA
 */
public interface JasperExporter {

	/**
	 * Creates a new exporter for the specific format
	 * @param report Report that is to be generated
	 * @param destination where the report is to be generated
	 * @return an already configured exporter
	 */
	Exporter<ExporterInput, ?, ?, ?> getJasperExporter(ReportBean report, ContentWritable destination); // NOSONAR
}
