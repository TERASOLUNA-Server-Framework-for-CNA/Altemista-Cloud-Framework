package org.altemista.cloudfwk.core.reporting;

/*
 * #%L
 * altemista-cloud reporting
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.altemista.cloudfwk.common.model.ContentReadable;
import org.altemista.cloudfwk.common.model.ContentWritable;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;

/**
 * Report builder; the main interface of the report module.
 * @author NTT DATA
 */
public interface ReportBuilder {
	
	/**
	 * Initializes a new empty report bean
	 * @return a new empty report bean
	 */
	ReportBean newReport();
	
	/**
	 * Builds a report and writes it to the specified destination.
	 * @param report Report that is to be generated
	 * @param destination ContentWritable where the content of the report will be written
	 */
	void build(ReportBean report, ContentWritable destination);
	
	/**
	 * Optional operation that builds a report without specifying a destination.
	 * Please note that the implementation of this method is optional
	 * and can be memory unfriendly, so prefer always the version that receives the ContentWritable
	 * @param report Report that is to be generated
	 * @return ContentReadable with the content of the report
	 * @see #build(ReportBean, ContentWritable)
	 */
	ContentReadable build(ReportBean report);
}
