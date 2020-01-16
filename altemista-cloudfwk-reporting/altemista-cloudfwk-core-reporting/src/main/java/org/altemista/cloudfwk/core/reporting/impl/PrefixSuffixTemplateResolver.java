package org.altemista.cloudfwk.core.reporting.impl;

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

import org.apache.commons.lang3.StringUtils;
import org.altemista.cloudfwk.common.util.ResourceUtil;
import org.altemista.cloudfwk.core.reporting.TemplateResolver;

/**
 * Template resolver implementation that composes a resource location
 * with a prefix, the logical name of the template and a suffix.
 * @author NTT DATA
 */
public class PrefixSuffixTemplateResolver implements TemplateResolver {

	/** Prefix of the resource location. By example: "classpath*:templates/" */
	private String prefix;
	
	/** Suffix of the resource location. By example: ".jasper" */
	private String suffix;

	/*
	 * (non-Javadoc)
	 * @see org.altemista.cloudfwk.reporting.jasper.resolver.TemplateResolver#getTemplate(java.lang.String)
	 */
	@Override
	public InputStream getTemplate(String templateName) {
		
		// (sanity checks)
		if (StringUtils.isBlank(templateName)) {
			return null;
		}
		
		final String templateLocation =
				StringUtils.trimToEmpty(this.prefix)
				+ StringUtils.trimToEmpty(templateName)
				+ StringUtils.trimToEmpty(this.suffix);
		
		return ResourceUtil.getInputStream(templateLocation);
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
