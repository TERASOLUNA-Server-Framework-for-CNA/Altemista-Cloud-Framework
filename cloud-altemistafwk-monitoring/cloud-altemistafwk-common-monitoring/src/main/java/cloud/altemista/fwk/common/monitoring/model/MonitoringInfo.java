package cloud.altemista.fwk.common.monitoring.model;

/*
 * #%L
 * altemista-cloud monitoring: common interfaces
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * Information about one execution of one {@code Indicator}
 * @author NTT DATA
 */
public class MonitoringInfo implements Serializable {

	/** The serialVersionUID */
	private static final long serialVersionUID = 5573152272222134677L;
	
	/** Unique identifier of the indicator */
	private String indicatorId;

	/** Execution time (in milliseconds since January 1, 1970, 00:00:00 GMT) */
	private long execTime;

	/** Response time (in milliseconds) */
	private long responseTime;

	/** Status of the monitored resource in this execution */
	private Status status;

	/** Additional information */
	private String message;

	/**
	 * Constructor (when the execution starts).
	 * Also sets the execution time.
	 * @param id Unique identifier of the indicator
	 */
	public MonitoringInfo(String id) {
		super();
		
		Assert.hasText(id, "The identifier of the indicator is required");
		
		this.indicatorId = id;
		this.execTime = System.currentTimeMillis();
		this.responseTime = 0L;
		this.status = Status.UNKNOWN;
		this.message = null;
	}

	/**
	 * Constructor (when the execution starts and the message is known beforehand).
	 * Also sets the execution time.
	 * @param id Unique identifier of the indicator
	 * @param message aditional information (usually, a description)
	 */
	public MonitoringInfo(String id, String message) {
		this(id);
		
		this.message = StringUtils.defaultString(message);
	}

	/**
	 * Constructor (when the execution is immediate).
	 * Also sets the execution time and the response time.
	 * @param id Unique identifier of the indicator
	 * @param message aditional information (usually, a description)
	 * @param status Status of the monitored resource in this execution
	 */
	public MonitoringInfo(String id, String message, Status status) {
		this(id, message);
		
		this.responseTime = 0L;
		this.status = ObjectUtils.defaultIfNull(status, Status.UNKNOWN);
	}
	
	/**
	 * Updates the status (when the execution ends and the message was kwnown beforehand).
	 * Also sets the response time.
	 * @param status Status of the monitored resource in this execution
	 * @return this
	 */
	public MonitoringInfo update(Status status) {
		
		this.responseTime = System.currentTimeMillis() - this.execTime;
		this.status = ObjectUtils.defaultIfNull(status, Status.UNKNOWN);
		
		return this;
	}
	
	/**
	 * Updates the status and the message (when the execution ends).
	 * Also sets the response time.
	 * @param status Status of the monitored resource in this execution
	 * @param message aditional information
	 * @return this
	 */
	public MonitoringInfo update(Status status, String message) {
		
		this.update(status);
		this.message = StringUtils.defaultString(message);
		
		return this;
	}

	/**
	 * @return the indicatorId
	 */
	public String getIndicatorId() {
	
		return this.indicatorId;
	}

	/**
	 * @return the execTime
	 */
	public long getExecTime() {
	
		return this.execTime;
	}

	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
	
		return this.responseTime;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
	
		return this.status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
	
		return this.message;
	}
}
