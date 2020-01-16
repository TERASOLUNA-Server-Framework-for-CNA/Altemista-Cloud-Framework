
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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.ReflectionUtils;
import org.altemista.cloudfwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Invocation handler over ResultSet.
 * Will feed values of a JdbcStatistics object based on the methods called.
 * @author NTT DATA
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.StatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.PreparedStatementHandler
 * @see org.altemista.cloudfwk.performance.jdbc.proxy.CallableStatementHandler
 */
// (avoid warning due CallableStatementHandler)
@SuppressWarnings("javadoc")
public final class ResultSetHandler implements InvocationHandler {

	/** ResultSet getter methods names */
	private static final Set<String> RESULT_SET_GETTERS = new HashSet<String>();
	static {
		try {
			// Avoids problems with changes in ResultSet between Java versions
			for (Method m : ResultSet.class.getMethods()) {
				if (m.getName().startsWith("get")
						&& (m.getParameterTypes().length > 0)
						&& ((m.getParameterTypes()[0] == String.class) || (m.getParameterTypes()[0] == int.class))) {
					RESULT_SET_GETTERS.add(m.getName());
				}
			}
			
		} catch (Exception ignored) { // NOSONAR
			// (ignored)
		}
	}

	/** The target ResultSet to handle */
	private ResultSet target;

	/** The task information started for this ResultSet */
	private JdbcTaskInfo taskInfo;

	/**
	 * Constructor
	 * @param target the ResultSet to handle
	 */
	public ResultSetHandler(ResultSet target) {
		super();
		this.target = target;
	}
	
	/**
	 * Convenience method to create a new ResultSetHandler over an existing ResultSet
	 * @param target the ResultSet to handle
	 * @param taskInfo the task information started by the Statement that created this ResultSet
	 * @return the proxy instance of the ResultSet, wrapped by the ResultSetHandler
	 */
	public static ResultSet handle(ResultSet target, JdbcTaskInfo taskInfo) {
		
		// (sanity check)
		if (target == null) {
			return null;
		}

		// Creates a new ResultSetHandler
		ResultSetHandler handler = new ResultSetHandler(target);
		handler.taskInfo = taskInfo;
		
		// Returns the proxied ResultSet
		return (ResultSet) Proxy.newProxyInstance(
				ResultSetHandler.class.getClassLoader(), new Class<?>[]{ ResultSet.class }, handler);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object object, Method method, Object[] args) throws Throwable { // NOSONAR

		final String methodName = method.getName();
		final boolean isNext = "next".equals(methodName);
		final boolean isAnyGetter = RESULT_SET_GETTERS.contains(methodName);

		// If the method is any getter, increases the field count
		if (isAnyGetter) {
			this.taskInfo.incFieldCount();
		}
		
		try {
			// Invokes the target method
			Object result = method.invoke(target, args);
			
			// If the method is next, increases the result count
			// (but only increases the result count if there is a result)
			if (isNext && BooleanUtils.isTrue((Boolean) result)) {
				this.taskInfo.incResultCount();
			}
			
			return result;
			
		} catch (InvocationTargetException e) {
			// Handles the InvocationTargetException and stores the underlying exception
			this.saveTargetExceptionAndHandleInvocationTargetException(e);
			throw new IllegalStateException("Should never get here", e);
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
			this.taskInfo.setThrownException(e.toString());
			throw e;
		}
	}
}
