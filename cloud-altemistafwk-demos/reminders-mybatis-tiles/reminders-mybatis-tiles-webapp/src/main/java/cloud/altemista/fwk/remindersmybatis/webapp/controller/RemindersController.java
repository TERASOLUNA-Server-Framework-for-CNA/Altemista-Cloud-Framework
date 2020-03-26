package cloud.altemista.fwk.remindersmybatis.webapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;
import cloud.altemista.fwk.remindersmybatis.module.exception.RemindersException;
import cloud.altemista.fwk.remindersmybatis.module.model.TaskDTO;
import cloud.altemista.fwk.remindersmybatis.module.service.RemindersService;

/**
 * Main controller of the demo application
 * 
 * @author NTT DATA
 */
@Controller
@RequestMapping(value = "/reminders", method = RequestMethod.GET)
@SessionAttributes(names = "task")
@TransactionTokenCheck("transactionTokenCheckExample")
public class RemindersController {

	/** The business of the demo application */
	@Autowired
	private RemindersService service;

	/**
	 * Entry point of the application: shows a list with the uncompleted
	 * reminders
	 * 
	 * @param model
	 * @return to the reminders list view
	 */
	@RequestMapping
	public String listReminders(Model model) {

		List<TaskDTO> todos = this.service.getTodosList(null);
		model.addAttribute("todos", todos);
		return "tasks/todolist";
	}

	/**
	 * Shows a list with the archived reminders
	 * 
	 * @param model
	 * @return to the archived reminders list view
	 */
	@RequestMapping("done")
	public String listArchivedReminders(Model model) {

		List<TaskDTO> complete = this.service.getCompleteTasks(null);
		model.addAttribute("completeTasks", complete);
		return "tasks/completelist";
	}

	/**
	 * Opens the page for creating a new reminder
	 * 
	 * @param model
	 * @return to the edit form, with a new blank reminder
	 */
	@RequestMapping("new")
	public String createNewTask(Model model) {

		// Puts a blank reminder in the model
		model.addAttribute("task", new TaskDTO());
		return "tasks/taskedit";
	}

	/**
	 * Opens the page for editing an existing reminder
	 * 
	 * @param model
	 * @param id
	 *            the identifier for the reminder to be edited
	 * @return to the edit form, with the existing reminder loaded
	 */
	@RequestMapping("edit/{id}")
	public String editTask(Model model, RedirectAttributes redir, @PathVariable("id") long id) {

		// Loads the existing reminder in the model
		TaskDTO detail = this.service.getDetail(id);
		if (detail == null) { // If null, redirect to main page
			ResultMessages messages = ResultMessages.danger().add("e.rm.vl.9001");
			redir.addFlashAttribute(messages);
			return "redirect:/";
		}
		model.addAttribute("task", detail);
		return "tasks/taskedit";
	}

	/**
	 * Archives (marks as completed) a task
	 * 
	 * @param id
	 *            the identifier for the reminder
	 * @return to the reminders list view
	 */
	@RequestMapping("archive/{id}")
	public String archiveTask(RedirectAttributes redir, @PathVariable("id") long id) {
		this.service.complete(id);
		ResultMessages messages = ResultMessages.success().add("s.rm.co.0001");
		redir.addFlashAttribute(messages);
		return "redirect:/";
	}

	/**
	 * Deletes a task
	 * 
	 * @param id
	 *            the identifier for the reminder
	 * @return to the reminders list view
	 */
	@RequestMapping("delete/{id}")
	public String deleteTask(Model model, @PathVariable("id") long id, RedirectAttributes redir) {
		this.service.deleteTask(id);
		ResultMessages messages = ResultMessages.success().add("s.rm.co.0002");
		redir.addFlashAttribute(messages);
		return "redirect:/";
	}

	/**
	 * Saves a task being created or edited
	 * 
	 * @param task
	 * @param bindingResult
	 *            BindingResult
	 * @param model
	 *            Model
	 * @return to the reminders list view
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute("task") @Valid TaskDTO task, BindingResult bindingResult, Model model,
			RedirectAttributes redir) {

		// Checks for errors
		if (bindingResult.hasErrors()) {
			ResultMessages messages = ResultMessages.danger().add("e.rm.vl.5002");
			model.addAttribute(messages);
			return "tasks/taskedit";
		}

		// Creates or updates the task
		String msg;
		try {
			if (task.getId() == TaskDTO.NO_ID) {
				this.service.addTask(task, null);
				msg = "s.rm.co.0003";

			} else {
				this.service.editTask(task, null);
				msg = "s.rm.co.0004";
			}

		} catch (RemindersException e) { // NOSONAR
			ResultMessages messages = ResultMessages.danger().add("e.rm.dt.5001");
			model.addAttribute(messages);
			return "tasks/taskedit";
		}

		ResultMessages messages = ResultMessages.success().add(msg);
		redir.addFlashAttribute(messages);
		return "redirect:/";
	}

	@RequestMapping(params = "initToken", method = RequestMethod.POST)
	@TransactionTokenCheck(type = TransactionTokenType.BEGIN)
	public String initToken() {

		return "Token init";
	}

	@RequestMapping(params = "checkToken", method = RequestMethod.POST)
	@TransactionTokenCheck
	public String checkToken() {

		return "Token check";
	}

}
