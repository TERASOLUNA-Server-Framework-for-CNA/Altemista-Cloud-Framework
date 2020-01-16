
package org.altemista.cloudfwk.performance.jdbc.proxy;

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
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.util.ReflectionUtils;
import org.altemista.cloudfwk.core.performance.MeasuredTaskHolder;
import org.altemista.cloudfwk.core.performance.MeasuresStorage;
import org.altemista.cloudfwk.core.performance.exception.PerformanceException;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.core.performance.model.MethodExecutionTaskInfo;
import org.altemista.cloudfwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Invocation handler over Statement.
 * Will feed values of a JdbcStatistics object based on the methods called.
 * @author NTT DATA
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.PreparedStatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.CallableStatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.ResultSetHandler
 */
// (avoid warning due CallableStatementHandler)
@SuppressWarnings("javadoc")
public final class StatementHandler implements InvocationHandler {
	/*
	 * TODO Improve the support for addBatch/clearBatch/executeBatch
	 */

	/** The target Statement to handle */
	private Statement target;

	/** The holder class for the handlers */
	private MeasuredTaskHolder holder;
	
	/** The storage to use for registering JDBC statistics */
	private MeasuresStorage storage;
	
	/**
	 * Constructor
	 * @param target the Statement to handle
	 */
	private StatementHandler(Statement target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new StatementHandler over an existing Statement
	 * @param target the Statement to handle
	 * @param holder the holder class for the handlers
	 * @param storage the storage to use for registering measured tasks
	 * @return the proxy instance of the Statement, wrapped by the StatementHandler
	 */
	public static Statement handle(Statement target, MeasuredTaskHolder holder, MeasuresStorage storage) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new StatementHandler
		StatementHandler handler = new StatementHandler(target);
		handler.holder = holder;
		handler.storage = storage;
		
		// Returns the proxied Statement
		return (Statement) Proxy.newProxyInstance(
				StatementHandler.class.getClassLoader(), new Class<?>[]{ Statement.class }, handler);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object object, Method method, Object[] args) throws Throwable { // NOSONAR

		final String methodName = method.getName();
		final boolean isAddBatch = "addBatch".equals(methodName);
		final boolean isExecute = "execute".equals(methodName);
		final boolean isExecuteQuery = "executeQuery".equals(methodName);
		final boolean isExecuteUpdate = "executeUpdate".equals(methodName);
		final boolean isGetResultSet = "getResultSet".equals(methodName);
		final boolean isClose = "close".equals(methodName);
		
		// Starts a task in the holder class (that will take care of nesting)
		if (isAddBatch || isExecute || isExecuteQuery || isExecuteUpdate) {
			this.getCurrentTaskInfo().setSqlQuery((String) args[0]);
		}
		
		try {
			// Invokes the target method
			Object result = method.invoke(target, args);
	
			// Wraps the result
			if ((isExecuteQuery || isGetResultSet) && (result instanceof ResultSet)) {
				this.getCurrentTaskInfo().markResultSetAccessed();
				result = ResultSetHandler.handle((ResultSet) result, this.getCurrentTaskInfo());
			}
			
			return result;
			
		} catch (InvocationTargetException e) {
			// Handles the InvocationTargetException and stores the underlying exception
			this.saveTargetExceptionAndHandleInvocationTargetException(e);
			throw new IllegalStateException("Should never get here", e);
			
		} finally {
			// If is the close method, stops the task and puts it in the storage
			if (isClose) {
				this.close();
			}
		}
	}
	
	/**
	 * Handles the InvocationTargetException and stores the underlying exception
	 * @param ite InvocationTargetException
	 */
	private void saveTargetExceptionAndHandleInvocationTargetException(InvocationTargetException ite) {
		
		try {
			// Handles the InvocationTargetException
			ReflectionUtils.handleInvocationTargetException(ite);
			throw new IllegalStateException("Should never get here");
			
		} catch (RuntimeException e) {
			// Stores the underlying exception
			MeasuredTask current = this.holder.getCurrent();
			if ((current != null) && (current.getTaskInfo() instanceof MethodExecutionTaskInfo)) {
				((MethodExecutionTaskInfo) current.getTaskInfo()).setThrownException(e.toString());
			}
			throw e;
		}
	}
	
	/**
	 * Closes the task if it was still running
	 */
	private void close() {
		
		MeasuredTask task = this.holder.getCurrent();
		if (task.isRunning()) {
			this.storage.put(this.holder.stop());
		}
	}
	
	/**
	 * Returns the currently active JdbcTaskInfo
	 * @return JdbcTaskInfo
	 * @throws PerformanceException if could not retrieve the current active task information
	 */
	private JdbcTaskInfo getCurrentTaskInfo() {
		
		MeasuredTask current = this.holder.getCurrent();
		if ((current == null) || (!(current.getTaskInfo() instanceof JdbcTaskInfo))) {
			throw new PerformanceException("unexpected_task_type");
		}
		
		// Arguments (if possible)
		return (JdbcTaskInfo) current.getTaskInfo();
	}
}
