/**
 * 
 */
package cloud.altemista.fwk.core.performance.impl;

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
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import cloud.altemista.fwk.core.performance.MeasuresStorage;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;

/**
 * Simple storage policy for keeping the longest measured tasks
 * @author NTT DATA
 */
public class LongestMeasuresStorageImpl implements MeasuresStorage {

	/** The default value for the maximum number of measured tasks in this container */
	private static final int DEFAULT_REGISTRY_MAX_SIZE = 100;
	
	/** The maximum number of measured tasks in this container */
	private int maxSize = DEFAULT_REGISTRY_MAX_SIZE;

	/** The actual container of measured tasks */
	private final NavigableSet<MeasuredTask> registry = new TreeSet<MeasuredTask>(MeasuredTask.COMPARATOR);

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.MeasuresStorage#put(cloud.altemista.fwk.common.performance.model.MeasuredTask)
	 */
	@Override
	public void put(MeasuredTask task) {
		
		// Puts the measured task in the registry
		synchronized (this.registry) {
			this.registry.add(task);
			if (this.registry.size() > maxSize) {
				this.registry.pollLast();
			}
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.TimingPeriodStorage#get()
	 */
	@Override
	public List<MeasuredTask> get() {
		
		// Returns a copy of the registry
		synchronized (this.registry) {
			return new ArrayList<MeasuredTask>(this.registry);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.TimingPeriodStorage#clear()
	 */
	@Override
	public void clear() {
		
		synchronized (this.registry) {
			this.registry.clear();
		}
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
