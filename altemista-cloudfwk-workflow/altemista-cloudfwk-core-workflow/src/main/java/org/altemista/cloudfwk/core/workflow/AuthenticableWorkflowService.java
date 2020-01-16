
package org.altemista.cloudfwk.core.workflow;

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


/**
 * Convenience interface for services that contains methods that can be invoked with authentication 
 * @param <T> The class or interface that actually contains the authentication aware methods
 * @author NTT DATA
 */
public interface AuthenticableWorkflowService<T> {
	
	/**
	 * Uses no user for invoking the authentication aware methods
	 * @return Instance of T for invoking methods anonymously 
	 */
	T anonymously();
	
	/**
	 * Uses the current user for invoking the authentication aware methods
	 * @return Instance of T for invoking methods anonymously with the current user
	 */
	T withCurrentUser();
	
	/**
	 * Uses the specified user for invoking the authentication aware methods
	 * @param userId the user ID
	 * @return Instance of T for invoking methods with the specified user 
	 */
	T withUser(String userId);
}
