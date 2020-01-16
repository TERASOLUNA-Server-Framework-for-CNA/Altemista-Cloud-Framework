package org.altemista.cloudfwk.remindersjsf.module.soap;

import java.util.List;

import javax.jws.WebService;

import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;

/**
 * Interface which defines the Webservice
 * 
 * @author NTT DATA
 *
 */
@WebService
public interface RemindersWS {

	/**
	 * @param user
	 * @return List of reminders for a specific user
	 */
	public List<TaskDTO> getTodosList();

	/**
	 * @param user
	 * @return List of complete tasks for a specific user
	 */
	public List<TaskDTO> getCompleteTasks();

	/**
	 * @param id
	 * @return detail of specific reminder
	 */
	public TaskDTO getDetail(long id);

	/**
	 * Add a new task
	 * 
	 * @param task
	 * @param user
	 */
	public void addTask(TaskDTO task);

	/**
	 * Edit one task
	 * 
	 * @param task
	 * @param user
	 */
	public void editTask(TaskDTO task);

	/**
	 * Sets one task as complete
	 * 
	 * @param id
	 */
	public void complete(long id);

	/**
	 * Delete one task
	 * @param id
	 */
	public void deleteTask(long id);

}
