/**
 * 
 */
package cloud.altemista.fwk.example.reporting;

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


import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import cloud.altemista.fwk.common.model.ContentType;
import cloud.altemista.fwk.common.model.ContentWritable;
import cloud.altemista.fwk.core.reporting.ReportBuilder;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.model.JasperReportBean;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class ReportBuilderExample {
	
	//tag::example[]
	@Autowired
	private ReportBuilder reportBuilder; //<1>
	
	public void generateAndSaveReport(String value, ContentWritable destination) {
		
		ReportBean report = this.reportBuilder.newReport(); //<2>
		report.setTemplate("helloWorld"); //<3>
		report.setContentType(ContentType.PDF); //<4>
		report.setFilename("hello-" + value); //<5>
		
		report.putParameter("personName", value); //<6>
		// (...)
		
		this.reportBuilder.build(report, destination); //<7>
	}
	//end::example[]
	
	public void generateAndSaveJasperReport(String value, ContentWritable destination) {
		
		//tag::jasper[]
		ReportBean report = this.reportBuilder.newReport(); //<1>
		// ...
		
		Collection<Map<String, ?>> collection = // ...
		//end::jasper[]
				null;
		//tag::jasper[]
		((JasperReportBean) report).setJrDataSource(new JRMapCollectionDataSource(collection)); //<2>
		
		this.reportBuilder.build(report, destination); //<3>
		//end::jasper[]
	}
}
