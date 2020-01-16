package org.altemista.cloudfwk.remindersmybatis.module.controller;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.remindersmybatis.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersmybatis.module.service.RemindersService;

/**
 * Controller that exposes the application as a REST API
 * 
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = RemindersRestController.MAPPING)
public class RemindersRestController {

	/** Root context. */
	public static final String MAPPING = "/api/reminders";

	/** The success message */
	private static final String SUCCESS_MESSAGE = "Successful operation";

	/** The failure message */
	private static final String FAIL_MESSAGE = "Invalid Operation";

	/** The business interface */
	@Autowired
	private RemindersService service;
	

	/**
	 * Lists the TO-DOs
	 * 
	 * @param completed
	 *            flag to filter the completed TO-DOs
	 * @return list of TO-DOs
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<TaskDTO> getList(@RequestParam(name = "completed", defaultValue = "false") String completed) {
		return BooleanUtils.toBoolean(completed) ? this.service.getCompleteTasks(this.getSpringSecurityUser())
				: this.service.getTodosList(this.getSpringSecurityUser());
	}

	/**
	 * Gets one TO-DO
	 * 
	 * @param id
	 *            the identifier of the TO-DO
	 * @return the TO-DO
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TaskDTO get(@PathVariable(value = "id") String id) {
		return this.service.getDetail(this.formatId(id));
	}

	/**
	 * Adds a TO-Do
	 * 
	 * @param task
	 *            the TO-DO
	 * @return success message
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String post(@RequestBody TaskDTO task) {
		this.service.addTask(task, this.getSpringSecurityUser());
		return SUCCESS_MESSAGE;
	}

	/**
	 * Updates a TO-DO
	 * 
	 * @param id
	 *            the identifier of the TO-DO
	 * @param task
	 *            the new values of the TO-DO
	 * @return success message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String put(@PathVariable(value = "id") String id, @RequestBody TaskDTO task) {

		// (this implementation does not support modifying the identifier)
		if (task.getId() != this.formatId(id)) {
			return FAIL_MESSAGE;
		}

		this.service.editTask(task, this.getSpringSecurityUser());
		return SUCCESS_MESSAGE;
	}

	/**
	 * Marks a TO-DO as completed
	 * 
	 * @param id
	 *            the identifier of the TO-DO
	 * @return success message
	 */
	@RequestMapping(value = "/{id}/done", method = RequestMethod.PUT)
	public String completeTask(@PathVariable(value = "id") String id) {

		this.service.complete(this.formatId(id));
		return SUCCESS_MESSAGE;
	}

	/**
	 * Deletes a TO-DO
	 * 
	 * @param id
	 *            the identifier of the TO-DO
	 * @return success message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable(value = "id") String id) {

		this.service.deleteTask(this.formatId(id));
		return SUCCESS_MESSAGE;
	}

	/**
	 * Convenience method to convert the identifier of the TO-DO received as a
	 * PathVariable
	 * 
	 * @param value
	 *            the identifier of the TO-DO received as a PathVariable
	 * @return the identifier of the TO-DO
	 */
	private long formatId(String value) {

		return NumberUtils.toLong(value, -1L);
	}
	
	/**
	 * Convenience method to obtain the current user from the Spring Security Context
	 * @return user
	 */
	private String getSpringSecurityUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		return user;
	}
}
