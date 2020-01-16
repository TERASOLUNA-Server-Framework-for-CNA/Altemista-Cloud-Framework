package org.altemista.cloudfwk.reporting.jasper.exporter.impl;

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


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.model.ContentType;
import org.altemista.cloudfwk.core.reporting.model.ReportBean;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporter;
import org.altemista.cloudfwk.reporting.jasper.exporter.JasperExporterPolicy;

/**
 * JasperExporterPolicy implementation that maps content types to default JasperExporter implementations.
 * The JasperExporter implementation provided for each content type can be overwritten by simply setting it.
 * @author NTT DATA
 */
public class DefaultJasperExporterPolicyImpl implements JasperExporterPolicy, InitializingBean {
	
	/** Jasper Reports Exporter objects for "Comma-Separated Values" format */
	private CsvReportExporter csvReportExporter;
	
	/** Jasper Reports Exporter objects for "HyperText Markup Language (HTML)" format */
	private HtmlReportExporter htmlReportExporter;
	
	/** Jasper Reports Exporter objects for "Microsoft Excel" format */
	private MsExcelReportExporter msExcelReportExporter;
	
	/** Jasper Reports Exporter objects for "Microsoft Word" format */
	private MsWordReportExporter msWordReportExporter;
	
	/** Jasper Reports Exporter objects for "Microsoft PowerPoint" format */
	private MsPowerPointReportExporter msPowerPointReportExporter;
	
	/** Jasper Reports Exporter objects for "Microsoft Office - OOXML - Spreadsheet" format */
	private OoxmlExcelReportExporter ooxmlExcelReportExporter;
	
	/** Jasper Reports Exporter objects for "Adobe Portable Document Format" format */
	private PdfReportExporter pdfReportExporter;
	
	/** Jasper Reports Exporter objects for "Rich Text Formats" format */
	private RtfReportExporter rtfReportExporter;
	
	/** Jasper Reports Exporter objects for "Text file" format */
	private TextReportExporter textReportExporter;
	
	/** Jasper Reports Exporter objects for "Extensible Markup Language (XML)" format */
	private XmlReportExporter xmlReportExporter;
	
	/** Maps content types to JasperExporter implementations */
	private Map<ContentType, JasperExporter> map = new HashMap<ContentType, JasperExporter>();
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// Sets the default implementation for each exporter if not set
		this.map.put(ContentType.CSV,
				ObjectUtils.defaultIfNull(this.csvReportExporter, new CsvReportExporter()));
		
		this.map.put(ContentType.HTML,
				ObjectUtils.defaultIfNull(this.htmlReportExporter, new HtmlReportExporter()));
		
		this.map.put(ContentType.MS_EXCEL,
				ObjectUtils.defaultIfNull(this.msExcelReportExporter, new MsExcelReportExporter()));
		
		this.map.put(ContentType.MS_WORD,
				ObjectUtils.defaultIfNull(this.msWordReportExporter, new MsWordReportExporter()));
		
		this.map.put(ContentType.MS_POWERPOINT,
				ObjectUtils.defaultIfNull(this.msPowerPointReportExporter, new MsPowerPointReportExporter()));
		
		this.map.put(ContentType.OOXML_EXCEL,
				ObjectUtils.defaultIfNull(this.ooxmlExcelReportExporter, new OoxmlExcelReportExporter()));
		
		this.map.put(ContentType.PDF,
				ObjectUtils.defaultIfNull(this.pdfReportExporter, new PdfReportExporter()));
		
		this.map.put(ContentType.RTF,
				ObjectUtils.defaultIfNull(this.rtfReportExporter, new RtfReportExporter()));
		
		this.map.put(ContentType.TEXT,
				ObjectUtils.defaultIfNull(this.textReportExporter, new TextReportExporter()));
		
		this.map.put(ContentType.XML,
				ObjectUtils.defaultIfNull(this.xmlReportExporter, new XmlReportExporter()));
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.JasperExporterPolicy#getReportExporter(org.altemista.cloudfwk.common.reporting.model.Report)
	 */
	@Override
	public JasperExporter getReportExporter(ReportBean report) {
		
		// (sanity check)
		if ((report == null) || (report.getContentType() == null)) {
			return null;
		}
		
		return this.map.get(report.getContentType());
	}

	/**
	 * @param csvReportExporter the csvReportExporter to set
	 */
	public void setCsvReportExporter(CsvReportExporter csvReportExporter) {
		this.csvReportExporter = csvReportExporter;
	}


	/**
	 * @param htmlReportExporter the htmlReportExporter to set
	 */
	public void setHtmlReportExporter(HtmlReportExporter htmlReportExporter) {
		this.htmlReportExporter = htmlReportExporter;
	}


	/**
	 * @param msExcelReportExporter the msExcelReportExporter to set
	 */
	public void setMsExcelReportExporter(MsExcelReportExporter msExcelReportExporter) {
		this.msExcelReportExporter = msExcelReportExporter;
	}


	/**
	 * @param ooxmlExcelReportExporter the ooxmlExcelReportExporter to set
	 */
	public void setOoxmlExcelReportExporter(OoxmlExcelReportExporter ooxmlExcelReportExporter) {
		this.ooxmlExcelReportExporter = ooxmlExcelReportExporter;
	}


	/**
	 * @param msWordReportExporter the msWordReportExporter to set
	 */
	public void setMsWordReportExporter(MsWordReportExporter msWordReportExporter) {
		this.msWordReportExporter = msWordReportExporter;
	}


	/**
	 * @param msPowerPointReportExporter the msPowerPointReportExporter to set
	 */
	public void setMsPowerPointReportExporter(MsPowerPointReportExporter msPowerPointReportExporter) {
		this.msPowerPointReportExporter = msPowerPointReportExporter;
	}


	/**
	 * @param pdfReportExporter the pdfReportExporter to set
	 */
	public void setPdfReportExporter(PdfReportExporter pdfReportExporter) {
		this.pdfReportExporter = pdfReportExporter;
	}


	/**
	 * @param rtfReportExporter the rtfReportExporter to set
	 */
	public void setRtfReportExporter(RtfReportExporter rtfReportExporter) {
		this.rtfReportExporter = rtfReportExporter;
	}


	/**
	 * @param textReportExporter the textReportExporter to set
	 */
	public void setTextReportExporter(TextReportExporter textReportExporter) {
		this.textReportExporter = textReportExporter;
	}


	/**
	 * @param xmlReportExporter the xmlReportExporter to set
	 */
	public void setXmlReportExporter(XmlReportExporter xmlReportExporter) {
		this.xmlReportExporter = xmlReportExporter;
	}
}
