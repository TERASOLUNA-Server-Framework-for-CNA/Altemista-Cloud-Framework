package org.altemista.cloudfwk.it.reporting.jasper.controller;

/*
 * #%L
 * altemista-cloud reporting jasper integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.common.model.ContentType;
import org.altemista.cloudfwk.core.reporting.ReportBuilder;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.it.reporting.jasper.model.Account;
import org.altemista.cloudfwk.reporting.jasper.model.JasperReportBean;
import org.altemista.cloudfwk.web.model.HttpServletResponseWritable;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * REST controller to test ExceptionCounterAspectImpl
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = JasperReportController.MAPPING, method = RequestMethod.GET)
public class JasperReportController {

	/** MAPPING String */
	public static final String MAPPING = "/jasperReport";
	
	/** Report builder */
	@Autowired
	private ReportBuilder reportBuilder;

	@RequestMapping(value = "/1")
	public void buildBlankAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.HTML);
		HttpServletResponseWritable destination = new HttpServletResponseWritable(response);
		this.reportBuilder.build(report, destination);
	}

	@RequestMapping(value = "/2")
	public void buildBlankPdfAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.PDF);
		HttpServletResponseWritable destination = new HttpServletResponseWritable(response);
		this.reportBuilder.build(report, destination);
	}

	@RequestMapping(value = "/3")
	public void buildAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.HTML);
		HttpServletResponseWritable destination = new HttpServletResponseWritable(response);
		this.reportBuilder.build(report, destination);
	}

	@RequestMapping(value = "/4")
	public void buildPdfAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.PDF);
		HttpServletResponseWritable destination = new HttpServletResponseWritable(response);
		this.reportBuilder.build(report, destination);
	}
	
	/**
	 * Convenience method
	 * @param request HttpServletRequest
	 * @return ReportBean
	 */
	private ReportBean newReport(HttpServletRequest request, String template, ContentType contentType) {
		
		ReportBean report = this.reportBuilder.newReport();
		report.setTemplate(template);
		report.setContentType(contentType);
		report.setFilename("test_report");
		report.putParameter("Titulo", request.getParameter("Titulo"));
		((JasperReportBean) report).setJrDataSource(this.getParamDataSource());
		return report;
	}
	
	/**
	 * Convenience method
	 * @return JRDataSource
	 */
	private JRDataSource getParamDataSource() {
		
		// create List of Accounts to use in Report
		List<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < 10; i++) {
			Account account = new Account("Number" + i, "Name" + i);
			account.setEntityId(i);
			accounts.add(account);
		}

		// Create new JRDataSource from list
		return new JRBeanCollectionDataSource(accounts);
	}
}
