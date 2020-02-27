package cloud.altemista.fwk.test.reporting.jasper;

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


import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.springframework.beans.factory.annotation.Autowired;
import cloud.altemista.fwk.common.model.ContentReadable;
import cloud.altemista.fwk.common.model.ContentType;
import cloud.altemista.fwk.common.model.StreamContentWritable;
import cloud.altemista.fwk.common.model.TempMixedOutputStream;
import cloud.altemista.fwk.core.reporting.ReportBuilder;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Abstract base class for test report building (with JasperReports implementation)
 * @author NTT DATA
 */
public abstract class AbstractJasperReportsTest extends AbstractSpringContextTest {

	/** The report builder to be tested */
	@Autowired
	private ReportBuilder reportBuilder;
	
	/**
	 * Convenience method to test building the "blank" report
	 * @param contentType the content type of the report that is to be generated
	 * @throws Exception
	 */
	protected void testBuildBlankReports(ContentType contentType) throws Exception {
		
		// Test for building a report to the specified destination
		ReportBean report = this.getReportBuilder().newReport();
		report.setTemplate("blank");
		report.setContentType(contentType);
		this.additionalReportSetup(report);
		this.testBuildReport(report);
		
		// Test for building a report without specifying a destination
		ReportBean readableReport = this.getReportBuilder().newReport();
		readableReport.setTemplate("blank");
		readableReport.setContentType(contentType);
		this.additionalReportSetup(readableReport);
		this.testBuildReadableReport(readableReport);
	}
	
	/**
	 * Performs additional operations to set up the ReportBean
	 * @param report ReportBean
	 */
	protected void additionalReportSetup(ReportBean report) {
		
		// (default empty implementation)
	}

	/**
	 * Test for building a report to the specified destination.
	 * @param report Report information
	 * @throws Exception
	 */
	protected void testBuildReport(ReportBean report) throws Exception {
		
		Assume.assumeNotNull(report);
		Assume.assumeNotNull(report.getTemplate());
		Assume.assumeNotNull(report.getContentType());
		Assume.assumeNotNull(report.getParameters());
		
		// Uses a temporary file to generate the report
		TempMixedOutputStream tempfile = null;
		StreamContentWritable destination = null;
		try {
			tempfile = new TempMixedOutputStream("report_", "." + report.getContentType().getDefaultExtension());
			destination = new StreamContentWritable(tempfile);
			
			// Builds the report
			this.reportBuilder.build(report, destination);
			
		} finally {
			IOUtils.closeQuietly(tempfile);
			IOUtils.closeQuietly(destination);
		}
		
		// Asserts the report has been built and the content type and filename been set
		Assert.assertEquals(report.getContentType().getContentType(), destination.getContentType());
		Assert.assertTrue(report.getContentType().isProperFilename(destination.getPath()));
		Assert.assertNotEquals(0, tempfile.getInputStream().available());
	}
	
	/**
	 * Test for building a report without specifying a destination.
	 * @param report Report information
	 * @throws Exception
	 */
	protected void testBuildReadableReport(ReportBean report) throws Exception {
		
		Assume.assumeNotNull(report);
		Assume.assumeNotNull(report.getTemplate());
		Assume.assumeNotNull(report.getContentType());
		Assume.assumeNotNull(report.getParameters());
		
		// Builds the report
		ContentReadable readable = this.reportBuilder.build(report);
		
		// Asserts the report has been built and the content type and filename been set
		Assert.assertEquals(report.getContentType().getContentType(), readable.getContentType());
		Assert.assertTrue(report.getContentType().isProperFilename(readable.getPath()));
		Assert.assertNotEquals(0, readable.getInputStream().available());
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
