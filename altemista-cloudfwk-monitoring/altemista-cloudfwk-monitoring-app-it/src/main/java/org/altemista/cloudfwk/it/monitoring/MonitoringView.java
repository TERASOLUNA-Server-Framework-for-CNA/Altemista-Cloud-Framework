package org.altemista.cloudfwk.it.monitoring;

/*
 * #%L
 * altemista-cloud monitoring integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;

import org.altemista.cloudfwk.common.monitoring.model.Status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitoringView implements Serializable {

	private static final long serialVersionUID = 4421251666622612961L;
	
	/** Id of the indicator who generates this DTO */
	private String indicatorId;

	/** Status */
	@JsonProperty
	private Status status;

	/** Response time in ms */
	@JsonProperty
	private long responseTime;

	/** Time when the execution was done (in ms) */
	@JsonProperty
	private long execTime;

	/** String with aditional information */
	@JsonProperty
	private String message;

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return the execTime
	 */
	public long getExecTime() {
		return execTime;
	}

	/**
	 * @param execTime the execTime to set
	 */
	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the indicatorId
	 */
	public String getIndicatorId() {
		return indicatorId;
	}

	/**
	 * @param indicatorId the indicatorId to set
	 */
	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
	}

	
	
}
