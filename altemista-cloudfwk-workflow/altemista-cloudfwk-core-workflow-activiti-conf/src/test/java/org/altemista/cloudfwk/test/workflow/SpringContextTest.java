/**
 * 
 */
package org.altemista.cloudfwk.test.workflow;

/*
 * #%L
 * altemista-cloud workflow: Activiti implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.activiti.engine.ProcessEngine;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.altemista.cloudfwk.core.workflow.WorkflowExecutionService;
import org.altemista.cloudfwk.core.workflow.WorkflowHistoryService;
import org.altemista.cloudfwk.core.workflow.WorkflowHumanTaskService;
import org.altemista.cloudfwk.core.workflow.exception.WorkflowException;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the jBPM integration (just tests the Spring Context is valid)
 * @author NTT DATA
 */
@ContextConfiguration(locations = {
	"classpath:spring/test-app-context-activiti.xml"
})
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	WithSecurityContextTestExecutionListener.class
})
public class SpringContextTest extends AbstractSpringContextTest {
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private WorkflowExecutionService workflowExecutionService;
	
	@Autowired
	private WorkflowHumanTaskService workflowHumanTaskService;
	
	@Autowired
	private WorkflowHistoryService workflowHistoryService;
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	 * Tests the Spring Context is valid
	 */
	@Test
	public void testSpringContext() {
		
		Assert.assertTrue(true);
	}
	
	/**
	 * Tests the Spring Context has the desired beans
	 */
	@Test
	public void testSpringContextActivitiBeans() {
			
		Assert.assertNotNull(this.processEngine);
		Assert.assertNotNull(this.processEngine.getRepositoryService());
		Assert.assertNotNull(this.processEngine.getRuntimeService());
		Assert.assertNotNull(this.processEngine.getFormService());
		Assert.assertNotNull(this.processEngine.getTaskService());
		Assert.assertNotNull(this.processEngine.getHistoryService());
		Assert.assertNotNull(this.processEngine.getIdentityService());
		Assert.assertNotNull(this.processEngine.getManagementService());
		Assert.assertNotNull(this.processEngine.getDynamicBpmnService());
		Assert.assertNotNull(this.processEngine.getProcessEngineConfiguration());
	}
	
	/**
	 * Tests the Spring Context has the desired framework beans
	 */
	@Test
	public void testSpringContextFrameworkBeans() {
			
		Assert.assertNotNull(this.workflowExecutionService);
		Assert.assertNotNull(this.workflowHumanTaskService);
		Assert.assertNotNull(this.workflowHistoryService);
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowExecutionServiceAnonymously() {
		
		Assert.assertNotNull(this.workflowExecutionService.anonymously());
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowHumanTaskServiceAnonymously() {
		
		Assert.assertNotNull(this.workflowHumanTaskService.anonymously());
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowExecutionServiceWithCurrentUserUnathenticated() {
		
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("unauthenticated");
		
		this.workflowExecutionService.withCurrentUser();
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowHumanTaskServiceWithCurrentUserUnathenticated() {
		
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("unauthenticated");
		
		this.workflowHumanTaskService.withCurrentUser();
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	@WithMockUser
	public void testWorkflowExecutionServiceWithCurrentUser() {
		
		Assert.assertNotNull(this.workflowExecutionService.withCurrentUser());
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	@WithMockUser
	public void testWorkflowHumanTaskServiceWithCurrentUser() {
		
		Assert.assertNotNull(this.workflowHumanTaskService.withCurrentUser());
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWorkflowExecutionServiceWithUserNull() {
		
		Assert.assertNotNull(this.workflowExecutionService.withUser(null));
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWorkflowHumanTaskServiceWithUserNull() {
		
		Assert.assertNotNull(this.workflowHumanTaskService.withUser(null));
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWorkflowExecutionServiceWithUserEmpty() {
		
		Assert.assertNotNull(this.workflowExecutionService.withUser(""));
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWorkflowHumanTaskServiceWithUserEmpty() {
		
		Assert.assertNotNull(this.workflowHumanTaskService.withUser(""));
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowExecutionServiceWithUser() {
		
		Assert.assertNotNull(this.workflowExecutionService.withUser("foo"));
	}
	
	/**
	 * Tests the AuthenticableWorkflowService interface
	 */
	@Test
	public void testWorkflowHumanTaskServiceWithUser() {
		
		Assert.assertNotNull(this.workflowHumanTaskService.withUser("foo"));
	}
	
}
