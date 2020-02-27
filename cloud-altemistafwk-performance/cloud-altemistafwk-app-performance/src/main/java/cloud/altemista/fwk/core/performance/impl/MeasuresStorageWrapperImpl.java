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


import java.util.Collections;
import java.util.List;

import cloud.altemista.fwk.core.performance.MeasuresStorage;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;

/**
 * Base class for implementing wrappers around storage policies
 * that provide common optional functionality (e.g.: logging)
 * This class does not modify the behavior of the wrapped class.
 * @author NTT DATA
 */
public abstract class MeasuresStorageWrapperImpl implements MeasuresStorage {
	
	/** The original MeasuresStorage that will be wrapped */
	private MeasuresStorage target;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.MeasuresStorage#put(cloud.altemista.fwk.common.performance.model.MeasuredTask)
	 */
	@Override
	public void put(MeasuredTask task) {
		
		if (this.target != null) {
			this.target.put(task);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.MeasuresStorage#get()
	 */
	@Override
	public List<MeasuredTask> get() {
		
		return (this.target == null)
				? Collections.<MeasuredTask> emptyList()
				: this.target.get();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.MeasuresStorage#clear()
	 */
	@Override
	public void clear() {
		
		if (this.target != null) {
			this.target.clear();
		}
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(MeasuresStorage target) {
		this.target = target;
	}

}
