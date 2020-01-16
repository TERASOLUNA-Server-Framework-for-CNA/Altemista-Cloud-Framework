
package org.altemista.cloudfwk.core.async.impl;

/*
 * #%L
 * altemista-cloud asynchronous and scheduled executions
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.async.AsyncStoragePolicy;
import org.altemista.cloudfwk.core.async.model.FutureWrapper;

/**
 * In-memory map based storage policy implementation.
 *
 * @author NTT DATA
 * @param <T> actual execution result type
 */
public class MemoryStoragePolicy<T> implements AsyncStoragePolicy<T>, InitializingBean {

	/**  The SLF4J Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryStoragePolicy.class);

	/**  Default maximum time that a completed tasks is to be kept: 60 seconds (1 minute). */
	private static final long COMPLETED_TASKS_DEFAULT_MAX_TIME_LAST_CHECK = 60;
	
	/**  Default maximum time that a pending tasks is to be kept: 300 seconds (5 minutes). */
	private static final long PENDING_TASKS_DEFAULT_MAX_TIME_LAST_CHECK = 300;

	/**  In-memory map that actually stores the futures. */
	private Map<String, FutureWrapper<T>> storage = new HashMap<String, FutureWrapper<T>>();
	
	/**  Maximum time that a completed tasks is to be kept (in seconds). */
	private long completedTasksMaxTimeLastCheck = COMPLETED_TASKS_DEFAULT_MAX_TIME_LAST_CHECK;
	
	/**  Maximum time that a pending tasks is to be kept (in seconds). */
	private long pendingTasksMaxTimeLastCheck = PENDING_TASKS_DEFAULT_MAX_TIME_LAST_CHECK;
	
	/**
	 * Gets the completed tasks max time last check.
	 *
	 * @return the completed tasks max time last check
	 */
	public long getCompletedTasksMaxTimeLastCheck() {
		return completedTasksMaxTimeLastCheck;
	}

	/**
	 * Sets the completed tasks max time last check.
	 *
	 * @param completedTasksMaxTimeLastCheck the new completed tasks max time last check
	 */
	public void setCompletedTasksMaxTimeLastCheck(long completedTasksMaxTimeLastCheck) {
		this.completedTasksMaxTimeLastCheck = completedTasksMaxTimeLastCheck;
	}

	/**
	 * Gets the pending tasks max time last check.
	 *
	 * @return the pending tasks max time last check
	 */
	public long getPendingTasksMaxTimeLastCheck() {
		return pendingTasksMaxTimeLastCheck;
	}

	/**
	 * Sets the pending tasks max time last check.
	 *
	 * @param pendingTasksMaxTimeLastCheck the new pending tasks max time last check
	 */
	public void setPendingTasksMaxTimeLastCheck(long pendingTasksMaxTimeLastCheck) {
		this.pendingTasksMaxTimeLastCheck = pendingTasksMaxTimeLastCheck;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		Assert.isTrue(this.completedTasksMaxTimeLastCheck >= 0);
		Assert.isTrue(this.pendingTasksMaxTimeLastCheck >= 0);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.async.AsyncStoragePolicy#put(java.util.concurrent.Future)
	 */
	@Override
	public String put(Future<T> future) {
		
		// Uses UUID to generate a random, unique ID
		String id;
		do {
			id = UUID.randomUUID().toString();
		} while (this.containsId(id));
		
		this.put(id, future);
		return id;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.async.AsyncStoragePolicy#put(java.lang.String, java.util.concurrent.Future)
	 */
	@Override
	public void put(String id, Future<T> future) {
		this.storage.put(id, FutureWrapper.from(future));
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.async.AsyncStoragePolicy#containsId(java.lang.String)
	 */
	@Override
	public boolean containsId(String id) {
		return this.storage.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.async.AsyncStoragePolicy#get(java.lang.String)
	 */
	@Override
	public Future<T> get(String id) {
		return this.storage.get(id);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.async.AsyncStoragePolicy#remove(java.lang.String)
	 */
	@Override
	public void remove(String id) {
		this.storage.remove(id);
	}

	/**
	 * Cleans asynchronous execution results from the storage.
	 * To determine if an asynchronous execution results should be removed,
	 * checks the last time the execution was accessed and compares it
	 * with the maximum intervals defined for completed and pending tasks. 
	 */
	@Override
	public void cleanStorage() {
		
		final long currentTime = System.currentTimeMillis();
		final long completedExpirationTime = currentTime
				+ this.completedTasksMaxTimeLastCheck * DateUtils.MILLIS_PER_SECOND;
		final long pendingExpirationTime = currentTime
				+ this.pendingTasksMaxTimeLastCheck * DateUtils.MILLIS_PER_SECOND;
		
		for (Entry<String, FutureWrapper<T>> entry : this.storage.entrySet()) {
			String id = entry.getKey();
			FutureWrapper<T> future = entry.getValue();
			
			if (future.isDone()) {
				// Completed task
				if (future.getLastCheck() > completedExpirationTime) {
					LOGGER.debug(
							"Completed task with ID {} removed. Not accessed in last {} minutes",
							id, this.completedTasksMaxTimeLastCheck);
					this.remove(id);
				}
				
			} else {
				// Pending task
				if (future.getLastCheck() > pendingExpirationTime) {
					LOGGER.info(
							"Pending task with ID {} removed. Not accessed in last {} minutes",
							id, this.pendingTasksMaxTimeLastCheck);
					this.remove(id);
				}
			}
		}
	}


}
