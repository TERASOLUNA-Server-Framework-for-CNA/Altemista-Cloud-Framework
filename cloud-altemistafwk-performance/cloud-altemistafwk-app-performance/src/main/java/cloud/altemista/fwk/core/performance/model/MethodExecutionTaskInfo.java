/**
 * 
 */
package cloud.altemista.fwk.core.performance.model;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Information about the execution of a method as a measured task
 * @author NTT DATA
 */
public class MethodExecutionTaskInfo implements TaskInfo {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = 7933121651035685906L;

	/** The string representation of the signature of the method */
	private String signature;
	
	/** The string representation of the actual arguments received by the method (serializable by construction) */
	private List<String> arguments; // NOSONAR
	
	/** The string representation of the value returned by the method on successful executions */
	private String returnValue;
	
	/** The returned boolean, to explicitly inform that a void method has returned */
	private boolean returned;
	
	/** The string representation of the exception (or error) thrown by the method on failed executions */
	private String thrownException;

	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		
		return ((this.arguments == null) || this.arguments.isEmpty())
				? Collections.<String> emptyList()
				: new ArrayList<String>(this.arguments);
	}

	/**
	 * Adds a string representation of one argument received by the method
	 * @param argument the argument to add
	 */
	public void addArgument(String argument) {
		
		if (this.arguments == null) {
			this.arguments = new ArrayList<String>();
		}
		
		this.arguments.add(argument);
	}
	
	/**
	 * Marks a void method that has returned.
	 * Consequently, resets both the value returned and the exception thrown
	 */
	public void setReturned() {
		
		this.returned = true;
		this.returnValue = null;
		this.thrownException = null;
	}
	
	/**
	 * Sets the string representation of the value returned by the method
	 * and, consequently, resets the exception thrown
	 * @param returnValue the returnValue to set
	 */
	public void setReturnValue(String returnValue) {
		
		this.returned = true;
		this.returnValue = returnValue;
		this.thrownException = null;
	}

	/**
	 * Sets the string representation of the exception (or error) thrown by the method
	 * and, consequently, resets the value returned
	 * @param thrownException the thrownException to set
	 */
	public void setThrownException(String thrownException) {
		
		this.returned = false;
		this.returnValue = null;
		this.thrownException = thrownException;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.model.TaskInfo#getDescription()
	 */
	@Override
	public String getDescription() {

		List<String> fragments = new ArrayList<String>();
		
		if (StringUtils.isNotEmpty(this.signature)) {
			fragments.add(this.signature);
		}
		
		if ((this.arguments != null) && (!this.arguments.isEmpty())) {
			fragments.add("(" + StringUtils.join(arguments, ", ") + ")");
		}
		
		if (this.returned) {
			fragments.add("returned");
			if (StringUtils.isNotEmpty(this.returnValue)) {
				fragments.add(this.returnValue);
			}
		}
		
		if (StringUtils.isNotEmpty(this.thrownException)) {
			fragments.add("thrown " + this.thrownException);
		}
		
		return StringUtils.join(fragments, " ");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// Uses the description also for toString()
		return new ToStringBuilder(this).append(this.getDescription()).toString();
	}
	
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the returnValue
	 */
	public String getReturnValue() {
		return returnValue;
	}

	/**
	 * @return the returned
	 */
	public boolean hasReturned() {
		return returned;
	}

	/**
	 * @return the thrownException
	 */
	public String getThrownException() {
		return thrownException;
	}
}
