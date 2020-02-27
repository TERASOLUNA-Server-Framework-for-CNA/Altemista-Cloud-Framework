package cloud.altemista.fwk.reporting.jasper.filler.impl;

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


import java.util.Map;

import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFiller;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFillerPolicy;

/**
 * JasperFillerPolicy implementation that maps template names to JasperFiller implementations.
 * @author NTT DATA
 */
public class TemplateBasedFillerPolicyImpl implements JasperFillerPolicy {

	/** Maps template names to JasperFiller implementations */
	private Map<String, JasperFiller> templateFillerMap;
	
	/** An optional fallback policy in case the template name is not found in the map */
	private JasperFillerPolicy fallbackPolicy;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.JasperFillerPolicy#getJasperFiller(cloud.altemista.fwk.common.reporting.model.Report)
	 */
	@Override
	public JasperFiller getJasperFiller(ReportBean report) {
		
		// Looks for the JasperFiller in the map by template name
		JasperFiller filler = this.templateFillerMap.get(report.getTemplate());
		if (filler != null) {
			return filler;
		}
		
		// If not found, uses the fallback policy
		if (this.fallbackPolicy != null) {
			return this.fallbackPolicy.getJasperFiller(report);
		}
		
		// No JasperFiller found
		return null;
	}

	/**
	 * @param templateFillerMap the templateFillerMap to set
	 */
	public void setTemplateFillerMap(Map<String, JasperFiller> templateFillerMap) {
		this.templateFillerMap = templateFillerMap;
	}

	/**
	 * @param fallbackPolicy the fallbackPolicy to set
	 */
	public void setFallbackPolicy(JasperFillerPolicy fallbackPolicy) {
		this.fallbackPolicy = fallbackPolicy;
	}
}
