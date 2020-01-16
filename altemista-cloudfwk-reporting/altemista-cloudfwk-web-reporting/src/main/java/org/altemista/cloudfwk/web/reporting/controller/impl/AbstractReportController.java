package org.altemista.cloudfwk.web.reporting.controller.impl;

/*
 * #%L
 * altemista-cloud reporting: web part
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.altemista.cloudfwk.core.reporting.ReportBuilder;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.web.model.HttpServletResponseWritable;

/**
 * Base class to implement controllers that serves reports, both to be shown inline and downloaded
 * @author NTT DATA
 */
public abstract class AbstractReportController extends WebContentGenerator implements InitializingBean {
	
	/** Report builder */
	@Autowired
	private ReportBuilder reportBuilder;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.reportBuilder, "The ReportBuilder is mandatory");
	}
	
	/**
	 * Builds a report and serves it to be downloaded
	 * @param report Report that is to be generated
	 * @param response the HttpServletResponse
	 */
	protected void build(ReportBean report, HttpServletResponse response) {
		
		this.build(report, response, false);
	}
	
	/**
	 * Builds a report and serves it to be show inline or downloaded
	 * @param report Report that is to be generated
	 * @param response the HttpServletResponse
	 * @param inline if the content is to be shown inline or downloaded as an attachment
	 */
	protected void build(ReportBean report, HttpServletResponse response, boolean inline) {
		
		HttpServletResponseWritable destination = new HttpServletResponseWritable(response, inline);
		
		// Sets the value of the HTTP Cache-Control header
		// (this must be done before the response is actually written)
		this.prepareResponse(response);
		
		// Builds the report
		this.reportBuilder.build(report, destination);
	}

	/**
	 * @return the reportBuilder
	 */
	protected ReportBuilder getReportBuilder() {
		return reportBuilder;
	}

	/**
	 * @param reportBuilder the reportBuilder to set
	 */
	public void setReportBuilder(ReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}
}
