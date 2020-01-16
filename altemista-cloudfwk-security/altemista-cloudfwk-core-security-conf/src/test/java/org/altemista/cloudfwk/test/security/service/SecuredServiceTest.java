/**
 * 
 */
package org.altemista.cloudfwk.test.security.service;

/*
 * #%L
 * altemista-cloud security: annotation-based authorization CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the annotation-driven security
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-security.xml")
@TestExecutionListeners(listeners = {
		DependencyInjectionTestExecutionListener.class,
		WithSecurityContextTestExecutionListener.class
})
public class SecuredServiceTest extends AbstractSpringContextTest {
	
	/** The service SecuredService */
	@Autowired
	private SecuredService service;
	
	@Test
	public void testSpringContext() {
		
		Assert.notNull(this.service);
	}
	
	@Test
	public void testNotSecured() {
		
		Assert.notNull(this.service.notSecured());
	}
	
	/*
	 * Methods secured by Spring Security annotations
	 */
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testSpringSecuredToAnonymousWithoutAuthenticationCredentials() {
		
		Assert.notNull(this.service.springSecuredToAnonymous());
	}
	
	@Test
	@WithMockUser
	public void testSpringSecuredToAnonymousWithMockUser() {
		
		Assert.notNull(this.service.springSecuredToAnonymous());
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser
	public void testSpringSecuredToRoleAdminWithMockUser() {
		
		Assert.notNull(this.service.springSecuredToRoleAdmin());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSpringSecuredToRoleAdminWithMockAdmin() {
		
		Assert.notNull(this.service.springSecuredToRoleAdmin());
	}
	
	/*
	 * Methods secured by JSR 250 annotations
	 */
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testJsr250PermitAllWithoutAuthenticationCredentials() {
		
		Assert.notNull(this.service.jsr250PermitAll());
	}
	
	@Test
	@WithMockUser
	public void testJsr250PermitAllWithMockUser() {
		
		Assert.notNull(this.service.jsr250PermitAll());
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser
	public void testJsr250RolesAllowedAdminWithMockUser() {
		
		Assert.notNull(this.service.jsr250RolesAllowedAdmin());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testJsr250RolesAllowedAdminWithMockAdmin() {
		
		Assert.notNull(this.service.jsr250RolesAllowedAdmin());
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser
	public void testJsr250DenyAllWithMockUser() {
		
		Assert.notNull(this.service.jsr250DenyAll());
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser(roles = "ADMIN")
	public void testJsr250DenyAllWithMockAdmin() {
		
		Assert.notNull(this.service.jsr250DenyAll());
	}
	
	/*
	 * Methods secured by Spring Security annotations with expression-based syntax
	 */
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testSpringExpressionSecuredToAnonymousWithoutAuthenticationCredentials() {
		
		Assert.notNull(this.service.springExpressionSecuredToAuthenticated());
	}
	
	@Test
	@WithMockUser
	public void testSpringExpressionSecuredToAnonymousWithMockUser() {
		
		Assert.notNull(this.service.springExpressionSecuredToAuthenticated());
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser
	public void testSpringExpressionSecuredToRoleAdminWithMockUser() {
		
		Assert.notNull(this.service.springExpressionSecuredToRoleAdmin());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	public void testSpringExpressionSecuredToRoleAdminWithMockAdmin() {
		
		Assert.notNull(this.service.springExpressionSecuredToRoleAdmin());
	}
	
}
