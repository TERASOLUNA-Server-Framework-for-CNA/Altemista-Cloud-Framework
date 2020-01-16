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


import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.altemista.cloudfwk.core.workflow.WorkflowExecutionService;
import org.altemista.cloudfwk.core.workflow.WorkflowHistoryService;
import org.altemista.cloudfwk.core.workflow.model.WorkflowHistoricProcessInstance;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Tests the historic workflows service implementation with Activiti
 * @author NTT DATA
 */
@ContextConfiguration(locations = {
	"classpath:spring/test-app-context-activiti.xml"
})
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})
public class WorkflowHistoryServiceTest extends AbstractSpringContextTest {
	
	/** The process definition declared in "oneTaskProcess.bpmn20.xml" */
	private static final String DEFINITION_ID = "oneTaskProcess";
	
	/** No pagination */
	private static final Pageable NO_PAGINATION = new PageRequest(0, Integer.MAX_VALUE);
	
	/** Allows sharing the process instance ID between @Before, @Test and @After methods */
	private final ThreadLocal<String> tlProcessInstanceId = new ThreadLocal<String>();
	
	/** The workflow execution service */
	@Autowired
	private WorkflowExecutionService executionService;
	
	/** The historic workflows service */
	@Autowired
	private WorkflowHistoryService historyService;
	
	/**
	 * Starts a process with one task and pass the task ID to the test
	 */
	@Before
	public void before() {
		
		final String processInstanceId = this.executionService.start(DEFINITION_ID);
		Assume.assumeNotNull(processInstanceId);
		this.tlProcessInstanceId.set(processInstanceId);
	}
	
	/**
	 * Aborts the process
	 */
	@After
	public void after() {
		
		final String processInstanceId = this.tlProcessInstanceId.get();
		if (processInstanceId != null) {
			this.executionService.abortInstance(processInstanceId, null);
			this.tlProcessInstanceId.remove();
		}
	}
	
	/**
	 * Tests the getProcessInstance method
	 */
	@Test
	public void testGetProcessInstance() {
		
		final String processInstanceId = this.tlProcessInstanceId.get();
		final WorkflowHistoricProcessInstance instance = this.historyService.getProcessInstance(processInstanceId);
		Assert.assertNotNull(instance);
		Assert.assertEquals(DEFINITION_ID, instance.getProcesDefinitionId());
		Assert.assertEquals(processInstanceId, instance.getProcessInstanceId());
		Assert.assertTrue(instance.isActive());
		Assert.assertNotNull(instance.getStartTime());
		Assert.assertNull(instance.getEndTime());
		Assert.assertNull(instance.getAbortReason());
		Assert.assertNotNull(instance.getProcessInstanceVariables());
		Assert.assertTrue(instance.getProcessInstanceVariables().isEmpty());
	}
	
	/**
	 * Tests the findAllProcessInstances method
	 */
	@Test
	public void testFindAllProcessInstances() {
		
		final Page<WorkflowHistoricProcessInstance> page =
				this.historyService.findAllProcessInstances(NO_PAGINATION);
		Assert.assertNotNull(page);
		Assert.assertNotNull(page.getContent());
		Assert.assertFalse(page.getContent().isEmpty());
	}
	
	/**
	 * Tests the findAllProcessInstances method with sorting
	 */
	@Test
	public void testFindAllProcessInstancesWithSorting() {
		
		// No provided Order should throw any exception
		for (Order order : new Order[]{
				WorkflowHistoryService.PROCESS_DEFINITION_ID_DESC,
				WorkflowHistoryService.PROCESS_DEFINITION_ID_DESC,
				WorkflowHistoryService.PROCESS_INSTANCE_ID_ASC,
				WorkflowHistoryService.PROCESS_INSTANCE_ID_DESC,
				WorkflowHistoryService.START_TIME_ASC,
				WorkflowHistoryService.START_TIME_DESC,
				WorkflowHistoryService.END_TIME_ASC,
				WorkflowHistoryService.END_TIME_DESC,
				WorkflowHistoryService.DURATION_ASC,
				WorkflowHistoryService.DURATION_DESC }) {
			
			// First page
			Pageable pageable = new PageRequest(0, 5, new Sort(order));
			Page<WorkflowHistoricProcessInstance> page = this.historyService.findAllProcessInstances(pageable);
			Assert.assertNotNull(page);
		}
	}
	
