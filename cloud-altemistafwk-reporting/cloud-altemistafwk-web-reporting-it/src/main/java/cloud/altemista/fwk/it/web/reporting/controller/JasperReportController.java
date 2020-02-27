package cloud.altemista.fwk.it.web.reporting.controller;

import java.util.ArrayList;
import java.util.List;

/*
 * #%L
 * altemista-cloud web reporting integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.common.model.ContentType;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.it.web.reporting.model.Account;
import cloud.altemista.fwk.reporting.jasper.model.JasperReportBean;
import cloud.altemista.fwk.web.reporting.controller.impl.AbstractReportController;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Controller for test
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = JasperReportController.MAPPING, method = RequestMethod.GET)
public class JasperReportController extends AbstractReportController {

	/** MAPPING String */
	public static final String MAPPING = "/jasperReport";
	
	/**
	 * Default constructor
	 */
	public JasperReportController() {
		super();
		
		// Sets a no-cache policy for this controller (this is just an example)
		super.setCacheControl(CacheControl.noCache());
	}
	
	@RequestMapping(value = "/1")
	public void buildBlankAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.HTML);
		this.build(report, response);
	}

	@RequestMapping(value = "/2")
	public void buildBlankAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.HTML);
		this.build(report, response, true);
	}

	@RequestMapping(value = "/3")
	public void buildBlankPdfAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.PDF);
		this.build(report, response);
	}

	@RequestMapping(value = "/4")
	public void buildBlankPdfAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "blank", ContentType.PDF);
		this.build(report, response, true);
	}

	@RequestMapping(value = "/5")
	public void buildAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.HTML);
		this.build(report, response);
	}

	@RequestMapping(value = "/6")
	public void buildAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.HTML);
		this.build(report, response, true);
	}

	@RequestMapping(value = "/7")
	public void buildPdfAndDownload(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.PDF);
		this.build(report, response);
	}

	@RequestMapping(value = "/8")
	public void buildPdfAndInline(HttpServletRequest request, HttpServletResponse response) {
		
		ReportBean report = this.newReport(request, "AccountListReportList", ContentType.PDF);
		this.build(report, response, true);
	}
	
	/**
	 * Convenience method
	 * @param request HttpServletRequest
	 * @return ReportBean
	 */
	private ReportBean newReport(HttpServletRequest request, String template, ContentType contentType) {
		
		ReportBean report = this.getReportBuilder().newReport();
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
			Account account = new Account();
			account.setEntityId(i);
			account.setName("Name" + i);
			account.setNumber("Number" + i);
			accounts.add(account);
		}

		// Create new JRDataSource from list
		return new JRBeanCollectionDataSource(accounts);
	}
}
