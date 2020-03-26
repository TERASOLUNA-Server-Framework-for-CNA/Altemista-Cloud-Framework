package cloud.altemista.fwk.remindersjsf.webapp.mbean;

import java.util.List;

import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import cloud.altemista.fwk.remindersjsf.module.model.TaskDTO;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersService;

/**
 * @author NTT DATA
 * 
 *         ManagedBean to cover actions available in Home Page
 *
 */
@Controller
@SessionScoped
public class RemindersBean {

	@Autowired
	private RemindersService service;

	private List<TaskDTO> todoList;

	/**
	 * Recover tasks uncompleted calling the service
	 * 
	 * @return List of uncompleted tasks
	 */
	public List<TaskDTO> getTodoList() {
		this.todoList = service.getTodosList(null);
		return todoList;
	}

	/**
	 * Set one task as completed (by id of task)
	 * 
	 * @param id
	 */
	public void markTaskCompleted(String id) {
		service.complete(Long.parseLong(id, 10));
	}
}