	/**
	 * Tests the findAllProcessInstances method
	 */
	@Test
	public void testFindAllProcessInstancesByProcessDefinitionId() {
		
		final Page<WorkflowHistoricProcessInstance> page =
				this.historyService.findAllProcessInstances(NO_PAGINATION, DEFINITION_ID);
		Assert.assertNotNull(page);
		Assert.assertNotNull(page.getContent());
		Assert.assertFalse(page.getContent().isEmpty());
		
		for (WorkflowHistoricProcessInstance instance : page.getContent()) {
			Assert.assertNotNull(instance);
			Assert.assertEquals(DEFINITION_ID, instance.getProcesDefinitionId());
		}
	}
	
	/**
	 * Tests the findActiveProcessInstances method
	 */
	@Test
	public void testFindActiveProcessInstances() {
		
		final Page<WorkflowHistoricProcessInstance> page =
				this.historyService.findActiveProcessInstances(NO_PAGINATION, DEFINITION_ID);
		Assert.assertNotNull(page);
		Assert.assertNotNull(page.getContent());
		Assert.assertFalse(page.getContent().isEmpty());
		
		for (WorkflowHistoricProcessInstance instance : page.getContent()) {
			Assert.assertNotNull(instance);
			Assert.assertEquals(DEFINITION_ID, instance.getProcesDefinitionId());
		}
	}
	
	/**
	 * Tests the findFinishedProcessInstances method
	 */
	@Test
	public void testFindFinishedProcessInstances() {
		
		final Page<WorkflowHistoricProcessInstance> page =
				this.historyService.findFinishedProcessInstances(NO_PAGINATION, DEFINITION_ID);
		Assert.assertNotNull(page);
		Assert.assertNotNull(page.getContent());
		
		for (WorkflowHistoricProcessInstance instance : page.getContent()) {
			Assert.assertNotNull(instance);
			Assert.assertEquals(DEFINITION_ID, instance.getProcesDefinitionId());
		}
	}
	
	/**
	 * Tests the deleteFinishedProcessInstances method
	 */
	@Test
	public void deleteFinishedProcessInstances() {
		
		final String processInstanceId = this.tlProcessInstanceId.get();
		this.executionService.abortInstance(processInstanceId, "testing");
		this.tlProcessInstanceId.remove();
		
		// Verifies the instance is aborted
		WorkflowHistoricProcessInstance instance = this.historyService.getProcessInstance(processInstanceId);
		Assert.assertNotNull(instance);
		Assert.assertFalse(instance.isActive());
		Assert.assertNotNull(instance.getEndTime());
		Assert.assertNotNull(instance.getAbortReason());
		
		// Deletes the finished processes
		this.historyService.deleteFinishedProcessInstances();

		// Ensures the process instance is no longer recoverable
		for (WorkflowHistoricProcessInstance i : this.historyService.findAllProcessInstances(NO_PAGINATION)) {
			Assert.assertNotSame(processInstanceId, i.getProcessInstanceId());
		}
	}
	
	/**
	 * Tests the deleteFinishedProcessInstances method
	 */
	@Test
	public void deleteFinishedProcessInstancesByProcessDefinitionId() {
		
		final String processInstanceId = this.tlProcessInstanceId.get();
		this.executionService.abortInstance(processInstanceId, "testing");
		this.tlProcessInstanceId.remove();
		
		// Verifies the instance is aborted
		WorkflowHistoricProcessInstance instance = this.historyService.getProcessInstance(processInstanceId);
		Assert.assertNotNull(instance);
		Assert.assertFalse(instance.isActive());
		Assert.assertNotNull(instance.getEndTime());
		Assert.assertNotNull(instance.getAbortReason());
		
		// Deletes the finished processes
		this.historyService.deleteFinishedProcessInstances(DEFINITION_ID);

		// Ensures the process instance is no longer recoverable
		for (WorkflowHistoricProcessInstance i : this.historyService.findAllProcessInstances(NO_PAGINATION)) {
			Assert.assertNotSame(processInstanceId, i.getProcessInstanceId());
		}
	}

}
