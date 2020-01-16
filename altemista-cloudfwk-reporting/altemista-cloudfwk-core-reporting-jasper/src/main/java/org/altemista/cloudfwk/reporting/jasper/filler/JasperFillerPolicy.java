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


import org.altemista.cloudfwk.core.reporting.model.ReportBean;

/**
 * Defines a policy to retrieve the proper JasperFiller based on the report information
 * @author NTT DATA
 */
public interface JasperFillerPolicy {

	/**
	 * Retrieves the proper JasperFiller based on the report information
	 * @param report Report that is to be generated
	 * @return JasperFiller to be used
	 */
	JasperFiller getJasperFiller(ReportBean report);
}
