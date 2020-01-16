
package org.altemista.cloudfwk.core.performance.aop;

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


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.altemista.cloudfwk.common.util.ReflectionToStringUtil;
import org.altemista.cloudfwk.common.util.ToStringUtil;
import org.altemista.cloudfwk.core.performance.MeasuredTaskHolder;
import org.altemista.cloudfwk.core.performance.MeasuresStorage;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.core.performance.model.MethodExecutionTaskInfo;

/**
 * Aspect class to gather performance information about method executions
 */
@Aspect
public class PerformanceAspect implements InitializingBean {

	/** The holder class for the aspect */
	private MeasuredTaskHolder holder;
	
	/** The storage policy for registering measured tasks */
	private MeasuresStorage storage;
	
	/** Enables or disables the performance aspect (enabled by default) */
	private boolean enabled = true;
	
	/**
	 * Enables or disables the use of reflection to introspect the objects for obtaining their string representations
	 * (enabled by default)
	 * @see org.altemista.cloudfwk.common.util.ToStringUtil
	 * @see org.altemista.cloudfwk.common.util.ReflectionToStringUtil
	 */
	private boolean reflectionToStringEnabled = true;
	
	/**
	 * If <code>reflectionToStringEnabled</code> is enabled,
	 * the maximum length of the string representations of each nested object;
	 * otherwise, the maximum length of the string representations of objects
	 * (256 characters by default)
	 */
	private int maxLength = 256; // NOSONAR
	
	/** Maximum depth for the introspection of the object (3 levels by default) */
	private int maxDepth = 3; // NOSONAR
	
	/**
	 * Maximum length of the object after which the rest of the nested elements will be omitted
	 * (1024 characters by default)
	 */
	private int stopPrintingAfter = maxLength * (maxDepth + 1);
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.holder, "The holder class for the aspect is required");
		Assert.notNull(this.storage, "The storage policy for registering measured tasks is required");
	}
	
	/**
	 * Actual aspect implementation.
	 * @param pjp the ProceedingJoinPoint
	 * @return the object returned by the target method
	 * @throws Throwable the thrown target exception if any
	 */
	public Object invoke(ProceedingJoinPoint pjp) throws Throwable { // NOSONAR
		
		// If performance is disabled or the holder is not accepting new tasks, just invokes the target method
		if ((!this.enabled) || (!this.holder.isAcceptingTasks())) {
			try {
				return pjp.proceed();
				
			} catch (InvocationTargetException e) {
				// Handles the InvocationTargetException
				ReflectionUtils.handleInvocationTargetException(e);
				throw new IllegalStateException("Should never get here", e);
			}
		}
		
		// Initializes a task information based on the ProceedingJoinPoint
		MethodExecutionTaskInfo taskInfo = this.generateTaskInfo(pjp);
		
		// Starts a task in the holder class (that will take care of nesting)
		this.holder.start(taskInfo);
		
		try {
			// Invokes the target method and stores the return value
			Object ret = pjp.proceed();
			this.saveReturnValue(pjp, taskInfo, ret);
			return ret;
			
		} catch (InvocationTargetException e) {
			// Handles the InvocationTargetException and stores the underlying exception
			this.saveTargetExceptionAndHandleInvocationTargetException(taskInfo, e);
			throw new IllegalStateException("Should never get here", e);
			
		} finally {
			// Stops the task and puts it in the storage
			MeasuredTask measuredTask = this.holder.stop();
			if (!measuredTask.isNestedTask()) {
				this.storage.put(measuredTask);
			}
		}
	}
	
	/**
	 * Creates the initial task information based on the ProceedingJoinPoint
	 * @param pjp ProceedingJoinPoint
	 * @return MethodExecutionTaskInfo
	 */
	private MethodExecutionTaskInfo generateTaskInfo(ProceedingJoinPoint pjp) {
		
		MethodExecutionTaskInfo taskInfo = new MethodExecutionTaskInfo();
		
		// Method signature
		final Signature signature = pjp.getSignature();
		taskInfo.setSignature(signature.toLongString());
		
		// Arguments (if possible)
		if (signature instanceof MethodSignature) {
			final MethodSignature methodSignature = (MethodSignature) signature;
			final Object[] values = pjp.getArgs();
			final Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
			
			for (int i = 0, n = values.length; i < n; i++) {
				taskInfo.addArgument(this.toString(values[i], parameterAnnotations[i]));
			}
		}
		
		return taskInfo;
	}
	
	/**
	 * Stores the return value in the task information
	 * @param pjp ProceedingJoinPoint
	 * @param taskInfo MethodExecutionTaskInfo
	 * @param returnValue Object
	 */
	private void saveReturnValue(ProceedingJoinPoint pjp, MethodExecutionTaskInfo taskInfo, Object returnValue) {

		final Signature signature = pjp.getSignature();
		
		// (should never get here, but leaves the TaskInfo in a consistent state just in case)
		if (!(signature instanceof MethodSignature)) {
			taskInfo.setReturned();
			return;
		}
		
		// Methods that return void
		MethodSignature methodSignature = (MethodSignature) signature;
		if (Void.class.equals(methodSignature.getReturnType())) {
			taskInfo.setReturned();
			return;
		}
		
		// Methods that return a value
		taskInfo.setReturnValue(this.toString(returnValue, methodSignature.getMethod().getAnnotations()));
	}
	
	/**
	 * Handles the InvocationTargetException and stores the underlying exception
	 * @param taskInfo MethodExecutionTaskInfo
	 * @param ite InvocationTargetException
	 */
	private void saveTargetExceptionAndHandleInvocationTargetException(
			MethodExecutionTaskInfo taskInfo, InvocationTargetException ite) {
		
		try {
			// Handles the InvocationTargetException
			ReflectionUtils.handleInvocationTargetException(ite);
			throw new IllegalStateException("Should never get here");
			
		} catch (RuntimeException e) {
			// Stores the underlying exception
			taskInfo.setThrownException(e.toString());
			throw e;
		}
	}
	
	/**
	 * Null-safe, <code>@HiddenValue</code>-aware toString() using the provided values
	 * @param object Object
	 * @param annotations Additional array of Annotations to look for the <code>@HiddenValue</code> annotation
	 * @return string representation of the object, the placeholder literal, or "null"
	 */
	private String toString(Object object, Annotation[] annotations) {
		
		// Uses ReflectionToStringUtil or ToStringUtil depending on the value of reflectionToStringEnabled
		if (this.reflectionToStringEnabled) {
			return ReflectionToStringUtil.toString(object, annotations,
					this.maxLength, this.maxDepth, this.stopPrintingAfter);
			
		} else {
			return ToStringUtil.hiddenAwareToString(object, annotations, this.maxLength);
		}
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the reflectionToStringEnabled
	 */
	public boolean isReflectionToStringEnabled() {
		return reflectionToStringEnabled;
	}

	/**
	 * @param reflectionToStringEnabled the reflectionToStringEnabled to set
	 */
	public void setReflectionToStringEnabled(boolean reflectionToStringEnabled) {
		this.reflectionToStringEnabled = reflectionToStringEnabled;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @param maxDepth the maxDepth to set
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * @return the stopPrintingAfter
	 */
	public int getStopPrintingAfter() {
		return stopPrintingAfter;
	}

	/**
	 * @param stopPrintingAfter the stopPrintingAfter to set
	 */
	public void setStopPrintingAfter(int stopPrintingAfter) {
		this.stopPrintingAfter = stopPrintingAfter;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder(MeasuredTaskHolder holder) {
		this.holder = holder;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(MeasuresStorage storage) {
		this.storage = storage;
	}
}
