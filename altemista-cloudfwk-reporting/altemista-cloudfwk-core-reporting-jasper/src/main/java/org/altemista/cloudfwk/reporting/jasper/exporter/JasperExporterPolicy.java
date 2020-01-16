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


import org.altemista.cloudfwk.core.reporting.model.ReportBean;

/**
 * Defines a policy to retrieve the proper JasperExporter based on the report information
 * @author NTT DATA
 */
public interface JasperExporterPolicy {

	/**
	 * Retrieves the proper JasperExporter based on the report information
	 * @param report Report that is to be generated
	 * @return JasperExporter to be used
	 */
	JasperExporter getReportExporter(ReportBean report);
}
