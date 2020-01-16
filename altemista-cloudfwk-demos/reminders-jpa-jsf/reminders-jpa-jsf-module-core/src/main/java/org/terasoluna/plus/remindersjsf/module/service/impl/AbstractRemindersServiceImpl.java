/**
 * 
 */
package org.altemista.cloudfwk.remindersjsf.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersjsf.module.service.RemindersService;
import org.altemista.cloudfwk.remindersjsf.module.service.RemindersValidationService;

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
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#addTask(org.altemista.cloudfwk.reminders.TaskDTO)
	 */
	public void addTask(TaskDTO task) {
		
		this.validationService.validateTask(task);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#editTask(org.altemista.cloudfwk.reminders.TaskDTO)
	 */
	public void editTask(TaskDTO task) {
		
		this.validationService.validateTask(task);
	}
}
