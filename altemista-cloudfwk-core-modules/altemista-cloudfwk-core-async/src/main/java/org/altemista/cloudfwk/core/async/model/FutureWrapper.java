package org.altemista.cloudfwk.core.async.model;

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


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Adds a time stamp to a Future to keep track of when this object is accessed.
 * Internally used by {@link org.altemista.cloudfwk.core.async.impl.MemoryStoragePolicy}
 * @param <V>
 * @author NTT DATA
 */
public final class FutureWrapper<V> implements Future<V> {
	
	/** The wrapped future */
	private Future<V> future;
	
	/** The moment the wrapped future was last accessed */
	private long lastCheck;
	
	/**
	 * Private constructor.
	 * @param future The future to be wrapped
	 */
	private FutureWrapper(Future<V> future) {
		super();
		
		this.future = future;
		this.touch();
	}
	
	/**
	 * Creates a new wrapper over a future
	 * @param future The future to be wrapped, not null
	 * @return The wrapped future
	 */
	public static <W> FutureWrapper<W> from(Future<W> future) {
		if (future == null) {
			throw new IllegalArgumentException("the future is null");
		}
		
		// Highly unlikely, but avoid to wrap again an already wrapped future
		if (future instanceof FutureWrapper) {
			return (FutureWrapper<W>) future;
		}
		
		return new FutureWrapper<W>(future);
	}

	/**
	 * Updates the last access to the wrapped future
	 */
	private void touch() {
		this.lastCheck = System.currentTimeMillis();
	}

	/**
	 * @return the lastCheck
	 */
	public long getLastCheck() {
		return lastCheck;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		this.touch();
		return this.future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return this.future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return this.future.isDone();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		this.touch();
		return this.future.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		this.touch();
		return this.future.get(timeout, unit);
	}

}
