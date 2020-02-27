/**
 * 
 */
package cloud.altemista.fwk.batch.spring.impl;

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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cloud.altemista.fwk.batch.spring.model.BatchJobExecution;
import cloud.altemista.fwk.common.util.CollectionUtil;
import cloud.altemista.fwk.core.batch.BatchService;
import cloud.altemista.fwk.core.batch.exception.BatchException;
import cloud.altemista.fwk.core.batch.model.BatchParameter;

/**
 * Batch module implementation using Spring Batch
 * @author NTT DATA
 */
@Service
public class BatchServiceImpl implements BatchService {
	
	/** The runtime service registry, used here for retrieving job configurations */
	@Autowired
	private JobRegistry jobRegistry;
	
	/** Inpects and controls jobs */
	@Autowired
	private JobOperator jobOperator;
	
	/** Browses executions of running or historical jobs */
	@Autowired
	private JobExplorer jobExplorer;
	
	/** Spring batch meta-data repository */
	@Autowired
	private JobRepository jobRepository;
	
	/** Simple interface for controlling jobs */
	@Autowired
	private JobLauncher jobLauncher;

	//
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#countJobs()
	 */
	@Override
	public int countJobs() {
		
		return this.jobRegistry.getJobNames().size();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#getJobs(int, int)
	 */
	@Override
	public List<String> listJobs(int start, int count) {
		
		return CollectionUtil.page(this.jobRegistry.getJobNames(), start, count);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#isLaunchable(java.lang.String)
	 */
	@Override
	public boolean isLaunchable(String jobName) {
		
		return this.existsJobQuietly(jobName);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#launch(java.lang.String, cloud.altemista.fwk.core.batch.model.BatchParameter[])
	 */
	@Override
	public Long launch(String jobName, BatchParameter<?>... parameters) {
		
		// Fail-fast validation
		Job job = this.getJobQuietly(jobName);
		if (job == null) {
			return null;
		}

		// If already launched, check if it should restart
		JobParameters jobParameters = this.parametersFromModel(parameters);
		JobExecution lastJobExecution = this.jobRepository.getLastJobExecution(jobName, jobParameters);
		boolean restart = false;
		if (lastJobExecution != null) {
			BatchStatus status = lastJobExecution.getStatus();
			if (status.isUnsuccessful() && (status != BatchStatus.ABANDONED)) {
				restart = true;
			}
		} 

		// If it is not a restart, increments the parameters
		if ((!restart) && (job.getJobParametersIncrementer() != null)) {
			jobParameters = job.getJobParametersIncrementer().getNext(jobParameters);
		}

		// Invokes Spring Batch
		try {
			JobExecution execution = this.jobLauncher.run(job, jobParameters);
			return execution.getId();
			
		} catch (JobExecutionException e) {
			this.handleJobExecutionException(jobName, null, e);
			throw new IllegalStateException("Should never get here"); // NOSONAR
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#isLaunchableNextInstance(java.lang.String)
	 */
	@Override
	public boolean isLaunchableNextInstance(String jobName) {
		
		// To allow launchNextInstance, the job must have jobParametersIncrementer
		Job job = this.getJobQuietly(jobName);
		return (job != null)
				&& job.getJobParametersIncrementer() != null;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#launchNextInstance(java.lang.String)
	 */
	@Override
	public Long launchNextInstance(String jobName) {
		
		// Fail-fast validation
		if (!this.isLaunchableNextInstance(jobName)) {
			return null;
		}
		
		// Invokes Spring Batch
		try {
			return this.jobOperator.startNextInstance(jobName);
			
		} catch (JobExecutionException e) {
			this.handleJobExecutionException(jobName, null, e);
			throw new IllegalStateException("Should never get here"); // NOSONAR
		}
	}
	
	//
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#countJobInstances(java.lang.String)
	 */
	@Override
	public int countJobInstances(String jobName) {
		
		// Fail-fast validation
		if (!this.existsJobQuietly(jobName)) {
			return 0;
		}
		
		// Invokes Spring Batch
		try {
			return this.jobExplorer.getJobInstanceCount(jobName);
			
		} catch (NoSuchJobException e) {
			this.handleJobExecutionException(jobName, null, e);
			throw new IllegalStateException("Should never get here"); // NOSONAR
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#listJobInstances(java.lang.String, int, int)
	 */
	@Override
	public List<Long> listJobInstances(String jobName, int start, int count) {
		
		// Fail-fast validation
		if (!this.existsJobQuietly(jobName)) {
			return Collections.emptyList();
		}
		
		// Invokes Spring Batch
		List<? extends JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, start, count);
		
		// Converts the list of job instances in a list of job instance IDs 
		List<Long> list = new ArrayList<Long>();
		for (JobInstance instance : jobInstances) {
			list.add(instance.getInstanceId());
		}
		return list;
	}
	
	//
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#getExecutions(long)
	 */
	@Override
	public List<javax.batch.runtime.JobExecution> getExecutions(long instanceId) {
		
		JobInstance instance = jobExplorer.getJobInstance(instanceId);
		if (instance == null) {
			return Collections.emptyList();
		}
		
		List<JobExecution> executions = jobExplorer.getJobExecutions(instance);
		return this.executionsToModel(executions);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#getRunningExecutions(java.lang.String)
	 */
	@Override
	public List<javax.batch.runtime.JobExecution> getRunningExecutions(String jobname) {
		
		Set<JobExecution> executions = jobExplorer.findRunningJobExecutions(jobname);
		return this.executionsToModel(executions);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#getParameters(long)
	 */
	@Override
	public List<BatchParameter<?>> getParameters(long executionId) {
	
		JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
		if (jobExecution == null) {
			throw new BatchException("execution_unknown");
		}
		
		return this.parametersToModel(jobExecution.getJobParameters());
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#stop(long)
	 */
	@Override
	public boolean stop(long executionId) {
		
		// Invokes Spring Batch
		try {
			return this.jobOperator.stop(executionId);
			
		} catch (NoSuchJobExecutionException e) { // NOSONAR
			// Avoid exception if the execution does not exists
			return false;
			
		} catch (JobExecutionNotRunningException e) { // NOSONAR
			// Avoid exception if the execution was already stopped
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#restart(long)
	 */
	@Override
	public Long restart(long executionId) {
		
		// Invokes Spring Batch
		try {
			return this.jobOperator.restart(executionId);
			
		} catch (NoSuchJobExecutionException e) { // NOSONAR
			// Avoid exception if the execution does not exists
			return null;
			
		} catch (JobInstanceAlreadyCompleteException e) { // NOSONAR
			// Avoid exception if the execution was already completed
			return null;
			
		} catch (UnexpectedJobExecutionException e) {
			// Avoid exception if the execution was already running
			if (e.getCause() instanceof JobExecutionAlreadyRunningException) {
				return null;
			}
			throw e;
			
		} catch (JobExecutionException e) {
			this.handleJobExecutionException(null, executionId, e);
			throw new IllegalStateException("Should never get here"); // NOSONAR
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.batch.BatchService#abandon(long)
	 */
	@Override
	public javax.batch.runtime.JobExecution abandon(long executionId) {
		
		// Invokes Spring Batch
		try {
			return this.executionToMdoel(this.jobOperator.abandon(executionId));
			
		} catch (NoSuchJobExecutionException e) { // NOSONAR
			// Avoid exception if the execution does not exists
			return null;
			
		} catch (JobExecutionAlreadyRunningException e) { // NOSONAR
			// Avoid exception if the execution was not yet stopped
			return null;
		}
	}
	
	//
	
	/**
	 * Convenience method to check the existence of a job name without exceptions
	 * @param jobName the name of the job
	 * @return true if the job exists
	 */
	private boolean existsJobQuietly(String jobName) {
		
		if (StringUtils.isEmpty(jobName)) {
			return false;
		}
		
		return this.jobRegistry.getJobNames().contains(jobName);
	}
	
	/**
	 * Convenience method to retrieve a Job object without exceptions
	 * @param jobName the name of the job
	 * @return Job or null
	 */
	private Job getJobQuietly(String jobName) {
		
		if (StringUtils.isEmpty(jobName)) {
			return null;
		}
		
		try {
			return this.jobRegistry.getJob(jobName);
			
		} catch (NoSuchJobException e) { // NOSONAR
			return null;
		}
	}
	
	/**
	 * Convenience method to convert a JobExecutionException into a BatchException preserving the name of the job
	 * @param jobName the name of the job
	 * @param e the JobExecutionException to handle
	 */
	private void handleJobExecutionException(String jobName, Long executionId, JobExecutionException e) {
		
		if (e instanceof NoSuchJobException) {
			throw new BatchException("job_unknown", new Object[]{ jobName }, e);
		}
		
		if (e instanceof NoSuchJobExecutionException) {
			throw new BatchException("execution_unknown", new Object[]{ executionId }, e);
		}
		
		throw new BatchException("invalid_state", new Object[]{ jobName, executionId }, e);
	}
	
	//
	
	/**
	 * Convenience method to convert Spring Batch JobExecutions to Javax JobExecutions
	 * @param executions collection of Spring Batch JobExecution
	 * @return List of Javax JobExecutions
	 */
	private List<javax.batch.runtime.JobExecution> executionsToModel(Collection<JobExecution> executions) {
		
		// (sanity check)
		if ((executions == null) || executions.isEmpty()) {
			return Collections.emptyList();
		}
		
		// Actual conversion
		List<javax.batch.runtime.JobExecution> list = new ArrayList<javax.batch.runtime.JobExecution>();
		for (JobExecution execution : executions) {
			list.add(this.executionToMdoel(execution));
		}
		return list;
	}
	
	/**
	 * Convenience method to convert a Spring Batch JobExecution to a Javax JobExecution
	 * @param execution Spring Batch JobExecution
	 * @return Javax JobExecution
	 */
	private javax.batch.runtime.JobExecution executionToMdoel(JobExecution execution) {
		
		// (sanity check)
		if (execution == null) {
			return null;
		}
		
		// Actual conversion (wrapping, in fact)
		return new BatchJobExecution(execution);
	}
	
	/**
	 * Convenience method to convert an array of BatchParameters to Spring Batch JobParameters
	 * @param parameters array of BatchParameter
	 * @return JobParameters
	 */
	private JobParameters parametersFromModel(BatchParameter<?>... parameters) {
		
		JobParametersBuilder builder = new JobParametersBuilder();
		if (parameters != null) {
			for (BatchParameter<?> parameter : parameters) {
				if (parameter instanceof BatchParameter.StringParameter) {
					String value = ((BatchParameter.StringParameter) parameter).getValue();
					builder.addString(parameter.getName(), value, parameter.isIdentifier());
					
				} else if (parameter instanceof BatchParameter.DateParameter) {
					Date value = ((BatchParameter.DateParameter) parameter).getValue();
					builder.addDate(parameter.getName(), value, parameter.isIdentifier());
					
				} else if (parameter instanceof BatchParameter.LongParameter) {
					Long value = ((BatchParameter.LongParameter) parameter).getValue();
					builder.addLong(parameter.getName(), value, parameter.isIdentifier());
	
				} else if (parameter instanceof BatchParameter.DoubleParameter) {
					Double value = ((BatchParameter.DoubleParameter) parameter).getValue();
					builder.addDouble(parameter.getName(), value, parameter.isIdentifier());
	
				} else if (parameter instanceof BatchParameter.BooleanParameter) {
					String value = BooleanUtils.toStringTrueFalse(((BatchParameter.BooleanParameter) parameter).getValue());
					builder.addString(parameter.getName(), value, parameter.isIdentifier());
				}
			}
		}
		return builder.toJobParameters();
	}

	/**
	 * Convenience method to convert Spring Batch JobParameters to a List of BatchParameters 
	 * @param parameters JobParameters
	 * @return List of BatchParameter
	 */
	private List<BatchParameter<?>> parametersToModel(JobParameters parameters) {
		
		if ((parameters == null) || parameters.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<BatchParameter<?>> list = new ArrayList<BatchParameter<?>>();
		for (Entry<String, JobParameter> entry : parameters.getParameters().entrySet()) {
			list.add(this.parameterToModel(entry.getKey(), entry.getValue()));
		}
		return list;
	}
	
	/**
	 * Convenience method to convert a Spring Batch JobParameter to a BatchParameters 
	 * @param name String
	 * @param parameter JobParameter
	 * @return BatchParameter
	 */
	private BatchParameter<?> parameterToModel(String name, JobParameter parameter) {
		
		if (parameter == null) {
			return null;
		}
		
		switch (parameter.getType()) {
		case STRING:
			return new BatchParameter.StringParameter(name, (String) parameter.getValue(), parameter.isIdentifying());
			
		case DATE:
			return new BatchParameter.DateParameter(name, (Date) parameter.getValue(), parameter.isIdentifying());
			
		case LONG:
			return new BatchParameter.LongParameter(name, (Long) parameter.getValue(), parameter.isIdentifying());
			
		case DOUBLE:
			return new BatchParameter.DoubleParameter(name, (Double) parameter.getValue(), parameter.isIdentifying());
			
		default:
			throw new IllegalStateException("Should never get here"); // NOSONAR
		}
	}
}
