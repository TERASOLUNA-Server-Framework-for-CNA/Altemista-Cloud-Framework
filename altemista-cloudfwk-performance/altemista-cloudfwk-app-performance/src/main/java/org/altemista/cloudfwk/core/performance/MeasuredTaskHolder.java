/**
 * 
 */
package org.altemista.cloudfwk.core.performance;

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


import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.core.NamedThreadLocal;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.core.performance.model.TaskInfo;

/**
 * Holder class that associates a Stack of MeasuredTask with the current thread
 * and allows starting and stopping tasks that will be automatically nested
 * @author NTT DATA
 */
public class MeasuredTaskHolder {

	/** The thread-local Stack of MeasuredTask  */
	private final ThreadLocal<Deque<MeasuredTask>> threadLocalStack =
			new NamedThreadLocal<Deque<MeasuredTask>>("Measured task");
	
	/** The maximum number of nested tasks this holder will use to create the non-nested tasks */
	private int nestingLevels = 1;

	/**
	 * Default constructor
	 */
	public MeasuredTaskHolder() {
		super();
	}
	
	/**
	 * Returns the currently active MeasuredTask
	 * @return MeasuredTask, or null if there is no active MeasuredTask
	 */
	public MeasuredTask getCurrent() {
		
		Deque<MeasuredTask> stack = this.threadLocalStack.get();
		if ((stack == null) || stack.isEmpty()) {
			return null;
		}
		
		return stack.peek();
	}
	
	/**
	 * Checks if the holder is accepting new tasks
	 * @return if the holder is accepting new tasks
	 */
	public boolean isAcceptingTasks() {
		
		MeasuredTask currentTask = this.getCurrent();
		return (currentTask == null)
				|| currentTask.allowsNestedTasks();
	}
	
	/**
	 * Starts a new MeasuredTask with the specified task information.
	 * The MeasuredTask will be automatically nested (if necessary) and pushed into the thread-local stack. 
	 * @param information TaskInfo
	 * @return the new MeasuredTask
	 * @see #stop()
	 */
	public MeasuredTask start(TaskInfo information) {
		
		// Initializes the stack
		Deque<MeasuredTask> stack = this.threadLocalStack.get();
		if (stack == null) {
			stack = new ArrayDeque<MeasuredTask>();
			this.threadLocalStack.set(stack);
		}
		
		// If the stack is empty, this is the first task
		if (stack.isEmpty()) {
			MeasuredTask task = new MeasuredTask(information, this.nestingLevels);
			stack.push(task);
			return task;
		}
		
		// Otherwise, initializes a nested task
		MeasuredTask nestedTask = stack.peek().startNestedTask(information);
		stack.push(nestedTask);
		return nestedTask;
	}
	
	/**
	 * Stops the most recent MeasuredTask in the stack and pops it from the thread-local stack
	 * @return the stopped task
	 * @see #start(TaskInfo)
	 */
	public MeasuredTask stop() {
		
		// (this should never occur, 
		Deque<MeasuredTask> stack = this.threadLocalStack.get();
		if ((stack == null) || stack.isEmpty()) {
			return null;
		}
		
		// Stops the task and pops it from the stack
		MeasuredTask task = stack.pop();
		if (stack.isEmpty()) {
			// (avoids a probable memory leak)
			this.threadLocalStack.remove();
		}
		task.stop();
		return task;
	}

	/**
	 * @return the nestingLevels
	 */
	public int getNestingLevels() {
		return nestingLevels;
	}

	/**
	 * @param nestingLevels the nestingLevels to set
	 */
	public void setNestingLevels(int nestingLevels) {
		this.nestingLevels = nestingLevels;
	}
}
