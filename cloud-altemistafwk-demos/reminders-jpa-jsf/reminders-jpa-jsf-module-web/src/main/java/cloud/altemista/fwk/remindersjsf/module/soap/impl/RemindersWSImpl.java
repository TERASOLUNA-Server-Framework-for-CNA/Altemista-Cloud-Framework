package cloud.altemista.fwk.remindersjsf.module.soap.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import cloud.altemista.fwk.remindersjsf.module.model.TaskDTO;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersService;
import cloud.altemista.fwk.remindersjsf.module.soap.RemindersWS;

/**
 * Web service implementation
 */

//@WebService(serviceName="RemindersService",
//        endpointInterface="cloud.altemista.fwk.reminders.ws.service.RemindersWS")
@Service
public class RemindersWSImpl implements RemindersWS {

	@Autowired
	private RemindersService service;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#getTodosList(java.lang.String)
	 */
	public List<TaskDTO> getTodosList() {
		return service.getTodosList(this.getSpringSecurityUser());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#getCompleteTasks(java.lang.String)
	 */
	public List<TaskDTO> getCompleteTasks() {
		return service.getCompleteTasks(this.getSpringSecurityUser());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#getDetail(long)
	 */
	public TaskDTO getDetail(long id) {
		return service.getDetail(id);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#addTask(cloud.altemista.fwk.reminders.model.TaskDTO, java.lang.String)
	 */
	public void addTask(TaskDTO task) {
		service.addTask(task, this.getSpringSecurityUser());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#editTask(cloud.altemista.fwk.reminders.model.TaskDTO, java.lang.String)
	 */
	public void editTask(TaskDTO task) {
		service.editTask(task, this.getSpringSecurityUser());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#complete(long)
	 */
	public void complete(long id) {
		service.complete(id);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.ws.service.RemindersWS#deleteTask(long)
	 */
	public void deleteTask(long id) {
		service.deleteTask(id);
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
