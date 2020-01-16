/**
 * 
 */
package org.altemista.cloudfwk.core.workflow.impl;

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


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.altemista.cloudfwk.core.workflow.AuthenticableWorkflowService;
import org.altemista.cloudfwk.core.workflow.exception.WorkflowException;

/**
 * Convience base class for implementing the AuthenticableWorkflowService interface
 * @param <T> The class or interface that actually contains the authentication aware methods
 * @author NTT DATA
 */
public abstract class AbstractAuthenticableWorkflowService<T> implements AuthenticableWorkflowService<T> {

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.workflow.AuthenticableWorkflowService#withCurrentUser()
	 */
	@Override
	public T withCurrentUser() {
		
		return this.withUser(this.getCurrentUserId());
	}

	/**
	 * Convenience method to obtain the current user from Spring security context
	 * @return the current user ID
	 */
	protected String getCurrentUserId() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new WorkflowException("unauthenticated");
		}

		return authentication.getName();
	}
	
	/**
	 * Convenience method to obtain the current user groups from Spring security context
	 * @return the current user group IDs
	 */
	protected String[] getCurrentGroupIds() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new WorkflowException("unauthenticated");
		}

		List<String> groupIds = new ArrayList<String>();
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			groupIds.add(authority.getAuthority());
		}
		return groupIds.toArray(new String[groupIds.size()]);
	}
}
