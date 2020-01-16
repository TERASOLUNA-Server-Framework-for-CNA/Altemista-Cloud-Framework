/**
 * 
 */
package org.altemista.cloudfwk.core.performance.impl;

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


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.performance.exception.PerformanceException;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;

/**
 * Wrapper around storage policy that logs MeasuredTasks when they are being added to the storage.
 * The default values of this class make it suitable for logging execution traces,
 * although the output is customizable to serve any MeasuredTask (supporting nested tasks).
 * @author NTT DATA
 */
public class LogMeasuresStorageWrapperImpl extends MeasuresStorageWrapperImpl implements InitializingBean {

	/** The name of the logger where the performance information will be written */
	private String loggerName = "org.altemista.cloudfwk.PERFORMANCE";
	
	/**
	 * Customizable literal before the actual sentence.
	 * Will receive one parameter: the start time of the task formatted with the <code>startTimePattern</code>
	 * @see LogMeasuresStorageWrapperImpl#startTimePattern
	 */
	private String beforeSentence = "START EXECUTION TRACE:" + SystemUtils.LINE_SEPARATOR
			+ "Operation started on: %s" + SystemUtils.LINE_SEPARATOR;
	
	/**
	 * Patter to format the start time of the task
	 * @see LogMeasuresStorageWrapperImpl#beforeSentence
	 */
	private String startTimePattern = "EEE, dd MMM yyyy HH:mm:ss.SSSS z";
	
	/** Customizable literal after the actual sentence */
	private String afterSentence = "END EXECUTION TRACE";
	
	/** Indentation to apply to each nesting level */
	private String indentation = "    ";
	
	/**
	 * Pattern to format each task. Will receive three String paramters:
	 * <ol>
	 * <li>The duration of the task formatted using <code>totalTimePattern</code>,
	 * or the "running" literal as specified by <code>runningPattern</code></li>
	 * <li>The indentation depending of the nesting level of the task</li>
	 * <li>The task description returned by <code>MeasuredTask.getDescription()</code></li>
	 * </ol>
	 */
	private String taskPattern = "%1$s%2$s|----> %3$s" + SystemUtils.LINE_SEPARATOR;
	
	/** The pattern to format the duration of the task */
	private String totalTimePattern = "%8dms\t";
	
	/** The "running" literal to be used in place of the duration of the task when the task is still running */
	private String runningPattern = "running...\t";
	
	/** The logger Logger */
	private Logger logger;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// Checks the format are not null
		Assert.notNull(this.beforeSentence);
		Assert.notNull(this.startTimePattern);
		Assert.notNull(this.afterSentence);
		Assert.notNull(this.indentation);
		Assert.notNull(this.taskPattern);
		Assert.notNull(this.totalTimePattern);
		Assert.notNull(this.runningPattern);
		
		// Checks the formats are valid before using them
		try {
			String.format(beforeSentence, StringUtils.EMPTY); // NOSONAR
			FastDateFormat.getInstance(this.startTimePattern).format(0L); // NOSONAR
			String.format(taskPattern, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY); // NOSONAR
			String.format(totalTimePattern, 0L); // NOSONAR
			
		} catch (IllegalArgumentException e) {
			throw new PerformanceException("illegal_format", e);
		}
		
		// Obtains the logger
		Assert.notNull(this.loggerName);
		this.logger = LoggerFactory.getLogger(this.loggerName);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.common.performance.impl.MeasuresStorageWrapperImpl#put(org.altemista.cloudfwk.common.performance.model.MeasuredTask)
	 */
	@Override
	public void put(MeasuredTask task) {
		super.put(task);
		
		// If nothing is to be logged, do nothing
		if (!this.logger.isInfoEnabled()) {
			return;
		}
		
		// Starts the sentence
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(beforeSentence,
				FastDateFormat.getInstance(this.startTimePattern).format(task.getStartTimeMillis())));
		
		// Task and nested tasks
		this.append(sb, task, 0);
		
		// Finishes
		sb.append(this.afterSentence);
		this.logger.info(sb.toString());
	}
	
	/**
	 * Appends a task to the trace
	 * @param sb StringBuilder to append the task information to
	 * @param task MeasuredTask to append
	 * @param depth int with the depth of the trace
	 */
	private void append(StringBuilder sb, MeasuredTask task, int depth) {
		
		sb.append(String.format(this.taskPattern,
				
				// Duration of the task
				task.isRunning()
						? this.runningPattern
						: String.format(this.totalTimePattern, task.getTotalTimeMillis()),
					
				// Indentation
				StringUtils.repeat(this.indentation, depth),
				
				// Task description
				task.getDescription()));
		
		// Nested tasks increasing the depth
		for (MeasuredTask nestedTask : task.getNestedTasks()) {
			this.append(sb, nestedTask, depth + 1);
		}
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * @param beforeSentence the beforeSentence to set
	 */
	public void setBeforeSentence(String beforeSentence) {
		this.beforeSentence = beforeSentence;
	}

	/**
	 * @param startTimePattern the startTimePattern to set
	 */
	public void setStartTimePattern(String startTimePattern) {
		this.startTimePattern = startTimePattern;
	}

	/**
	 * @param afterSentence the afterSentence to set
	 */
	public void setAfterSentence(String afterSentence) {
		this.afterSentence = afterSentence;
	}

	/**
	 * @param indentation the indentation to set
	 */
	public void setIndentation(String indentation) {
		this.indentation = indentation;
	}

	/**
	 * @param taskPattern the taskPattern to set
	 */
	public void setTaskPattern(String taskPattern) {
		this.taskPattern = taskPattern;
	}

	/**
	 * @param totalTimePattern the totalTimePattern to set
	 */
	public void setTotalTimePattern(String totalTimePattern) {
		this.totalTimePattern = totalTimePattern;
	}

	/**
	 * @param runningPattern the runningPattern to set
	 */
	public void setRunningPattern(String runningPattern) {
		this.runningPattern = runningPattern;
	}
}
