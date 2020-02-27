
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
import org.springframework.test.context.ContextConfiguration;
import cloud.altemista.fwk.common.model.ContentType;

/**
 * Test report building (with JasperReports implementation) when using a DataSource
 * @author NTT DATA
 */
@ContextConfiguration(locations = { "classpath:spring/test-app-context-reporting-jasper-datasource.xml" })
public class ReportJpaTest extends AbstractJasperReportsTest {
	
	@Test
	public void sanityChecks() {
		
		// Ensures the context has been correctly initialized
		Assert.assertNotNull(this.getReportBuilder());
	}
	
	@Test
	public void testExport() throws Exception {
		
		this.testBuildBlankReports(ContentType.HTML);
	}
}
