package org.altemista.cloudfwk.remindersjsf.webapp.mbean;

import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersjsf.module.service.RemindersService;

/**
 * @author NTT DATA
 * 
 *         ManagedBean to perform the actions of the Completed Tasks Page
 *
 */
@Controller
@SessionScoped
public class CompletedTasksBean {

	@Autowired
	private RemindersService service;

	private List<TaskDTO> completedList;

	/**
	 * Call service for recover the list of complete tasks
	 * 
	 * @return List of complete tasks
	 */
	public List<TaskDTO> getCompletedList() {
		completedList = service.getCompleteTasks(null);
		return completedList;
	}

	/**
	 * Redirect to Home
	 */
	public void goToHome() {
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
				.handleNavigation(FacesContext.getCurrentInstance(), null, "home");
	}

	/**
	 * Delete a specific task (by ID) calling to the service and then, return
	 * the user to Home page.
	 * 
	 * @param id
	 */
	public void deleteTask(String id) {
		service.deleteTask(Long.parseLong(id, 10));
		goToHome();
	}

}
