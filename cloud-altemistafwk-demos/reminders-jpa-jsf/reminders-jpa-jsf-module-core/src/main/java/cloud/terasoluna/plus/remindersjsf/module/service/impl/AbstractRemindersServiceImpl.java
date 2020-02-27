/**
 * 
 */
package cloud.altemista.fwk.remindersjsf.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import cloud.altemista.fwk.remindersjsf.module.model.TaskDTO;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersService;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersValidationService;

/**
 * Base implementation of the demo application business interface.
 * Includes the invocation of the validation service.
 * @author NTT DATA
 */
public abstract class AbstractRemindersServiceImpl implements RemindersService {
	
	/** business validations of the demo application */
	@Autowired
	private RemindersValidationService validationService;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.service.RemindersService#addTask(cloud.altemista.fwk.reminders.TaskDTO)
	 */
	public void addTask(TaskDTO task) {
		
		this.validationService.validateTask(task);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.service.RemindersService#editTask(cloud.altemista.fwk.reminders.TaskDTO)
	 */
	public void editTask(TaskDTO task) {
		
		this.validationService.validateTask(task);
	}
}
