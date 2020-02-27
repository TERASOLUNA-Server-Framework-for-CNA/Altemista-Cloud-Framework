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


import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Simple information about a measured task
 * @author NTT DATA
 */
public class SimpleTaskInfo implements TaskInfo {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -2515485194467037327L;
	
	/** The task description */
	private String description;
	
	/**
	 * Default constructor
	 */
	public SimpleTaskInfo() {
		this(null);
	}

	/**
	 * Constructor
	 * @param description The task description
	 */
	public SimpleTaskInfo(String description) {
		super();
		
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.model.TaskInfo#getDescription()
	 */
	@Override
	public String getDescription() {
		
		return this.description;
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
