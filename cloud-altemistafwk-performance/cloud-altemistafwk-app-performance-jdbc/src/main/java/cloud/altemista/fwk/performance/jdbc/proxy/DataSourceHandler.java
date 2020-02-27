/**
 * 
 */
package cloud.altemista.fwk.performance.jdbc.proxy;

/*
 * #%L
 * altemista-cloud performance: JDBC monitoring and performance
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.util.ReflectionUtils;
import cloud.altemista.fwk.core.performance.MeasuredTaskHolder;
import cloud.altemista.fwk.core.performance.MeasuresStorage;

/**
 * Invocation handler over DataSource.
 * Invocations to getConnection() methods will return proxied versions of Connection.
 * @author NTT DATA
 * @see cloud.altemista.fwk.performance.jdbc.proxy.ConnectionHandler
 */
public final class DataSourceHandler implements InvocationHandler {
	
	/** The original DataSource that will be wrapped */
	private DataSource target;

	/** The holder class for the handlers */
	private MeasuredTaskHolder holder;
	
	/** The storage policy for registering measured tasks */
	private MeasuresStorage storage;
	
	/** The optional pattern to match the execution point */ 
	private String executionTracePattern;

	/**
	 * Constructor
	 * @param target the DataSource to handle
	 */
	private DataSourceHandler(DataSource target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new DataSourceHandler over an existing DataSource
	 * @param target the DataSource to handle
	 * @param holder the holder class for the handlers
	 * @param storage the storage to use for registering measured tasks
	 * @param executionTracePattern pattern to match the execution point (optional) 
	 * @return the proxy instance of the DataSource, wrapped by the DataSourceHandler
	 */
	public static DataSource handle(DataSource target,
			MeasuredTaskHolder holder, MeasuresStorage storage, String executionTracePattern) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new DataSourceHandler
		DataSourceHandler handler = new DataSourceHandler(target);
		handler.holder = holder;
		handler.storage = storage;
		handler.executionTracePattern = executionTracePattern;
		
		// Returns the proxied Connection
		return (DataSource) Proxy.newProxyInstance(
				DataSourceHandler.class.getClassLoader(), new Class<?>[]{ DataSource.class }, handler);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { // NOSONAR
		
		final String methodName = method.getName();
		final boolean isGetConnection = "getConnection".equals(methodName);

		// Invokes the target method
		Object result = null;
		try {
			result = method.invoke(target, args);
			
		} catch (InvocationTargetException e) {
			ReflectionUtils.handleInvocationTargetException(e);
		}

		// Wraps the result
		if (isGetConnection && (result instanceof Connection)) {
			result = ConnectionHandler.handle((Connection) result,
					this.holder, this.storage, this.executionTracePattern);
		}

		return result;
	}

}
