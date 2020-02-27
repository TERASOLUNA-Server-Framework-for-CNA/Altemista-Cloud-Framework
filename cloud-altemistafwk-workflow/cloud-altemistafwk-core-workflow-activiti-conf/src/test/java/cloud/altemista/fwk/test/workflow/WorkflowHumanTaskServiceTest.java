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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import cloud.altemista.fwk.common.exception.FrameworkException;
import cloud.altemista.fwk.core.workflow.WorkflowAuthenticationAwareHumanTaskService;
import cloud.altemista.fwk.core.workflow.WorkflowExecutionService;
import cloud.altemista.fwk.core.workflow.WorkflowHumanTaskService;
import cloud.altemista.fwk.core.workflow.exception.WorkflowException;
import cloud.altemista.fwk.core.workflow.model.WorkflowTask;
import cloud.altemista.fwk.core.workflow.model.WorkflowTaskComment;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * 
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
public class WorkflowHumanTaskServiceTest extends AbstractSpringContextTest {
	
	/** The process definition declared in "oneTaskProcess.bpmn20.xml" */
	private static final String DEFINITION_ID = "oneTaskProcess";
	
	/** The username for the mock user */
	private static final String USER_ID = "john_doe";
	
	/** No pagination */
	private static final Pageable NO_PAGINATION = new PageRequest(0, Integer.MAX_VALUE);
	
	/** Allows sharing the process instance ID between @Before, @Test and @After methods */
	private final ThreadLocal<String> tlProcessInstanceId = new ThreadLocal<String>();
	
	/** The workflow execution service */
	@Autowired
	private WorkflowExecutionService executionService;
	
	/** The human task service */
	@Autowired
	private WorkflowHumanTaskService taskService;
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
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
	 * Tests the findTasks method
	 */
	@Test
	public void testfindTasks() {
		
		// First page
		Pageable pageable = new PageRequest(0, 3);
		Page<WorkflowTask> page = this.taskService.findTasks(pageable);
		Assert.assertNotNull(page);
		Assert.assertFalse(page.getContent().isEmpty());
		
		// Next pages until the end of the data
		do {
			pageable = pageable.next();
			page = this.taskService.findTasks(pageable);
			Assert.assertNotNull(page);
		} while (!page.getContent().isEmpty());
	}
	
	/**
	 * Tests the findCandidateTasks method with sorting
	 */
	@Test
	@WithMockUser
	public void testfindCandidateTaskWithSorting() {
		
		// No provided Order should throw any exception
		for (Order order : new Order[]{
				WorkflowHumanTaskService.TASK_ID_ASC,
				WorkflowHumanTaskService.TASK_ID_DESC,
				WorkflowHumanTaskService.PROCESS_INSTANCE_ID_ASC,
				WorkflowHumanTaskService.PROCESS_INSTANCE_ID_DESC,
				WorkflowHumanTaskService.NAME_ASC,
				WorkflowHumanTaskService.NAME_DESC,
				WorkflowHumanTaskService.SUBJECT_ASC,
				WorkflowHumanTaskService.SUBJECT_DESC,
				WorkflowHumanTaskService.DESCRIPTION_ASC,
				WorkflowHumanTaskService.DESCRIPTION_DESC,
				WorkflowHumanTaskService.PRIORITY_ASC,
				WorkflowHumanTaskService.PRIORITY_DESC,
				WorkflowHumanTaskService.ASIGNEE_ASC,
				WorkflowHumanTaskService.ASIGNEE_DESC,
				WorkflowHumanTaskService.OWNER_ASC,
				WorkflowHumanTaskService.OWNER_DESC,
				WorkflowHumanTaskService.CREATOR_ASC,
				WorkflowHumanTaskService.CREATOR_DESC,
				WorkflowHumanTaskService.EXPIRATION_DATE_ASC,
				WorkflowHumanTaskService.EXPIRATION_DATE_DESC,
				WorkflowHumanTaskService.CREATION_DATE_ASC,
				WorkflowHumanTaskService.CREATION_DATE_DESC }) {
			
			// First page
			Pageable pageable = new PageRequest(0, 5, new Sort(order));
			Page<WorkflowTask> page = this.taskService.findCandidateTasks(pageable);
			Assert.assertNotNull(page);
		}
	}
	
