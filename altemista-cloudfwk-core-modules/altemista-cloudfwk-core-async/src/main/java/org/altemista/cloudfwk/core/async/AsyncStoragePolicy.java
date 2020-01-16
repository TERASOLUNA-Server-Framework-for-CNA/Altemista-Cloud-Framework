
package org.altemista.cloudfwk.core.async;

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


import java.util.concurrent.Future;


/**
 * Define storage policies for registering asynchronous executions.
 * @param <T> actual execution result type
 * @author NTT DATA
 */
public interface AsyncStoragePolicy<T> {
	
	/**
	 * Puts an asynchronous execution result (a future) in the storage.
	 * @param future the Future to store
	 * @return id String with the unique id assigned to this execution
	 */
	String put(Future<T> future);
	
	/**
	 * Puts an asynchronous execution result (a future) in the storage with an specific id
	 * @param id String with an unique id assigned to this execution
	 * @param future the Future to store
	 */
	void put(String id, Future<T> future);
	
	/**
	 * Checks if there is an asynchronous execution result with the specified id in the storage
	 * @param id String with the unique id assigned to the execution
	 * @return boolean
	 */
	boolean containsId(String id);
	
	/**
	 * Gets the execution result with the specified id from the storage
	 * @param id String with the unique id assigned to the execution
	 * @return asynchronous execution result 
	 */
	Future<T> get(String id);
	
	/**
	 * Removes the execution result with the specified id from the storage
	 * @param id String with the unique id assigned to the execution
	 */
	void remove(String id);
	
	/**
	 * Cleans asynchronous execution results from the storage.
	 * Determines if an asynchronous execution results should be removed.
	 */
	void cleanStorage();
}
