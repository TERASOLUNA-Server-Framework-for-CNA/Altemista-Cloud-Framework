/**
 * 
 */
package org.altemista.cloudfwk.core.performance;

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


import java.util.List;

import org.altemista.cloudfwk.core.performance.model.MeasuredTask;

/**
 * Define storage policies for registering measured tasks
 * @author NTT DATA
 */
public interface MeasuresStorage {
	
	/**
	 * Puts a timing period into the container.
	 * @param task the measured task
	 */
	void put(MeasuredTask task);

	/**
	 * Gets the measured tasks that are currently in the container.
	 * Implementations are suggested to (but not required to) return this list already sorted
	 * @return list of measured tasks
	 */
	List<MeasuredTask> get();

	/**
	 * Clears the container, removing all the measured tasks in it
	 */
	void clear();
}