	/**
	 * Tests the no-operation methods (in the Activiti implementation)
	 */
	@Test
	@WithMockUser
	public void testNoopMethods() {
		
		final WorkflowTask task = this.fetchTaskAssumeValid();
		
		// Test all the authentication possibilities (no one should throw any exception)
		for (WorkflowAuthenticationAwareHumanTaskService impl : new WorkflowAuthenticationAwareHumanTaskService[]{
				this.taskService,
				this.taskService.anonymously(),
				this.taskService.withCurrentUser(),
				this.taskService.withUser(USER_ID) }) {
			
			impl.startTask(task.getTaskId());
			impl.stopTask(task.getTaskId());
			impl.suspendTask(task.getTaskId());
			impl.resumeTask(task.getTaskId());
		}
	}
	
	/**
	 * Tests the complete method
	 */
	@Test
	public void testCompleteTask() {
		
		final WorkflowTask task = this.fetchTaskAssumeValid();
		this.taskService.completeTask(task.getTaskId());
		
		try {
			this.executionService.abortInstance(task.getProcessInstanceId(), null);
			Assert.fail("The process instance should have been removed because the task was completed");
			
		} catch (WorkflowException e) {
			Assert.assertTrue(e.getCode(), StringUtils.endsWithIgnoreCase(e.getCode(), "process_instance_not_found"));
			this.tlProcessInstanceId.remove();
		}
	}
	
	/**
	 * Tests the complete method
	 */
	@Test
	@WithMockUser
	public void testCompleteTaskWithCurrentUser() {
		
		final WorkflowTask task = this.fetchTaskAssumeValid();
		this.taskService.withCurrentUser().completeTask(task.getTaskId());
		
		try {
			this.executionService.abortInstance(task.getProcessInstanceId(), null);
			Assert.fail("The process instance should have been removed because the task was completed");
			
		} catch (WorkflowException e) {
			Assert.assertTrue(e.getCode(), StringUtils.endsWithIgnoreCase(e.getCode(), "process_instance_not_found"));
			this.tlProcessInstanceId.remove();
		}
	}
	
	/**
	 * Tests the complete method with variables
	 */
	@Test
	public void testCompleteTaskWithVariables() {
		
		final Map<String, Object> parameters = Collections.<String, Object> singletonMap("foo", "bar");
		final WorkflowTask task = this.fetchTaskAssumeValid();
		this.taskService.completeTask(task.getTaskId(), parameters);
		
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("process_instance_not_found");
		
		try {
			this.executionService.getInstanceVariables(this.tlProcessInstanceId.get());
			Assert.fail("The process instance should have been removed because the task was completed");
			
		} catch (WorkflowException e) {
			this.tlProcessInstanceId.remove(); // (to avoid error on @After)
			throw e;
		}
	}
	
	/**
	 * Tests the complete method with variables
	 */
	@Test
	@WithMockUser
	public void testCompleteTaskWithVariablesWithCurrentUser() {
		
		final Map<String, Object> parameters = Collections.<String, Object> singletonMap("foo", "bar");
		final WorkflowTask task = this.fetchTaskAssumeValid();
		this.taskService.withCurrentUser().completeTask(task.getTaskId(), parameters);
		
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("process_instance_not_found");
		
		try {
			this.executionService.getInstanceVariables(this.tlProcessInstanceId.get());
			Assert.fail("The process instance should have been removed because the task was completed");
			
		} catch (WorkflowException e) {
			this.tlProcessInstanceId.remove(); // (to avoid error on @After)
			throw e;
		}
	}
	
	/**
	 * Tests the skip method (unsupported in the Activiti implementation)
	 */
	@Test
	public void testUnsupportedSkipMethod() {

		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("unsupported_operation");
		
		final WorkflowTask task = this.fetchTaskAssumeValid();
		this.taskService.skipTask(task.getTaskId());
	}
	
	/**
	 * Tests the claim method
	 */
	@Test
	public void testClaimAnonymouslyNotSupported() {
		
		// Fetches one task
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		Assert.assertNull(originalTask.getAssigneeId());
		final String taskId = originalTask.getTaskId();
		
		this.thrown.expect(WorkflowException.class);
		this.thrown.expectMessage("unsupported_operation");
		
		// Claims the task
		this.taskService.claimTask(taskId);
	}
	
