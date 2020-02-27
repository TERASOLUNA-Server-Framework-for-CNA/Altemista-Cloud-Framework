
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
import java.sql.CallableStatement;
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
 * Invocation handler over CallableStatement.
 * Will feed values of a JdbcStatistics object based on the methods called.
 * @author NTT DATA
 * @see cloud.altemista.fwk.performance.jdbc.proxy.StatementHandler
 * @see cloud.altemista.fwk.performance.jdbc.proxy.PreparedStatementHandler
 * @see cloud.altemista.fwk.performance.jdbc.proxy.ResultSetHandler
 * @deprecated This is not handling property setters with named parameters neither output parameters 
 */
@Deprecated
public final class CallableStatementHandler implements InvocationHandler {

	/** CallableStatement setter methods names */
	private static final Set<String> CALLABLE_STATEMENT_SETTERS = new HashSet<String>();
	static {
		try {
			// Avoids problems with changes in ResultSet between Java versions
			for (Method m : CallableStatement.class.getMethods()) {
				if (m.getName().startsWith("set")
						&& (m.getParameterTypes().length >= 2)
						&& (m.getParameterTypes()[0] == int.class)) {
					CALLABLE_STATEMENT_SETTERS.add(m.getName());
				}
			}
			
		} catch (Exception ignored) { // NOSONAR
			// (ignored)
		}
	}

	/** The target CallableStatement to handle */
	private CallableStatement target;

	/** The holder class for the handlers */
	private MeasuredTaskHolder holder;
	
	/** The storage to use for registering JDBC statistics */
	private MeasuresStorage storage;

	/**
	 * Constructor
	 * @param target the CallableStatement to handle
	 */
	private CallableStatementHandler(CallableStatement target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new CallableStatementHandler over an existing CallableStatement
	 * @param target the CallableStatement to handle
	 * @param holder the holder class for the handlers
	 * @param storage the storage to use for registering measured tasks
	 * @return the proxy instance of the CallableStatement, wrapped by the CallableStatementHandler
	 */
	public static CallableStatement handle(
			CallableStatement target, MeasuredTaskHolder holder, MeasuresStorage storage) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new CallableStatementHandler
		CallableStatementHandler handler = new CallableStatementHandler(target);
		handler.holder = holder;
		handler.storage = storage;
		
		// Returns the proxied CallableStatement
		return (CallableStatement) Proxy.newProxyInstance(
				CallableStatementHandler.class.getClassLoader(), new Class<?>[]{ CallableStatement.class }, handler);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object object, Method method, Object[] args) throws Throwable { // NOSONAR

		final String methodName = method.getName();
		final boolean isClearParameters = "clearParameters".equals(methodName);
		final boolean isClose = "close".equals(methodName);
//		final boolean isGetObject = methodName.equals("getObject")
//		final boolean isGetResultSet = methodName.equals("getResultSet")
//		final boolean isRegisterOutParameter = methodName.equals("registerOutParameter")
		final boolean isSetNull = "setNull".equals(methodName);
		final boolean isAnySetter = CALLABLE_STATEMENT_SETTERS.contains(methodName);

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
			return method.invoke(target, args);
			
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
