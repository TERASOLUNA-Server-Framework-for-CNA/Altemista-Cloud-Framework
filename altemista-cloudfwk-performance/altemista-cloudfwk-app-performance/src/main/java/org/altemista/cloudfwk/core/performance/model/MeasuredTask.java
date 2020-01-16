/**
 * 
 */
package org.altemista.cloudfwk.core.performance.model;

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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.altemista.cloudfwk.core.performance.exception.PerformanceException;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Represents a measured task
 * @author NTT DATA
 * @see org.altemista.cloudfwk.core.performance.model.MeasuredTask.View
 */
public class MeasuredTask implements Serializable {
	
	/** MeasuredTask comparator instance that sorts longest periods first, most recent first. */
	public static final Comparator<MeasuredTask> COMPARATOR = new MeasuredTaskComparator();
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -808626551339167859L;
	
	/** The task information */
	private final TaskInfo taskInfo;
	
	/** Is this task a nested task? */
	private final boolean isNestedTask;
	
	/** The start time of the task */
	private final long startTimeMillis;
	
	/** The stop time of the task, or null if the task is currently running */
	private Long stopTimeMillis;
	
	/** The maximum number of nested tasks levels this task will support */
	private final int nestingLevels;
	
	/** Nested tasks (serializable by construction) */
	private final List<MeasuredTask> nestedTasks; // NOSONAR
	
	/**
	 * Constructor
	 * @param information the task information
	 * @param nestingLevels the maximum number of nested tasks levels this task will support
	 */
	public MeasuredTask(TaskInfo information, int nestingLevels) {
		this(information, false, nestingLevels);
	}
	
	/**
	 * Constructor
	 * @param information the task information
	 * @param isNestedTask if this task is a nested task
	 * @param nestingLevels the maximum number of nested tasks levels this task will support
	 */
	private MeasuredTask(TaskInfo information, boolean isNestedTask, int nestingLevels) {
		super();
		
		this.taskInfo = information;
		this.isNestedTask = isNestedTask;
		this.startTimeMillis = System.currentTimeMillis();
		this.stopTimeMillis = null;
		this.nestingLevels = (nestingLevels > 0) ? nestingLevels : 0;
		this.nestedTasks = (nestingLevels > 0) ? new ArrayList<MeasuredTask>() : null;
	}

	/**
	 * Returns the task information
	 * @return the task information
	 */
	public TaskInfo getTaskInfo() {
		
		return this.taskInfo;
	}
	
	/**
	 * Returns if this task is a nested task
	 * @return if this task is a nested task
	 */
	public boolean isNestedTask() {
		
		return this.isNestedTask;
	}

	/**
	 * Returns the task description
	 * @return the task description
	 */
	@JsonView(MeasuredTask.View.class)
	public String getDescription() {
		
		return this.taskInfo.getDescription();
	}

	/**
	 * Returns the start time of the task
	 * @return the start time of the task
	 */
	@JsonView(MeasuredTask.View.class)
	public long getStartTimeMillis() {
		
		return this.startTimeMillis;
	}
	
	/**
	 * Is the task currently running?
	 * @return if the task is currently running
	 */
	@JsonView(MeasuredTask.View.class)
	public boolean isRunning() {
		
		return this.stopTimeMillis == null;
	}
	
	/**
	 * Returns the duration of the task
	 * @return the duration of the task, or null if the task is currently running
	 */
	@JsonView(MeasuredTask.View.class)
	public Long getTotalTimeMillis() {
		
		return this.isRunning() ? null : (this.stopTimeMillis - this.startTimeMillis);
	}
	
	/**
	 * Does this task allow nested tasks?
	 * @return if the task allows nested tasks
	 */
	public boolean allowsNestedTasks() {
		
		return this.nestingLevels > 0;
	}

	/**
	 * Returns the nested tasks
	 * @return a list with the nested tasks, never null
	 */
	@JsonView(MeasuredTask.View.class)
	public List<MeasuredTask> getNestedTasks() {
		
		return ((this.nestedTasks == null) || (this.nestedTasks.isEmpty()))
				? Collections.<MeasuredTask> emptyList()
				: new ArrayList<MeasuredTask>(this.nestedTasks);
	}
	
