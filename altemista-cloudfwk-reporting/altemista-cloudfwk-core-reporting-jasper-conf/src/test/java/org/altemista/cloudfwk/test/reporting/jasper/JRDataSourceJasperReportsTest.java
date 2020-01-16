
package org.altemista.cloudfwk.test.reporting.jasper;

/*
 * #%L
 * altemista-cloud reporting: JasperReports CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.common.model.ContentType;
import org.altemista.cloudfwk.core.reporting.exception.ReportingException;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.test.reporting.model.Account;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Test report building (with JasperReports implementation) using JRBeanCollectionDataSource as data source.
 * This class tests multiple exporters.
 * @author NTT DATA
 */
public class JRDataSourceJasperReportsTest extends AbstractJasperReportsTest {
	
	@Test
	public void sanityChecks() {
		
		// Ensures the context has been correctly initialized
		Assert.assertNotNull(this.getReportBuilder());
	}
	
	@Test
	public void testCsv() throws Exception {
		
		this.testBuildBlankReports(ContentType.CSV);
	}
	
	@Test
	public void testHtml() throws Exception {
		
		this.testBuildBlankReports(ContentType.HTML);
	}
	
	@Test
	public void testMsExcel() throws Exception {
		
		this.testBuildBlankReports(ContentType.MS_EXCEL);
	}
	
	@Test
	public void testMsPowerPoint() throws Exception {
		
		this.testBuildBlankReports(ContentType.MS_POWERPOINT);
	}
	
	@Test
	public void testMsWord() throws Exception {
		
		this.testBuildBlankReports(ContentType.MS_WORD);
	}
	
	@Test
	public void testOoxmlExcel() throws Exception {
		
		this.testBuildBlankReports(ContentType.OOXML_EXCEL);
	}
	
	@Test(expected = ReportingException.class)
	public void testOoxmlWord() throws Exception {
		
		// (expected exception as there is no exporter available for "Microsoft Office - OOXML - Word Document")
		this.testBuildBlankReports(ContentType.OOXML_WORD);
	}
	
	@Test(expected = ReportingException.class)
	public void testOoxmlPowerPoint() throws Exception {
		
		// (expected exception as there is no exporter available for "Microsoft Office - OOXML - Presentation")
		this.testBuildBlankReports(ContentType.OOXML_POWERPOINT);
	}
	
	@Test
	public void testPdf() throws Exception {
		
		this.testBuildBlankReports(ContentType.PDF);
	}
	
	@Test
	public void testRtf() throws Exception {
		
		this.testBuildBlankReports(ContentType.RTF);
	}
	
	@Test
	public void testText() throws Exception {
		
		this.testBuildBlankReports(ContentType.TEXT);
	}
	
	@Test
	public void testXml() throws Exception {
		
		this.testBuildBlankReports(ContentType.XML);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.test.reporting.jasper.AbstractJasperReportTest#additionalReportSetup(org.altemista.cloudfwk.common.reporting.model.ReportBean)
	 */
	@Override
	protected void additionalReportSetup(ReportBean report) {
		
		report.getParameters().put("PARAM_DATASOURCE", this.createJasperDataSource());
	}

	private JRDataSource createJasperDataSource(){
		
//		return new JRBeanCollectionDataSource(Collections.emptyList());
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
