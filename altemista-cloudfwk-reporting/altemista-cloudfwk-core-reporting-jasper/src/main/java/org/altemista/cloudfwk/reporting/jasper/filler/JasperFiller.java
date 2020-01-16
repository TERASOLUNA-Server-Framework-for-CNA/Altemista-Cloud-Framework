
package org.altemista.cloudfwk.reporting.jasper.filler;

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


import java.io.InputStream;

import org.altemista.cloudfwk.core.reporting.model.ReportBean;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * Defines a bean capable of filling a Jasper Reports template based on the report information
 * @author NTT DATA
 */
public interface JasperFiller {

	/**
	 * Fills the compiled report design loaded from the specified file and
	 * returns the generated report object.
	 * @param report Report that is to be generated
	 * @param template InputStream with the Jasper Reports template
	 * @return a JasperPrint object that can be exported 
	 */
	JasperPrint fillReport(ReportBean report, InputStream template);
}
