
package cloud.altemista.fwk.core.performance.jmx;

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


/**
 * Interface for the JMX managed bean to adjust the parameters and settings of the performance aspect
 * @author NTT DATA
 * @see cloud.altemista.fwk.core.performance.aop.PerformanceAspect
 */
public interface PerformanceAspectManager {
	
	/**
	 * Returns if the performance aspect is enabled
	 * @return if the performance aspect is enabled
	 */
	boolean isEnabled();
	
	/**
	 * Enables the performance aspect
	 */
	void enable();
	
	/**
	 * Disables the performance aspect
	 */
	void disable();
	
	/**
	 * Returns the maximum number of nesting levels when recording tasks performance
	 * @return the maximum number of nesting levels
	 */
	int getNestingLevels();
	
	/**
	 * Sets the maximum number of nesting levels when recording tasks performance
	 * @param nestingLevels the maximum number of nesting levels
	 */
	void setNestingLevels(int nestingLevels);
	
	/**
	 * Returns if the use of reflection to introspect the objects for obtaining their string representation is enabled
	 * @return if the use of reflection is enabled
	 */
	boolean isReflectionEnabled();
	
	/**
	 * Enables the use of reflection to introspect the objects for obtaining their string representation
	 */
	void enableReflection();
	
	/**
	 * Disables the use of reflection to introspect the objects for obtaining their string representation
	 */
	void disableReflection();
	
	/**
	 * If <code>isReflectionEnabled()</code>,
	 * returns the maximum length of the string representations of each nested object.
	 * Otherwise, returns the maximum length of the string representations of objects
	 * @return the maximum length of the string representations of objects
	 */
	int getMaxLength();
	
	/**
	 * Sets the maximum length of the string representations of objects
	 * @param maxLength the maximum length of the string representations of objects
	 */
	void setMaxLength(int maxLength);
	
	/**
	 * Returns the maximum depth of introspection of objects when using reflection
	 * @return the maximum depth of introspection
	 */
	int getMaxDepth();
	
	/**
	 * Sets the maximum depth for introspection when using reflection
	 * @param maxDepth the maximum depth of introspection
	 */
	void setMaxDepth(int maxDepth);
	
	/**
	 * Returns the maximum length of the object
	 * after which the rest of the nested elements will be omitted when using reflection
	 * @return the maximum length of the object
	 */
	int getStopPrintingAfter();
	
	/**
	 * Sets the maximum length of the object
	 * after which the rest of the nested elements will be omitted when using reflection
	 * @param stopPrintingAfter the maximum length of the object
	 */
	void setStopPrintingAfter(int stopPrintingAfter);
}
