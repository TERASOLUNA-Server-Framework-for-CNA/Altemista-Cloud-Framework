
package org.altemista.cloudfwk.core.performance.jmx.impl;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.performance.MeasuredTaskHolder;
import org.altemista.cloudfwk.core.performance.aop.PerformanceAspect;
import org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager;

/**
 * JMX managed bean to adjust the parameters and settings of the performance aspect
 * @author NTT DATA
 */
@ManagedResource(
		objectName = "PerformanceAspectManager",
		description = "Adjusts the settings of the performance aspect")
public class PerformanceAspectManagerImpl implements PerformanceAspectManager, InitializingBean {
	
	/** The performance aspect instance */
	private PerformanceAspect performanceAspect;

	/** The holder class for the aspect */
	private MeasuredTaskHolder holder;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.performanceAspect, "The performance aspect instance is required");
		Assert.notNull(this.holder, "The holder class for the aspect is required");
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#isEnabled()
	 */
	@ManagedOperation(description = "Returns if the performance aspect is enabled")
	@Override
	public boolean isEnabled() {
		
		return this.performanceAspect.isEnabled();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#enable()
	 */
	@ManagedOperation(description = "Enables the performance aspect")
	@Override
	public void enable() {

		this.performanceAspect.setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#disable()
	 */
	@ManagedOperation(description = "Disables the performance aspect")
	@Override
	public void disable() {

		this.performanceAspect.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#getNestingLevels()
	 */
	@ManagedOperation(description = "Returns the maximum number of nesting levels when recording tasks performance")
	@Override
	public int getNestingLevels() {

		return this.holder.getNestingLevels();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#setNestingLevels(int)
	 */
	@ManagedOperation(description = "Sets the maximum number of nesting levels when recording tasks performance")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name = "nestingLevels", description = "The maximum number of nesting levels")
	})
	@Override
	public void setNestingLevels(int nestingLevels) {

		this.holder.setNestingLevels(nestingLevels);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#isReflectionEnabled()
	 */
	@ManagedOperation(description = "Returns if the use of reflection to introspect the objects "
			+ "for obtaining their string representation is enabled")
	@Override
	public boolean isReflectionEnabled() {
		
		return this.performanceAspect.isReflectionToStringEnabled();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#enableReflection()
	 */
	@ManagedOperation(description = "Enables the use of reflection to introspect the objects "
			+ "for obtaining their string representation")
	@Override
	public void enableReflection() {

		this.performanceAspect.setReflectionToStringEnabled(true);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#disableReflection()
	 */
	@ManagedOperation(description = "Disables the use of reflection to introspect the objects "
			+ "for obtaining their string representation")
	@Override
	public void disableReflection() {

		this.performanceAspect.setReflectionToStringEnabled(false);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#getMaxLength()
	 */
	@ManagedOperation(description = "If isReflectionEnabled(), "
			+ "returns the maximum length of the string representations of each nested object. "
			+ "Otherwise, returns the maximum length of the string representations of objects")
	@Override
	public int getMaxLength() {
		
		return this.performanceAspect.getMaxLength();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#setMaxLength(int)
	 */
	@ManagedOperation(description = "Sets the maximum length of the string representations of objects")
	@Override
	public void setMaxLength(int maxLength) {

		this.performanceAspect.setMaxLength(maxLength);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#getMaxDepth()
	 */
	@ManagedOperation(description = "Returns the maximum depth of introspection of objects when using reflection")
	@Override
	public int getMaxDepth() {
		
		return this.performanceAspect.getMaxDepth();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#setMaxDepth(int)
	 */
	@ManagedOperation(description = "Sets the maximum depth for introspection when using reflection")
	@Override
	public void setMaxDepth(int maxDepth) {

		this.performanceAspect.setMaxDepth(maxDepth);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#getStopPrintingAfter()
	 */
	@ManagedOperation(description = "Returns the maximum length of the object "
			+ "after which the rest of the nested elements will be omitted when using reflection")
	@Override
	public int getStopPrintingAfter() {
		
		return this.performanceAspect.getStopPrintingAfter();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.performance.jmx.PerformanceAspectManager#setStopPrintingAfter(int)
	 */
	@ManagedOperation(description = "Sets the maximum length of the object "
			+ "after which the rest of the nested elements will be omitted when using reflection")
	@Override
	public void setStopPrintingAfter(int stopPrintingAfter) {

		this.performanceAspect.setStopPrintingAfter(stopPrintingAfter);
	}

	/**
	 * @param performanceAspect the performanceAspect to set
	 */
	public void setPerformanceAspect(PerformanceAspect performanceAspect) {
		this.performanceAspect = performanceAspect;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder(MeasuredTaskHolder holder) {
		this.holder = holder;
	}
}
