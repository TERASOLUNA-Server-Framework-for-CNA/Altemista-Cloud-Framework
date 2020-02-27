/**
 * 
 */
package cloud.altemista.fwk.test.security.service;

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


//tag::jsr250[]
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
//end::jsr250[]

//tag::secured[]
import org.springframework.security.access.annotation.Secured;
//end::secured[]
//tag::expression[]
import org.springframework.security.access.prepost.PreAuthorize;
//end::expression[]
//tag::class[]

//end::class[]
/**
 * Example service for security unit tests
 * @author NTT DATA
 */
//tag::class[]
public interface SecuredService {
	
	//end::class[]
	/**
	 * Not secured method
	 */
	String notSecured();
	
	//tag::secured[]
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY") //<1>
	String springSecuredToAnonymous();
	
	@Secured("ROLE_ADMIN") // <2>
	String springSecuredToRoleAdmin();
	//end::secured[]
	
	//tag::jsr250[]
	@PermitAll //<1>
	String jsr250PermitAll();
	
	@RolesAllowed("ROLE_ADMIN") // <2>
	String jsr250RolesAllowedAdmin();
	
	@DenyAll // <3>
	String jsr250DenyAll();
	//end::jsr250[]
	
	//tag::expression[]
	@PreAuthorize("isAuthenticated()") //<1>
	String springExpressionSecuredToAuthenticated();
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") // <2>
	String springExpressionSecuredToRoleAdmin();
	//end::expression[]

//tag::class[]
}
//end::class[]
