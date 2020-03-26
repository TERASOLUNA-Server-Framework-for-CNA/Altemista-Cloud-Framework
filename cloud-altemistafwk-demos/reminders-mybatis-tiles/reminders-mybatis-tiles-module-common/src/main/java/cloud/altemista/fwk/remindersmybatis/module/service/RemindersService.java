package cloud.altemista.fwk.remindersmybatis.module.service;

import java.util.List;

import cloud.altemista.fwk.remindersmybatis.module.model.TaskDTO;

/**
 * Main interface for the business of the demo application
 * @author NTT DATA
 */
public interface RemindersService {
	
	/**
	 * Gets the list of all the pending reminders for a specific user
	 * @return List of reminders
	 */
	List<TaskDTO> getTodosList(String user);
	
	/**
	 * Gets the list of all the archived (completed) reminders for a specific user
	 * @return List of archived (completed) reminders
	 */
	List<TaskDTO> getCompleteTasks(String user );
	
	/**
	 * Gets a reminder
	 * @param id the identifier for the reminder
	 * @return A reminder
	 */
	TaskDTO getDetail(long id);
	
	/**
	 * Creates a new reminder
	 * @param task the reminder to be created
	 */
	void addTask(TaskDTO task, String user);
	
	/**
	 * Updates an existing reminder
	 * @param task the reminder to be updated
	 */
	void editTask(TaskDTO task, String user);
	
	/**
	 * Marks a reminder as completed
	 * @param id the identifier for the reminder
	 */
	void complete(long id);
	
	/**
	 * Deletes an existing reminder
	 * @param id the identifier for the reminder
	 */
	void deleteTask(long id);
	
}
