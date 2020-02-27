
package cloud.altemista.fwk.core.workflow.model.impl;

import java.io.Serializable;

/*
 * #%L
 * altemista-cloud workflow
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Date;

import cloud.altemista.fwk.common.util.DefensiveUtil;
import cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment;

/**
 * A serializable implementation of a comment on a human task.
 * Different provider implementations are encouraged to return this class (or subclasses),
 * rather than bridge implementations to their native classes in order to allow serialization. 
 * @author NTT DATA
 */
public class WorkflowTaskCommentImpl implements WorkflowTaskComment, Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -2310023755960368524L;

	/** The comment ID */
	private String commentId;
	
	/** The user ID that made this comment */
	private String userId;
	
	/** The time and date when this comment was made */
	private Date date;

	/** The comment text */
	private String text;
	
	/**
	 * Default constructor
	 */
	public WorkflowTaskCommentImpl() {
		super();
	}
	
	/**
	 * Constructor
	 * @param that WorkflowTaskComment that will be cloned
	 */
	public WorkflowTaskCommentImpl(WorkflowTaskComment that) {
		this();
		
		this.setCommentId(that.getCommentId());
		this.setUserId(that.getUserId());
		this.setDate(that.getDate());
		this.setText(that.getText());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment#getCommentId()
	 */
	@Override
	public String getCommentId() {
		
		return this.commentId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment#getUserId()
	 */
	@Override
	public String getUserId() {
		
		return userId;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment#getDate()
	 */
	@Override
	public Date getDate() {
		
		return DefensiveUtil.copyOf(date);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment#getText()
	 */
	@Override
	public String getText() {
		
		return text;
	}

	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = DefensiveUtil.copyOf(date);
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
