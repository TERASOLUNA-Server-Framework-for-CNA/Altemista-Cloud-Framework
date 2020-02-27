
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


import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.common.model.ContentType;
import cloud.altemista.fwk.core.reporting.exception.ReportingException;
import cloud.altemista.fwk.core.reporting.model.ReportBean;

/**
 * Test report building (with JasperReports implementation) using JRBeanCollectionDataSource as data source.
 * @author NTT DATA
 */
public class SanityChecksTest extends AbstractJasperReportsTest {
	
	@Test
	public void sanityChecks() {
		
		// Ensures the context has been correctly initialized
		Assert.assertNotNull(this.getReportBuilder());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sanityChecksNull() {
		
		Assert.assertNull(this.getReportBuilder().build(null));
	}
	
	@Test(expected = ReportingException.class)
	public void sanityChecksNullTemplate() {
		
		ReportBean reportBean = this.getReportBuilder().newReport();
		Assert.assertNotNull(reportBean);
		Assert.assertNull(this.getReportBuilder().build(reportBean));
	}
	
	@Test(expected = ReportingException.class)
	public void sanityChecksEmptyTemplate() {
		
		ReportBean reportBean = this.getReportBuilder().newReport();
		reportBean.setTemplate("");
		Assert.assertNull(this.getReportBuilder().build(reportBean));
	}
	
	@Test(expected = ReportingException.class)
	public void sanityChecksBlankTemplate() {
		
		ReportBean reportBean = this.getReportBuilder().newReport();
		reportBean.setTemplate("   ");
		Assert.assertNull(this.getReportBuilder().build(reportBean));
	}
	
	@Test(expected = ReportingException.class)
	public void sanityChecksNullContentType() {
		
		ReportBean reportBean = this.getReportBuilder().newReport();
		reportBean.setTemplate("blank");
		Assert.assertNull(this.getReportBuilder().build(reportBean));
	}
	
	@Test(expected = ReportingException.class)
	public void sanityChecksInvalidContentType() {
		
		ReportBean reportBean = this.getReportBuilder().newReport();
		reportBean.setTemplate("blank");
		reportBean.setContentType(ContentType.register("application/foo", "bar"));
		Assert.assertNull(this.getReportBuilder().build(reportBean));
	}
}
