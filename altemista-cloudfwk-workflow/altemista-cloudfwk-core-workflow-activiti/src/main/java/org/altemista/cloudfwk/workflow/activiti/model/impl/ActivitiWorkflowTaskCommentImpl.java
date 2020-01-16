/**
 * 
 */
package org.altemista.cloudfwk.workflow.activiti.model.impl;

/*
 * #%L
 * altemista-cloud workflow: Activiti implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Date;

import org.activiti.engine.task.Comment;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.core.workflow.model.WorkflowTaskComment;

/**
 * 
 * @author NTT DATA
 */
public class ActivitiWorkflowTaskCommentImpl implements WorkflowTaskComment {
	
	/** The Activiti task comment */
	private final Comment comment;
	
	/**
	 * Constructor
	 * @param comment the Activiti task comment
	 */
	public ActivitiWorkflowTaskCommentImpl(Comment comment) {
		super();
		Assert.notNull(comment, "The Comment is required");
		
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowTaskComment#getCommentId()
	 */
	@Override
	public String getCommentId() {
		
		return this.comment.getId();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowTaskComment#getUserId()
	 */
	@Override
	public String getUserId() {
		
		return this.comment.getUserId();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowTaskComment#getDate()
	 */
	@Override
	public Date getDate() {
		
		return this.comment.getTime();
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.model.WorkflowTaskComment#getText()
	 */
	@Override
	public String getText() {
		
		return this.comment.getFullMessage();
	}
}
