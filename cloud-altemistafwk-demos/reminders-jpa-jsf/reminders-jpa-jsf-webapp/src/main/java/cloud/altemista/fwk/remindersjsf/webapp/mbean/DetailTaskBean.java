package cloud.altemista.fwk.remindersjsf.webapp.mbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import cloud.altemista.fwk.remindersjsf.module.exception.RemindersException;
import cloud.altemista.fwk.remindersjsf.module.model.TaskDTO;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersService;
import cloud.altemista.fwk.remindersjsf.webapp.controller.AlertMessages;

/**
 * @author NTT DATA
 * Managed Bean used in edit operations
 *
 */
@Controller
@SessionScoped
public class DetailTaskBean {

	@Autowired
	private RemindersService service;

	private TaskDTO task;
	private String id;
	private ActionType actionType;
	private String headerMsg;
	private boolean showDelete;

	// Getters and Setters

	public boolean isshowDelete() {
		return showDelete;
	}

	public void setHideDelete(boolean showDelete) {
		this.showDelete = showDelete;
	}

	public String getHeaderMsg() {
		return headerMsg;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaskDTO getTask() {
		return task;
	}

	// Load Task

	/**
	 * Conditional load (add or edit)
	 */
	public void loadTask() {
		if (this.id.equals("-1")) {
			loadAdd();
			return;
		}
		loadEdit(Long.parseLong(id, 10));
		if (this.task == null) {
			goToHome();
		}
	}


	/**
	 * Handler when Submit is clicked.
	 * If RemindersExceptions are thrown, they are processed
	 */
	public void submit() {
		try {
			if (this.actionType.equals(ActionType.ADD)) {
				doAdd();
			} else {
				doEdit();
			}
			// FIXME -> Currently, this is not showing
			getCtx().addMessage(AlertMessages.CLIENT_ID,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Operation succesfully complete", null));
			goToHome();

		} catch (RemindersException e) { // NOSONAR
			getCtx().addMessage(AlertMessages.CLIENT_ID,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getCodes()[0], null));
		}

	}
	
	/**
	 * Returns navigation to "home" view
	 */
	public void goToHome() {
		getCtx().getApplication().getNavigationHandler()
				.handleNavigation(FacesContext.getCurrentInstance(), null, "home");
	}

	/**
	 * Handler when Delete is clicked.
	 */
	public void deleteTask(String id) {
		this.service.deleteTask(Long.parseLong(id, 10));
		goToHome();
	}
	
	/**
	 * Edit logic that refresh data with this operation
	 */
	private void loadEdit(long id) {
		this.actionType = ActionType.EDIT;
		this.showDelete = true;
		this.headerMsg = "Updating task...";
		this.task = service.getDetail(id);
	}

	/**
	 * Add logic that refresh data with this operation
	 */
	private void loadAdd() {
		this.actionType = ActionType.ADD;
		this.headerMsg = "Adding new task...";
		this.showDelete = false;
		this.task = new TaskDTO();
	}

	/**
	 * Call to service when submit is clicked in edit form
	 */
	private void doEdit() {
		this.service.editTask(task, null);
	}

	/**
	 * Call to service when submit is clicked in add form
	 */
	private void doAdd() {
		this.service.addTask(task, null);
	}
	
	/**
	 * Return the current FacesContext
	 * @return FacesContext
	 */
	private FacesContext getCtx() {
		return FacesContext.getCurrentInstance();
	}


}
