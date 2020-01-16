/**
 * 
 */
package org.altemista.cloudfwk.core.batch;

/*
 * #%L
 * altemista-cloud batch
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import javax.batch.runtime.JobExecution;

import org.altemista.cloudfwk.core.batch.model.BatchParameter;

/**
 * Main interface for the batch module.
 * @author NTT DATA
 */
public interface BatchService {

	/**
	 * Count the number of currently registered job names
	 * @return the number of currently registered job names
	 */
	int countJobs();
	
	/**
	 * List a page of currently registered job names
	 * @param start the start index of the instances to return
	 * @param count the maximum number of instances to return
	 * @return the registered job names up to a maximum of count values
	 */
	List<String> listJobs(int start, int count);
	
	/**
	 * Convenience method to determine if a job is available for launching
	 * @param jobName the name of the job
	 * @return true if the job is available for launching
	 */
	boolean isLaunchable(String jobName);
	
	/**
	 * Launch a job with the parameters provided.
	 * If an instance with the parameters provided has already failed (and is not abandoned) it will be restarted.
	 * @param jobName the name of the job
	 * @param parameters the BatchParameter
	 * @return the resulting execution ID if successful
	 */
	Long launch(String jobName, BatchParameter<?>... parameters);
	
	/**
	 * Convenience method to determine if a job is available for launching as a sequence of instances.
	 * @param jobName the name of the job
	 * @return true if the job is available for launching as a sequence of instances
	 */
	boolean isLaunchableNextInstance(String jobName);
	
	/**
	 * Launch the next in a sequence of instances. 
	 * @param jobName the name of the job
	 * @return the resulting execution ID if successful
	 */
	Long launchNextInstance(String jobName);
	
	//
	
	/**
	 * Count the number of job instances in the repository for a given job name.
	 * @param jobName the name of the job
	 * @return the number of currently registered job instances
	 */
	int countJobInstances(String jobName);
	
	/**
	 * List a page of job instances for a given job name.
	 * @param jobName the name of the job
	 * @param start the start index of the instances to return
	 * @param count the maximum number of instances to return
	 * @return the registered job instances up to a maximum of count values
	 */
	List<Long> listJobInstances(String jobName, int start, int count);
	
	//
	
	/**
	 * List the job executions associated with a particular job instance.
	 * @param instanceId the id of the job instance
	 * @return the job executions associated with the job instance
	 */
	List<JobExecution> getExecutions(long instanceId);
	
	/**
	 * List the running job executions for a given job name.
	 * @param jobname the name of the job
	 * @return the running job executions for the given job name
	 */
	List<JobExecution> getRunningExecutions(String jobname);
	
	/**
	 * Get the job parameters used by a particular job execution
	 * @param executionId the id of the job execution
	 * @return the job parameters used by the job execution
	 */
	List<BatchParameter<?>> getParameters(long executionId); // NOSONAR
	
	/**
	 * Send a stop signal to a running job execution
	 * @param executionId the id of the job execution
	 * @return true if the message was successfully sent;
	 * false otherwise, or if the job execution did not exist or was not running
	 */
	boolean stop(long executionId);
	
	/**
	 * Restarts a stopped job execution.
	 * @param executionId the if of the job execution
	 * @return the resulting execution ID if successful;
	 * false otherwise, or if the job execution did not exist or was not stopped
	 */
	Long restart(long executionId);
	
	/**
	 * Marks the job execution as abandoned.
	 * @param executionId the if of the job execution
	 * @return the job execution that was aborted,
	 * or null if the job execution was not stopped first
	 */
	JobExecution abandon(long executionId);

}
