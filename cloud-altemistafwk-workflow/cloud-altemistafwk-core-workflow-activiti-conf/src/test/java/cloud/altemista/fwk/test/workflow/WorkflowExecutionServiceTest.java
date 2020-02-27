/**
 * 
 */
package cloud.altemista.fwk.test.workflow;

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


import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
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
import cloud.altemista.fwk.core.workflow.WorkflowExecutionService;
import cloud.altemista.fwk.core.workflow.exception.WorkflowException;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Tests the workflow execution service Activiti implementation
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
public class WorkflowExecutionServiceTest extends AbstractSpringContextTest {

	/** The process definition declared in "oneTaskProcess.bpmn20.xml" */
	private static final String DEFINITION_ID = "oneTaskProcess";
	
	/** The key of the instance variable used for the activiti:initiator property in the process definition */
	private static final String INITIATOR_KEY = "initiator";
	
	/** The username for the mock user */
	private static final String USER_ID = "john_doe";

	/** The workflow execution service */
	@Autowired
	private WorkflowExecutionService executionService;
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	 * Tests the start method anonymously
	 */
	@Test
	public void testStartWorkflow() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
	}
	
	/**
	 * Tests the start method anonymously
	 */
	@Test
	public void testStartWorkflowAnonymously() {
		
		final String instanceId = this.executionService.anonymously().start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
	}
	
	/**
	 * Tests the start method anonymously with the current user
	 */
	@Test
	@WithMockUser(username = USER_ID)
	public void testStartWorkflowWithCurrentUser() {
		
		final String instanceId = this.executionService.withCurrentUser().start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertEquals(USER_ID, this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
	}
	
	/**
	 * Tests the start method anonymously with specific user
	 */
	@Test
	public void testStartWorkflowWithUser() {
		
		final String instanceId = this.executionService.withUser(USER_ID).start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertEquals(USER_ID, this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
	}
	
	/**
	 * Tests the start method with parameters
	 */
	@Test
	public void testStartWorkflowWithParameters() {
		
		final Map<String, Object> parameters = Collections.<String, Object> singletonMap("foo", "bar");
		final String instanceId = this.executionService.start(DEFINITION_ID, parameters);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(2, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
		Assert.assertNotNull(this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "baz"));
	}
	
	/**
	 * Tests the start method with parameters anonymously
	 */
	@Test
	public void testStartWorkflowWithParametersAnonymously() {
		
		final Map<String, Object> parameters = Collections.<String, Object> singletonMap("foo", "bar");
		final String instanceId = this.executionService.anonymously().start(DEFINITION_ID, parameters);
		Assert.assertNotNull(instanceId);

		Assert.assertNotNull(this.executionService.getInstanceVariables(instanceId));
		Assert.assertEquals(2, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, INITIATOR_KEY));
		Assert.assertNotNull(this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "baz"));
	}
	
	/**
	 * Tests the start method with parameters
	 */
	@Test
	public void testWorkflowParameters() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(instanceId);
		Assume.assumeNotNull(this.executionService.getInstanceVariables(instanceId));
		Assume.assumeTrue(this.executionService.getInstanceVariables(instanceId).isEmpty());
		
		// Adds the first variable
		this.executionService.setInstanceVariable(instanceId, "foo", "bar");
		Assert.assertFalse(this.executionService.getInstanceVariables(instanceId).isEmpty());
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertEquals("bar", this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "baz"));
		
		// Adds a second variable
		this.executionService.setInstanceVariable(instanceId, "baz", "qux");
		Assert.assertEquals(2, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertEquals("bar", this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertEquals("qux", this.executionService.getInstanceVariable(instanceId, "baz"));
		
		// Removes the first variable
		this.executionService.removeInstanceVariable(instanceId, "foo");
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertEquals("qux", this.executionService.getInstanceVariable(instanceId, "baz"));
		
		// Replaces the second variable
		this.executionService.setInstanceVariable(instanceId, "baz", "quux");
		Assert.assertEquals(1, this.executionService.getInstanceVariables(instanceId).size());
		Assert.assertEquals("quux", this.executionService.getInstanceVariable(instanceId, "baz"));
		
		// Removes the second variable
		this.executionService.removeInstanceVariable(instanceId, "baz");
		Assert.assertTrue(this.executionService.getInstanceVariables(instanceId).isEmpty());
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "foo"));
		Assert.assertNull(this.executionService.getInstanceVariable(instanceId, "baz"));
	}
	
	/**
	 * Tests the suspend method
	 */
	@Test
	public void testSuspendWorkflow() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(instanceId);
		
		this.executionService.suspendInstance(instanceId);
	}
	
	/**
	 * Tests the suspend method
	 */
	@Test
	public void testSuspendWorkflowTwice() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(instanceId);
		
		// The first suspendInstance invocation must complete
		this.executionService.suspendInstance(instanceId);

		// The second suspendInstance invocation must throw invalid_process_instance_state 
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("invalid_process_instance_state");
		
		this.executionService.suspendInstance(instanceId);
	}
	
	/**
	 * Tests the continue method
	 */
	@Test
	public void testContinueWorkflow() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(instanceId);
		
		// invalid_process_instance_state because the continued workflow was not suspended  
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("invalid_process_instance_state");
		
		this.executionService.continueInstance(instanceId);
	}
	
	/**
	 * Tests the continue method
	 */
	@Test
	public void testContinueSuspendedWorkflow() {
		
		final String instanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(instanceId);
		
		this.executionService.suspendInstance(instanceId);
		
		this.executionService.continueInstance(instanceId);
	}
	
	/**
	 * Tests the abort method
	 */
	@Test
	public void testAbortWorkflow() {
		
		String instanceId = this.executionService.start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);
		
		this.executionService.abortInstance(instanceId, null);
	}
	
	/**
	 * Tests the abort method with reason
	 */
	@Test
	public void testAbortWorkflowWithReason() {
		
		String instanceId = this.executionService.start(DEFINITION_ID);
		Assert.assertNotNull(instanceId);
		
		this.executionService.abortInstance(instanceId, "foo");
	}
}