	/**
	 * Stops the task
	 */
	public void stop() {
		
		// Only a running task can be stopped
		if (!this.isRunning()) {
			throw new PerformanceException("illegal_state");
		}
		
		// Stops the children task and the task and their children
		this.stopLastNestedTask();
		this.stopTimeMillis = System.currentTimeMillis();
	}
	
	/**
	 * Convenience method to stop the last nested task
	 */
	protected void stopLastNestedTask() {
		
		// No nested task to stop
		if ((this.nestedTasks == null) || (this.nestedTasks.isEmpty())) {
			return;
		}
		
		// Stops the last nested task if was still running
		MeasuredTask lastNestedTask = this.nestedTasks.get(this.nestedTasks.size() - 1);
		if (lastNestedTask.isRunning()) {
			lastNestedTask.stop();
		}
	}
	
	/**
	 * Starts a nested task.
	 * If this task does not allow nested tasks this method will complete normally
	 * and the new nested task will be returned, but the task won't be added to the nested tasks list
	 * @param description the nested task description
	 * @return the new nested task
	 */
	public MeasuredTask startNestedTask(String description) {
		
		return this.startNestedTask(new SimpleTaskInfo(description));
	}
	
	/**
	 * Starts a nested task.
	 * @param information the nested task information
	 * @return the new nested task
	 * @throws PerformanceException if the task is not running or does not allow nested tasks
	 */
	public MeasuredTask startNestedTask(TaskInfo information) {
		
		// Only a running task can have new nested tasks
		if (!this.isRunning()) {
			throw new PerformanceException("illegal_state");
		}
		if (!this.allowsNestedTasks()) {
			throw new PerformanceException("max_nesting_level");
		}
		
		// Stops the last existing nested task
		this.stopLastNestedTask();
		
		// Creates the nested task, and appends it only if nesting is supported 
		MeasuredTask nestedTask = new MeasuredTask(
				information, true, (this.nestingLevels > 0) ? this.nestingLevels - 1 : 0);
		this.nestedTasks.add(nestedTask);
		return nestedTask;
	}
	
	/**
	 * Suggested view when exposing <code>MeasuredTask</code>s as REST response.
	 * The purpose of this <code>@JsonView</code> is to hide internal details and expose only relevant information
	 * and to avoid serialization problems, specially in <code>TaskInfo</code> implementations
	 * @author NTT DATA
	 */
	public interface View {
		// (marker interface)
	}
	
	/**
	 * MeasuredTask comparator that sorts longest periods first, most recent first.
	 * @author NTT DATA
	 */
	public static class MeasuredTaskComparator implements Comparator<MeasuredTask>, Serializable {
		
		/** The serialVersionUID */
		private static final long serialVersionUID = 1301182955146568130L;

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(MeasuredTask a, MeasuredTask b) {
			
			// (sanity checks)
			if ((a == null) && (b == null)) {
				return 0;
			}
			if ((a == null) || (b == null)) {
				throw new IllegalArgumentException();
			}
			
			// Unfinished periods first; if both periods are unfinished,
			// then uses the chronological order of their start times (i.e.: longer active first)
			if (a.stopTimeMillis == null) {
				// Both periods unfinished: 
				return (b.stopTimeMillis == null)
						? this.compare(a.startTimeMillis, b.startTimeMillis)
						: -1;
			}
			if (b.stopTimeMillis == null) {
				return 1;
			}
			
			// If both are finished, then the longest finished periods first
			int durationComparison = this.compare(
					a.stopTimeMillis - a.startTimeMillis,
					b.stopTimeMillis - b.startTimeMillis);
			if (durationComparison != 0) {
				return -durationComparison; // NOSONAR
			}
			
			// If both had the same duration, then the most recent one first
			return -this.compare(a.startTimeMillis, b.startTimeMillis); // NOSONAR
		}
		
		/**
		 * Convenience method (Long.compare is not available in Java 6)
		 * @param x the first long to compare
		 * @param y the second long to compare
		 * @return the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
		 */
		private int compare(long x, long y) {
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
	}
}
