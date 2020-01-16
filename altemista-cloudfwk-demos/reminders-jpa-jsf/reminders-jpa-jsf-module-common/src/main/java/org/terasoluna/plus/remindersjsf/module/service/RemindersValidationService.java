package org.altemista.cloudfwk.remindersjsf.module.service;

import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;

/**
 * Interface for the business validations of the demo application
 * @author NTT DATA
 */
public interface RemindersValidationService {

	/**
	 * Validates the added/edited task
	 * @param task the reminder to be added/edited
	 */
	void validateTask(TaskDTO task);

}
