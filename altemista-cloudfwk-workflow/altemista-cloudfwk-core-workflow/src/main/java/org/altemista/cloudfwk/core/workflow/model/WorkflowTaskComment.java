
package org.altemista.cloudfwk.core.workflow.model;

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

/**
 * A comment on a human task
 * @author NTT DATA
 */
public interface WorkflowTaskComment {
	
	/**
	 * Gets the comment ID
	 * @return the comment ID
	 */
	String getCommentId();
	
	/**
	 * Gets the user ID that made this comment
	 * @return the user ID that made this comment
	 */
	String getUserId();
	
	/**
	 * Gets the time and date when this comment was made
	 * @return the time and date when this comment was made
	 */
	Date getDate();

	/**
	 * Gets the comment text
	 * @return the comment text
	 */
	String getText();
}