	/**
	 * Tests the claim and release methods
	 */
	@Test
	@WithMockUser(value = USER_ID)
	public void testClaimAndReleaseMethods() {

		// Initially, there are no tasks assigned to USER_ID
		Assert.assertTrue(this.taskService.findAssignedTasks(NO_PAGINATION).getContent().isEmpty());
		
		// Fetches one tasks
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		Assert.assertNull(originalTask.getAssigneeId());
		final String taskId = originalTask.getTaskId();
		
		// Claims the task
		this.taskService.withCurrentUser().claimTask(taskId);

		// The claimed task should now appear as assigned to USER_ID
		final List<WorkflowTask> claimedTasks = this.taskService.findAssignedTasks(NO_PAGINATION).getContent();
		Assert.assertFalse(claimedTasks.isEmpty());
		final WorkflowTask claimedTask = claimedTasks.iterator().next(); 
		Assert.assertEquals(taskId, claimedTask.getTaskId());
		Assert.assertEquals(USER_ID, claimedTask.getAssigneeId());
		
		// Claims the same task a second time
		try {
			this.taskService.withUser("bar").claimTask(taskId);
			Assert.fail("Cannot claim an already claimed task");
			
		} catch (FrameworkException e) {
			Assert.assertTrue(e.getCode(), StringUtils.contains(e.getCode(), "invalid_task_state"));
		}
		
		// Releases the task
		this.taskService.releaseTask(claimedTask.getTaskId());

		// The released task should no longer appear as assigned to USER_ID
		Assert.assertTrue(this.taskService.findAssignedTasks(NO_PAGINATION).getContent().isEmpty());
		final WorkflowTask releasedTask = this.taskService.getTask(taskId); 
		Assert.assertEquals(taskId, releasedTask.getTaskId());
		Assert.assertNull(originalTask.getAssigneeId());
		
		// Releases the same task a second time (this operation is allowed)
		this.taskService.releaseTask(releasedTask.getTaskId());
	}
	
	/**
	 * Tests the delegate and resolve methods
	 */
	@Test
	@WithMockUser(value = USER_ID)
	public void testDelegateAndResolveMethods() {

		// Initially, there are no tasks assigned to neither USER_ID nor "bar"
		Assume.assumeTrue(this.taskService.findTasksOwnedBy(NO_PAGINATION, USER_ID, "bar").getContent().isEmpty());
		Assume.assumeTrue(this.taskService.findTasksAssignedTo(NO_PAGINATION, USER_ID, "bar").getContent().isEmpty());
		
		// Fetches one tasks and claims it
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		final String taskId = originalTask.getTaskId();
		this.taskService.withCurrentUser().claimTask(taskId);
		final WorkflowTask claimedTask = this.taskService.getTask(taskId); 
		Assert.assertEquals(USER_ID, claimedTask.getOwnerId());
		Assert.assertEquals(USER_ID, claimedTask.getAssigneeId());
		
		// Delegates the task
		this.taskService.delegateTask(taskId, "bar");
		
		// The delegated task should be assigned to the delegated user
		WorkflowTask delegatedTask = this.taskService.getTask(taskId); 
		Assert.assertEquals(USER_ID, delegatedTask.getOwnerId());
		Assert.assertEquals("bar", delegatedTask.getAssigneeId());
		
		// Delegates the task again
		this.taskService.delegateTask(taskId, "baz");
		
		// The delegated task should be assigned to the delegated user
		delegatedTask = this.taskService.getTask(taskId); 
		Assert.assertEquals(USER_ID, delegatedTask.getOwnerId());
		Assert.assertEquals("baz", delegatedTask.getAssigneeId());
		
		// Resolves the task
		this.taskService.resolveTask(taskId);
		
		// The released task should be assigned to the owner
		final WorkflowTask resolvedTask = this.taskService.getTask(taskId); 
		Assert.assertEquals(USER_ID, resolvedTask.getOwnerId());
		Assert.assertEquals(USER_ID, resolvedTask.getAssigneeId());
		
		// Resolves the task again
		this.taskService.resolveTask(taskId);
	}
	
