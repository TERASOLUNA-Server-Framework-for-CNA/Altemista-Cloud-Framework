
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.ReflectionUtils;
import cloud.altemista.fwk.core.performance.MeasuredTaskHolder;
import cloud.altemista.fwk.core.performance.MeasuresStorage;
import cloud.altemista.fwk.core.performance.exception.PerformanceException;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;
import cloud.altemista.fwk.core.performance.model.MethodExecutionTaskInfo;
import cloud.altemista.fwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Invocation handler over PreparedStatement.
 * Will feed values of a JdbcStatistics object based on the methods called.
 * @author NTT DATA
 * @see cloud.altemista.fwk.performance.jdbc.proxy.StatementHandler
 * @see cloud.altemista.fwk.performance.jdbc.proxy.CallableStatementHandler
 * @see cloud.altemista.fwk.performance.jdbc.proxy.ResultSetHandler
 */
// (avoid warning due CallableStatementHandler)
@SuppressWarnings("javadoc")
public final class PreparedStatementHandler implements InvocationHandler {

	/** PreparedStatement setter methods names */
	private static final Set<String> PREPARED_STATEMENT_SETTERS = new HashSet<String>();
	static {
		try {
			// Avoids problems with changes in ResultSet between Java versions
			for (Method m : PreparedStatement.class.getMethods()) {
				if (m.getName().startsWith("set")
						&& (m.getParameterTypes().length >= 2)
						&& (m.getParameterTypes()[0] == int.class)) {
					PREPARED_STATEMENT_SETTERS.add(m.getName());
				}
			}
			
		} catch (Exception ignored) { // NOSONAR
			// (ignored)
		}
	}

	/** The target PreparedStatement to handle */
	private PreparedStatement target;

	/** The holder class for the handlers */
	private MeasuredTaskHolder holder;
	
	/** The storage to use for registering JDBC statistics */
	private MeasuresStorage storage;

	/**
	 * Constructor
	 * @param target the PreparedStatement to handle
	 */
	private PreparedStatementHandler(PreparedStatement target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new PreparedStatementHandler over an existing PreparedStatement
	 * @param target the PreparedStatement to handle
	 * @param holder the holder class for the handlers
	 * @param storage the storage to use for registering measured tasks
	 * @return the proxy instance of the PreparedStatement, wrapped by the StatementHandler
	 */
	public static PreparedStatement handle(
			PreparedStatement target, MeasuredTaskHolder holder, MeasuresStorage storage) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new PreparedStatementHandler
		PreparedStatementHandler handler = new PreparedStatementHandler(target);
		handler.holder = holder;
		handler.storage = storage;
		
		// Returns the proxied PreparedStatement
		return (PreparedStatement) Proxy.newProxyInstance(
				PreparedStatementHandler.class.getClassLoader(), new Class<?>[]{ PreparedStatement.class }, handler);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object object, Method method, Object[] args) throws Throwable { // NOSONAR

		final String methodName = method.getName();
		final boolean isClearParameters = "clearParameters".equals(methodName);
		final boolean isClose = "close".equals(methodName);
		final boolean isExecuteQuery = "executeQuery".equals(methodName);
		final boolean isGetResultSet = "getResultSet".equals(methodName);
		final boolean isSetNull = "setNull".equals(methodName);
		final boolean isAnySetter = PREPARED_STATEMENT_SETTERS.contains(methodName);

		// If is the clear parameters method, clears the stored parameters
		if (isClearParameters) {
			this.getCurrentTaskInfo().clearSqlParameters();
			
		// If the method is any setter, stores the parameter
		} else if (isSetNull) {
			this.getCurrentTaskInfo().setSqlParameter((Integer) args[0], null);
			
		} else if (isAnySetter) {
			this.getCurrentTaskInfo().setSqlParameter((Integer) args[0], args[1]);
		}
		
		try {
			// Invokes the target method
			Object result = method.invoke(target, args);
			
			// Wraps the result
			if ((isExecuteQuery || isGetResultSet) && (result instanceof ResultSet)) {
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
