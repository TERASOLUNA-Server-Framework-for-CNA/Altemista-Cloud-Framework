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


import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.reporting.model.ReportBean;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFiller;
import cloud.altemista.fwk.reporting.jasper.filler.JasperFillerPolicy;

/**
 * Simple JasperFillerPolicy implementation that always returns the same JasperFiller
 * @author NTT DATA
 */
public class SimpleJasperFillerPolicyImpl implements JasperFillerPolicy, InitializingBean {
	
	/** The single JasperFiller */
	private JasperFiller filler;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		Assert.notNull(this.filler, "The JaperFiller is required");
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reporting.jasper.JasperFillerPolicy#getReportExporter(cloud.altemista.fwk.common.reporting.model.Report)
	 */
	@Override
	public JasperFiller getJasperFiller(ReportBean report) {
		
		return this.filler;
	}

	/**
	 * @param filler the filler to set
	 */
	public void setFiller(JasperFiller filler) {
		this.filler = filler;
	}
}