	/**
	 * Tests the add user method
	 */
	@Test
	@WithMockUser(value = USER_ID)
	public void testAddUserMethod() {

		// Initially, USER_ID is no candidate for any task
		Assert.assertTrue(this.taskService.findCandidateTasks(NO_PAGINATION).getContent().isEmpty());
		Assert.assertTrue(this.taskService.findAssignedTasks(NO_PAGINATION).getContent().isEmpty());
		
		// Fetches one tasks and adds a user to it
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		final String taskId = originalTask.getTaskId();
		this.taskService.addCandidateUser(taskId, USER_ID);
		
		// USER_ID is now candidate for the task but not assignee
		Assert.assertFalse(this.taskService.findCandidateTasks(NO_PAGINATION).getContent().isEmpty());
		Assert.assertTrue(this.taskService.findAssignedTasks(NO_PAGINATION).getContent().isEmpty());
	}
	
	/**
	 * Tests the add group method
	 */
	@Test
	public void testAddGroupMethod() {

		// Initially, group "bar" is no candidate for any task
		Assert.assertTrue(
				this.taskService.findCandidateTasks(NO_PAGINATION, null, new String[]{ "bar" })
				.getContent().isEmpty());
		
		// Fetches one tasks and adds a group to it
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		final String taskId = originalTask.getTaskId();
		this.taskService.addCandidateGroup(taskId, "bar");
		
		// group "bar" is now candidate for the task but not assignee
		Assert.assertFalse(
				this.taskService.findCandidateTasks(NO_PAGINATION, null, new String[]{ "bar" })
				.getContent().isEmpty());
	}
	
	/**
	 * Tests the get/add/delete comments method
	 */
	@Test
	@WithMockUser(username = USER_ID)
	public void testCommentsMethods() {
		
		// Fetches one tasks
		final WorkflowTask originalTask = this.fetchTaskAssumeValid();
		final String taskId = originalTask.getTaskId();
		
		// Initially there are no comments
		List<WorkflowTaskComment> comments = this.taskService.getComments(taskId);
		Assert.assertNotNull(comments);
		Assert.assertTrue(comments.isEmpty());
		
		// Adds an anonymous comment
		this.taskService.addComment(taskId, "bar");
		
		// Verifies the comment has been inserted
		comments = this.taskService.getComments(taskId);
		Assert.assertNotNull(comments);
		Assert.assertEquals(1, comments.size());
		WorkflowTaskComment firstComment = comments.iterator().next();
		Assert.assertNotNull(firstComment.getCommentId());
		Assert.assertNull(firstComment.getUserId());
		Assert.assertNotNull(firstComment.getDate());
		Assert.assertEquals("bar", firstComment.getText());
		
		// Adds a second comment (with user "baz")
		this.taskService.withUser("baz").addComment(taskId, "qux");
		Assert.assertEquals(2, this.taskService.getComments(taskId).size());
		
		// Removes the first comment
		this.taskService.deleteComment(taskId, firstComment.getCommentId());
		
		// Verifies only the second comment remains
		comments = this.taskService.getComments(taskId);
		Assert.assertNotNull(comments);
		Assert.assertEquals(1, comments.size());
		WorkflowTaskComment secondComment = comments.iterator().next();
		Assert.assertNotNull(secondComment.getCommentId());
		Assert.assertEquals("baz", secondComment.getUserId());
		Assert.assertNotNull(secondComment.getDate());
		Assert.assertEquals("qux", secondComment.getText());
		
		// Removes the second comment (with current user; should be irrelevant)
		this.taskService.withCurrentUser().deleteComment(taskId, secondComment.getCommentId());

		// Verifies there are no comments left
		Assert.assertTrue(this.taskService.getComments(taskId).isEmpty());
	}
	
	/**
	 * Fetches the unclaimed task, assuming there is one
	 * @return WorkflowTask
	 */
	private WorkflowTask fetchTaskAssumeValid() {
		
		final String processInstanceId = this.tlProcessInstanceId.get();
		
		for (WorkflowTask task : this.taskService.findTasks(NO_PAGINATION)) {
			if (StringUtils.equals(processInstanceId, task.getProcessInstanceId())) {
				return task;
			}
		}
		
		Assume.assumeFalse("No task found for the process", true);
		return null;
	}
}
