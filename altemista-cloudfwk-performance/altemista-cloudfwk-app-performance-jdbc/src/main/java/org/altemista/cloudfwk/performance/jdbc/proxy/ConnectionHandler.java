
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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.util.ReflectionUtils;
import org.altemista.cloudfwk.common.util.MatchUtil;
import org.altemista.cloudfwk.common.util.ToStringUtil;
import org.altemista.cloudfwk.core.performance.MeasuredTaskHolder;
import org.altemista.cloudfwk.core.performance.MeasuresStorage;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.core.performance.model.MethodExecutionTaskInfo;
import org.altemista.cloudfwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Invocation handler over Connection.
 * Invocations to createStatement(), prepareStatement() and prepareCall() methods
 * will return proxied versions of Statement, PreparedStatement and CallableStatement.
 * @author NTT DATA
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.StatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.PreparedStatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.CallableStatementHandler
 */
// (avoid warning due CallableStatementHandler)
@SuppressWarnings("javadoc")
public final class ConnectionHandler implements InvocationHandler {

	/** The target Connection to handle */
	private Connection target;

	/** The holder class for the handlers */
	private MeasuredTaskHolder holder;
	
	/** The storage policy for registering measured tasks */
	private MeasuresStorage storage;
	
	/** The pattern to match the execution point */
	private String executionTracePattern;

	/**
	 * Constructor
	 * @param target the Connection to handle
	 */
	private ConnectionHandler(Connection target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new ConnectionHandler over an existing Connection
	 * @param target the Connection to handle
	 * @param holder the holder class for the handlers
	 * @param storage the storage to use for registering measured tasks
	 * @param executionTracePattern pattern to match the execution point (optional) 
	 * @return the proxy instance of the Connection, wrapped by the ConnectionHandler
	 */
	public static Connection handle(Connection target,
			MeasuredTaskHolder holder, MeasuresStorage storage, String executionTracePattern) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new ConnectionHandler
		ConnectionHandler handler = new ConnectionHandler(target);
		handler.holder = holder;
		handler.storage = storage;
		handler.executionTracePattern = executionTracePattern;
		
		// Returns the proxied Connection
		return (Connection) Proxy.newProxyInstance(
				ConnectionHandler.class.getClassLoader(), new Class<?>[]{ Connection.class }, handler);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	// CallableStatementHandler currently marked as deprecated
	@SuppressWarnings({ "deprecation" })
	public Object invoke(Object object, Method method, Object[] args) throws Throwable { // NOSONAR
		
		final String methodName = method.getName();
		final boolean isCreateStatement = "createStatement".equals(methodName);
		final boolean isPrepareStatement = "prepareStatement".equals(methodName);
		final boolean isCallableStatement = "prepareCall".equals(methodName);
		final boolean isClose = "close".equals(methodName);
		
		// Creates the initial task information
		if (isCreateStatement || isPrepareStatement || isCallableStatement) {
			this.holder.start(this.generateTaskInfo(method, args));
		}

		// Invokes the target method
		Object result = null;
		try {
			result = method.invoke(target, args);
			
			// Wraps the result
			if (isCreateStatement && (result instanceof Statement)) {
				result = StatementHandler.handle((Statement) result, this.holder, this.storage);
				
			} else if (isPrepareStatement && (result instanceof PreparedStatement)) {
				result = PreparedStatementHandler.handle((PreparedStatement) result, this.holder, this.storage);
	
			} else if (isCallableStatement && (result instanceof CallableStatement)) {
				result = CallableStatementHandler.handle((CallableStatement) result, this.holder, this.storage);
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
	 * Creates the initial task information
	 * @param method Method
	 * @param args Object[]
	 * @return TaskInfo
	 */
	private JdbcTaskInfo generateTaskInfo(Method method, Object[] args) {
		
		JdbcTaskInfo taskInfo = new JdbcTaskInfo();
		
		final String methodName = method.getName();
		final boolean isPrepareStatement = "prepareStatement".equals(methodName);
		final boolean isCallableStatement = "prepareCall".equals(methodName);
		
		// Stack trace element where the Statement was executed
		taskInfo.setSignature(ToStringUtil.toString(MatchUtil.stackTraceElementMatching(this.executionTracePattern)));
		
		// Arguments (if possible)
		if (isPrepareStatement || isCallableStatement) {
			taskInfo.setSqlQuery((String) args[0]);
		}
		
		return taskInfo;
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
		if ((task != null) && task.isRunning()) {
			this.storage.put(this.holder.stop());
		}
	}
}
