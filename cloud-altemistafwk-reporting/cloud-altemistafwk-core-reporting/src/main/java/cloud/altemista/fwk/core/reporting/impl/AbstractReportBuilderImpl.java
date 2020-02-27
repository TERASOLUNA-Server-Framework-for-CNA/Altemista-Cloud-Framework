	package cloud.altemista.fwk.core.reporting.impl;

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


import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import cloud.altemista.fwk.common.model.ContentReadable;
import cloud.altemista.fwk.common.model.ContentType;
import cloud.altemista.fwk.common.model.ContentWritable;
import cloud.altemista.fwk.common.model.StreamContentWritable;
import cloud.altemista.fwk.common.model.StreamSourceContentReadable;
import cloud.altemista.fwk.common.model.TempMixedOutputStream;
import cloud.altemista.fwk.core.reporting.ReportBuilder;
import cloud.altemista.fwk.core.reporting.TemplateResolver;
import cloud.altemista.fwk.core.reporting.exception.ReportingException;
import cloud.altemista.fwk.core.reporting.model.ReportBean;

/**
 * Base implementation for report builders.
 * @author NTT DATA
 */
public abstract class AbstractReportBuilderImpl implements ReportBuilder, InitializingBean {
	
	/** How the logical name of the templates will be resolved to the actual templates */
	private TemplateResolver templateResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		Assert.notNull(this.templateResolver, "The TemplateResolver is required");
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.reporting.ReportBuilder#newReport()
	 */
	@Override
	public ReportBean newReport() {
		
		return new ReportBean();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.reporting.ReportBuilder#build(cloud.altemista.fwk.common.reporting.model.Report, cloud.altemista.fwk.common.model.ContentWritable)
	 */
	@Override
	public void build(ReportBean report, ContentWritable destination) {
		
		Assert.notNull(report, "The ReportBean is required");
		Assert.notNull(destination, "The ContentWritable is required");
		
		// Resolves the actual template
		InputStream templateInputStream = this.templateResolver.getTemplate(report.getTemplate());
		if (templateInputStream == null) {
			throw new ReportingException("template_not_found", new Object[]{ report.getTemplate() });
		}
		
		// Builds the report
		this.preprocess(report, destination);
		this.internalBuild(report, templateInputStream, destination);
		this.postprocess(report, destination);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.reporting.ReportBuilder#build(cloud.altemista.fwk.common.reporting.model.Report)
	 */
	@Override
	public ContentReadable build(ReportBean report) {
		
		TempMixedOutputStream tempfile = null;
		StreamContentWritable destination = null;
		try {
			// Uses a temporary file backed to store the report
			tempfile = new TempMixedOutputStream();
			destination = new StreamContentWritable(tempfile);
			
			// Builds the report
			this.build(report, destination);
			
		} finally {
			IOUtils.closeQuietly(tempfile);
			IOUtils.closeQuietly(destination);
		}
		
		// Returns the temporary file as readable content
		return new StreamSourceContentReadable(tempfile, destination.getContentType(), destination.getPath());
	}

	/**
	 * Providers can implement this method to perform any additional set up before the report is generated.
	 * The default implementation of this method sets the destination content type and filename (with proper extension);
	 * this is done here instead of within <code>postprocess()</code> method as some ContentWritable implementations
	 * require the headers to be set before the response is written (e.g.: the HttpServletResponse based).
	 * @param report Report that is to be generated
	 * @param destination ContentWritable where the content of the report will be written
	 */
	protected void preprocess(ReportBean report, ContentWritable destination) {
		
		// (sanity check)
		ContentType contentType = report.getContentType();
		if (contentType == null) {
			return;
		}
		
		// Sets the content type
		destination.setContentType(contentType.getContentType());
		
		// Will use with the specified filename, defaults to the template logical name
		String filename = StringUtils.defaultString(
				StringUtils.trimToNull(report.getFilename()), StringUtils.trimToEmpty(report.getTemplate()));
		
		// Checks the extension does match content type, otherwise appends the default extension of the content type
		if (contentType.isProperFilename(filename)) {
			destination.setPath(filename);
			
		} else {
			destination.setPath(filename + FilenameUtils.EXTENSION_SEPARATOR_STR + contentType.getDefaultExtension());
		}
	}

	/**
	 * Actually builds a report and writes it to the specified destination.
	 * @param report Report that is to be generated
	 * @param InputStream to the template that should be used to generate the report
	 * @param destination ContentWritable where the content of the report will be written
	 */
	protected abstract void internalBuild(ReportBean report, InputStream template, ContentWritable destination);

	/**
	 * Providers can implement this method to perform any additional tasks after the report has been generated,
	 * such as close connections, streams or other resources.
	 * @param report Report that has been generated
	 * @param destination ContentWritable where the content of the report has been written
	 */
	protected void postprocess(ReportBean report, ContentWritable destination) {
		
		// (default empty implementation)
	}

	/**
	 * @param templateResolver the templateResolver to set
	 */
	public void setTemplateResolver(TemplateResolver templateResolver) {
		this.templateResolver = templateResolver;
	}
}
