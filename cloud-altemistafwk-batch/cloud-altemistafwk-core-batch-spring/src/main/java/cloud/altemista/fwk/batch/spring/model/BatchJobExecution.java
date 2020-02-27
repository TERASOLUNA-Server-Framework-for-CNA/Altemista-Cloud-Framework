/**
 * 
 */
package cloud.altemista.fwk.batch.spring.model;

/*
 * #%L
 * altemista-cloud batch: Spring Batch implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Date;
import java.util.EnumMap;
import java.util.Properties;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.util.Assert;
import cloud.altemista.fwk.common.util.DefensiveUtil;

/**
 * Wrapper of Spring Batch JobExecution as a Javax JobExecution
 * @author NTT DATA
 */
public class BatchJobExecution implements JobExecution {
	
	/** Maps org.springframework.batch.core.BatchStatus to javax.batch.runtime.BatchStatus */
	private static final EnumMap<org.springframework.batch.core.BatchStatus, BatchStatus> STATUSES
			= new EnumMap<org.springframework.batch.core.BatchStatus, BatchStatus>(
					org.springframework.batch.core.BatchStatus.class);
	static{
		STATUSES.put(org.springframework.batch.core.BatchStatus.COMPLETED, BatchStatus.COMPLETED);
		STATUSES.put(org.springframework.batch.core.BatchStatus.STARTING, BatchStatus.STARTING);
		STATUSES.put(org.springframework.batch.core.BatchStatus.STARTED, BatchStatus.STARTED);
		STATUSES.put(org.springframework.batch.core.BatchStatus.STOPPING, BatchStatus.STOPPING);
		STATUSES.put(org.springframework.batch.core.BatchStatus.STOPPED, BatchStatus.STOPPED);
		STATUSES.put(org.springframework.batch.core.BatchStatus.FAILED, BatchStatus.FAILED);
		STATUSES.put(org.springframework.batch.core.BatchStatus.ABANDONED, BatchStatus.ABANDONED);
		STATUSES.put(org.springframework.batch.core.BatchStatus.UNKNOWN, null);
	}
	
	/** The Spring Batch JobExecution */
	private final org.springframework.batch.core.JobExecution job;
	
	/**
	 * Constructor
	 * @param job The Spring Batch JobExecution
	 */
	public BatchJobExecution(org.springframework.batch.core.JobExecution job) {
		super();
		
		Assert.notNull(job, "The JobExecution is required");
		
		this.job = job;
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getExecutionId()
	 */
	@Override
	public long getExecutionId() {
		
		return this.job.getId();
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getJobName()
	 */
	@Override
	public String getJobName() {
		
		JobInstance instance = this.job.getJobInstance();
		if (instance == null) {
			return null;
		}
		return instance.getJobName();
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getBatchStatus()
	 */
	@Override
	public BatchStatus getBatchStatus() {
		
		// Maps org.springframework.batch.core.BatchStatus to javax.batch.runtime.BatchStatus
		return STATUSES.get(this.job.getStatus());
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		
		return DefensiveUtil.copyOf(this.job.getStartTime());
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		
		return DefensiveUtil.copyOf(this.job.getEndTime());
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getExitStatus()
	 */
	@Override
	public String getExitStatus() {
		
		ExitStatus exitStatus = this.job.getExitStatus();
		if (exitStatus == null) {
			return null;
		}
		return exitStatus.getExitCode();
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getCreateTime()
	 */
	@Override
	public Date getCreateTime() {
		
		return DefensiveUtil.copyOf(this.job.getCreateTime());
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getLastUpdatedTime()
	 */
	@Override
	public Date getLastUpdatedTime() {
		
		return DefensiveUtil.copyOf(this.job.getLastUpdated());
	}

	/* (non-Javadoc)
	 * @see javax.batch.runtime.JobExecution#getJobParameters()
	 */
	@Override
	public Properties getJobParameters() {
		
		JobParameters parameters = this.job.getJobParameters();
		if (parameters == null) {
			return null;
		}
		return parameters.toProperties();
	}
}
